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
   static public enum State{STARTUP,NEWGAME,CLEARED,SOLVED,ERROR};
   private State  _state;
   private String _message;
   private String _sstate;
   private SudokuBlock[][] _block;
   private SudokuBlock[] _singleBlock;
   private int[][] _intBlock;

   {
      _state       = State.STARTUP;
      _message     = null;
      _sstate      = null;
      _block       = null;
      _singleBlock = null;
      _intBlock    = null;
   };

   /////////////////////////////Constructors//////////////////////////
   //
   //
   //
   SudokuState(String message, State state, SudokuBlock[][] block){
      this._message = new String(message);
      this._state   = state;
      this._block   = block;
      this.setStringState(this._state);
   }

   //
   //
   //
   SudokuState(String message, State state, SudokuBlock[] block){
      this._message     = new String(message);
      this._state       = state;
      this._singleBlock = block;
      this.setStringState(this._state);
   }

   //
   //
   //
   SudokuState(String message, String state, SudokuBlock[][] block){
      this._message = new String(message);
      this._sstate  = new String(state);
      //This may change at a later date
      this._block   = block;
   }

   //
   //
   //
   SudokuState(String message, String state, SudokuBlock[] block){
      this._message     = new String(message);
      this._sstate      = new String(state);
      //This may change at a later date
      this._singleBlock = block;
   }

   //
   //
   //
   SudokuState(String message, String state, int[][] block){
      this._message = new String(message);
      this._sstate  = new String(state);
      //This may change at a later data
      this._intBlock = block;
   }
 
   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public String message(){
      return this._message;
   }

   //
   //
   //
   public State state(){
      return this._state;
   }

   //
   //
   //
   public String stringState(){
      return this._sstate;
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

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setStringState(State state){
      if(state == State.STARTUP){
         this._sstate = new String("STARTUP");
      }
      else if(state == State.NEWGAME){
         this._sstate = new String("NEWGAME");
      }
      else if(state == State.CLEARED){
         this._sstate = new String("CLEARED");
      }
      else if(state == State.SOLVED){
         this._sstate = new String("SOLVED");
      }
      else if(state == State.ERROR){
         this._sstate = new String("ERROR");
      }
   }
}
//////////////////////////////////////////////////////////////////////
