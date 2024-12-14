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
   private double    _density;  //Density of the fuel
   private double    _emptyRate;
   private String    _error;
   private String    _fuel;     //The Fuel Type
   private boolean   _isError;
   private int       _number;   //Tank Number for the Stage
   private double    _temperature;

   {
      _capacity       = Double.NaN;
      _density        = Double.NaN;
      _emptyRate      = Double.NaN;
      _error          = null;
      _fuel           = null;
      _isError        = false;
      _number         = -1;
      _temperature    = Double.NaN;
   };
   
   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericTankData
   (
      double   capacity,
      double   density,
      double   emptyRate,
      String   error,
      String   fuel,
      boolean  isError,
      int      number,
      double   temperature
   ){
      this.capacity(capacity);
      this.density(density);
      this.emptyRate(emptyRate);
      this.error(error);
      this.fuel(fuel);
      this.isError(isError);
      this.number(number);
      this.temperature(temperature);
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
   private void density(double dens){
      if(dens >= 0.){
         this._density = dens;
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

   //Set the Fuel Type...
   //
   //
   private void fuel(String fuel){
      this._fuel = fuel;
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
      if(num > 0){
         this._number = num;
      }
   }

   //
   //
   //
   private void temperature(double temp){
      this._temperature = temp;
   }

   //////////////////TankData Interface Implementation////////////////
   //
   //
   //
   public double capacity(){ return this._capacity; }

   //
   //
   //
   public double density(){ return this._density; }

   //
   //
   //
   public double emptyRate(){ return this._emptyRate; }

   //
   //
   //
   public String error(){ return this._error; }

   //Fuel Type
   //
   //
   public String fuel(){ return this._fuel; }

   //
   //
   //
   public boolean isError(){ return this._isError; }

   //
   //
   //
   public double massLossRate(){
      //Convert from Liters to m^3
      //Convert to mass by multiplying by density...
      double mass = (this.emptyRate()/1000.)*this.density();
      return mass;
   }

   //
   //
   //
   public int number(){ return this._number; }

   //
   //
   //
   public double temperature(){ return this._temperature; }

   //
   //
   //
   public double weight(){
      double g      = 9.81;
      double mass   = (this.capacity()/1000.)*this.density();
      double weight = mass * g;
      return weight;
   }

   //
   //
   //
   public String toString(){
      String value = new String("\nTank: "+this.number());
      value += "\nError?       " + this.isError();
      if(this.isError()){
         value += "\nErrors:   "+this.error() + "\n"; 
      }
      value += "\nCapacity:         " + this.capacity();
      value += "\nDensity:          " + this.density();
      value += "\nWeight:           " + this.weight();
      value += "\nEmpty Rate:       " + this.emptyRate();
      value += "\nMass Loss Rate:   " + this.massLossRate();
      value += "\nTemperature:      " + this.temperature();
      value += "\nFuel Type:        " + this.fuel();
      return value;
   }
}
//////////////////////////////////////////////////////////////////////
