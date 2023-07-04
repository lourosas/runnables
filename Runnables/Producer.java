/////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class Producer implements Runnable{
   private String [] buffer = new String[8];
   private int       pi     = 0; //produce index
   private int       gi     = 0; //get index
   private long      start  = 0; //Start time

   //////////////////////Constructors////////////////////////////////
   /*
   */
   public Producer(){
      this.start = System.currentTimeMillis();
   }

   ///////////////////////Public Methods/////////////////////////////
   /*
   Only consume when there is something to consume.
   */
   //public synchronized String consume(){
   public synchronized void consume(){
      String returnString = null;
      System.out.println("1 " + Thread.currentThread().getName());
      //System.out.print("(1) ");
      //System.out.println("consume() pi: "+this.pi+" gi: "+this.gi);
      //While the buffer is empty
      while(this.pi == this.gi){
         try{
            this.wait();
         }
         catch(InterruptedException ie){}
      }
      //System.out.print("(2) ");
      //System.out.println("consume() pi: "+this.pi+" gi: "+this.gi);
      System.out.println("consume() buffer: "+(this.gi & 0x7));
      returnString = this.buffer[this.gi & 0x7];
      ++this.gi;
      System.out.println("2 " + Thread.currentThread().getName());
      //System.out.print("(3) ");
      //System.out.println("consume() pi: "+this.pi+" gi: "+this.gi);
      System.out.println("Consumed: " + returnString);
      this.notifyAll();
      //return returnString;
   }

   ////////////Implementation of the Runnable Interface//////////////
   /*
   */
   public void run(){
      while(true){
         this.produce();
      }
   }

   ///////////////////////Private Methods////////////////////////////
   /*
   */
   private String banana(){
      return "" + (int)(System.currentTimeMillis() - start);
   }

   /*
   Only produce something when the buffer is not full.
   While the buffer is full (cannot hold anymore) wait until
   something gets consumed.
   */
   private synchronized void produce(){
      //System.out.print("(1) ");
      //System.out.println("produce() pi: "+this.pi+" gi: "+this.gi);
      //While there is NOT room in the buffer
      //This is to ensure the correct buffer size
      System.out.println("3 " + Thread.currentThread().getName());
      while(this.pi - this.gi + 1 > this.buffer.length){
         try{
            this.wait();
         }
         catch(InterruptedException ie){}
      }
      //System.out.print("(2) ");
      //System.out.println("produce() pi: "+this.pi+" gi: "+this.gi);
      buffer[this.pi&(0x7)] = this.banana();
      //System.out.println("produce() buffer: "+(this.pi & 0x7));
      System.out.println("produced["+(this.pi&0x7)+"] " + 
                                           this.buffer[this.pi&0x7]);
      System.out.println("4 " + Thread.currentThread().getName());
      System.out.println();
      ++this.pi;
      this.notifyAll();
   }
}
