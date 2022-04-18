/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DifferentialEvolutionAlgorithm;

import FitnessFunction.Parameters;
import JasonParser.MySqlOperations;
import JasonParser.SqlOperation;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;

/**
 *
 * @author Tarek
 */
public class DifferentialEvolutionAlgorithm {

    /**
     * @param args the command line arguments
     */
    SqlOperation SO;
    ResultSet RS;
    DifferentialEvolutionInstance DEI;
    double[] productIdSum = new double[100];
    public DifferentialEvolutionAlgorithm(int Populaion) throws SQLException, ParseException
    {
        Parameters.iniProjectValue();
        Parameters.initializePackages();
        try {
            SO = new MySqlOperations();
        } catch (Exception e) {
            e.printStackTrace();
        }
     //   System.out.println("Parameters.chromosomeLength: "+Parameters.chromosomeLength);
        DEI = new DifferentialEvolutionInstance(Populaion,Parameters.chromosomeLength);
    }
    
    
    public void Run(int Iterations ,double  mutate ,double  recombination) throws SQLException, ParseException
    {
       DEI.Run(Iterations, mutate, recombination);
    }
        public void PrintResult( double[] Solution ) throws SQLException, ParseException {
        SO.Connect();
        RS = SO.SelectFromTable("SELECT pc.contractors_id, pp.package_id ,cs.contructor_id,pp.product_id , dis.dis_from , dis.dis_to , dis.rate , cp.sell_price, cp.buy_price\n"
                + "  From contractors cs, packages_contractors pc, packages_products pp , discounts dis , contractors_products cp\n"
                + "  where pc.contractors_id = cs.contructor_id AND pp.package_id = pc.package_id AND dis.contractor_id = cs.contructor_id AND dis.product_id = pp.product_id AND cp.contractor_id = cs.contructor_id AND cp.product_id = pp.product_id order by pc.contractors_id,pp.product_id,dis.dis_from");

        int i = 0;
       // double[] Solution = ffly.solution()[1];
        while (RS.next()) {
            int contractors_id = RS.getInt("contractors_id");
            int product_id = RS.getInt("product_id");
            String dis_from = RS.getString("dis_from");
            String dis_to = RS.getString("dis_to");
            String rate = RS.getString("rate");
            int SellPrice = RS.getInt("sell_price");
            int BuyPrice = RS.getInt("buy_price");
            productIdSum[product_id] += Solution[i];
            double Numerator = (Solution[i] * ((SellPrice * (1.0 - Double.parseDouble(rate))) - BuyPrice));

            LinkedList<Integer> ll = Parameters.ll[contractors_id][product_id];
            for (int i2 = 0; i2 < ll.size(); i2++) {

                int PID = ll.get(i2);
                if (Parameters.CompareDate(Parameters.timeFrom[PID], Parameters.timeTo[PID], dis_from, dis_to)) {
                    System.out.println("Quantity :" + new BigDecimal(Solution[i]).toPlainString() + " contractors_id : " + contractors_id + " , " + "product_id : " + product_id + " , " + "SellPrice : " + SellPrice + " , " + "BuyPrice : " + BuyPrice + " , " + "dis_from : " + dis_from + " , " + "dis_to : " + dis_to + " , " + "rate : " + rate + " Numerator :" + Numerator);

                }
            }
            i++;

        }

        RS = SO.SelectFromTable("select product_id,sum(quantity) quantity from packages_products group by product_id order by product_id");
        while (RS.next()) {
            int product_id = RS.getInt("product_id");
            int quantity = RS.getInt("quantity");
            System.out.println("Product id : " + product_id + " PSO Quantity : " + new BigDecimal(productIdSum[product_id]).toPlainString() + "Real Quantity:" + quantity);
        }

      //  for(int i1=0;i1<100;i1++) System.out.println("Product id : " + i1 + " QSum : " +  new  BigDecimal(productIdSum[i1]).toPlainString());
        SO.CloseConnection();
    }

    public double [] getSolution() {return DEI.getSolution();}
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
