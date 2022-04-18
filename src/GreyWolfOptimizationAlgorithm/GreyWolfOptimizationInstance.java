/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GreyWolfOptimizationAlgorithm;

import FitnessFunction.FitnessFunction;
import FitnessFunction.Parameters;
import BinarySearchTree.BinaryTree;
import BinarySearchTree.Node;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Tim Arden
 */
public class GreyWolfOptimizationInstance {
    
    int MinValue = 0;
    int MaxValue = 100;
    int PopulationSize;
    int ChromosomeLength;
    Node Fitness[];
    double RouletteWheelFitness[][];

    BinaryTree Bt = new BinaryTree();

    public double GenerateNextDouble(int MinValue,int MaxValue)
    {
        Random r = new Random();
        double randomValue = MinValue + (MaxValue - MinValue) * r.nextDouble();
        return randomValue;
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
    public double CalculateFitness(double [] Individual) throws SQLException, ParseException
    {
      return new FitnessFunction(Individual).CalculateFitnessFunction(); 
    }
    
    public double [][] InitializePopulation(int PopulationSize,int ChromosomeLength) throws SQLException, ParseException
    {
      // System.out.println("PopulationSize: "+PopulationSize + " "+ "ChromosomeLength: " + ChromosomeLength);
       double [][]  Population = new double [PopulationSize][];
       for(int i=0;i<PopulationSize;i++)
       {
           Population[i] = GenerateRandomArray(ChromosomeLength);
           this.Fitness[i] = new Node(i,this.CalculateFitness(Population[i]));
           this.Bt.root = this.Bt.InsertNode(this.Bt.root,this.Fitness[i] );
       }
       
       return Population;
    }

    

    
    public double Clip(double Value,double Min,double Max)
    {
        //if(Value>Max) Value = Max;
        if(Value<Min) Value = Min;
        return Value;
    }

    
    

   public double [][] GreyWolfOptimizing(double [][] offspring,int M,int Cont) throws SQLException, ParseException
    {
        Random rand = new Random();
        double MaxA = 2.0;
        int I =offspring.length;
       // System.out.println("------");
       // this.Bt.PrintTree(this.Bt.root);
       // System.out.println("------");
        Node [] bestSolutions = this.Bt.GetBestNSolution(this.Bt.root, 3);
        double a = MaxA -  M * MaxA / (double) Cont;
        for(int i=0;i<I;i++)
        {
           
            for(int j=0;j<offspring[i].length;j++)
            {
                double r1 = rand.nextDouble();
                double r2 = rand.nextDouble();

                double A1 = 2 * a * r1 - a;
                double C1 = 2 * r2;
                double DAlpha = Math.abs(C1 * offspring[bestSolutions[0].index][j] - offspring[i][j]);
                double X1 = offspring[bestSolutions[0].index][j] - A1 * DAlpha;

                r1 = rand.nextDouble();
                r2 = rand.nextDouble();

                double A2 = 2 * a * r1 - a;
                double C2 = 2 * r2;
                double DBeta = Math.abs(C2 *  offspring[bestSolutions[1].index][j] - offspring[i][j]);
                double X2 = offspring[bestSolutions[1].index][j] - A2 * DBeta;

                r1 = rand.nextDouble();
                r2 = rand.nextDouble();

                double A3 = 2 * a * r1 - a;
                double C3 = 2 * r2;
                double DDelta = Math.abs(C3 *  offspring[bestSolutions[2].index][j] - offspring[i][j]);
                double X3 = offspring[bestSolutions[2].index][j] - A3 * DDelta;

                offspring[i][j] = Clip((X1 + X2 + X3) / 3,this.MinValue,this.MaxValue);
            }
           
            
            

                this.Bt.DeleteNode( this.Fitness[i]);
                this.Fitness[i] =new Node(i,this.CalculateFitness(offspring[i])); 
                this.Bt.root=this.Bt.InsertNode(this.Bt.root, this.Fitness[i]);
               
               // offspring[i] = d.clone();
            

           
        }
        
       
        
        return offspring;
    }
   
   

       public void Check(double [][] offspring) throws SQLException, ParseException
    {
        for(int i=0;i<this.PopulationSize;i++)
        {
            if(this.Fitness[i].value!= this.CalculateFitness(offspring[i]))
            {
                System.out.println("i: " + i +  " this.CalculateFitness(offspring[i]): " +  this.CalculateFitness(offspring[i]) +" this.Fitness[i]: "+ this.Fitness[i].value );
            }
        }
    }

    
    
    public double [] GreyWolfOptimizationRun(int generations) throws SQLException, ParseException
    {
       // System.out.println("GreyWolfOptimizationRun2 ");
        int count =0;
        double [][] Population = this.InitializePopulation(this.PopulationSize,Parameters.chromosomeLength);
        double [][] offspring = Population.clone();
                double [] bestSolution = null;
        double BestFitness = Double.MAX_VALUE;
        while (count < generations)
        {
           // System.out.println("count:"+ count);


            offspring = this.GreyWolfOptimizing(offspring,count,generations);
            
            double [] a =  offspring[this.Bt.GetBestNSolution(this.Bt.root, 1)[0].index];
           // System.out.println(Arrays.toString(a));
            double f = this.CalculateFitness(a);
            if(BestFitness>f)
            {
                BestFitness = f;
                bestSolution = a.clone();
            }
            count++;
        }
        
        
       // System.out.println("Done");
        
        
        
        
        
        
        
        
        return bestSolution;//offspring[this.Bt.GetBestNSolution(this.Bt.root, 1)[0].index];
       
    }
    
    

    
    public GreyWolfOptimizationInstance(int PopulationSize,int ChromosomeLength)
    {
        this.PopulationSize = PopulationSize;
        this.ChromosomeLength = ChromosomeLength;
        Fitness = new Node [this.PopulationSize];
      
    }
}
