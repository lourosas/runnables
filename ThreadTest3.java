//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;

public class ThreadTest3 implements Runnable{
   private boolean _toRun;
   private boolean _isActive;
   private int     _sleeptime;
   
   {
      _isActive  = true;
      _toRun     = true;
      _sleeptime = 1000;
   };

   ///////////////////////////Constructors////////////////////////////
   /**/
   public ThreadTest3(){}

   /**/
   public ThreadTest3(int sleepLength){
      this.sleeptime(sleepLength);
   }

   ////////////////Runnable Interface Implementation//////////////////
   public void run(){
      while(this._toRun){
         /*
         if(this._isActive){
            try{
               System.out.println(Thread.currentThread().getName());
               System.out.println(Thread.currentThread().getId());
               Thread.sleep(this._sleeptime);
            }
            catch(InterruptedException ie){
               Thread.currentThread().interrupt();
            }
         }
         */
         try{
            synchronized(this){
               while(!this._isActive){
                  this.wait();
               }
            }
            System.out.println(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getId());
            Thread.sleep(this._sleeptime);
         }
         catch(InterruptedException ie){
            Thread.currentThread().interrupt();
         }
      }
   }

   ////////////////////////Public Methods/////////////////////////////
   /**/
   public void sleeptime(int sleepLength){
      this._sleeptime = sleepLength;
   }

   /**/
   public synchronized void toActive(boolean isActive){
      this._isActive = isActive;
      if(this._isActive){
         this.notify();
      }
   }

   /**/
   public synchronized void toRun(boolean toRun_){
      this._toRun = toRun_;
   }
   
}

//////////////////////////////////////////////////////////////////////
