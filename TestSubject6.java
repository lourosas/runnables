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

public class TestSubject6 implements Runnable{
   private Random random = null;
   private Object o0     = null;
   private boolean toRun = true;
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TestSubject6(){
      this(new Object());
   }

   //
   //
   //
   public TestSubject6(Object o){
      this.o0 = o;
      //Get the random in here...
      this.random = new Random();
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public int requestData(){
      int rand = -1;
      synchronized(this.o0){
         rand = this.random.nextInt(100);
         System.out.println("\nTestSubject6");
         System.out.println(Thread.currentThread().getName());
         System.out.println(Thread.currentThread().getId());
         System.out.println("Random = " + rand);
      }
      return rand;
   }

   //
   //
   //
   public void stopRun(){
      //No need to worry about a race condition--only one object from
      //one Thread will call this...
      this.toRun = false;
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private long printItOut(long number){
      synchronized(this.o0){ //Baby step it
         long numb = number;
         //Baby Step development...
         System.out.println("\nTestSubject6");
         System.out.println(Thread.currentThread().getName());
         System.out.println(Thread.currentThread().getId());
         System.out.println("Number = " + numb);
         return numb;
      }
   }

   /////////////////////////Runnable Interface////////////////////////
   //
   //
   //
   public void run(){
      try{
         while(this.toRun){
            synchronized(this.o0){
               long n = Thread.currentThread().getId()%100;
               System.out.println("return = "+this.printItOut(n));
            }
            Thread.sleep(200);
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
