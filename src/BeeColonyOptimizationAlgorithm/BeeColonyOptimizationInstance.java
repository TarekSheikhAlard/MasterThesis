/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeeColonyOptimizationAlgorithm;

import FitnessFunction.Parameters;
import FitnessFunction.FitnessFunction;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Random;

/**
 *
 * @author Tarek
 */
public class BeeColonyOptimizationInstance {
    
    int SwarmPopulationSize = 8;
   // int Iteration=15;
   // int DimintionCount = 1;
    int Limit = 8;
    int NumberOfFoodSource = 4;
    double [][]FoodSource;
    int ChromosomeLength;
    double [] Fitness,Trial;
    int rangeMax =100;
    int rangeMin =0;
    BeeColonyOptimizationInstance( int swarmPopulationSize) throws SQLException, ParseException
    {
        
        ChromosomeLength = Parameters.chromosomeLength;
        this.SwarmPopulationSize = swarmPopulationSize;
        Limit = SwarmPopulationSize/2*ChromosomeLength;
        NumberOfFoodSource = SwarmPopulationSize / 2;
        Fitness = new double[NumberOfFoodSource];
        Trial = new double[NumberOfFoodSource];

        Random rand = new Random();
        FoodSource = new double[NumberOfFoodSource][];
        for(int i=0;i< NumberOfFoodSource;i++)
        {
            FoodSource[i]= new double[ChromosomeLength];
            for(int j=0;j<FoodSource[i].length;j++)
            {
                  FoodSource[i][j] = GenerateNextDouble();
            }
            Fitness[i] = new FitnessFunction( FoodSource[i]).CalculateFitnessFunction();
        }
    }
    
    
    public double GenerateNextDouble()
    {
        Random r = new Random();
        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        return randomValue;
    }
    
    public  void Run(int N) throws SQLException, ParseException
    {
        for(int i=0;i<N;i++)
        {
            sendEmployedBees();
            sendOnlookerBees();
            sendScoutBees();
        }

    }
    
     public void sendEmployedBees() throws SQLException, ParseException {
        int neighborBeeIndex = 0;
        for(int i = 0; i < NumberOfFoodSource; i++) {
            //A randomly chosen solution is used in producing a mutant solution of the solution i
            //neighborBee = getRandomNumber(0, Food_Number-1);
            neighborBeeIndex = getExclusiveRandomNumber(NumberOfFoodSource-1, i);

            SendToWork(i, neighborBeeIndex);
        }
    }
     
     public void SendToWork(int i,int neighborBeeIndex) throws SQLException, ParseException
     {
       int j = getExclusiveRandomNumber( this.FoodSource[i].length, -1);  
       
       double Y,Yp,Ynew,fi;
       Y =  this.FoodSource[i][j];
       Yp = this.FoodSource[neighborBeeIndex][j];
       Random rand = new Random();
       fi = rand.nextDouble();
       Ynew = Y  + fi * ( Y  - Yp);
       
       
       double [] currentSolution = this.FoodSource[i].clone();
       currentSolution[j] = Ynew;
       
       double currentFitness =  new FitnessFunction(currentSolution).CalculateFitnessFunction();
       if(currentFitness< this.Fitness[i])
       {
            this.Fitness[i] = currentFitness;
            this.FoodSource[i] = currentSolution.clone();
       }
       else
       {
           this.Trial[i] ++ ;
       }
       
       
     }


     
    public int getExclusiveRandomNumber(int high, int except) {
        boolean done = false;
        int getRand = 0;
        Random rand = new Random();
        while(!done) {
            getRand = rand.nextInt(high);
            if(getRand != except){
                done = true;
            }
        }

        return getRand;     
    }
    
    public void sendOnlookerBees() throws SQLException, ParseException
    {
        int i = 0;
        int t = 0;
        int neighborBeeIndex = 0;
       // double [] currentBee = null;
      //  double [] neighborBee = null;

        while(t < NumberOfFoodSource) {
          //  currentBee = FoodSource[i];
            Random rand = new Random();
            if(rand.nextDouble() < this.Probability(this.Fitness[i])) {
                t++;
                neighborBeeIndex = getExclusiveRandomNumber(NumberOfFoodSource-1, i);
	           // neighborBee = FoodSource[neighborBeeIndex];
	         SendToWork(i, neighborBeeIndex);
            }
            i++;
            if(i == NumberOfFoodSource) {
                i = 0;
            }
        }
    }
   
    
    public double Probability( double d)
    {
        return d/this.FitnessSum();
    }
    
    
    public int getBestSolution()
    {
        int k=0;
        double minFitness = 0;
        for(int i=0;i<this.Fitness.length;i++)
        {
            if(minFitness > this.Fitness[i])
            {
                minFitness = this.Fitness[i];
                k=i;
            }
        }
        
        return k;
    }
    
    public double [] GetSolution()
    {
        return this.FoodSource[this.getBestSolution()];
    }
    
    public double FitnessSum()
    {
        double res=0;
        for(int i=0;i<this.NumberOfFoodSource;i++) res+=this.Fitness[i];
        return res;
    }
    
    public  double[] GenerateRandomArray(int Length)
    {
        double [] a = new double [Length];
        Random rand = new Random();
        for (int i=0; i <Length;i++)
        {
            a[i] = GenerateNextDouble();
        }
        return a;
    }

    public void sendScoutBees() {
        double []  currentBee = null;

        for(int i =0; i < NumberOfFoodSource; i++) {
           
            if(this.Trial[i] >= this.Limit) {

               this.Trial[i] =0;
               FoodSource[i] = GenerateRandomArray(this.ChromosomeLength);
            }
        }
    }
    
}
