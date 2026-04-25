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

public class SimModual_8 implements Runnable{
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

   /////////////////////////Constructor///////////////////////////////
   //
   //
   //
   public SimModual_8(Object o){
      this._o = o;
   }

   /////////////////////////Public Methods////////////////////////////
   //
   //
   //
   public void alert(){
      this._toNotify = true;
   }

   //
   //
   //
   public void kill(){
      this._toKill = true;
   }

   //
   //
   //
   public void pause(){
      this._lock.lock();
      this._toWait = true;
      this._lock.unlock();
   }

   //
   //
   //
   public void setRun(boolean run){
      this._toRun = run;
   }


   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void check(){
      if(this._toNotify){
         synchronized(this._o){
            this._o.notify();
         }
         this._toNotify = false;
      }
   }

   //
   //
   //
   private void go(){
      this._lock.lock();
      if(this._toWait){
         try{
            synchronized(this._o){
               System.out.println("SimModual_8--wait");
               this._o.wait();
               System.out.println("SimModual_8--out of wait");
            }
         }
         catch(InterruptedException ie){
            ie.printStackTrace();
         }
         finally{
            this._toWait = false;
         }
      }
      this._lock.unlock();
      System.out.print("SimModual_8.go(): ");
      System.out.println(Thread.currentThread().getName());
   }

   //////////////////Interface Implementation/////////////////////////
   //
   //
   //
   public void run(){
      int sleepTime = 5000;
      try{
         while(!this._toKill){
            if(this._toRun){
               this.go();
               Thread.sleep(sleepTime);
               this.check();
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
