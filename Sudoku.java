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
   private List<Subscribers> _subscribers;
   private SudokuBlock _block[]; //All 81 numbers!!!
   private SudokuGroup _cube;
   private SudokuGroup _row;
   private SudokuGroup _column;

   {
      _subscribers = null;
      _block       = new SudukoBlock[TOTAL];
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
   }


   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this._suscribers.add(subscriber);
      }
      catch(NullPointerException npe){
         this._subscribers = new LinkedList<Subscriber>();
         this._subscribers.add(subscriber);
      }
      finally{
         this.notify()
      }
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void notify(){}
}
//////////////////////////////////////////////////////////////////////
