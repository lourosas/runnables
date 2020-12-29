/////////////////////////////////////////////////////////////////////
/*
*/
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestThreads{
   public static void main(String [] args){
      new TestThreads();
   }

   public TestThreads(){
      System.out.println("Hello World");
      try{
         ThreadTest3 tt3 = new ThreadTest3(3000);
         Thread t0 = new Thread(tt3);
         ThreadTest3 tt3_1 = new ThreadTest3(1000);
         Thread t1 = new Thread(tt3_1);
         t0.start();
         t1.start();
         Thread.sleep(20000);
         tt3_1.toActive(false);
         Thread.sleep(12000);
         tt3_1.toActive(true);
         Thread.sleep(6000);
         tt3.toActive(false);
         Thread.sleep(6000);
         tt3.toActive(true);
         //Thread.sleep(6000); //Remove
         tt3_1.toRun(false);
         tt3.toRun(false);
         System.out.println(Thread.currentThread().getName());
         System.out.println(Thread.currentThread().getId());
         Thread.sleep(200);
         t0.join();
         t1.join();
      }
      catch(InterruptedException e){e.printStackTrace();}
   }
}
