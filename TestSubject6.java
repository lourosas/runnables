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
      return -1;
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private long printItOut(long number){
      return -1;
   }

   /////////////////////////Runnable Interface////////////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
