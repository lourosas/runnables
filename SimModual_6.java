//////////////////////////////////////////////////////////////////////
/*
Copyright 2024 Lou Rosas

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import rosas.lou.runnables.*;

public class SimModual_6 implements Runnable{
   private Lock     _lock;
   private Object   _o;
   private boolean  _toRun;
   private boolean  _toKill;
   private boolean  _toWait;
   private boolean  _toNotify;
   private int      _val;

   {
      _lock         = new ReentrantLock();
      _toRun        = true;
      _toKill       = false;
      _toWait       = false;
      _toNotify     = false;
      _val          = 0;
      _o            = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public SimModual_6(Object o){
      this._o = o;
   }

   /////////////////////Public Methods////////////////////////////////
   //
   //
   //
   public void kill(){
      this._toKill = true;
   }

   //
   //
   //
   public void setRun(boolean run){
      this._toRun = run;
   }

   //
   //
   //
   public void setToNotify(boolean toNotify){
      this._toNotify = toNotify;
   }

   //
   //
   //
   public void setToWait(boolean toWait){
      this._toWait = toWait;
   }

   ////////////////////Private Methods////////////////////////////////
   //
   //
   //
   private void alert(){
      if(this._toNotify){
         synchronized(this._o){
            this._o.notify();
         }
      }
   }

   //
   //
   //
   private void go(){
      if(this._toWait){
         try{
            //Wait until notified...
            synchronized(this._o){
               this._o.wait();
            }
         }
         catch(InterruptedException ie){
            ie.printStackTrace();
         }
      }
      System.out.print("SimModel_6.go(): ");
      System.out.println(Thread.currentThread().getName());
   }

   ///////////////////Interface Implementations///////////////////////
   //
   //
   //
   public void run(){
      int sleepTime = 1000;
      try{
         while(!this._toKill){
            if(this._toRun){
               try{
                  this._lock.lock();   //Probably not needed
                  this.go();
               }
               finally{
                  this._lock.unlock(); //Probably not needed
               }
               Thread.sleep(sleepTime);
               try{
                  this._lock.lock();   //Probably not needed
                  this.alert();
               }
               finally{
                  this._lock.unlock(); //Probably not needed
               }
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
   }


}

//////////////////////////////////////////////////////////////////////
