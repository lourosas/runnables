//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestModules{
   public static void main(String[] arg){
      new TestModules();
   }

   public TestModules(){
      System.out.println("Hello World");
      SimModual_0 sim0 = new SimModual_0();
      SimModual_1 sim1 = new SimModual_1();
      SimModual_2 sim2 = new SimModual_2();
      Thread t0 = new Thread(sim0);
      Thread t1 = new Thread(sim1);
      Thread t2 = new Thread(sim2);
      t0.start();
      try{
         //Sleep before setting up the second thread
         Thread.sleep(10000);
      }
      catch(InterruptedException ie){}
      t2.start();
      try{
         Thread.sleep(7000);
      }
      catch(InterruptedException ie){}
      t1.start();
      try{
         Thread.sleep(10000);
         sim0.setRun(false);
         Thread.sleep(10000);
         sim1.setRun(false);
         Thread.sleep(5000);
         sim0.setRun(true);
         Thread.sleep(15000);
         sim2.setRun(false);
	 System.exit(0);
      }
      catch(InterruptedException ie){}
   }
}

//////////////////////////////////////////////////////////////////////
