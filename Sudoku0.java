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
import rosas.lou.runnables.*;

public class Sudoku0 implements SudokuInterface{
   private static final int TOTAL = 81;
   private static final int COLS  = 9;
   private static final int ROWS  = 9;
   private static final int SIZE  = 9;

   private List<Subscribers> _subscribers;
   private SudukoBlock[][]   _block;

   {
      _subscribers = null;
      _block       = new SudokuBlock[ROWS][COLS];
   };

   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public Sudoku0{
      for(int i = 0; i < ROWS; ++i){
         for(int j = 0; j< COLS; ++j){
            this._block[i][j] = new SudokuBlock();
            this._block[i][j].setIndex(i,j);
         }
      }
      this.setUpBlock();
   }

   ///////////////////////////Private Methods/////////////////////////
   //
   //
   //
   private void setUpBlock(){
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
      
      for(int i = 0; i < ROWS; ++i){
         for(int j = 0; j < COLS; ++j){
            boolean mutable = !(grid[i][j] > 0);
            this._block[i][j].value(grid[i][j], mutable);
         }
      }
   }

   //////////////////////Interface Implementation/////////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){}

   //
   //
   //
   public void solve(){}
}
