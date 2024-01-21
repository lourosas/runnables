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

public class SudokuEngine{
   private static final int TOTAL = 81;
   private static final int COLS  = 9;
   private static final int ROWS  = 9;
   private static final int SIZE  = 9;

   private SudokuBlock[][] _block;
   private SudokuGroup     _column;
   private SudokuGroup     _cube;
   private SudokuGroup     _row;

   {
      _block   = null;
      _column  = null;
      _cube    = null;
      _row     = null;
   };

   ////////////////////////Constructors///////////////////////////////
   //
   //
   //
   public SudokuEngine(){
      this._column = new SudokuColumn();
      this._cube   = new SudokuCube();
      this._row    = new SudokuRow();
      for(int i = 0; i < ROWS; ++i){
         for(int j = 0; j < COLS; ++j){
            this._block[i][j] = new SudokuBlock();
            this._block[i][j].setIndex(i,j);
         }
      }
   }

   //////////////////////Public Methods///////////////////////////////
   //
   //
   //
   public void setBlock(int[][] block){}

   //
   //
   //
   public void setBlock(SudokuBlock[][] block){}

   //
   //
   //
   public boolean solve(){
      return false;
   }

   //
   //
   //
   public boolean solve(int[][] block){
      return false;
   }

   //
   //
   //
   public boolean solve(SudokuBlock[][] block){
      return false;
   }


   //////////////////////Private Methods//////////////////////////////
   //
   //
   //
   private boolean isSafe(int row, int col, int num){
      return false;
   }

   //
   //
   //
   private boolean solveSudoku(int row, int col){
      return false;
   }
}
