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

public class SimModual_0 implements Runnable{
   private boolean _toRun  = true;
   private boolean _toKill = false;

   ///////////////////////Constructor/////////////////////////////////
   //
   //
   //
   public SimModual_0(){}

   //////////////////////////Public Methods///////////////////////////
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
      System.out.println("run() "+this._toRun+" "+
                                    Thread.currentThread().getName());
   }


   ///////////////////////Interface Implementatino////////////////////
   /////////////////////////Runnable Interface////////////////////////
   //
   //
   //
   public void run(){
      int sleepTime = 1000;//Sleep for a second
      int count     = 0;
      try{
         while(!this._toKill){
            if(this._toRun){
               Thread.sleep(sleepTime);
               System.out.println(Thread.currentThread().getName());
            }
         }
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
   }
}
//////////////////////////////////////////////////////////////////////
