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
import java.io.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;
public class TestSubject implements Publisher,Runnable{

   private Thread t0             = null;
   private Subscriber subscriber = null;
   private Random     random     = null;
   private Object     o          = null;

   
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TestSubject(){
      this.setUpThread();
      this.random = new Random();
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void addObject(Object obj){
      this.o = obj;
   }

   //The Typical way to request data without having to alert the
   //Test Model.  The Test Model will request this data (by means of
   //Controller)
   //
   public int requestData(){
      //Technically, not needed...already obtained in TestModel
      synchronized(this.o){
         System.out.println("Test Subject");
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
   private void setUpThread(){
      this.t0 = new Thread(this,"TestSubject:Publisher-TestSubject");
      this.t0.start();
   }

   ////////////////////////Publisher Interface////////////////////////
   //
   //
   //
   public void add(Subscriber s){
      this.subscriber = s;
   }

   //
   //
   //
   public void error(String s, Object o){
      try{
         //Put in a mutually exclusive block, to be safe...
         synchronized(this.o){
            this.subscriber.error(new RuntimeException(s), o);
         }
      }
      catch(NullPointerException npe){ npe.printStackTrace(); }
   }

   //
   //
   //
   public void notify(String s, Object o){}

   //
   //
   //
   public void remove(Subscriber s){}

   /////////////////////////Runnable Interface////////////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            try{
               synchronized(this.o){
                  //Thread.sleep(10000);
                  Thread.sleep(100);
                  int ranNum = this.random.nextInt(1000);
                  System.out.println("\nTest Subject");
                  System.out.println("In Thread");
                  System.out.println(Thread.currentThread().getName());
                  System.out.println(Thread.currentThread().getId());
                  System.out.println("Number = "+ranNum);
                  System.out.println();
                  if(ranNum == 3){ //Indicate an error
                     Integer i = Integer.valueOf(ranNum);
                     this.error("Runtime Number Error", i);
                  }
               }
            }
            catch(NullPointerException npe){}
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
