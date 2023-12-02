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

   {
      _value = null;
   };
   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public SudokuBlock(){
      this._value = new Integer(Integer.MIN_VALUE);
   }

   /////////////////////////Public Methods////////////////////////////
   //
   //
   //THIS WILL NEED TO SYNCHRONIZE!!!
   public void value(int value){
      this._value = new Integer(value);
   }

   //
   //
   //THIS WILL NEED TO SYCHRONIZE!!!!
   public Integer value(){
      return this._value;
   }

}
//////////////////////////////////////////////////////////////////////