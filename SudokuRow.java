//////////////////////////////////////////////////////////////////////
/*
Copyright 2023 Lou Rosas

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
import rosas.lou.runnables.*;

public class SudokuRow extends SudokuGroup implements Runnable{
   
   {
      indices    = null;
      block      = null;
      isSolved   = false;
      solveIt    = false;
      values     = null;
      tempValues = null;
   };

   //////////////////////Constructor//////////////////////////////////
   //
   //
   //
   public SudokuRow(){
      this.indices = new int[TOTAL];
      this.solveIt = false;
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   public void setTempValues(int index){
      try{
         this.tempValues.clear();
      }
      catch(NullPointerException npe){
         this.tempValues = new LinkedList<Integer>();
      }
      //Step 1:  Calculate the Cube Number
      int row        = index/(TOTAL*3);
      //row /= 3;
      //Step 2:  Caculate the Column Number
      int col        = index % TOTAL;
      int offset     = col/3;
      int cubeNumber = row*3 + offset;
      //Step 3:  Get the Cube Number Indices, set the temp values
      int cubeRow  = cubeNumber/3;
      int startRow = cubeRow*27;
      try{
         for(int i = 0; i < 3; ++i){
            int idx = startRow + (cubeNumber%3)*3 + i;
            int val = this.block[idx].value().intValue();
            if(val > 0){
               this.tempValues.add(Integer.valueOf(val));
            }
         }
         for(int i = 3; i < 6; ++i){
            int idx = startRow + (cubeNumber%3)*3 + i + 6;
            int val = this.block[idx].value().intValue();
            if(val > 0){
               this.tempValues.add(Integer.valueOf(val));
            }
         }
         for(int i = 6; i < 9; ++i){
            int idx = startRow + (cubeNumber%3)*3 + i + 12;
            int val = this.block[idx].value().intValue();
            if(val > 0){
               this.tempValues.add(Integer.valueOf(val));
            }
         }
         for(int i = 0; i < TOTAL; ++i){
            //Step 4:  Get the Column Number Indices, set the 
            //temp values
            int idx = col + TOTAL*i;
            int val = this.block[idx].value().intValue();
            if(val > 0){
               this.tempValues.add(Integer.valueOf(val));
            }
         }

      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      catch(IndexOutOfBoundsException ooe){
         ooe.printStackTrace();
      }
   }

   ///////////////////////SudokuGroup Overrides///////////////////////
   //
   //
   //
   public void indices(int index){
      if(index > -1 && index < TOTAL){
         for(int i = 0; i < TOTAL; ++i){
            this.indices[i] = index*TOTAL + i;
         }
      }
   }

   //
   //
   //
   public void print(){
      for(int i = 0; i < TOTAL; ++i){
         System.out.println(this.indices[i]);
      }
      for(int i = 0; i < TOTAL; ++i){
         System.out.println(this.block[this.indices[i]].value());
      }
   }

   ////////////////Runnable Interface Implementation//////////////////
   //
   //
   //
   public void run(){
      int sleepTime = 50;
      boolean toRun = true;
      while(toRun){
         try{
            if(this.solveIt){
               for(int i = 0; i < this.indices.length; ++i){
                  int idx = this.indices[i];
                  int val = this.block[idx].value().intValue();
                  if(val < 0){
                     this.setTempValues(idx);
                     int j = 1;
                     boolean toContinue = true;
                     while(toContinue && j < 10){
                        //This will need to change...
                        Integer current = Integer.valueOf(j);
                        if(!this.values.contains(current) &&
                           !this.tempValues.contains(current)){
                           this.block[idx].value(j);
                           this.values.add(Integer.valueOf(j));
                           toContinue = false;
                        }
                        ++j;
                     }
                  }
               }
               this.solve(false);
               this.solved(true);
            }
            Thread.sleep(sleepTime);
         }
         catch(InterruptedException ie){
            toRun = false;
         }
      }
   }
}
//////////////////////////////////////////////////////////////////////
