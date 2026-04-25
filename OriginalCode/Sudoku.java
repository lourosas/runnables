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

public class Sudoku implements SudokuInterface{
   private static final int  TOTAL = 81;
   private static final int  SIZE  = 9;
   private List<Subscriber>  _subscribers;
   private SudokuBlock[]     _block;//for now, all 81 numbers!

   {
      _subscribers = null;
      _block       = new SudokuBlock[TOTAL];
   };

   //////////////////////////Constructor//////////////////////////////
   //
   //
   //
   public Sudoku(){
      for(int i = 0; i < TOTAL; ++i){
         this._block[i] = new SudokuBlock();
         this._block[i].setIndex(i);
      }
      this.setUpBlock();
   }


   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this._subscribers.add(subscriber);
      }
      catch(NullPointerException npe){
         this._subscribers = new LinkedList<Subscriber>();
         this._subscribers.add(subscriber);
      }
      finally{
         this.notifySubscribers();
      }
   }

   //
   //
   //
   public void clear(){
      System.out.println("CLEAR");
   }

   //
   //
   //
   public void open(String pathAndFile){}

   //
   //
   //
   public void set(int[][] grid){}

   //
   //
   //
   public void set(SudokuBlock[][] grid){}

   //
   //
   //
   public void set(String[] input){}

   //
   //
   //
   public void solve(){
      //For the time being, all of this is temporary...
      int grid[][] = { { 3, 0, 6, 5, 0, 8, 4, 0, 0 },
                       { 5, 2, 0, 0, 0, 0, 0, 0, 0 },
                       { 0, 8, 7, 0, 0, 0, 0, 3, 1 },
                       { 0, 0, 3, 0, 1, 0, 0, 8, 0 },
                       { 9, 0, 0, 8, 6, 3, 0, 0, 5 },
                       { 0, 5, 0, 0, 9, 0, 6, 0, 0 },
                       { 1, 3, 0, 0, 0, 0, 2, 5, 0 },
                       { 0, 0, 0, 0, 0, 0, 0, 7, 4 },
                       { 0, 0, 5, 2, 0, 6, 3, 0, 0 } };

      this.print(grid);
      if(this.solveSudoku(grid, 0, 0)){
         this.print(grid);
      }
      else{
         System.out.println("No Solution Exists");
      }
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private boolean isSafe(int[][] grid,int row,int col,int num){
      boolean safe = true;
      //Check the Row
      for(int i = 0;((i < SIZE) && safe); ++i){
         if(grid[row][i] == num){
            safe = false;
         }
      }
      //Check the Column
      for(int i = 0;((i < SIZE) && safe); ++i){
         if(grid[i][col] == num){
            safe = false;
         }
      }
      //Check the Cube
      int startRow = row - (row % 3);
      int startCol = col - (col % 3);
      for(int i = 0; ((i < 3) && safe); ++i){
         for(int j = 0; ((j < 3) && safe); ++j){
            if(grid[i + startRow][j + startCol] == num){
               safe = false;
            }
         }
      }
      return  safe;
   }

   //
   //
   //
   private void notifySubscribers(){
      System.out.println("Notify Subscribers");
      Iterator it = this._subscribers.iterator();
      while(it.hasNext()){
         Subscriber s = (Subscriber)it.next();
         s.update(this._block);
      }
   }

   //
   //
   //
   private void print(int [][] grid){
      for(int i = 0; i < SIZE; ++i){
         for(int j = 0; j < SIZE; ++j){
            System.out.print(grid[i][j]+" ");
         }
         System.out.println();
      }
      System.out.println();
   }

   //
   //This may be used at another time...
   //
   private void setUpBlock(){
      this._block[0].value(8, false);
      this._block[1].value(7, false);
      this._block[5].value(1, false);
      this._block[8].value(5, false);
      this._block[10].value(5,false);
      this._block[12].value(3,false);
      this._block[13].value(8,false);
      this._block[18].value(9,false);
      this._block[19].value(1,false);
      this._block[22].value(5,false);
      this._block[25].value(6,false);
      this._block[28].value(3,false);
      this._block[30].value(6,false);
      this._block[31].value(2,false);
      this._block[33].value(5,false);
      this._block[36].value(5,false);
      this._block[38].value(2,false);
      this._block[42].value(7,false);
      this._block[44].value(6,false);
      this._block[47].value(7,false);
      this._block[49].value(4,false);
      this._block[50].value(8,false);
      this._block[52].value(3,false);
      this._block[55].value(8,false);
      this._block[58].value(6,false);
      this._block[61].value(9,false);
      this._block[62].value(3,false);
      this._block[67].value(7,false);
      this._block[68].value(3,false);
      this._block[70].value(5,false);
      this._block[72].value(3,false);
      this._block[75].value(8,false);
      this._block[79].value(7,false);
      this._block[80].value(4,false);
   }

   //Solve via the Naive Approach...the first attempt at a solution
   //
   //
   private boolean solveSudoku(int[][] grid, int row, int col){
      boolean isSolved = false;
      if((row == SIZE - 1) && (col == SIZE)){
         isSolved = true;
      }
      else{
         if(col == SIZE){
            ++row;
            col = 0;
         }
         if(grid[row][col] > 0){
            isSolved = this.solveSudoku(grid, row, col+1);
         }
         else{
            for(int i = 1; (i < (SIZE+1) && !isSolved); ++i){
               if(this.isSafe(grid,row,col,i)){
                  if(row == 0 && col == 7){
                     System.out.println(
                                   "Row: "+row+" Col: "+col+" i: "+i);
                  }
                  grid[row][col] = i;
                  isSolved = solveSudoku(grid, row, col+1);
               }
               //Essentially, a backtrack by removing/clearing out a
               //number set (number > 0)
               if(!isSolved){
                  //if(row == 0 && col ==4){
                     System.out.println(
                           "!Solved Row: "+row+" Col: "+col+" i: "+i);
                  //}
                  grid[row][col] = 0;
               }
            }
         }
      }
      return isSolved;
   }
}
//////////////////////////////////////////////////////////////////////
