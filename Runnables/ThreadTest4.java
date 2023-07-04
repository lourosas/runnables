//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;

public class ThreadTest4 implements Runnable{
   private boolean _toRun;
   private boolean _isActive;
   private int     _sleeptime;

   {
      _isActive  = true;
      _toRun     = true;
      _sleeptime = 1000;
   };

   /////////////////////////Constructors//////////////////////////////
   /**/
   public ThreadTest4(){}

   /**/
   public ThreadTest4(int sleepLength){
      this.sleepTime(sleepLength);
   }

   ///////////////Runnable Interface Implementation///////////////////
   /**/
   public void run(){
      while(this._toRun){
         try{
            synchronized(this){
               while(!this._isActive){ this.wait(); }
            }
            System.out.print(Thread.currentThread().getName());
            System.out.println(" " + Thread.currentThread().getId());
            Thread.sleep(this._sleeptime);
         }
         catch(InterruptedException ie){
            Thread.currentThread().interrupt();
         }
      }
   }

   ////////////////////////Public Methods/////////////////////////////
   /**/
   public synchronized void isActive(boolean active){
      if(!this._isActive && active){
         this._isActive = active;
         this.notify();
      }
      else{ this._isActive = active; }
   }

   /**/
   public void sleepTime(int sleepLength){
      this._sleeptime = sleepLength;
   }

   /**/
   public synchronized void toRun(boolean toRun_){
      this._toRun = toRun_;
   }
}
