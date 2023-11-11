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

public class ReservoirState implements ContainerState{
   private int    _mask;
   private double _capacity;
   private double _quantity;
   private String _state;

   {
      _mask     = ContainerStateMask.NONE;
      _capacity = Double.NaN;
      _quantity = Double.NaN;
      _state    = null;
   };

   /////////////////////////Constructors//////////////////////////////
   public ReservoirState
   (
      String state,
      double capacity,
      double quantity
   ){
      if(state != null){
         this.state(state);
      }
      if(!Double.isNaN(capacity)){
         this.capacity(capacity);
      }
      if(!Double.isNaN(quantity)){
         this.quantity(quantity);
      }
   }

   ///////////////////////////Private Methods/////////////////////////
   //
   //
   //
   private void capacity(double capacity){
      if(capacity != this._capacity){
         this._capacity = capacity;
         this._mask    += ContainerStateMask.CAPACITY;
      }
   }

   //
   //
   //
   private void quantity(double quantity){
      if(quantity != this._quantity){
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
         if(test.equals("STARTUP") || test.equals("EMPTY") ||
            test.equals("FILLED")  || test.equals("WASFILLED")){
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
