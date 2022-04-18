/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FitnessFunction;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 *
 * @author Tarek
 */
public class Statistics {
    double[] data;
    int size;   
    public Statistics()
    {

    }
    public Statistics(double[] data) {
        this.data = data;
        size = data.length;
    }   

    public double getC()
    {
        double Value=0;
        for(int i=0;i<data.length;i++)
            for(int j=i;j<data.length;j++){
            //    System.out.println("data[i] : " + data[i] + " data[j] : " + data[j] + " data[i]-data[j] : " +Math.abs(data[i]-data[j]) + " Value : " +Value );
              Value+=Math.abs(data[i]-data[j]);

            }
        return Value;
    }
    double getMean() {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum/size;
    }

    double getVariance() {
        double mean = getMean();
        double temp = 0;
        for(double a :data)
            temp += (a-mean)*(a-mean);
        return temp/(size-1);
    }

    double getStdDev() {
        return Math.sqrt(getVariance());
    }

    public double median() {
       Arrays.sort(data);
       if (data.length % 2 == 0)
          return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
       return data[data.length / 2];
    }
    
    
    public  double calculateSD()
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = data.length;

        for(double num : data) {
            sum += num;
        }
        
        

        double mean = sum/length;
        for(double num: data) {
            standardDeviation += Math.pow(num - mean, 2);

        }

        return Math.sqrt(standardDeviation/length);
    }
    
        public  double calculateSD(double[][] Array)
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = 0;

        for(double[] data :Array)
            for(double num : data) {
                sum += num;
                length++;
            }
        
        

        double mean = sum/length;
        for(double[] data :Array)
            for(double num: data) {
                standardDeviation += Math.pow(num - mean, 2);

            }

        return Math.sqrt(standardDeviation/length);
    }
    
    public static void main(String[] args)
    {
        Statistics S=new Statistics(new double []{0.02569649 ,0.47781328,0.97789138, 0.13295945});
        System.out.println(new BigDecimal(S.calculateSD()).toPlainString());
        
        double [][] a = new double[2][2];
        a[0][0] = 0.02569649;
        a[0][1] = 0.47781328;
        a[1][0] = 0.97789138;
        a[1][1] = 0.13295945;
        System.out.println(new BigDecimal(S.calculateSD(a)).toPlainString());

        
        System.out.println( Math.pow(1-0.4,2));
    /*    System.out.println(new BigDecimal(Math.sqrt(S.getVariance())).toPlainString() + " , " +new BigDecimal(S.getC()).toPlainString());
        
           Statistics S1=new Statistics(new double []{0,0.1,0.01,0,100});
        System.out.println(new BigDecimal(Math.sqrt(S1.getVariance())).toPlainString() + " , " +new BigDecimal(S1.getC()).toPlainString());
    
            
            */
            }
}