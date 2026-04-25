//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestModelSubject6{
   //
   public static void main(String[] args){
      new TestModelSubject6();
   }

   //
   public TestModelSubject6(){
      Object o = new Object();
      TestSubject6 ts6 = new TestSubject6(o);
      TestModel6 tm6   = new TestModel6(o);
      tm6.setTestSubject(ts6);
      Thread t0 = new Thread(tm6,"TestModel6:Thread0");
      Thread t1 = new Thread(tm6,"TestModel6:Thread1");
      Thread t2 = new Thread(tm6,"TestModel6:Thread2");
      Thread t3 = new Thread(tm6,"TestModel6:Thread3");
      Thread t4 = new Thread(tm6,"TestModel6:Thread4");

      Thread t5 = new Thread(ts6,"TestSubject6:Thread0");
      Thread t6 = new Thread(ts6,"TestSubject6:Thread1");
      Thread t7 = new Thread(ts6,"TestSubject6:Thread2");
      Thread t8 = new Thread(ts6,"TestSubject6:Thread3");
      Thread t9 = new Thread(ts6,"TestSubject6:Thread4");

      System.out.println("Pre-Start: "+t0.getName()+", "+t0.getId());
      System.out.println("Pre-Start: "+t1.getName()+", "+t1.getId());
      System.out.println("Pre-Start: "+t2.getName()+", "+t2.getId());
      System.out.println("Pre-Start: "+t3.getName()+", "+t3.getId());
      System.out.println("Pre-Start: "+t4.getName()+", "+t4.getId());

      System.out.println("Pre-Start: "+t5.getName()+", "+t5.getId());
      System.out.println("Pre-Start: "+t6.getName()+", "+t6.getId());
      System.out.println("Pre-Start: "+t7.getName()+", "+t7.getId());
      System.out.println("Pre-Start: "+t8.getName()+", "+t8.getId());
      System.out.println("Pre-Start: "+t9.getName()+", "+t9.getId());

      t0.start();
      t5.start();
      try{
         Thread.sleep(2); //See what the fuck happens here
         t1.start();
         Thread.sleep(1);
         t2.start();
         //Sleep for a long period of time before start Thread 3
         Thread.sleep(3);
         t3.start();
         t4.start();
         Thread.sleep(2);
         t6.start();
         Thread.sleep(1);
         t7.start();
         Thread.sleep(10);
         t8.start();
         t9.start();
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
