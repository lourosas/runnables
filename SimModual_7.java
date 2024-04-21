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

public class SimModual_7 implements Runnable{
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
   public SimModual_7(Object o){
      this._o = o;
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void alert(){
      this._o.notify();
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
      try{
         this._o.wait();
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
   }

   //
   //
   //
   public void setRun(boolean run){
      this._toRun = run;
   }

   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private void go(){
      System.out.print("SimModual_7.go(): ");
      System.out.println(Thread.currentThread().getName());
   }

   ///////////////////Interface Implementations///////////////////////
   //
   //
   //
   public void run(){
      int sleepTime = 3000;
      try{
         while(!this._toKill){
            if(this._toRun){
               //No locks for this one...try this out...
               this.go();
               Thread.sleep(sleepTime);
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
