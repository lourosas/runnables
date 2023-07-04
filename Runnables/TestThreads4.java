//////////////////////////////////////////////////////////////////////
//
//
//
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestThreads4{
   public static void main(String [] args){
      new TestThreads4();
   }

   public TestThreads4(){
      System.out.println("Hello World");
      Station zebra = new Station();
      Random rand = new Random();
      try{
         Thread t0 = new Thread(zebra);
         Thread t1 = new Thread(zebra);
         Thread t2 = new Thread(zebra);
         Thread t3 = new Thread(zebra);
         Thread t4 = new Thread(zebra);
         Thread t5 = new Thread(zebra);
         
         t0.start();

         int sleep = rand.nextInt(5000);
         System.out.println("Sleep: " + sleep);
         Thread.sleep(sleep);
         t1.start();

         sleep = rand.nextInt(500);
         System.out.println("Sleep: "+sleep);
         Thread.sleep(sleep);
         t2.start();

         sleep = rand.nextInt(1500);
         System.out.println("Sleep: "+sleep);
         Thread.sleep(sleep);
         t3.start();

         sleep = rand.nextInt(2500);
         System.out.println("Sleep: "+sleep);
         Thread.sleep(sleep);
         t4.start();

         sleep = rand.nextInt(1000);
         System.out.println("Sleep: "+sleep);
         Thread.sleep(sleep);
         t5.start();
      }
      catch(InterruptedException ie){}
      //zebra.runAnother();
   }
}
/////////////////////////////////////////////////////////////////////
