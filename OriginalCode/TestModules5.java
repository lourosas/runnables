//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.SimModual_6;
import rosas.lou.runnables.SimModual_7;
import rosas.lou.runnables.SimModual_8;

public class TestModules5{
   public static void main(String[] args){
      new TestModules5();
   }

   public TestModules5(){
      Object o = new Object();
      System.out.println("Hello World");
      SimModual_6 sim6 = new SimModual_6(o);
      SimModual_7 sim7 = new SimModual_7(o);
      SimModual_8 sim8 = new SimModual_8(o);
      Thread t0        = new Thread(sim6);
      Thread t1        = new Thread(sim7);
      Thread t2        = new Thread(sim8);
      t0.start();
      t1.start();
      t2.start();
      try{
         Thread.sleep(10000);
         /*
         sim6.setRun(false);
         Thread.sleep(30000);
         sim8.setRun(false);
         Thread.sleep(15000);
         sim7.setRun(false);
         Thread.sleep(60000);
         sim6.setRun(true);
         Thread.sleep(3000);
         sim8.setRun(true);
         Thread.sleep(14000);
         sim7.setRun(true);
         Thread.sleep(10000);
         */
         sim7.pause();
         Thread.sleep(16000);
         sim8.pause();
         Thread.sleep(14000);
         sim6.pause();
         Thread.sleep(10000);
         synchronized(o){o.notifyAll();}
         /*
         synchronized(o){
            o.notify();
            //Thread.sleep(1);
            o.notify();
            //Thread.sleep(1);
            o.notify();
         }
         */
         //Thread.sleep(36000);
         //Thread.sleep(1);
         sim6.pause();
         //Thread.sleep(1);
         sim8.pause();
         Thread.sleep(13000);
         synchronized(o){o.notifyAll();}
         sim7.pause();
         sim8.pause();
         Thread.sleep(16000);
         synchronized(o){o.notifyAll();}
         sim6.pause();
         sim7.pause();
         Thread.sleep(30000);
         synchronized(o){o.notifyAll();}
         Thread.sleep(1000);
         sim6.kill();
         sim7.kill();
         sim8.kill();
         t0.join();
         System.out.println("Poop");
         t1.join();
         System.out.println("Poop");
         t2.join();
         System.out.println("Poop");
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////