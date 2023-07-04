/////////////////////////////////////////////////////////////////////
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.TimeUnit;


/**/
public class PrimitiveObserver implements Runnable{
   private String    name;
   private boolean   isTriggered;
   private boolean   shouldRun;
   private Lock      lock;
   private Condition triggered;
   private Calendar  cal;

   {
      name         = null;
      isTriggered  = false;
      shouldRun    = false;
      lock         = null;
      triggered    = null;
   };

   //////////////////////Constructors////////////////////////////////
   /*
   */
   public PrimitiveObserver(){
      this("");
   }

   /*
   */
   public PrimitiveObserver(String name){
      this.name      = new String(name);
      //this.lock      = new ReentrantLock();
      //this.triggered = lock.newCondition();
      this.setRun(true);
   }

   ///////////////////////Public Methods/////////////////////////////
   /*
   */
   public void setRun(boolean run){
      this.shouldRun = run;
   }

   /*
   */
   public void setTrigger(){
      //this.lock.lock();
      this.isTriggered = true;
      //this.lock.unlock();
   }

   /////////////Implementation of the Runnable Interface/////////////
   /*
   */
   public void run(){
      while(this.shouldRun){
         this.goAheadAndPrint();
      }
   }

   /////////////////////////Private Methods//////////////////////////
   /*
   */
   private void clearTrigger(){
      this.isTriggered = false;
   }

   /*
   */
   private void goAheadAndPrint(){
      boolean isReturned = false;
      //this.lock.lock();
      try{
         this.cal = Calendar.getInstance();
         //System.out.println(Thread.currentThread()+"0 "+cal.getTimeInMillis() + "");
         while(!this.isTriggered){
            //isReturned=this.triggered.await(50, TimeUnit.MILLISECONDS);
            //See if this works
            Thread.currentThread().sleep(50);
         }
         this.cal = Calendar.getInstance();
         //System.out.println(Thread.currentThread()+"1 "+cal.getTimeInMillis() + "");
         System.out.println(Thread.currentThread());
         System.out.println(Thread.currentThread().getName());
         System.out.println("Name: " + this.name);
         System.out.println("isReturned:  " + isReturned + "");
         System.out.println("isTriggered: "+this.isTriggered+"\n");
         //System.out.println("isTriggered: "+this.isTriggered);
         //this.triggered.signalAll();
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
      finally{
         this.clearTrigger();
         //this.lock.unlock();
      }
      /*
      this.lock.lock();
      if(this.isTriggered){
         //System.out.println(Thread.currentThread());
         //System.out.println(Thread.currentThread().getName());
         System.out.println("Name: " + this.name);
         this.clearTrigger();
      }
      this.lock.unlock();
      */
   }
}
