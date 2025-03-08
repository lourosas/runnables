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

public class TestSubject2 implements Runnable{

   private Thread t0      = null;
   private Random random  = null;
   private Object  o0     = null;
   private Object  o1     = null;

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TestSubject2(){
      this.random = new Random();
      this.setUpThread();
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void addObject0(Object obj){
      this.o0 = obj;
   }

   //
   //
   //
   public void addObject1(Object obj){
      this.o1 = obj;
   }

   //
   //
   //
   public int requestErrorValue(){
      //Stubbed out for now
      return 0;
   }

   //The Typical way to request data without having to alert the Test
   //Model.  The Test Model will request the data by means of
   //Controller
   //
   public int requestData(){
      synchronized(this.o0){
         System.out.println("TestSubject2");
         System.out.println("requestData()");
         System.out.println(Thread.currentThread().getName());
         System.out.println(Thread.currentThread().getId());
         return this.random.nextInt(10);
      }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void publishRandomNumber(){
      try{
         synchronized(this.o){
            int ranNum = this.random.nextInt(1000);
            System.out.println("\nTestSubject2");
            System.out.println("In Thread");
            System.out.println(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getId());
            System.out.println("Number = "+ranNum);
            if(ranNum == 3){
               //TBD
            }
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void setUpThread(){
      this.t0 = new Thread(this,"TestSubject2");
      this.t0.start();
   }

   /////////////////////////Runnable Interface////////////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            //Thread.sleep(10000);
            Thread.sleep(100);
            this.publishRandomNumber();
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
