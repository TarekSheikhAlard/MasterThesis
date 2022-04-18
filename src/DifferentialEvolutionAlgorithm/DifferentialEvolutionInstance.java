/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DifferentialEvolutionAlgorithm;

import FitnessFunction.FitnessFunction;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Random;

/**
 *
 * @author Tarek
 */
public class DifferentialEvolutionInstance {
    int MinValue = 0;
    int MaxValue = 100;
    int PopulationSize;
    int ChromosomeLength;
    double Fitness[];
    double bestFitness;
    double [] bestSolution;
    public DifferentialEvolutionInstance(int PopulationSize,int ChromosomeLength)
    {
        this.PopulationSize = PopulationSize;
        this.ChromosomeLength = ChromosomeLength;
        Fitness = new double [this.PopulationSize];
    }
    public double GenerateNextDouble(int MinValue,int MaxValue)
    {
        Random r = new Random();
        double randomValue = MinValue + (MaxValue - MinValue) * r.nextDouble();
        return randomValue;
    }
    
    public double CalculateFitness(double [] Individual) throws SQLException, ParseException
    {
      return new FitnessFunction(Individual).CalculateFitnessFunction(); 
    }

    public void RefreshPopulationFitness(double [][] Population) throws SQLException, ParseException
    {
        for(int i=0;i<this.PopulationSize;i++)
        {
            //System.out.println("Population[i]: "+i+" "+Population[i].length);
            this.Fitness[i] = CalculateFitness(Population[i]); 
        }
    }
    public  double[] GenerateRandomArray(int Length)
    {
        double [] a = new double [Length];
        Random rand = new Random();
        for (int i=0; i <Length;i++)
        {
            a[i] = GenerateNextDouble(this.MinValue,this.MaxValue);
        }
        return a;
    }
    
    public double [][] InitializePopulation(int PopulationSize,int ChromosomeLength)
    {
       double [][]  Population = new double [PopulationSize][];
       for(int i=0;i<PopulationSize;i++)
       {
           Population[i] = GenerateRandomArray(ChromosomeLength);
       }
       
       return Population;
    }
    
    
    
    public double [] Velocity(double [][] Population,double [] BestGlobal,int k0,double mutate,double recombination)
    {
        double [] bestGlobal = BestGlobal.clone();
        int k1 =0,k2=0;
        Random r = new Random();
        k1 =  r.nextInt(PopulationSize);
        k2 =  r.nextInt(PopulationSize);
        while(k1 ==k2) k1 =  r.nextInt(PopulationSize);
        for(int i=0; i<bestGlobal.length;i++)
        {
            double ri = new Random().nextDouble();
            if(ri<=recombination)
            {
                bestGlobal[i] = BestGlobal[i] + mutate* (Population[k1][i]-Population[k2][i]);
            }
            else
            {
                bestGlobal[i] = Population[k0][i];
            }
            
            /*  if (bestGlobal[i] > MaxValue)
            bestGlobal[i] = MaxValue;
              else*/ if (bestGlobal[i] < MinValue)
            bestGlobal[i] = MinValue;
        }
        
        return bestGlobal;
    }
    
    public double [][] SortPopulationBasedOnFitness( double [][] population )
    {
        double [][] Population = population.clone();
        for(int i=0;i<this.PopulationSize;i++)
        for(int j=i;j<this.PopulationSize;j++)
        {
            if(this.Fitness[i]>this.Fitness[j])
            {
                double currentFitness = this.Fitness[i];
                this.Fitness[i] = this.Fitness[j];
                this.Fitness[j] = currentFitness;
                
                double [] currentIndv = Population[i];
                Population[i] =Population[j];
                Population[j] = currentIndv;
            }
        }
        return Population;
    }
    
    public void Run(int Iterations ,double  mutate ,double  recombination) throws SQLException, ParseException
    {
        this.bestSolution = this.DifferentialEvolution(Iterations, mutate, recombination);
    }
    
    public double [] getSolution() {return this.bestSolution;}
    
    public double [] DifferentialEvolution(int Iterations ,double  mutate ,double  recombination) throws SQLException, ParseException
    {
        double [][] Population = this.InitializePopulation(this.PopulationSize,this.ChromosomeLength);
        this.RefreshPopulationFitness(Population);
        Population  = this.SortPopulationBasedOnFitness(Population);
        double [] BestGlobalSolution = Population[0];
        this.bestFitness = this.CalculateFitness(BestGlobalSolution);
        int Count = 0;
        while(Count<=Iterations)
        {
            //System.out.println("Count: "+Count);
            for(int i=0;i<this.PopulationSize;i++)
            {
               // if(this.Fitness[i] != this.CalculateFitness(Population[i])) System.out.println("No: " +i+ " " + Count);
                double [] Vi = this.Velocity(Population, BestGlobalSolution, i, mutate, recombination);
                double Fitness = this.CalculateFitness(Vi);
                
                if(Fitness<this.Fitness[i])
                {
                   this.Fitness[i] =  Fitness;
                   Population[i] = Vi;
                }
                
                if(Fitness<this.bestFitness)
                {
                    this.bestFitness = Fitness;
                    BestGlobalSolution = Vi.clone();
                }
            }
            Count++;
        }
        
        return BestGlobalSolution;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
}
