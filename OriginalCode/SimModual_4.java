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

public class SimModual_4 implements Runnable{
   private Lock lock        = new ReentrantLock();
   private boolean _toRun   = true;
   private boolean _toKill  = false;
   private int     _val     = 0;

   //////////////////////////Constructor//////////////////////////////
   //
   //
   //
   public SimModual_4(){}

   /////////////////////////Public Methods////////////////////////////
   //
   //
   //
   public void kill(){
      System.out.println("kill() "+Thread.currentThread().getName());
      this._toKill = true;
   }

   //
   //
   //
   public void setRun(boolean run){
      this._toRun = run;
      System.out.print("run() "+this._toRun+" ");
      System.out.println(Thread.currentThread().getName());
   }

   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private void printOut(){
      System.out.print("Unprotected Region:  ");
      System.out.println(Thread.currentThread().getName());
      try{
         lock.lock();
         System.out.print("Protected Region:  ");
         System.out.print(Thread.currentThread().getName()+":");
         long id = Thread.currentThread().getId();
         System.out.println(id);
         if(id == 12){
            this._val += 1;
         }
         else if(id == 13){
            //this._val += 3;
         }
         else if(id == 14){
            //this._val -= 2;
            this._val -=1;
         }
         System.out.println("_val = "+this._val);
         Thread.sleep(3000);
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
      finally{
         lock.unlock();
      }
   }

   ///////////////////Interface Implementations///////////////////////
   ///////////////////////Runnable Interface//////////////////////////
   //
   //
   //
   public void run(){
      int sleepTime = 1000;
      try{
         while(!this._toKill){
            if(this._toRun){
               this.printOut();
               //Thread.sleep(sleepTime);
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
