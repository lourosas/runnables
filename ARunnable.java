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
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import rosas.lou.runnables.*;

public class ARunnable implements Runnable{
   
   /////////////////////////Constructor///////////////////////////////
   //
   //
   //
   public ARunnable(){}

   //////////////////Interface Implementation/////////////////////////
   //
   //
   //
   public void run(){
      int sleepTime = 2000;
      System.out.println(Thread.currentThread().getName()+" start");
      try{
         Thread.sleep(sleepTime);
         System.out.println(Thread.currentThread().getName()+" end");
      }
      catch(InterruptedException ie){}
   }

}

//////////////////////////////////////////////////////////////////////
