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
   public void print(){
      for(int i = 0; i < TOTAL; ++i){
         System.out.println(this.indices[i]);
      }
      for(int i = 0; i < TOTAL; ++i){
          System.out.println(this.block[this.indices[i]].value());
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
            if(this.solveIt){}
            Thread.sleep(sleepTime);
         }
         catch(InterruptedException ie){
            toRun = false;
         }
      }
   }
}
//////////////////////////////////////////////////////////////////////
