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

public abstract class TankData{
   private double    _capacity; //Will change based on State
   private double    _density;  //Density of the fuel
   private double    _dryWeight;
   private double    _emptyRate;
   private String    _error;
   private String    _fuel;     //The Fuel Type
   private boolean   _isError;
   private double    _massLossRate;
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
      _massLossRate   = Double.NaN;
      _model          = Long.MIN_VALUE;
      _number         = -1;
      _stage          = -1;
      _temperature    = Double.NaN;
      _tolerance      = Double.NaN;
      _weight         = Double.NaN;
   };

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public double   capacity(){ return this._capacity; }

   //
   //
   //
   public double   density(){ return this._density; }

   //
   //
   //
   public double   dryWeight(){ return this._dryWeight; }

   //
   //
   //
   public double   emptyRate(){ return this._emptyRate; }

   //
   //
   //
   public String   error(){ return this._error; }

   //
   //
   //Fuel Type...
   public String   fuel(){ return this._fuel; }

   //
   //
   //
   public boolean  isError(){ return this._isError; }
   
   //
   //
   //Derived...
   public double   massLossRate(){ return this._massLossRate; }

   //
   //
   //
   public long     model(){ return this._model; }

   //
   //
   //
   public int      number(){ return this._number; }

   //
   //
   //
   public int      stage(){ return this._stage; }

   //
   //
   //
   public double   temperature(){ return this._temperature; }

   //
   //
   //
   public double   tolerance(){ return this._tolerance; }

   //
   //
   //Measured...
   public double   weight(){ return this._weight; }
   
   //
   //
   //
   public String   toString(){
      String data = new String("\nTankData: ");
      data += "\n------------------------------";
      data += "\nCapacity:            "+this.capacity();
   }
}
//////////////////////////////////////////////////////////////////////
