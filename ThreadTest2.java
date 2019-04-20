/////////////////////////////////////////////////////////////////////
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;

public class ThreadTest2 implements Runnable{
   private String threadName;
   private boolean isTriggered;

   {
      threadName  = null;
      isTriggered = false;
   };

   //////////////////////Constructors///////////////////////////////
   /*
   Constructor (real simple, must set the name)
   */
   public ThreadTest2(String name){
      this.threadName = new String(name);
   }

   /////////////////////Public Methods///////////////////////////////
   /*
   */
   public void setTrigger(){
      this.isTriggered = true;
   }

   ////////////////Implementation of the Runnable Interface//////////
   /*
   */
   public void run(){
      int SLEEPTIME = 1000;
      while(true){
         this.goAheadAndPrint();
         System.out.println(Thread.currentThread().getName());
      }
   }

   ////////////////////Private Methods///////////////////////////////
   /*
   Only Print if Triggered
   */
   private synchronized void goAheadAndPrint(){
      try{
         while(!isTriggered){
            this.wait();
         }
         System.out.println("Name:  " + this.threadName);
         this.setTrigger(false);
      }
      catch(InterruptedException ie){
         Thread.currentThread().interrupt();
      }
      catch(IllegalMonitorStateException e){ e.printStackTrace(); }
      finally{
         this.notifyAll();
      }
   }
   /*
   */
   private void setTrigger(boolean value){
      this.isTriggered = value;
   }
}
