/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import rosas.lou.runnables.*;

public class Worker implements Runnable{
   private final CountDownLatch startGate;
   private final CountDownLatch endGate;
   static Object obj = new Object();

   /**/
   public Worker
   (
      CountDownLatch start,
      CountDownLatch end
   ){
      this.startGate = start;
      this.endGate   = end;
   }

   /**/
   public void run(){
      try{
         this.startGate.await();
         synchronized(obj){
            this.doWork();
         }
         this.endGate.countDown();
      }
      catch(InterruptedException ie){}
   }

   /**/
   private void doWork(){
      System.out.print("doWork():  ");
      System.out.println(Thread.currentThread().getName());
      try{
         Thread.sleep(1000);
      }catch(InterruptedException ie){}
   }
}
