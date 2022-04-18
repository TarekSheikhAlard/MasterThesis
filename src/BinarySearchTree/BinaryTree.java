/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BinarySearchTree;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Tim Arden
 */
public class BinaryTree {
    
    public Node root = null;
    Node[] bestSolution;
    int i =0;
    public BinaryTree()
    {
      
        
    }
    
    public Node InsertNode(Node current,Node node)
    {
   
        
        
        if(current ==null)
        {
             return node; 
        }
        
        if(node.value<current.value)
        {
            current.left = InsertNode(current.left,node);
            current.left.Parent = current;
        }
        else
        {
             current.right = InsertNode(current.right,node);
             current.right.Parent = current;
        }
        return current; 
    }
    
    public void PrintTree (Node current)
    {
        if(current ==null) return;
       
        PrintTree(current.left);
        System.out.println("(" + current.index + " , " + current.value + ")");
        PrintTree(current.right);
        
    }
    
    public Node SearchNode(Node current,Node SearchNode)
    {
       
            
        if(current ==null) return null;
       
        if(current == SearchNode) return current;
        
        Node Left = SearchNode(current.left,SearchNode);
        Node Right = SearchNode(current.right,SearchNode);
        
        if(Right!= null) return Right;
        return Left;
    }
    
    boolean print = false;
    public Node [] GetBestNSolution(Node current,int N,boolean print)
    {
       this.bestSolution = new Node[N];
       this.i=0;
       this.print = print;
       GetBestSolution(current,N);
            
       return  this.bestSolution;

    }
    
    public Node [] GetBestNSolution(Node current,int N)
    {
       this.bestSolution = new Node[N];
       this.i=0;
       this.print = false;
       GetBestSolution(current,N);
            
       return  this.bestSolution;

    }
    
    public void GetBestSolution(Node current,int N)
    {
        
    
       if(current ==null||i>=N) return ;
         
        GetBestSolution(current.left,N);
        
        
        if(i>=N) return;
        if(this.print)
        System.out.println("(" + current.index + " , " + current.value  +" , " + i+")");
        this.bestSolution[i] = current;
        i++;
       
     
        GetBestSolution(current.right,N);
  
            
    }
    
    
    public void DeleteNode(Node DeleteNode)
    {
        Node parent = DeleteNode.Parent;
        if(DeleteNode.Parent == null && DeleteNode.left ==null &&DeleteNode.right ==null )
        {
            this.root = null;
            return;
        }
        if(parent ==null)
        {
                Node newnode = DeleteNode.right;
                if(newnode!=null)
                {
                while(newnode.left!=null)
                {
                   newnode = newnode.left;
                }
                
                
                
                
                DeleteNode(newnode);
                root = newnode;
                newnode.left = DeleteNode.left;
                newnode.right = DeleteNode.right;
                newnode.Parent = DeleteNode.Parent;
                
                
                if(newnode.left!=null)
                newnode.left.Parent = newnode;
                 if(newnode.right!=null)
                newnode.right.Parent = newnode;
                }
                else
                {
                      newnode = DeleteNode.left;
                       while(newnode.right!=null)
                        {
                           newnode = newnode.right;
                        }

                
                
                
                DeleteNode(newnode);
                root = newnode;
                newnode.left = DeleteNode.left;
                newnode.right = DeleteNode.right;
                newnode.Parent = DeleteNode.Parent;
                
                
                if(newnode.left!=null)
                newnode.left.Parent = newnode;
                 if(newnode.right!=null)
                newnode.right.Parent = newnode;
                }

        }
        else
        if(DeleteNode.left == null && DeleteNode.right ==null)
        {
            if(parent.left == DeleteNode)parent.left =null;
            if(parent.right == DeleteNode)parent.right =null;
            
           
        }
        else
        if(DeleteNode.left == null && DeleteNode.right !=null)
        {  
            if(parent.left == DeleteNode) 
            {
                Node newnode = DeleteNode.right;
                
                parent.left = newnode;
                newnode.Parent = parent;
            }
            else
            {
              Node newnode = DeleteNode.right;
              parent.right = newnode;
              newnode.Parent = parent;
            }
        }
        else
        if(DeleteNode.left != null && DeleteNode.right ==null)
        {  
            if(parent.left == DeleteNode) 
            {
                Node newnode = DeleteNode.left;
                DeleteNode.left.Parent = newnode;
                parent.left = newnode;
                newnode.Parent = parent;
            }
            else
            {
              Node newnode = DeleteNode.left;
              DeleteNode.left.Parent = newnode;
              parent.right = newnode;
              newnode.Parent = parent;
            }
        }
        
        else
        if(DeleteNode.left != null && DeleteNode.right !=null)
        {  
            if(parent.left == DeleteNode) 
            {
                Node newnode = DeleteNode.right;
                while(newnode.left!=null)
                {
                   newnode = newnode.left;
                }
                
                
                
                
                DeleteNode(newnode);
                parent.left = newnode;
                newnode.left = DeleteNode.left;
                newnode.right = DeleteNode.right;
                newnode.Parent = DeleteNode.Parent;
                
                if(newnode.left!=null)
                newnode.left.Parent = newnode;
                if(newnode.right!=null)
                newnode.right.Parent = newnode;
            }
            else
            {
                Node newnode = DeleteNode.left;
                while(newnode.right!=null)
                {
                   newnode = newnode.right;
                }
                
                
                
                
                DeleteNode(newnode);
                parent.right = newnode;
                newnode.left = DeleteNode.left;
                newnode.right = DeleteNode.right;
                newnode.Parent = DeleteNode.Parent;
                                
                if(newnode.left!=null)
                
                   newnode.left.Parent = newnode;
                
                if(newnode.right!=null)
                newnode.right.Parent = newnode;
            }
        }
        DeleteNode.left = null;
        DeleteNode.right = null;
        DeleteNode.Parent = null;
        
       
    }
    
    
    public int CountTreeNode(Node current)
    {
        if(current==null) return 0;
         return CountTreeNode(current.left)+ CountTreeNode(current.right) +1;
    }
    
    public static void  main(String[] args)
    {
        int N =20;
        BinaryTree bt  = new BinaryTree();
        Node []x = new Node [N];
        for(int i=0;i<N;i++)
        {
            x[i] = new Node(i,new Random().nextDouble()*100);
            bt.root = bt.InsertNode(bt.root,x[i]);
        }
        

        for(int i=0;i<N;i++)
        {
            
          Node parent =x[i].Parent;
          if(parent !=null)
          {
            System.out.println("(" + parent.index + " , " + parent.value + ")" + " , (" + x[i].index + " , " + x[i].value + ")"+ " , (" + x[i].left + " , " + x[i].right + ")");

          }
        }
        System.out.println();
        bt.PrintTree(bt.root);
        
        
        while(true)
        {
            
            Scanner myObj = new Scanner(System.in);  
            bt.DeleteNode(x[myObj.nextInt()]);


                 

            for(int i=0;i<N;i++)
            {

              Node parent =x[i].Parent;
              if(parent !=null)
              {
                System.out.println("(" + parent.index + " , " + parent.value + ")" + " , (" + x[i].index + " , " + x[i].value + ")");
                
              }
            }
            System.out.println();
            bt.PrintTree(bt.root);
            System.out.println("-------------");
        }
        
        
    }
    
    
}
