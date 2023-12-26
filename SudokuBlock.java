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
   private List<Integer>  _attempted;
   private Integer        _value;
   private Object         _o;
   private int            _index;
   private boolean        _isMutable;

   {
      _attempted = null;
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
   public boolean mutable(){
      return this._isMutable;
   }

   //
   //
   //
   public void reset(boolean toKeep){}

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

   //
   //
   //
   public void value(int value){
      boolean isSet = false;
      //In this case, the _value is only set once...
      synchronized(this._o){
         if(this._value.intValue() <= 0 && value > 0){
            Integer temp = Integer.valueOf(value);
            try{
               if(!this._attempted.contains(temp)){ isSet = true; }
            }
            catch(NullPointerException npe){ isSet = true; }
            if(isSet){
               this._value = temp;
            }
         }
      }
   }

   //
   //
   //
   //
   public void value(Integer value){
      this.value(value.intValue());
   }

   //
   //
   //
   public void value(int value, boolean mutable){
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
}
//////////////////////////////////////////////////////////////////////
