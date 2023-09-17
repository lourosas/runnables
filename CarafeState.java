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

public class CarafeState implements ContainerState{
   private int    _mask;
   private double _capacity;
   private double _quantity;
   private String _state;

   {
      _mask     = ContainerStateMask.NONE;
      _capacity =   0.;
      _quantity =   0.;
      _state    = null;      
   };

   ////////////////////////Constructors///////////////////////////////
   public CarafeState(String state, double capacity, double quantity){
      if(state != null){
         this.state(state);
      }
      if(capacity > 0.){
         this.capacity(capacity);
      }
      if(quantity > 0.){
         this.quantity(quantity);
      }
   }

   ////////////////////////////Private Methods////////////////////////
   //
   //
   //
   private void capacity(double capacity){
      this._capacity = 0.;
      if(capacity > this._capacity){
         this._capacity = capacity;
         this._mask    += ContainerStateMask.CAPACITY;
      }
   }

   //
   //
   //
   private void quantity(double quantity){
      this._quantity = 0.;
      if(quantity > this._quantity){
         this._quantity = quantity;
         this._mask    += ContainerStateMask.QUANTITY;
      }
   }

   //
   //
   //
   private void state(String state){
      this._state = null;
      if(state != null){
         String test = state.toUpperCase();
         if(test.equals("HOME")   ||
            test.equals("PULLED") ||
            test.equals("POURING")){
            this._state = state;
            this._mask += ContainerStateMask.STATE;
         }
      }
   }
   //////////////////////Interface Implementations////////////////////
   //
   //
   //
   public double capacity(){
      return this._capacity;
   }

   //
   //
   //
   public int mask(){
      return this._mask;
   }

   //
   //
   //
   public double quantity(){
      return this._quantity;
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
      return new String(this.mask()+", "+this.quantity()+", "+
                        this.capacity()+", "+this.state());
   }
}

//////////////////////////////////////////////////////////////////////
