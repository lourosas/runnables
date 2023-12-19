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

public class SudokuBlock{
   private Integer _value;
   private Object  _o;
   private int     _index;

   {
      _value = null;
      _o     = null;
      _index = -1;
   };
   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public SudokuBlock(){
      this._value = Integer.valueOf(Integer.MIN_VALUE);
      this._o     = new Object();
   }

   /////////////////////////Public Methods////////////////////////////
   //
   //
   //
   public void value(int value){
      if(this._index == 2 || this._index == 6){
         System.out.println("Index: "+this._index);
         System.out.println("Thread: "+Thread.currentThread().getId());
         System.out.println("_value: "+this._value);
         System.out.println("value: "+value);
      }
      synchronized(this._o){
         //avoid a race condition...
         if(this._value.intValue() <= 0 && value > 0){
            if(this._index == 2){
               System.out.println("Thread Inside: "+Thread.currentThread().getId());
               System.out.println("value Inside: "+value);
            }
            this._value = Integer.valueOf(value);
         }
      }
   }

   //
   //
   //
   public Integer value(){
      //avoid a race condition...
      synchronized(this._o){
         return this._value;
      }
   }

   //
   //
   //
   public void setIndex(int idx){
      synchronized(this._o){
         if(idx > 0){
            this._index = idx;
         }
      }
   }

}
//////////////////////////////////////////////////////////////////////
