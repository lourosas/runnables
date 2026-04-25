//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class Response implements Runnable{
   private boolean _quit;
   private boolean _triggered;
   private int     _sleepTime;

   {
      _quit      = false;
      _triggered = false;
      _sleepTime = 500;
   };

   /*
   */
   public Response(){}

   /*
   */
   public void quit(boolean quit_){
      this._quit = quit_;
      if(this._quit){
         this.trigger(true);
      }
   }

   /*
   */
   public void sleepTime(int sleepTime_){
      this._sleepTime = sleepTime_;
   }

   /*
   */
   public synchronized void trigger(boolean trigger_){
      this._triggered = trigger_;
      if(this._triggered){
         this.notify();
      }
   }

   /////////////////Runnable Interface Implementation/////////////////
   /*
   */
   public void run(){
      while(!this._quit){
         try{
            synchronized(this){
               while(!this._triggered){
                  this.wait();
                }
	    }
	    if(!this._quit){
               System.out.println(Thread.currentThread().getName());
            }
	    this.trigger(false);
	 }
         catch(InterruptedException e){ e. printStackTrace(); }
      }
   }

}
