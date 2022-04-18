/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FireFlyAlgorithm;

import FitnessFunction.Parameters;
import FitnessFunction.FitnessFunction;
import JasonParser.MySqlOperations;
import JasonParser.SqlOperation;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;

class Fitness extends f_xj // Rosenbrock's valley     f(x)=0.0     -2.048<x[i]<2.048
{

    @Override
    double func(double x[]) throws SQLException, ParseException {
//çözümü istenen fonksiyon	

       // double d =0;
       // for(int i=0;i<x.length;i++) d+=x[i];
        return new FitnessFunction(x).CalculateFitnessFunction();
    }
}

/**
 *
 * @author Tarek
 */
public class FireFlyAlgorithm {

    SqlOperation SO;
    ResultSet RS;

    double[] productIdSum = new double[100];
    FireFlyInstance ffly;

    public FireFlyAlgorithm(int Populaion ,int Iteration) throws SQLException, ParseException {
        Parameters.iniProjectValue();
        Parameters.initializePackages();
        try {
            SO = new MySqlOperations();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double[] Lower = new double[Parameters.chromosomeLength];
        double[] Upper = new double[Parameters.chromosomeLength];
        for (int i = 0; i < Parameters.chromosomeLength; i++) {
            Lower[i] = 0;
            Upper[i] = 1000;
        }
        int N = 10;
        double gamma = 1.0;
        double alpha0 = 0.2;
        double alphan = 0.001;
        Fitness ff = new Fitness();
        ffly = new FireFlyInstance(ff, N, Lower, Upper, gamma, alpha0, alphan, 5);
        //ffly.toStringnew();
    }
    public double [] getSolution() {return ffly.solution()[1];}
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

    public static void main(String arg[]) throws SQLException, ParseException {

    //    FireFlyAlgorithm ffa = new FireFlyAlgorithm();
    //    ffa.PrintResult();
    }
}
