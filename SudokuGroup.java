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

   protected int           indices[];
   protected SudokuBlock   block[];
   protected boolean       solveIt;
   protected List<Integer> values;
   
   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void block(SudokuBlock blk[]){
      this.block = blk;
      this.setValues();
   }

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

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setValues(){
      try{
         this.values.clear();
      }
      catch(NullPointerException npe){
         this.values = new LinkedList<Integer>();
      }
      try{
         for(int i = 0; i < TOTAL; ++i){
            int val = this.block[this.indices[i]].value().intValue();
            if(val > 0){
               this.values.add(this.block[this.indices[i]].value());
            }
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   /////////////////Runnable Interface Implementation/////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
