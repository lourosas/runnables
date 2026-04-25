//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;
import rosas.lou.runnables.*;

public class ThreadTest3 implements Runnable{
   private boolean     _toRun;
   private boolean     _isActive;
   private int         _sleeptime;
   private ThreadTest4 _tt4;
   
   {
      _isActive  = true;
      _toRun     = true;
      _sleeptime = 1000;
      _tt4       = null;
   };

   ///////////////////////////Constructors////////////////////////////
   /**/
   public ThreadTest3(){}

   /**/
   public ThreadTest3(int sleepLength){
      this.sleeptime(sleepLength);
   }

   ////////////////Runnable Interface Implementation//////////////////
   /*
   For me, this is the perfect example of simple multi-threading in
   Java...
   */
   public void run(){
      int currentCount = 0;
      while(this._toRun){
         try{
            synchronized(this){
               while(!this._isActive){
                  currentCount = 0;
                  this.wait();
               }
            }
            System.out.print(Thread.currentThread().getName());
            System.out.println(" " + Thread.currentThread().getId());
            Thread.sleep(this._sleeptime);
            ++currentCount;
            if(currentCount%30 == 0){
               this._tt4.isActive(true);
            }
            if(currentCount%50 == 0){ 
               this._tt4.isActive(false);
            }
            if(currentCount >= 1000){
               this._tt4.toRun(false);
               this.toRun(false);
            }
         }
         catch(InterruptedException ie){
            Thread.currentThread().interrupt();
         }
      }
   }

   ////////////////////////Public Methods/////////////////////////////
   /**/
   public void addThreadTest4(ThreadTest4 tt4){
      this._tt4 = tt4;
   }

   /**/
   public void sleeptime(int sleepLength){
      this._sleeptime = sleepLength;
   }

   /**/
   public synchronized void toActive(boolean isActive){
      this._isActive = isActive;
      if(this._isActive){
         this.notify();
         //this.notifyAll();
      }
   }

   /**/
   public synchronized void toRun(boolean toRun_){
      this._toRun = toRun_;
   }
   
}

//////////////////////////////////////////////////////////////////////
