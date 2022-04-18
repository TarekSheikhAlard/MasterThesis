/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BinarySearchTree;
/**
 *
 * @author Tim Arden
 */
public class Node {
   public int index;
   public double value;
   public Node Parent,right,left;
   public Node(int inx,double val)
   {
       this.index = inx;
       this.value = val;
   }
}
