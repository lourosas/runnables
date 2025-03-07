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
//@Model
//By means of Controller (Not MVC, but controlls the messaging of
//objects when to enact their Behavior)
public class TestModel implements Publisher, Subscriber, Runnable{

   private Thread t0      = null;
   private TestSubject ts = null;
   private Object      o =  null;

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public TestModel(){
      this.setUpThread();
      this.ts = new TestSubject();
      ts.add(this);
      this.o = new Object();
      this.ts.addObject(o);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setUpThread(){
      this.t0 = new Thread(this,"TestModel:Publisher and Subscriber");
      this.t0.start();
   }

   ////////////////////////Publisher Interface////////////////////////
   //
   //
   //
   public void add(Subscriber s){}

   //
   //
   //
   public void error(String s, Object o){}

   //
   //
   //
   public void notify(String s, Object o){}

   //
   //
   //
   public void remove(Subscriber s){}

   ////////////////////////Subscriber Interface///////////////////////
   //
   //
   //
   public void update(Object o){}

   //
   //
   //
   public void update(Object o, String s){}

   //
   //
   //
   public void error(RuntimeException re){}

   //
   //
   //
   public void error(RuntimeException re, Object o){
      try{
         //Technically, not needed...but understanding Threading...
         synchronized(this.o){
            Integer integer = (Integer)o;
            System.out.println("\nTest Model");
            System.out.println(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getId());
            int i = integer.intValue();
            System.out.println("Error = " + i);
            System.out.println();
         }
      }
      catch(ClassCastException cce){ cce.printStackTrace(); }
   }

   //
   //
   //
   public void error(String error){}

   /////////////////////////Runtime Interface/////////////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            try{
               synchronized(this.o){
                  System.out.println("\nTest Model");
                  System.out.println("In Thread");
                  System.out.println(Thread.currentThread().getName());
                  System.out.println(Thread.currentThread().getId());
                  int data = this.ts.requestData();
                  System.out.println("Returned = "+data);
                  System.out.println();
                  Thread.sleep(1000);
               }
            }
            catch(NullPointerException npe){
               npe.printStackTrace();
               Thread.sleep(1000);
            }
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
