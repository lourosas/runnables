//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestModules2{
   public static void main(String[] args){
      new TestModules2();
   }

   public TestModules2(){
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
         /*
         Thread.sleep(10000);
         sim6.pause();
         Thread.sleep(30000);
         sim7.pause();
         Thread.sleep(15000);
         sim8.pause();
         //sim8.alert();
         //synchronized(o){ o.notify(); }
         Thread.sleep(10000);
         synchronized(o){ o.notifyAll(); }
         //synchronized(o){ o.notify(); }
         //sim8.alert();
         Thread.sleep(30000);
         sim7.pause();
         sim8.pause();
         */
         Thread.sleep(30000);
         synchronized(o){ o.notifyAll(); }
         sim6.pause();
         sim8.pause();
         Thread.sleep(30000);
         synchronized(o){ o.notifyAll(); }
         sim6.pause();
         sim7.pause();
         Thread.sleep(30000);
         synchronized(o){ o.notifyAll(); }
         Thread.sleep(30000);
         sim6.kill();
         sim7.kill();
         sim8.kill();
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
