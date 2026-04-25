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

public class BoundedHashSetRunnable implements Runnable{
   BoundedHashSet<Integer> set;
   int number;
   int addedNumber;

   ////////////////////////Constructor////////////////////////////////
   //
   //
   //
   public BoundedHashSetRunnable(BoundedHashSet<Integer> set){
      this.set    = set;
      this.number = -1;
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void addToSet(){
      //int val = (int)(Math.random() * 2000);
      //this.set.add(Integer.valueOf(val));
      ++number;
      this.set.add(Integer.valueOf(number));
      this.addedNumber = number;
   }

   //
   //
   //
   private void removeFromSet(){
      if(this.set.remove(Integer.valueOf(addedNumber))){
         --number;
         System.out.print(addedNumber+" Removed ");
      }
      else{
         System.out.print(addedNumber+" Not Removed ");
      }
      System.out.println(Thread.currentThread().getName());
   }

   /////////////////Interface Implementation//////////////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            System.out.println("BoundedHashSetRunnable");
            this.addToSet();
            int sleep = (int)(Math.random()*10000);
            Thread.sleep(sleep);
            this.removeFromSet();
         }
      }
      catch(InterruptedException ie){}
      
   }
}
