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
   private double    _dryWeight;
   private double    _emptyRate;
   private String    _error;
   private String    _fuel;     //The Fuel Type
   private boolean   _isError;
   private long      _model;
   private int       _number;   //Tank Number for the Stage
   private int       _stage;    //This is needed!!!
   private double    _temperature;
   private double    _tolerance;
   private double    _weight;

   {
      _capacity       = Double.NaN;
      _density        = Double.NaN;
      _dryWeight      = Double.NaN;
      _emptyRate      = Double.NaN;
      _error          = null;
      _fuel           = null;
      _isError        = false;
      _model          = Long.MIN_VALUE;
      _number         = -1;
      _stage          = -1;
      _temperature    = Double.NaN;
      _tolerance      = Double.NaN;
      _weight         = Double.NaN;
   };
   
   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericTankData
   (
      double   capacity,
      double   density,
      double   dryWeight,
      double   emptyRate,
      String   error,
      String   fuel,
      boolean  isError,
      long     model,
      int      number,
      int      stage,
      double   temperature,
      double   tolerance,
      double   weight
   ){
      this.capacity(capacity);
      this.density(density);
      this.dryWeight(dryWeight);
      this.emptyRate(emptyRate);
      this.error(error);
      this.fuel(fuel);
      this.isError(isError);
      this.model(model);
      this.number(number);
      this.stage(stage);
      this.temperature(temperature);
      this.tolerance(tolerance);
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
   private void density(double dens){
      if(dens >= 0.){
         this._density = dens;
      }
   }

   //
   //
   //
   private void dryWeight(double dw){
      if(dw >= 0.){
         this._dryWeight = dw;
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

   //
   //
   //
   private void model(long model){
      if(model > 0){
         this._model = model;
      }
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
   private void stage(int st){
      if(st > 0){
         this._stage = st;
      }
   }

   //
   //
   //
   private void temperature(double temp){
      this._temperature = temp;
   }

   //
   //
   //
   private void tolerance(double tol){
      if(tol >= 0.){
         this._tolerance = tol;
      }
   }

   //
   //
   //
   private void weight(double w){
      if(w >= 0.){
         this._weight = w;
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
   public double density(){ return this._density; }

   //
   //
   //
   public double dryWeight(){ return this._dryWeight; }

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
      double mass = this.emptyRate()*this.density();
      int temp = (int)(mass * 100);
      mass = temp * 0.01;
      return mass;
   }

   //
   //
   //
   public long model(){ return this._model; }

   //
   //
   //
   public int number(){ return this._number; }

   //
   //
   //
   public int stage(){ return this._stage; }

   //
   //
   //
   public double temperature(){ return this._temperature; }

   //
   //
   //
   public double tolerance(){ return this._tolerance; }

   //
   //
   //
   public double weight(){
      /*
      double g      = 9.81;
      mass   = L*kg/L
      weight = mass*9.81
      double mass   = this.capacity()*this.density();
      double weight = mass * g;
      return weight;
      */
      double mass = this.capacity()*this.density();
      double weight = mass * 9.81;
      int temp = (int)(weight * 100);
      weight = temp * 0.01;
      //Weight of fuel plus the weight of the empty tank
      this._weight = weight + this.dryWeight();
      return this._weight;
   }

   //
   //
   //
   public String toString(){
      String value = new String("\nTank:             "+this.number());
      value += "\nStage:            "+this.stage();
      value += "\nModel:            "
         +String.format("0x%08X",this.model());
      value += "\nError?            " + this.isError();
      if(this.isError()){
         value += "\nErrors:   "+this.error() + "\n"; 
      }
      value += "\nCapacity:         " + this.capacity();
      value += "\nDensity:          " + this.density();
      value += "\nDry Weight:       " + this.dryWeight();
      value += "\nWeight:           " + this.weight();
      value += "\nEmpty Rate:       " + this.emptyRate();
      value += "\nMass Loss Rate:   " + this.massLossRate();
      value += "\nTemperature:      " + 
                            String.format("%.2f", this.temperature());
      value += "\nTolerance:        " + this.tolerance();
      value += "\nFuel Type:        " + this.fuel();
      return value;
   }
}
//////////////////////////////////////////////////////////////////////
