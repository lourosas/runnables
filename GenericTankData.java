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

public class GenericTankData implements TankData{
   private double    _capacity; //Will change based on State
   private double    _emptyRate;
   private String    _error;
   private boolean   _isError;
   private int       _number;//Tank Number for the Stage
   private double    _temperature;
   private String    _type;//The Fuel Type
   private double    _weight; //Will change based on State

   {
      _capacity       = Double.NaN;
      _emptyRate      = Double.NaN;
      _error          = null;
      _isError        = false;
      _number         = -1;
      _temperature    = Double.NaN;
      _type           = null;
      _weight         = Double.NaN;
   };
   
   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericTankData
   (
      double   capacity,
      double   emptyRate,
      String   error,
      boolean  isError,
      int      number,
      double   temperature,
      String   type,
      double   weight
   ){
      this.capacity(capacity);
      this.emptyRate(emptyRate);
      this.error(error);
      this.isError(isError);
      this.number(number);
      this.temperature(temperature);
      this.type(type);
      this.weight(weight);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void capacity(double cap){
      if(cap >= 0.){
         this._capacity = cap;
      }
   }
   //
   //
   //
   private void emptyRate(double er){
      if(er >= 0.){
         this._emptyRate = er;
      }
   }

   //
   //
   //
   private void error(String err){
      this._error = err;
   }

   //
   //
   //
   private void isError(boolean error){
      this._isError = error;
   }

   //Save off the Tank Number in the Stage
   //
   //
   private void number(int num){
      if(number > 0){
         this._number = num;
      }
   }

   //
   //
   //
   private void temperature(double temp){
      this._temperature = temp;
   }

   //Save off the Fuel Type
   //
   //
   private void type(String fuelType){
      this.type = fuelType;
   }

   //
   //
   //
   private void weight(double weight){
      if(weight >= 0.){
         this._weight = weight;
      }
   }


   //////////////////TankData Interface Implementation////////////////
   //
   //
   //
   public double capacity(){ return this._capacity; }

   //
   //
   //
   public double emptyRate(){ return this._emptyRate; }

   //
   //
   //
   public String error(){ return this._error; }

   //
   //
   //
   public boolean isError(){ return this._isError; }

   //
   //
   //
   public int number(){ return this._number; }

   //
   //
   //
   public int temperature(){ return this._temperature; }

   //
   //
   //
   public String type(){  return this._type; }

   //
   //
   //
   public double weight(){ return this._weight; }

   //
   //
   //
   public String toString(){
      String value = new String("\nTank: "+this.number);
      value += "\nError?       " + this.isError();
      if(this.isError()){
         value += "\nErrors:   "+this.error();
      }
      value += "\nCapacity:         " + this.capacity();
      value += "\nWeight:           " + this.weight();
      value += "\nEmpty Rate:       " + this.emptyRate();
      value += "\nTemperature:      " + this.temperature();
      value += "\nFuel Type:        " + this.type();
      return value;
   }
}
//////////////////////////////////////////////////////////////////////
