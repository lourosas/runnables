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

//
//
//
//
public class MakerState{
   private int    _mask;
   private String _power;
   private String _state;

   {
      _mask  = MakerStateMask.NONE;
      _power = null;
      _state = null;
   };
   
   ///////////////////////////Constructors////////////////////////////
   public MakerState(String power, String state){
      if(power != null){
         this.setPower(power);
      }
      if(state != null){
         this.setState(state);
      }
   }


   /////////////////////////Public Methods////////////////////////////
   //
   //
   //
   public int mask(){
      return this._mask;
   }

   //
   //
   //
   public String power(){
      return this._power;
   }

   //
   //
   //
   public String state(){
      return this._state;
   }

   //
   //
   //
   public String toString(){
      return new String(this.mask()+", " +this.power()+", "+
                                                        this.state());
   }

   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private void setPower(String power){
      this._power = null;
      if(power != null){
         String test = power.toUpperCase();
         if(test.equals("OFF") || test.equals("ON")){
            this._power = power;
            this._mask += MakerStateMask.POWER;
         }
      }
   }

   //
   //
   //
   private void setState(String state){
      this._state = null;
      if(state != null){
         String test = state.toUpperCase();
         if(test.equals("READY") || test.equals("BREWING")){
            this._state = state;
            this._mask += MakerStateMask.STATE;
         }
      }
   }
}
//////////////////////////////////////////////////////////////////////
