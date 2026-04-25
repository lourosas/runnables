/////////////////////////////////////////////////////////////////////
/*
*/

package rosas.lou.runnables;

import java.util.*;
import java.lang.*;

public class ThreadTest implements Runnable{
   private String threadName;
   private boolean trigger;
   private int count;

   {
      threadName = null;
      trigger    = false;
      count      = 0;
   };

   ///////////////////Constructors///////////////////////////////////
   /*
      Constructor (real simple, must set the name)
   */
   public ThreadTest(String name){
      this.threadName = new String(name);
   }

   ///////////////////Public Methods/////////////////////////////////
   /*
   Trigger the thread to run (somehow)
   */
   public void setTrigger(){
      this.trigger = true;
   }

   /////////Implementation of the Runnable Interface/////////////////
   /*
   */
   public void run(){
      int SLEEPTIME = 1000;
      while(true){
         try{
            if(this.trigger){
               System.out.println("Name:  " + this.threadName);
               this.setTrigger(false);
            }
            ++count;
            System.out.println(Thread.currentThread().getName() +" " + count);
            System.out.println();
            Thread.sleep(SLEEPTIME);
         }
         catch(InterruptedException ie){
            Thread.currentThread().interrupt();
         }
      }
   }

   ///////////////////Private Methods////////////////////////////////
   private void setTrigger(boolean value){
      this.trigger = value;
   }
}
