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
//@Model
//Instead of Publisher being a Subscriber, try a wait()/notify()
//approach on the same object
public class TestModel2 implements Publisher, Runnable{
   private Thread t0       = null;
   private TestSubject2 ts = null;
   private Object o0       = null;
   private Object o1       = null;

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TestModel2(){
      this.ts = new TestSubject2();
      this.setUpThread();
      this.o0 = new Object();
      this.o1 = new Object();
      this.ts.addObject0(this.o0);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void querySubject(){
      try{
         synchronized(this.o0){
            System.out.println("\nTestModel2");
            System.out.println("In Thread");
            System.out.println(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getId());
            int data = this.ts.requestData();
            System.out.println("Returned = "+data+"\n");
         }
      }
      catch(NullPointerException npe){ npe.printStackTrace(); }
   }

   //
   //
   //
   private void setUpThread(){
      this.t0 = new Thread(this,"TestModel2:Publisher Only");
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

   /////////////////////////Runnable Interface////////////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            this.querySubject();
            Thread.sleep(1000);
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
