/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MegroOptimizationAlgorithm;

import FitnessFunction.FitnessFunction;
import FitnessFunction.Parameters;
import FitnessFunction.Statistics;
import BinarySearchTree.BinaryTree;
import BinarySearchTree.Node;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
/**
 *
 * @author Tim Arden
 */
public class MegroOptimizationInstance {

    
    int MinValue = 0;
    int MaxValue = 100;
    int PopulationSize;
    int ChromosomeLength;
    Node Fitness[];
    double RouletteWheelFitness[][];

    BinaryTree Bt = new BinaryTree();

    
    public  double [][] UniformMutation(double [][] population) throws SQLException, ParseException
    {
        double [][]offspring = population.clone();
        for(int i = 0;i<this.PopulationSize;i++)
        {
            offspring[i] = population[i].clone();
            int k = (int)this.GenerateNextDouble(0, population[i].length);
            offspring[i][k] = this.GenerateNextDouble(this.MinValue,this.MaxValue);
            this.Bt.DeleteNode( this.Fitness[i]);
            this.Fitness[i] = new Node(i,this.CalculateFitness(offspring[i]));
            this.Bt.root=this.Bt.InsertNode(this.Bt.root,this.Fitness[i]);
        }
        return offspring;
    }
    
    

    
    public  double [][]SelectBestIndividuals(double[][] Population1,double [][] Population2,BinaryTree Bt1 ) throws SQLException, ParseException
    {
       
        //this.printArray("Start SelectBestIndividuals pop1",Population1);
        //this.printArray("Start SelectBestIndividuals pop2",Population2);
        Node []Fit1 = this.Bt.GetBestNSolution( this.Bt.root, Population1.length);
        Node []Fit2 = Bt1.GetBestNSolution( Bt1.root, Population2.length);
        BinaryTree bt2 = new BinaryTree();
        
        double offsprint[][] = new double [Population1.length][Parameters.chromosomeLength];
        
        int k =0;
        int i=0;int j=0;
        while (k<Population1.length)
        {
            if(Fit1[i].value<Fit2[j].value &&i<Population1.length)
            {
                //System.out.println("SelectBestIndividuals1");
                offsprint[k] = Population1[Fit1[i].index].clone();
                //System.out.println("K: " + k);
                this.Fitness[k] =new Node(k,Fit1[i].value);
            //   if(this.CalculateFitness(offsprint[k] ) != this.Fitness[k].value) System.out.println("No1");
                bt2.root=bt2.InsertNode(bt2.root, this.Fitness[k]);
                i++;
            }
            else
            {
                //System.out.println("SelectBestIndividuals2");
                offsprint[k] = Population2[Fit2[j].index].clone();
                this.Fitness[k] =new Node(k,Fit2[j].value);
            //    if(this.CalculateFitness(offsprint[k] ) != this.Fitness[k].value) System.out.println("No2");
                bt2.root= bt2.InsertNode(bt2.root, this.Fitness[k]);
                j++;
            }
         //   System.out.println(this.Fitness[k].value);
            k++;
        }
        
        this.Bt = bt2;
        
        
       // this.printArray("End SelectBestIndividuals",offsprint);
        return offsprint; 
    }
    
    
    
    public double GenerateNextDouble(int MinValue,int MaxValue)
    {
        Random r = new Random();
        double randomValue = MinValue + (MaxValue - MinValue) * r.nextDouble();
        return randomValue;
    } 
    
    public double Min(Node []a)
    {
        double current =Double.MAX_VALUE;
        for(int i=0;i<a.length;i++)
            if(current>a[i].value) current = a[i].value;
        return current;
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
    
    public double[][] CalculateRouletteWheelFitness()
    {
       RouletteWheelFitness = new double [PopulationSize][2]; 
       double FitnessSum =0;
       for(int i=0;i<PopulationSize;i++)
       {
           RouletteWheelFitness[i][0] = 1/(this.Fitness[i].value);
           FitnessSum+=(RouletteWheelFitness[i][0]);
       }
       RouletteWheelFitness[0][1] = RouletteWheelFitness[0][0];
       for(int i=1;i<PopulationSize;i++)
       {
            RouletteWheelFitness[i][1] = (RouletteWheelFitness[i][0] + RouletteWheelFitness[i-1][1]);
            
       }
       
       for(int i=0;i<PopulationSize;i++)
       {
       RouletteWheelFitness[i][1] = RouletteWheelFitness[i][1]/FitnessSum;
       }


       return RouletteWheelFitness;
    }
    
    
    public  int RouletteWheel()
    {
        int idx =0;
        double wheel = new Random().nextDouble();
        for(int i=0;i<this.PopulationSize;i++)
            if(wheel<this.RouletteWheelFitness[i][1])
            {
                idx =i; 
                break;
            }
        
        return idx;
    }
    
    public double Clip(double Value,double Min,double Max)
    {
        //if(Value>Max) Value = Max;
        if(Value<Min) Value = Min;
        return Value;
    }
       
    public double [][] HillClimbing2 (double [][] OffSpring,double mu) throws SQLException, ParseException
    {
         // System.out.println("HillClimbing");

          double [][] offspring_xhc = new double[2][Parameters.chromosomeLength];
          double b_offspring = 0;
          for(int w = 0; w<this.PopulationSize;w++)
          {
                              //System.out.println("Count: " + w);

            int parent1 = this.RouletteWheel();
            int parent2 = this.RouletteWheel();
            
            while(parent2 == parent1) 
            {
                parent2 = this.RouletteWheel();
                
            }
            for(int j=0;j<Parameters.chromosomeLength;j++)
            {
                
                double rand = new Random().nextDouble();
                double rand_b = new Random().nextDouble();
                if(rand <= 0.5)
                {
                    b_offspring = 2*(rand_b);
                    b_offspring = Math.pow(b_offspring, (1/(mu + 1)));
                }
                else
                if(rand>0.5)
                {
                    b_offspring = 1/(2*(1-rand_b));
                    b_offspring = Math.pow(b_offspring, (1/(mu + 1)));
                }
                //if(this.Fitness[parent1].value!= CalculateFitness(OffSpring[parent1])) System.out.println("No2");
                //if(this.Fitness[parent2].value!= CalculateFitness(OffSpring[parent2])) System.out.println("No3");

                offspring_xhc[0][j] = Clip(((1 + b_offspring)*OffSpring[parent1] [j] + (1 - b_offspring)*OffSpring[parent2][j])/2,this.MinValue,this.MaxValue);
                offspring_xhc[1][j] = Clip(((1 + b_offspring)*OffSpring[parent2] [j] + (1 - b_offspring)*OffSpring[parent1][j])/2,this.MinValue,this.MaxValue) ;

            }
            
            double offspring_xhc0 =  this.CalculateFitness(offspring_xhc[0]);
            double offspring_xhc1 =  this.CalculateFitness(offspring_xhc[1]);
            if(offspring_xhc1<offspring_xhc0)
            {
                offspring_xhc[0] = offspring_xhc[1].clone();
                offspring_xhc0 = offspring_xhc1;
            }
           
            if(this.Fitness[parent1].value<this.Fitness[parent2].value)
            {
                if(offspring_xhc0<this.Fitness[parent1].value)
                {
                   OffSpring[parent1] = offspring_xhc[0].clone();
                   this.Bt.DeleteNode( this.Fitness[parent1]);
                   this.Fitness[parent1].value = offspring_xhc0;
                   //System.out.println("4.1");
                   //this.Check(OffSpring);
                   this.Bt.root=this.Bt.InsertNode(this.Bt.root,this.Fitness[parent1]);
                }
            }
            else
            if(this.Fitness[parent2].value<this.Fitness[parent1].value)
            {
                if(offspring_xhc0<this.Fitness[parent2].value)
                {
                   OffSpring[parent2] = offspring_xhc[0].clone();
                   this.Bt.DeleteNode(  this.Fitness[parent2]);
                   this.Fitness[parent2].value = offspring_xhc0;
                   //System.out.println("4.2");
                   //this.Check(OffSpring);
                   this.Bt.root=this.Bt.InsertNode(  this.Bt.root,this.Fitness[parent2]);
                }
            }
            
           
            this.CalculateRouletteWheelFitness();
          }
        //  this.Bt.PrintTree(this.Bt.root);
          
          

          return OffSpring;
    }

    
    
    
    public double [][] Mutation(double [][] population,double MutationRate ,double eta) throws SQLException, ParseException
    {
       // this.printArray("Start Mutation",population);

        double d_mutation =0;
        double [][] OffSpring = population.clone();
        for(int i=0;i<this.PopulationSize;i++)
        {

            OffSpring[i] = population[i].clone();
            for(int j=0;j<Parameters.chromosomeLength;j++)
            {
                double probability = new Random().nextDouble();
                if(probability<MutationRate)
                {
                    double rand = new Random().nextDouble();
                    double rand_d = new Random().nextDouble();
                    if(rand<=0.5)
                    {
                        d_mutation = 2 * (rand_d);
                        d_mutation = Math.pow(d_mutation, (1/(eta+1))) -1;
                    }
                    else 
                    if(rand>0.5)
                    {
                        d_mutation = 2*(1- rand_d);
                        d_mutation = 1 - Math.pow(d_mutation, (1/(eta+1)));
                    }
                    OffSpring[i][j]= Clip((OffSpring[i][j] + d_mutation),this.MinValue, this.MaxValue);
                    
                //System.out.print("i: " + i + " "+"j: "+j);
                }
            }
            

                this.Bt.DeleteNode( this.Fitness[i]);
                this.Fitness[i] =new Node(i,this.CalculateFitness(OffSpring[i])); 
                this.Bt.root=this.Bt.InsertNode(this.Bt.root, this.Fitness[i]);
                
                this.CalculateRouletteWheelFitness();
            
        }
      //System.out.println(Arrays.toString(OffSpring[0]));
      //  this.printArray("End Mutation",OffSpring);
        return OffSpring;
    }

    
    
    public double [][] ArithmeticCrossover(double [][] population,double [][] RouletteWheelFitness,double alpha) throws SQLException, ParseException
    {

        

       double [][] population1 = new double[PopulationSize][];
       double [][] OffSpring = population.clone();
       this.CalculateRouletteWheelFitness();
        
       BinaryTree OffspringFitnessTree = new BinaryTree();
        for(int i = 0;i<this.PopulationSize;i++)
        {
            int parent1 = this.RouletteWheel();
            int parent2 = this.RouletteWheel();
            while(parent2 == parent1) 
            {
                parent2 = this.RouletteWheel();
             
            }
            //System.out.println("2");
            population1[i] = population[i].clone();
            OffSpring[i] = population[i].clone();
            for(int j=0;j<Parameters.chromosomeLength;j++)
            { 
                double x = (1 - alpha) * population[parent1] [j] + alpha * population[parent2] [j];
                OffSpring[i][j] =Clip(x,this.MinValue,this.MaxValue) ;
            }
            OffspringFitnessTree.root = OffspringFitnessTree.InsertNode(OffspringFitnessTree.root,new Node(i, this.CalculateFitness(OffSpring[i])));



            

        }


     
        //this.Bt.PrintTree(this.Bt.root);
        return this.SelectBestIndividuals(population1, OffSpring,OffspringFitnessTree).clone();
        
    }
    
   public double [][] MegroOptimizing(double [][] offspring,int M,int Cont) throws SQLException, ParseException
   {
        Random rand = new Random();
        double MaxA = 2.0;
        int I =offspring.length;
        
       // System.out.println("------");
       // this.Bt.PrintTree(this.Bt.root);
       // System.out.println("------");
        Node [] bestSolutions = this.Bt.GetBestNSolution(this.Bt.root, 3);
        double d [] = new double [Parameters.chromosomeLength];
        double a = MaxA - M * MaxA / (double) Cont;
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

                d[j] = Clip((X1 + X2 + X3) / 3,this.MinValue,this.MaxValue);
            }
           
            
            
            double d_Fitness = this.CalculateFitness(d);
           /* if(d_Fitness<  this.Fitness[i].value )
            {*/
                this.Bt.DeleteNode( this.Fitness[i]);
                this.Fitness[i] =new Node(i,d_Fitness); 
                this.Bt.root=this.Bt.InsertNode(this.Bt.root, this.Fitness[i]);
                offspring[i] = d.clone();
                this.CalculateRouletteWheelFitness();
           // }

           
        }
        
       
        
        return offspring;
    }
    
    
    public double [][] BlendCrossover(double [][] population,double alpha) throws SQLException, ParseException
    {
        
        //System.out.println("Breeding");
        //this.printArray("Start BlendCrossover",population);
        double [][] OffSpring = population.clone();
        double [][] population1 = new double[PopulationSize][];
        
        BinaryTree OffspringFitnessTree = new BinaryTree();
        double b_offspring =0;

        this.CalculateRouletteWheelFitness();
        
        for(int i = 0;i<this.PopulationSize;i++)
        {
           // if(this.Fitness[i]!= CalculateFitness(OffSpring[i])) System.out.println("No1: "+this.Fitness[i]+" "+ CalculateFitness(OffSpring[i])+" "+i);
            //System.out.println("Count: " + i);

            population1[i] = population[i].clone();
            OffSpring[i] = population[i].clone();
            int parent1 = this.RouletteWheel();
            int parent2 = this.RouletteWheel();
            while(parent2 == parent1) parent2 = this.RouletteWheel();
            //System.out.println("2");

            for(int j=0;j<Parameters.chromosomeLength;j++)
            {
                double rand = new Random().nextDouble();
                
                double gamma = (1 + 2 * alpha) * rand - alpha;
                double x = (1 - gamma) * population[parent1] [j] + gamma * population[parent2] [j];
                OffSpring[i][j] =Clip(x,this.MinValue,this.MaxValue) ;
            }

            /*
                this.Bt.DeleteNode( this.Fitness[i]);
                this.Fitness[i] =new Node(i, this.CalculateFitness(OffSpring[i])); 
                this.Bt.root=this.Bt.InsertNode(this.Bt.root, this.Fitness[i]);
           */

            //OffSpring = SortPopulationBasedOnFitness(OffSpring);
             OffspringFitnessTree.root = OffspringFitnessTree.InsertNode(OffspringFitnessTree.root,new Node(i, this.CalculateFitness(OffSpring[i])));

           // this.CalculateRouletteWheelFitness();
        }
     
        //this.printArray("End BlendCrossover",OffSpring);
        return   this.SelectBestIndividuals(population1, OffSpring,OffspringFitnessTree).clone();
        
    }
   
    
    
    
    
    
    
    public double [][] HillClimbing(double [][] population,int NumberOfNeighbors) throws SQLException, ParseException
    {
        
        double [][] offspring = population.clone();
        for(int i=0;i<offspring.length;i++)
        {
            offspring[i]= population[i].clone();
            double currentVFitnessValue = this.CalculateFitness(offspring[i]);
            double [] BestSolution  =  offspring[i].clone();
            int k=0;
            //System.out.println("HillClimbing2: " + i);
            while(k<NumberOfNeighbors)
            {
                int indx1,indx2;
                
                indx1 = ThreadLocalRandom.current().nextInt(0, offspring[i].length);
                indx2 = ThreadLocalRandom.current().nextInt(0, offspring[i].length);
                
                double currentValue = offspring[i][indx1];
                offspring[i][indx1] =  offspring[i][indx2];
                offspring[i][indx2] = currentValue;
                
                
                double NeighborFitnessValue = this.CalculateFitness(offspring[i]);
                if(currentVFitnessValue>NeighborFitnessValue)
                {
                    BestSolution = offspring[i].clone();
                    //System.out.println("Yes: "+ currentVFitnessValue+ " , " +NeighborFitnessValue );
                    currentVFitnessValue = NeighborFitnessValue;
                    this.Bt.DeleteNode( this.Fitness[i]);
                    this.Fitness[i] = new Node(i,NeighborFitnessValue);
                    this.Bt.root=this.Bt.InsertNode(this.Bt.root,this.Fitness[i]);
                    
                }
                k++;
                
            }
            offspring[i] = BestSolution.clone();
            
            
        }
        return offspring;
    }
    
    
    public void printArray(String title,double [][] offSpring) throws SQLException, ParseException
    {
        System.out.println(title);
        for(int i=0;i<offSpring.length;i++)
        {
            System.out.println("i: "+ i+ " "+ this.CalculateFitness(offSpring[i]));
            System.out.println(Arrays.toString(offSpring[i]));
             
        }
                
        
        System.out.println();
    }
    public double [] MegroAlgorithm(int generations,int NumberofNeighbors) throws SQLException, ParseException
    {
        int count =0;
        double [][] Population = this.InitializePopulation(this.PopulationSize,Parameters.chromosomeLength);
        this.CalculateRouletteWheelFitness();
        double [][] offspring = Population.clone();
        double [] bestSolution = null;
        double BestFitness = Double.MAX_VALUE;
        while (count < generations)
        {
           // System.out.println("count:"+ count);

           // offspring = this.BlendCrossover(offspring,0.9);
           //this.printArray("After BlendCrossover",offspring);
            offspring = this.MegroOptimizing(offspring,count,generations);
            //System.out.println("1");
            //this.Check(offspring);
            offspring = this.UniformMutation(offspring);//,mu,eta);
            //this.printArray("After Mutation",offspring);

            //System.out.println("2");
            //this.Check(offspring);
            offspring = this.HillClimbing(offspring, NumberofNeighbors);
            //System.out.println("3");
            //this.Check(offspring);

            //System.out.println("4");
           // this.Check(offspring);
            double [] a =  offspring[this.Bt.GetBestNSolution(this.Bt.root, 1)[0].index];
            double f = this.CalculateFitness(a);
            if(BestFitness>f)
            {
                BestFitness = f;
                bestSolution = a.clone();
            }
            count++;
        }
        
        
        //System.out.println("Done");
        
        
        
        
        
        
        
        
        return bestSolution;
       
    }
    
    
    public void Check(double [][] offSpring) throws SQLException, ParseException
    {
        double [][] offspring = offSpring.clone();
        for(int i=0;i<this.PopulationSize;i++)
        {
            offspring[i] = offSpring[i].clone();
            if(this.Fitness[i].value!= this.CalculateFitness(offspring[i])|| i != this.Fitness[i].index)
            {
                System.out.println("i: " + i +  " this.CalculateFitness(offspring[i]): " +  this.CalculateFitness(offspring[i]) +" this.Fitness[i]: "+ this.Fitness[i].value + " this.Fitness[i].index: " + this.Fitness[i].index );
            }
        }
        
        Node d[] = this.Bt.GetBestNSolution(this.Bt.root, this.PopulationSize);
        int Count1 =0,Count2 =0;
        for(int i=0;i<d.length;i++)
        {
            for(int j=0;j<this.Fitness.length;j++)
            {
                if(d[i].index == this.Fitness[j].index)
                {
                   // System.out.println("j: "+ j +" this.Fitness[j].index: "+ this.Fitness[j].index);
                    Count1 ++;
                    if(d[i].value != this.Fitness[j].value)
                    {
                         //System.out.println("No  j: "+ j +" this.Fitness[j].index: "+ this.Fitness[j].index);
                        
                        Count2 ++;
                        throw new NullPointerException("Error in Data");
                    }
                }
            }
        }
        System.out.println("Count1: " + Count1 + " Count2: " + Count2 + " Count3: " + this.Bt.CountTreeNode(this.Bt.root));
    }
    
    public MegroOptimizationInstance(int PopulationSize,int ChromosomeLength)
    {
        this.PopulationSize = PopulationSize;
        this.ChromosomeLength = ChromosomeLength;
        Fitness = new Node [this.PopulationSize];
      
    }
}
