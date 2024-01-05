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

public class SudokuCube extends SudokuGroup implements Runnable{

   {
      indices              = null;
      block                = null;
      isSolved             = false;
      solveIt              = false;
      unUsedCombos         = 0;
      values               = null;
      tempValues           = null;
      unUsedValues         = null;
      unUsedPositions      = null;
      unUsedPositionsList  = null;
   };

   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public SudokuCube(){
      this.indices = new int[TOTAL];
      this.solveIt = false;
   }

   ///////////////////////SudokuGroup Overrides///////////////////////
   //
   //
   //
   public void indices(int index){
      if(index > -1 && index < 9){
         int cubeRow  = index/3;
         int startRow = 9*cubeRow*3;
         for(int i = 0; i < 3; ++i){
            int idx = startRow + (index%3)*3 + i;
            this.indices[i] = idx;
         }
         for(int i = 3; i < 6; ++i){
            int idx = startRow + (index%3)*3 + i + 6;
            this.indices[i] = idx;
         }
         for(int i = 6; i < 9; ++i){
            int idx = startRow + (index%3)*3 + i + 12;
            this.indices[i] = idx;
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

   ////////////////////////Protected Methods//////////////////////////
   //
   //
   //
   protected void setTempValues(int index){
      try{
         this.tempValues.clear();
      }
      catch(NullPointerException npe){
         this.tempValues = new LinkedList<Integer>();
      }
      try{
         //Essentially, save all the values in the respective
         //Row and Column to compare the current value...
         int row = index/TOTAL;
         int col = index % TOTAL;
         //Row
         for(int i = 0; i < TOTAL; ++i){
            int idx = row*TOTAL + i;
            int val = this.block[idx].value().intValue();
            if(val > 0){
               this.tempValues.add(Integer.valueOf(val));
            }
         }
         //Column
         for(int i = 0; i < TOTAL; ++i){
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
      catch(IndexOutOfBoundsException oob){
         oob.printStackTrace();
      }
   }

   /////////////////Runnable Interface Implementation/////////////////
   //
   //
   //
   public void run(){
      int sleepTime = 50;
      boolean toRun = true;
      while(toRun){
         try{
            if(this.solveIt){
               this.setUnusedValues();
               /*
               for(int i = 0; i < this.indices.length; ++i){
                  int idx     = this.indices[i];
                  Integer val = this.block[idx].value();
                  if(val < 0){
                     this.setValues();
                     this.setTempValues(idx);
                     Integer cur        = null;
                     Iterator<Integer>it=this.unUsedValues.iterator();
                     boolean toContinue = true;
                     while(it.hasNext() && toContinue){
                        cur = it.next();
                        boolean inVal = this.values.contains(cur);
                        boolean inTemp= this.tempValues.contains(cur);
                        if(!inVal && !inTemp){
                           this.block[idx].value(cur);
                           this.values.add(cur);
                           toContinue = false;
                        }
                     }
                  }
               }
               if(!this.isSolvedCorrect()){
                  System.out.println("Cube: "+this.findFirstBlankIndex());
                  System.out.println(this.unUsedValues.size());
               }
               int attemptedCombos = 0;
               */
               int attempts = 0;
               while(!this.isSolvedCorrect()){
                  System.out.println("Cube");
                  System.out.println("Attempts: "+attempts);
                  this.resetAll(false);
                  this.setGroupValues();
                  ++attempts;
                  if(attempts > 24) break;
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
