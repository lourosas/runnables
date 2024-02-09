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

public class SudokuState{
   private String _message;
   private String _state;
   private SudokuBlock[][] _block;
   private SudokuBlock[] _singleBlock;
   private int[][] _intBlock;

   {
      _message     = null;
      _state       = null;
      _block       = null;
      _singleBlock = null;
      _intBlock    = null;
   };

   /////////////////////////////Constructors//////////////////////////
   //
   //
   //
   SudokuState(String message, String state, SudokuBlock[][] block){
      this._message = new String(message);
      this._state   = new String(state);
      //This may change at a later date
      this._block   = block;
   }

   //
   //
   //
   SudokuState(String message, String state, SudokuBlock[] block){
      this._message = new String(message);
      this._state   = new String(state);
      //This may change at a later date
      this._singleBlock = block;
   }

   //
   //
   //
   SudokuState(String message, String state, int[][] block){
      this._message = new String(message);
      this._state   = new String(state);
      //This may change at a later data
      this._intBlock = block;
   }
 
   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public String message(){
      return null;
   }

   //
   //
   //
   public String state(){
      return null;
   }

   //
   //
   //
   public SudokuBlock[][] block(){
      return this._block;
   }

   //
   //
   //
   public SudokuBlock[] singleBlock(){
      return this._singleBlock;
   }

   //
   //
   //
   public int[][] intBlock(){
      return this._intBlock;
   }
}
//////////////////////////////////////////////////////////////////////
