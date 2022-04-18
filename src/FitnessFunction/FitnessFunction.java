/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FitnessFunction;

import java.sql.SQLException;
import java.text.ParseException;

/**
 *
 * @author Tarek
 */
public class FitnessFunction {
   BiddersFunction BF;
   ProjectOwnerFunction POF;
   public FitnessFunction(double [] PSO) throws SQLException
   {

       BF =   new BiddersFunction(PSO);
       POF = new ProjectOwnerFunction(PSO);

   }

   
   public Double CalculateFitnessFunction() throws SQLException, ParseException
   {

       float A ,B;
       A = (float)  1;
       B =(float) 3;
       Double Result = 0.0;
       Result = Math.abs(A* BF.CalculateBiddersValue() - B* POF.CalculateProjectOwnerValue()) + 
               BF.getContractorsVarience();

       return Result ;
   
    }
   
   
   public double [] GetAllPlayerBenefits() throws SQLException, ParseException
   {
     BF.CalculateBiddersValue();
     double [] c1 = BF.GetContractors();
     double [] c2 = new double [Parameters.contractorsCount+1];
     double pof = POF.CalculateProjectOwnerValue();
     
     for(int i=0;i<Parameters.contractorsCount;i++)
     {
        c2[i] = c1[i]; 
     }
     c2[Parameters.contractorsCount] =pof;
     return c2;
   }
   
    public static void main(String[] args) throws SQLException
    {
              //System.out.println("Bidders : " +new FitnessFunction().CalculateFitnessFunction());
    }
    
    
}
