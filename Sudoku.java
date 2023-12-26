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

public class Sudoku{
   private static final int TOTAL  = 81;//Total number of values
   private static final int GROUPS = 9;
   private List<Subscriber> _subscribers;
   private SudokuBlock      _block[]; //All 81 numbers!!!
   private SudokuGroup      _cube;
   private SudokuGroup      _row;
   private SudokuGroup      _column;

   {
      _subscribers = null;
      _block       = new SudokuBlock[TOTAL];
      _cube        = null;
      _row         = null;
      _column      = null;
   };

   //////////////////////////Constructor//////////////////////////////
   //
   //
   //
   public Sudoku(){
      //For the time being, just create three...
      this._cube   = new SudokuCube();
      this._row    = new SudokuRow();
      this._column = new SudokuColumn();
      Thread t0    = new Thread(this._cube);
      Thread t1    = new Thread(this._row);
      Thread t2    = new Thread(this._column);
      t0.start();
      t1.start();
      t2.start();
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
   public void solve(){
      int[] indices = new int[9];
      try{
         for(int i = 0; i < 9; ++i){
            this._cube.indices(i);
            this._column.indices(i);
            this._row.indices(i);
            this._cube.block(this._block);
            this._column.block(this._block);
            this._row.block(this._block);
            this._cube.solve(true);
            this._column.solve(true);
            this._row.solve(true);
            while((!this._cube.solved())   || 
                  (!this._column.solved()) ||
                  (!this._row.solved())){
               System.out.println("Solving");
               Thread.sleep(50);
            }
            this.notifySubscribers();
         }
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private int[] getColumnIndices(int columnNumber){
      int[] indices = new int[9];

      if(columnNumber > -1 && columnNumber < 9){
         for(int i = 0; i < 9; ++i){
            indices[i] = columnNumber + 9*i;   
         }
      }
      return indices;
   }

   //
   //
   //
   private int[] getCubeIndices(int cubeNumber){
      int[] indices = new int[9];

      if(cubeNumber > -1 && cubeNumber < 9){
         int cubeRow  = cubeNumber/3;
         int startRow = 9*cubeRow*3;
         for(int i = 0; i < 3; ++i){
            int idx = startRow + (cubeNumber%3)*3 + i;
            indices[i] = idx;
         }
         for(int i = 3; i < 6; ++i){
            int idx = startRow + (cubeNumber%3)*3 + i + 6;
            indices[i] = idx;
         }
         for(int i = 6; i < 9; ++i){
            int idx = startRow + (cubeNumber%3)*3 + i + 12;
            indices[i] = idx;
         }
      }

      return indices; 
   }

   //
   //
   //
   private int[] getRowIndices(int rowNumber){
      int[] indices = new int[9];

      if(rowNumber > -1 && rowNumber < 9){
         for(int i = 0; i < 9; ++i){
            indices[i] = rowNumber*9 +i;
         }
      }
      return indices;
   }
   
   //
   //
   //
   private boolean isSolvedCorrect(){
      return false;
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
   private void reset(){}

   //
   //
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
}
//////////////////////////////////////////////////////////////////////
