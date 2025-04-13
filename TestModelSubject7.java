//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestModelSubject7{
   //
   public static void main(String[] args){
      new TestModelSubject7();
   }

   //
   public TestModelSubject7(){
      System.out.println("Hello World");
      Object o = new Object();
      TestSubject7 ts7 = new TestSubject7(o);
      TestModel7   tm7 = new TestModel7(o);
      tm7.setTestSubject(ts7);
      Thread t0 = new Thread(tm7,"TestModel7:Thread0");
      Thread t1 = new Thread(tm7,"TestModel7:Thread1");
      Thread t2 = new Thread(tm7,"TestModel7:Thread2");
      Thread t3 = new Thread(tm7,"TestModel7:Thread3");
      Thread t4 = new Thread(tm7,"TestModel7:Thread4");

      Thread t5 = new Thread(ts7,"TestSubject7:Thread0");
      Thread t6 = new Thread(ts7,"TestSubject7:Thread1");
      Thread t7 = new Thread(ts7,"TestSubject7:Thread2");
      Thread t8 = new Thread(ts7,"TestSubject7:Thread3");
      Thread t9 = new Thread(ts7,"TestSubject7:Thread4");

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
         Thread.sleep(2);
         t1.start();
         Thread.sleep(1);
         t2.start();
         Thread.sleep(10);
         t6.start();
         t3.start();
         t7.start();
         t4.start();
         Thread.sleep(1);
         t8.start();
         t9.start();
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
