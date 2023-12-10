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
   private static final int TOTAL = 81;//Total number of values
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
      //A temporary place to put this for the moment...will need
      //to eventually move...
      //Load the Cube, first...
      int[] indices;
      indices = new int[9];
      indices[0] =  0; indices[1] =  1; indices[2] =  2;
      indices[3] =  9; indices[4] = 10; indices[5] = 11;
      indices[6] = 18; indices[7] = 19; indices[8] = 20;
      this._cube.indices(indices);
      indices[0] =  0; indices[1] =  1; indices[2] =  2;
      indices[3] =  3; indices[4] =  4; indices[5] =  5;
      indices[6] =  6; indices[7] =  7; indices[8] =  8;
      this._row.indices(indices);
      indices[0] =  0; indices[1] =  9; indices[2] = 18;
      indices[3] = 27; indices[4] = 36; indices[5] = 45;
      indices[6] = 54; indices[7] = 63; indices[8] = 72;
      this._column.indices(indices);

      this._cube.block(this._block);
      this._row.block(this._block);
      this._column.block(this._block);

      //Start with the Cube ONLY first...
      this._cube.solve(true);
      this._column.solve(true);
      this._row.solve(true);
      try{
         while((!this._cube.solved())   || 
               (!this._column.solved()) ||
               (!this._row.solved())){
            //Just a test print, remove as needed...
            System.out.println("Solving");
            Thread.sleep(50);
         }
         this.notifySubscribers();
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
   }

   /////////////////////////Private Methods///////////////////////////
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
   private void setUpBlock(){
      this._block[0].value(8);
      this._block[1].value(7);
      this._block[5].value(1);
      this._block[8].value(5);
      this._block[10].value(5);
      this._block[12].value(3);
      this._block[13].value(8);
      this._block[18].value(9);
      this._block[19].value(1);
      this._block[22].value(5);
      this._block[25].value(6);
      this._block[28].value(3);
      this._block[30].value(6);
      this._block[31].value(2);
      this._block[33].value(5);
      this._block[36].value(5);
      this._block[38].value(2);
      this._block[42].value(7);
      this._block[44].value(6);
      this._block[47].value(7);
      this._block[49].value(4);
      this._block[50].value(8);
      this._block[52].value(3);
      this._block[55].value(8);
      this._block[58].value(6);
      this._block[61].value(9);
      this._block[62].value(3);
      this._block[67].value(7);
      this._block[68].value(3);
      this._block[70].value(5);
      this._block[72].value(3);
      this._block[75].value(8);
      this._block[79].value(7);
      this._block[80].value(4);
   }
}
//////////////////////////////////////////////////////////////////////
