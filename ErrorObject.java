//////////////////////////////////////////////////////////////////////
/*
Copyright 2025 Lou Rosas

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
import java.io.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;
public class ErrorObject{
   private int     errorValue;
   private boolean isError = false;

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public ErrorObject(){}

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public int get(){
      boolean hold = Thread.currentThread().holdsLock(this);
      System.out.println("get() hold " + hold);
      synchronized(this){
         System.out.println("get():  lock obtained");
         System.out.println(Thread.currentThread().getId());
         while(!this.isError){
            try{
               hold = Thread.currentThread().holdsLock(this);
               System.out.println(hold);
               this.wait(); //Wait for an error
               hold = Thread.currentThread().holdsLock(this);
               System.out.println("get() post wait() hold: "+hold);
               System.out.println(Thread.currentThread().getId());
            }
            catch(InterruptedException ie){}
         }
         this.isError = false;
         try{
            this.notify();
         }
         catch(IllegalMonitorStateException e){
            e.printStackTrace();
         }
         return this.errorValue;
      }
   }

   //
   //
   //
   public void set(int error){
      boolean hold = Thread.currentThread().holdsLock(this);
      System.out.println("set() hold " + hold);
      synchronized(this){
         System.out.println("set():  lock obtained");
         System.out.println(Thread.currentThread().getId());
         while(this.isError){
            try{
               hold = Thread.currentThread().holdsLock(this);
               System.out.println(hold);
               this.wait(); //Wait if there is already an error
               hold = Thread.currentThread().holdsLock(this);
               System.out.println("set() post wait() hold: "+hold);
            }
            catch(InterruptedException ie){}
         }
         this.errorValue = error;
         this.isError = true;
         try{
            this.notify();
            hold = Thread.currentThread().holdsLock(this);
            //See What happens...
            System.out.println("set() hold post notify()" + hold);
            System.out.println(Thread.currentThread().getId());
         }
         catch(IllegalMonitorStateException e){
            e.printStackTrace();
         }
      }
      hold = Thread.currentThread().holdsLock(this);
      System.out.println("set() hold " + hold);
      System.out.println(Thread.currentThread().getId());
   }
}
//////////////////////////////////////////////////////////////////////
