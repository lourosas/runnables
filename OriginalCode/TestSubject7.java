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
public class TestSubject7 implements Runnable{
   private Object o0        = null;
   private Random random    = null;
   private ErrorListener el = null;
   
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TestSubject7(){
      this(new Object());
   }

   //
   //
   //
   public TestSubject7(Object o){
      this.o0     = o;
      this.random = new Random();
   }

   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void addErrorListener(ErrorListener listener){
      this.el = listener;
   }

   //
   //
   //
   public int requestData(){
      synchronized(this.o0){
         System.out.println("\nTestSubject7:requestData()");
         System.out.println(Thread.currentThread().getName());
         System.out.println("Id: "+Thread.currentThread().getId());
         return this.random.nextInt(500);
      }
   }
   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void publishRandomNumber(){
      synchronized(this.o0){
         int ranNumber = this.random.nextInt(1000);
         System.out.println("\nTestSubject7: In Thread");
         System.out.println(Thread.currentThread().getName());
         System.out.println("Id: "+Thread.currentThread().getId());
         System.out.println("Number = " + ranNumber + "\n");
         if(ranNumber == 3){
            String error = new String("Error: "+ranNumber);
            ErrorEvent e = new ErrorEvent(this, error);
            this.el.errorOccurred(e);
         }
      }
   }


   ////////////////////////Runnable Interface/////////////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            this.publishRandomNumber();
            Thread.sleep(100);
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
