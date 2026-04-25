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
public class TestSubject5 implements Publisher, Runnable{
   private Thread t0             = null;
   private Subscriber subscriber = null;
   private Random random         = null;
   private Object o              = null;
   private int value             = -1;

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TestSubject5(){
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
      //synchronized(this.o){
         System.out.println("TestSubject5.requestData()");
         System.out.println(Thread.currentThread().getName());
         System.out.println(Thread.currentThread().getId());
         return this.random.nextInt(10);
      //}
   }

   //
   //
   //
   public void value(int val){
      //synchronized(this.o){
      this.value = val;
      System.out.println("\nTestSubject5.value()");
      System.out.println(Thread.currentThread().getName());
      System.out.println(Thread.currentThread().getId());
      System.out.println("Value = "+this.value+"\n");
      //}
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void publishRandomNumber(){
      try{
         //synchromized(this.o){
            int ranNum = this.random.nextInt(1000);
            System.out.println("\nTestSubject5: In Thread");
            System.out.println(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getId());
            System.out.println("Number = "+ranNum+"\n");
            if(ranNum == 3){
               Integer i = Integer.valueOf(ranNum);
               this.error("Runtime Number Error", i);
            }
         //}
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void setUpThread(){
      this.t0 = new Thread(this,"TestSubject5:Publisher");
      this.t0.start();
      System.out.print("TestSubject5: Thread Number = ");
      System.out.println(this.t0.getId());
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
         //synchronized(this.o){
            this.subscriber.error(new RuntimeException(s), o);
         //}
      }
      catch(NullPointerException npe){}
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
      //try{
         while(true){
            //Thread.sleep(100);
            //this.publishRandomNumber();
            this.value(3);
         }
      //}
      //catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
