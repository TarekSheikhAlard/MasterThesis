/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FitnessFunction;

import static FitnessFunction.Parameters.packagesCount;
import JasonParser.MySqlOperations;
import JasonParser.SqlOperation;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
/**
 *
 * @author Tarek
 */
public class ProjectOwnerFunction {
    
    static Double Budget = 0.0;
    SqlOperation SO;
    ResultSet RS;
    double[] Solution;

    public ProjectOwnerFunction( double[] Solution) throws SQLException
    {                              

        SO = new MySqlOperations();
        this.Solution = Solution.clone();

     //   System.out.println("ProjectOwner Function : " +new BigDecimal(this.CalculateProjectOwnerValue()).toPlainString());
    }
       

    public Double CalculateBudget(Double PEPrice)
    {
        Random r = new Random();
        double randomValue = (PEPrice-(PEPrice/5)) + ((PEPrice+(PEPrice/5)) - (PEPrice-(PEPrice/5)) ) * r.nextDouble();
        return randomValue;
    }
    
    public Double InflationRate(Double t)
    {
        return Math.exp(-1 * t);
    }
    
    
    public Double ConvertTime(String Date1,String Date2)
    {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inputString1 = Date1;
        String inputString2 = Date2;
        long diff = 0;
        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            diff = date2.getTime() - date1.getTime();
            //System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (diff/(1000*60*60*24)/(365*1.0));
    }
    
    public Double PackagesEstimatePrice() throws SQLException, ParseException
    {
                
       int iterator=0;
       double Result = 0.0, Package[] = new double[packagesCount];
        SO.Connect();
        ResultSet resultSet =   SO.SelectFromTable("  SELECT pc.contractors_id, pp.package_id ,cs.contructor_id,pp.product_id , dis.dis_from , dis.dis_to , dis.rate , cp.sell_price, cp.buy_price\n" +
                                  "  From contractors cs, packages_contractors pc, packages_products pp , discounts dis , contractors_products cp\n" +
                                  "  where pc.contractors_id = cs.contructor_id AND pp.package_id = pc.package_id AND dis.contractor_id = cs.contructor_id AND dis.product_id = pp.product_id AND cp.contractor_id = cs.contructor_id AND cp.product_id = pp.product_id order by pc.contractors_id,pp.product_id,dis.dis_from");
       

       while (resultSet.next()) 
       {
            int contractors_id = resultSet.getInt("contractors_id");
            int product_id = resultSet.getInt("product_id");
            int package_id = resultSet.getInt("package_id"); 
            String dis_from = resultSet.getString("dis_from");
            String dis_to = resultSet.getString("dis_to");
            String rate = resultSet.getString("rate");
            int SellPrice = resultSet.getInt("sell_price");
            int BuyPrice = resultSet.getInt("buy_price");
            //System.out.println("Quantity :" +  new  BigDecimal(Solution[i]).toPlainString() + " contractors_id : " + contractors_id + " , " + "product_id : " + product_id + " , " + "SellPrice : " + SellPrice + " , " + "BuyPrice : " + BuyPrice + " , " + "dis_from : " + dis_from + " , " + "dis_to : " + dis_to + " , " + "rate : " + rate);

            //System.out.println("PackagesEstimatePrice: contractors_id: " + contractors_id + " product_id: " + product_id + " package_id: " +package_id + " dis_from: " +dis_from + "dis_to: "+   dis_to + " rate: " +rate + " SellPrice: " + SellPrice + " BuyPrice: " + BuyPrice + " Parameters.timeFrom[package_id]: " +Parameters.timeFrom[package_id] + " Parameters.timeTo[package_id]: "+ Parameters.timeTo[package_id]  + " package_id: " +package_id + " Parameters.CompareDate(Parameters.timeFrom[package_id],Parameters.timeTo[package_id],dis_from,dis_to): " +Parameters.CompareDate(Parameters.timeFrom[package_id],Parameters.timeTo[package_id],dis_from,dis_to) );
            //System.out.println("InflationRate(Parameters.inflationRate * ConvertTime(Parameters.startDate, dis_to)): "+ InflationRate(Parameters.inflationRate * ConvertTime(Parameters.startDate, dis_to)) + " Parameters.inflationRate * ConvertTime(Parameters.startDate, dis_to): " + Parameters.inflationRate * ConvertTime(Parameters.startDate, dis_to));
            if(Parameters.CompareDate(Parameters.timeFrom[package_id],Parameters.timeTo[package_id],dis_from,dis_to))
            {
               Package[package_id]  += 
               (Solution[iterator] *  ((SellPrice * (1.0 - Double.parseDouble(rate)))) * 
               InflationRate(Parameters.inflationRate * ConvertTime(Parameters.startDate, dis_to)));
            }
            else
            {
              Package[package_id]  += 
               (Solution[iterator] *  SellPrice * 
               InflationRate(Parameters.inflationRate * ConvertTime(Parameters.startDate, dis_to)));

            }
               
                
                            //System.out.println("i2 : "+i2);
            
            iterator++;

            
          
        }

        
        SO.CloseConnection();
        for(int j=0;j<packagesCount;j++)
        {
            Result+=Package[j];
        }
        return Result;
    }
    
    
    
    
    

    public Double CalculateProjectOwnerValue() throws SQLException, ParseException
    {

        Double Result = 0.0;
        Double PEPrice = this.PackagesEstimatePrice();
        Budget =  23181444.0;
        if (Budget == 0.0)
        {
            Budget = this.CalculateBudget(PEPrice);
        }
       // System.out.println("CalculateProjectOwnerValue: " + " this.Budget: " + this.Budget + " Parameters.inflationRate: " + Parameters.inflationRate + " Parameters.startDate: " +Parameters.startDate + " Parameters.finishDate: " +Parameters.finishDate + "ConvertTime: "+   ConvertTime(Parameters.startDate,Parameters.finishDate) + " PEPrice: " +PEPrice);
        Result = (this.Budget *  InflationRate(Parameters.inflationRate*ConvertTime(Parameters.startDate,Parameters.finishDate)) / PEPrice)  -1;   
       // System.out.println("Result: " +Result);
        return Result ;
    }
    
        
    public static void main(String[] args) throws SQLException
    {

    }
    
}
