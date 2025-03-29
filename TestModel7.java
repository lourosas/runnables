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
public class TestModel7 implements Publisher, Runnable, ErrorListener{
   public Object       o0 = null;
   public TestSubject7 ts = null;
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TestModel7(Object o){
      this.o0 = o;
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void setTestSubject(TestSubject7 subject){
      this.ts = subject;
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void printOutFromSubject(int value){
      synchronized(this.o){}
   }

   //////////////////////ErrorListener Interface//////////////////////
   //
   //
   //
   public void errorOccurrend(ErrorEvent e){}

   ///////////////////////Publisher Interface/////////////////////////
   //
   //
   //
   public void addSubscriber(Subscriber s){}

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
      try{}
      catch()
   }
}
//////////////////////////////////////////////////////////////////////
