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

public abstract class SudokuGroup implements Runnable{
   protected static final int TOTAL = 9;
   protected static Object _o = new Object();

   protected int           indices[];
   protected SudokuBlock   block[];
   protected boolean       isSolved;
   protected boolean       solveIt;
   protected int           unUsedCombos;
   protected List<Integer> values;
   protected List<Integer> tempValues;
   protected List<Integer> unUsedValues;
   
   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void block(SudokuBlock blk[]){
      this.block = blk;
      this.setValues();
      this.solved(false);
   }

   //
   //
   //
   public void indices(int index){}

   //
   //
   //
   public void indices(int idxs[]){
      try{
         for(int i = 0; i < TOTAL; ++i){
            this.indices[i] = idxs[i];
         }
      }
      catch(ArrayIndexOutOfBoundsException obe){
         obe.printStackTrace();
      }
      this.solved(false);
   }

   //
   //
   //
   public void print(){}

   //
   //
   //
   public void solve(boolean toSolve){
      this.solveIt = toSolve;
   }

   //
   //
   //
   public boolean solved(){
      return this.isSolved;
   }

   ////////////////////////Protected Methods//////////////////////////
   //
   //
   //
   protected int findFirstBlankIndex(){
      int first     = -1;
      boolean found = false;
      try{
         for(int i = 0; i < TOTAL && !found; ++i){
            int idx = indices[i];
            //Might consider a synchronization...
            if(this.block[idx].value() < 1){
               first = i;
               found = true;
            }
         }
      }
      catch(ArrayIndexOutOfBoundsException obe){
         obe.printStackTrace();
         first = -1;
      }
      finally{
         return first;
      }
   }

   //
   //
   //
   protected int findLastBlankIndex(){
      int last      = -1;
      boolean found = false;
      try{
         for(int i = (TOTAL-1); i > -1 && !found; --i){
            int idx = indices[i];
            //Might consider a synchronization...
            if(this.block[idx].value() < 1){
               last  = i;
               found = true;
            }
         }
      }
      catch(ArrayIndexOutOfBoundsException obe){
         obe.printStackTrace();
         last = -1;
      }
      finally{
         return last;
      }
   }

   //
   //
   //
   protected boolean isSolvedCorrect(){
      boolean isCorrect = true;
      try{
         for(int i = 0; i < TOTAL & isCorrect; ++i){
            int idx = indices[i];
            //Might consider a synchronization...
            if(this.block[idx].value() < 1){
               isCorrect &= false;
            }
         }
      }
      catch(ArrayIndexOutOfBoundsException obe){
         obe.printStackTrace();
      }
      return isCorrect;
   }

   //Reset the entire Group
   //
   //
   protected void resetAll(boolean keepPrevious){
      for(int i = 0; i < TOTAL; ++i){
         //indicate the Block currently has a bad value 
         //reset the Block Value...
         int idx = indices[i];
         //indicate not to use the value in it again...
         this.block[idx].reset(keepPrevious);
         if(!keepPrevious){
            this.block[idx].clearAttempts();
         }
      }
   }

   //
   //
   //
   protected void resetAtAboveLastBlank(){
      int i   = this.findLastBlankIndex();
      //System.out.println(i);
      if(i > -1){
         i = (i > 0) ? i - 1: i;
         int idx = this.indices[i];
         while(!this.block[idx].mutable() && i > 0){
            --i;
            idx = this.indices[i];
         }
         //If the last value to check is immutable, start with the
         //last blank index...
         if(i == 0 && !this.block[idx].mutable()){
            System.out.println("SHIT!!!");
            i = this.findFirstBlankIndex();
         }
         System.out.println(i);
         System.out.println(idx);
         System.out.println(this.block[idx].mutable());
         //see what happens
         this.block[idx].reset(true);
         //System.out.println(this.block[idx].value());
      }

   }

   //
   //Set the values for the entire group
   //
   protected void setGroupValues(){
      for(int i = 0; i < this.indices.length; ++i){
         int idx     = this.indices[i];
         Integer val = this.block[idx].value();
         if(val < 0){
            this.setValues();
            this.setTempValues(idx);
            Integer cur    = null;
            boolean found  = false;
            Iterator<Integer>it = this.unUsedValues.iterator();
            while(!found && it.hasNext()){
               cur = it.next();
               boolean inVal  = this.values.contains(cur);
               boolean inTemp = this.tempValues.contains(cur);
               if(!inVal && !inTemp){
                  found = this.block[idx].value(cur);
                  System.out.println("Group found: "+found+", "+idx+", "+cur);
                  if(found){
                     this.values.add(cur);
                  }
                  //this.setValues();
                  //found = true;
               }
            }
         }
      }
   }

   //
   //
   //
   protected void setTempValues(int index){}

   //
   //
   //
   protected void setValues(){
      try{
         this.values.clear();
      }
      catch(NullPointerException npe){
         this.values = new LinkedList<Integer>();
      }
      try{
         for(int i = 0; i < TOTAL; ++i){
            Integer value = this.block[this.indices[i]].value();
            if(value > 0){
               this.values.add(value);
            }
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //
   //
   //
   protected void setUnusedValues(){
      List<Integer> temp = new LinkedList<Integer>();
      try{
         this.unUsedValues.clear();
      }
      catch(NullPointerException npe){
         this.unUsedValues = new LinkedList<Integer>();
      }
      for(int i = 0; i < TOTAL; ++i){
         int idx = this.indices[i];
         if(!this.block[idx].mutable()){
            temp.add(this.block[idx].value());
         }
      }
      for(int i = 1; i < TOTAL+1; ++i){
         Integer value = Integer.valueOf(i);
         if(!temp.contains(value)){
            this.unUsedValues.add(value);
         }
      }
      this.unUsedCombos = this.factorial(this.unUsedValues.size());
   }

   //
   //
   //
   protected void solved(boolean didSolve){
      this.isSolved = didSolve;
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private int factorial(int x){
      if(x == 0)
         return 0;
      else if(x < 2)
         return 1;
      else
         return x*factorial(x-1);
   }


   /////////////////Runnable Interface Implementation/////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
