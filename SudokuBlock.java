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
   private boolean _isMutable;

   {
      _value     = null;
      _o         = null;
      _index     = -1;
      _isMutable = true;
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
      if(this._isMutable){
         //avoid a race condition...
         synchronized(this._o){
            if(this._value.intValue() <= 0 && value > 0){
               this._value = Integer.valueOf(value);
            }
         }
      }
   }

   //
   //
   //
   public void value(int value, boolean mutable){
      /*
       * This logic in English:  If currently not Mutable, and
       * want to make Mutable, change the state first, so as to set
       * the value...the other cases make the attempt to set the
       * value (value(...) method will check its mutability before
       * changing, regardless, and then set the state (might be
       * redundant if the first statement is true, but insignificant
       * to 'double set')...regardless, if need to change the value
       * and the state from immutable to mutable, this method can
       * handle it properly
      */
      if(!this._isMutable && mutable){
         this._isMutable = mutable;
      }
      this.value(value);
      this._isMutable = mutable;
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
         if(idx > -1){
            this._index = idx;
         }
      }
   }

}
//////////////////////////////////////////////////////////////////////
