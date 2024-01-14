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

public class Sudoku1 implements SudokuInterface{
   private static final int TOTAL = 81;
   private static final int COLS  = 9;
   private static final int ROWS  = 9;
   private static final int SIZE  = 9;

   private List<Subscriber> _subscribers;
   private SudokuBlock[][]  _block;
   private SudokuGroup      _column;
   private SudokuGroup      _cube;
   private SudokuGroup      _row;

   {
      _subscribers = null;
      _block       = new SudokuBlock[ROWS][COLS];
      _column      = null;
      _cube        = null;
      _row         = null;
   };

   /////////////////////////Constructor///////////////////////////////
   //
   //
   //
   public Sudoku1(){
      this._column = new SudokuColumn();
      this._cube   = new SudokuCube();
      this._row    = new SudokuRow();
      for(int i = 0; i < ROWS; ++i){
         for(int j = 0; j < COLS; ++j){
            this._block[i][j] = new SudokuBlock();
            this._block[i][j].setIndex(i,j);
         }
      }
      this.setUpBlocks();
   }

   //////////////////////Private Methods//////////////////////////////
   //
   //
   //
   private void notifyErrors(String error){
      Iterator<Subscriber> it = this._subscribers.iterator();
      while(it.hasNext()){
         //Inform the Subscribers of the Error
         it.next().error(error);
      }
   }

   //
   //
   //
   private void notifySubscribers(){
      Iterator<Subscriber> it = this._subscribers.iterator();
      while(it.hasNext()){
         //Inform the Subscribers of the 2-D array
         it.next().update(this._block, "double");
      }
   }

   //
   //
   //
   private void setUpBlocks(){
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
      this._column.block(this._block);
      this._cube.block(this._block);
      this._row.block(this._block);
   }

   //
   //
   //
   private boolean solveSudoku(int row, int col){
      boolean isSolved = false;


      return isSolved;
   }

   ////////////////////Interface Implementation///////////////////////
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
   public void solve(){
      if(this.solveSudoku(0,0)){
         this.notifySubscribers();
      }
      else{
         this.notifyErrors("No Solution Exists");
      }
   }
}
