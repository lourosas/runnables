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

public class SimModual_1 implements Runnable{
   private boolean _toRun  = true;
   private boolean _toKill = false;

   ///////////////////////Constructor/////////////////////////////////
   //
   //
   //
   public SimModual_1(){}

   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void kill(){
      System.out.println(Thread.currentThread().getName());
      this._toKill = true;
   }

   //
   //
   //
   public void setRun(boolean run){
      System.out.println(Thread.currentThread().getName());
      this._toRun = run;
   }


   /////////////////////////Interface Implementation//////////////////
   //
   //
   //
   public void run(){
      int sleepTime = 1500; //Sleep for 1.5 seconds
      try{
         while(!this._toKill){
            if(this._toRun){
               Thread.sleep(sleepTime);
               System.out.println(Thread.currentThread().getName());
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
