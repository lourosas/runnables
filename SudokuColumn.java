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

public class SudokuColumn extends SudokuGroup implements Runnable{

   {
      indices             = null;
      block               = null;
      isSolved            = false;
      solveIt             = false;
      unUsedCombos        = 0;
      values              = null;
      tempValues          = null;
      unUsedValues        = null;
      unUsedPositions     = null;
      unUsedPositionsList = null; 
   };

   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public SudokuColumn(){
      this.indices = new int[TOTAL];
      this.solveIt = false;
   }

   ///////////////////////SudokuGroup Overrides///////////////////////
   //Check the Column for the value...
   //
   //
   public boolean contains(int row, int col, int num){
      boolean isIn = false;

      for(int i = 0; (i < TOTAL && !isIn); ++i){
         isIn = (this.block[i][col].value() == num );
      }
      return isIn;
   }

   //
   //
   //
   public void indices(int index){
      if(index > -1 && index < TOTAL){
         for(int i = 0; i < TOTAL; ++i){
            this.indices[i] = index + TOTAL*i;
         }
      }
   }

   //
   //
   //
   public void print(){
      /*
      for(int i = 0; i < TOTAL; ++i){
         System.out.println(this.indices[i]);
      }
      for(int i = 0; i < TOTAL; ++i){
          System.out.println(this.block[this.indices[i]].value());
      }
      */
   }

   ////////////////////////Protected Methods//////////////////////////
   //
   //
   //
   protected void setTempValues(int index){
      /*
      try{
         this.tempValues.clear();
      }
      catch(NullPointerException npe){
         this.tempValues = new LinkedList<Integer>();
      }
      //Step 1:  Calculate the Cube Number
      int row = index/(TOTAL*3);
      //Step 2:  Calculate the Column Number
      int col        = index % TOTAL;
      int offset     = col/3;
      //Step 3:  Get the Cube Number Indices, set the temp values
      int cubeNumber = row*3 + offset;
      int cubeRow    = cubeNumber/3;
      int startRow   = cubeRow*27;
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
         for(int i = 6; i < TOTAL; ++i){
            int idx = startRow + (cubeNumber%3)*3  + i + 12;
            int val = this.block[idx].value().intValue();
            if(val > 0){
               this.tempValues.add(Integer.valueOf(val));
            }
         }
         row = index/TOTAL;
         for(int i = 0; i < TOTAL; ++i){
            //Step 4: Get the Row Number Indices, set the
            //temp values
            int idx = row*TOTAL + i;
            int val = this.block[idx].value().intValue();
            if(val > 0){
               this.tempValues.add(Integer.valueOf(val));
            }
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      catch(IndexOutOfBoundsException obe){
         obe.printStackTrace();
      }
      */
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
               int attempts = 0;
               int combos   = this.unUsedCombos;
               //System.out.println("Col Combos: "+combos);
               while(!this.isSolvedCorrect()&&(attempts < combos)){
                  //System.out.println("Column");
                  //System.out.println("Attempts: "+attempts);
                  this.resetAll(false);
                  this.setGroupValues();
                  ++attempts;
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
