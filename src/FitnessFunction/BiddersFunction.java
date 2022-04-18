/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FitnessFunction;
import JasonParser.MySqlOperations;
import JasonParser.SqlOperation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.json.simple.parser.ParseException;
import java.math.BigDecimal;
import java.util.LinkedList;

/**
 *
 * @author Tarek
 */
public class BiddersFunction {

    SqlOperation SO;
    ResultSet resultSet;
    double[] Soultion, contractors , contractorsNumerator, contractorsDenominator;

    public BiddersFunction(double[] Solution) {

        this.Soultion = new double[Parameters.chromosomeLength];
        this.Soultion = Solution.clone();
        try {
            SO = new MySqlOperations();
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }

    public Double InflationRate(Double t) {
        return Math.exp(-1 * t);
    }

    public Double ConvertTime(String Date1, String Date2) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inputString1 = Date1;
        String inputString2 = Date2;
        long diff = 0;
        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            diff = date2.getTime() - date1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (diff / (1000 * 60 * 60 * 24) / (365 * 1.0));
    }


    
    public void initializeContractors() throws SQLException
    {  
        this.contractors = new double [Parameters.contractorsCount ]; 
        this.contractorsDenominator = new double [Parameters.contractorsCount ]; 
        this.contractorsNumerator = new double [Parameters.contractorsCount ]; 
    }
    
    
    public double getContractorsVarience()
    {
        return  new Statistics(this.contractors).getVariance();
    }
    
    public double[] getContractors()
    {
        return this.contractors;
    }
    
    
    

    public double CalculateBiddersValue() throws SQLException, java.text.ParseException {

        int iterator = 0;
        double Numerator = 0.0,Denominator = 0.0;
        this.initializeContractors();

        SO.Connect();
        resultSet = SO.SelectFromTable("SELECT pc.contractors_id, pp.package_id ,cs.contructor_id,pp.product_id , dis.dis_from , dis.dis_to , dis.rate , cp.sell_price, cp.buy_price\n"
                + "From contractors cs, packages_contractors pc, packages_products pp , discounts dis , contractors_products cp\n"
                + "where pc.contractors_id = cs.contructor_id AND pp.package_id = pc.package_id AND dis.contractor_id = cs.contructor_id AND dis.product_id = pp.product_id AND cp.contractor_id = cs.contructor_id AND cp.product_id = pp.product_id order by pc.contractors_id,pp.product_id,dis.dis_from");



        
        while (resultSet.next()) {
            
            /* Get Bidders Data */
            int contractors_id = resultSet.getInt("contractors_id");
            int product_id = resultSet.getInt("product_id");
            int package_id = resultSet.getInt("package_id");
            String dis_from = resultSet.getString("dis_from");
            String dis_to = resultSet.getString("dis_to");
            String rate = resultSet.getString("rate");
            int SellPrice = resultSet.getInt("sell_price");
            int BuyPrice = resultSet.getInt("buy_price");
           
            
            
      

            if(Parameters.CompareDate(Parameters.timeFrom[package_id],Parameters.timeTo[package_id],dis_from,dis_to))
            {
                //System.out.println("Soultion: "+ Soultion.length+" iterator: "+iterator);
                
                Numerator = 
                (Soultion[iterator] *  ((SellPrice * (1.0 - Double.parseDouble(rate))) - BuyPrice) * 
                 InflationRate(Parameters.inflationRate * ConvertTime(Parameters.startDate, dis_to)));

                Denominator = 
                (Soultion[iterator] * BuyPrice * InflationRate(Parameters.inflationRate * ConvertTime(Parameters.startDate, dis_to)));


                contractorsNumerator[contractors_id] += (Numerator);
                contractorsDenominator[contractors_id] += (Denominator);
            }
            else
            {
                 Numerator = 
                (Soultion[iterator] *  (SellPrice - BuyPrice) * 
                 InflationRate(Parameters.inflationRate * ConvertTime(Parameters.startDate, dis_to)));

                Denominator = 
                (Soultion[iterator] * BuyPrice * InflationRate(Parameters.inflationRate * ConvertTime(Parameters.startDate, dis_to)));


                contractorsNumerator[contractors_id] += (Numerator);
                contractorsDenominator[contractors_id] += (Denominator); 
            }
            
            iterator++;

        }
        
        
        SO.CloseConnection();

        return CalculateFinalValue();
    }
    
    public double CalculateFinalValue()
    {
        double value = 0.0;
        for (int i= 0; i< Parameters.contractorsCount; i++) {
            contractors[i] = contractorsNumerator[i] / contractorsDenominator[i];
            value += contractors[i];
        }
        return value;
    }
    
    public double [] GetContractors()
    {
        return contractors;
    }

    public static void main(String[] args) throws SQLException {

           System.out.println("BiddersFunction ");


    }

}
