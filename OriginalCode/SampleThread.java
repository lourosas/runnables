//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class SampleThread implements Runnable{
   public int processingCount = 0;

   ////////////////////////Constructor////////////////////////////////
   /**/
   public SampleThread(){}

   /**/
   public SampleThread(int count){
      this.processingCount = count;
   }

   /////////////////////////Runnable implementation///////////////////
   /**/
   public void run(){
      System.out.print("Thread: "+Thread.currentThread().getName());
      System.out.println(" started");
      while(this.processingCount > 0){
         try{
            System.out.println(this.processingCount);
            Thread.sleep(1000);
         }
         catch(InterruptedException ie){
            System.out.print("Thread: ");
            System.out.print(Thread.currentThread().getName() + " ");
            System.out.println("interrupted");
         }
         finally{ --this.processingCount;}
      }
      System.out.print("Thread: "+Thread.currentThread().getName());
      System.out.println(" exiting");
   }
}
