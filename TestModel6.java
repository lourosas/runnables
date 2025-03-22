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

public class TestModel6 implements Runnable{
   private Object o0 = null;
   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public TestModel6(){
      this.o0 = new Object();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private long printItOut(long number){
      synchronized(this.o0){
         //Set up a simple fucking number...
         long numb = number;
         System.out.println("\nTestModel6");
         System.out.println(Thread.currentThread().getName());
         System.out.println(Thread.currentThread().getId());
         System.out.println("Number = "+number);
         return numb;
      }
   }

   /////////////////////////Runnable Interface////////////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            //long number = Thread.currentThread().getId()%100;
            Thread.sleep(100);
            //Without the lock, print values could be out of sync...
            //which couls lead to a race condition in different
            //renditions...
            synchronized(this.o0){
               System.out.println("ret = " + this.printItOut(
                                 Thread.currentThread().getId()%100));
            }
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
