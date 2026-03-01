//////////////////////////////////////////////////////////////////////
/*
Copyright 2026 Lou Rosas

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
package rosas.lou.runables;

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public call GenericPayloadData implements PayloadData{
   private int             _crew;
   private double          _currentWeight; //Derived (Measured)
   private double          _dryWeight;
   private double          _emptyMass;
   private String          _error;
   private boolean         _isError;
   private boolean         _isOccupied;
   private double          _loadedMass;
   private double          _maxWeight;
   private String          _model;
   private double          _o2Percent;    //Derived (Measured)
   private double          _temperature;  //Derived (Measured)
   private double          _tolerance;
   private String          _type;

   {
      _crew           = -1;
      _currentWeight  = Double.NaN;
      _dryWeight      = Double.NaN;
      _emptyMass      = Double.NaN;
      _error          = null;
      _isError        = false;
      _isOccupied     = false;
      _loadedMass     = Double.NaN;
      _maxWeight      = Double.NaN;
      _model          = null;
      _o2Percent      = Double.NaN;
      _temperature    = Double.NaN;
      _tolerance      = Double.NaN;
      _type           = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericPayloadData
   (
      int     crew,
      double  currentWeight,
      double  dryWeight,
      double  emptyMass,
      String  error,
      boolean isError,
      boolean isOccupied,
      double  loadedMass,
      double  maxWeight,
      String  model,
      double  o2Percent,
      double  temperature,
      double  tolerance,
      String  type
   ){}

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void crew(int crew){ this._crew = crew; }

   //
   //
   //
   private void currentWeight(double cw){
      if(cw > 0.){
         this._currentWeight = cw;
      }
   }

   //
   //
   //
   private void dryWeight(double dw){
      if(dw > 0.){
         this._dryWeight = dw;
      }
   }

   //
   //
   //
   private void emptyMass(double ew){
      if(ew > 0.){
         this._emptyMass = ew;
      }
   }

   //
   //
   //
   private void error(String error){ this._error = error; }

   //
   //
   //
   private void isError(boolean isE){ this._isError = isE; }

   //
   //
   //
   private void isOccupied(double isO){ this._isOccupied = isO; }

   //
   //
   //
   private void loadedMass(double lm){
      if(lm > 0.){
         this._loadedMass = lm;
      }
   }

   //
   //
   //
   private void maxWeight(double mw){
      if(mw > 0.){
         this._maxWeight = mw;
      }
   }

   //
   //
   //
   private void model(String model){  this._model = model; }

   //
   //
   //
   private void o2Percent(double percent){
      if(percent > 0.){
         this._percent = percent;
      }
   }

   //Temperature in Kelvin...
   //
   //
   private void temperature(double temp){
      if(temp > 0.){
         this._temperature = temp;
      }
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
   private void type(String type){ this._type = type; }

   ////////////////PayloadData Interface Implementation///////////////
   //
   //
   //
   public int crew(){ return this._crew; }

   //
   //
   //
   public double currentWeight(){ return this._currentWeight; }

   //
   //
   //
   public double dryWeight(){ return this._dryWeight; }
   
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
   public boolean isOccupied(){ return this._isOccupied; }

   //
   //
   //
   public double maxWeight(){ return this._maxWeight; }

   //
   //
   //
   public String model(){ return this._model; }

   //
   //
   //
   public double o2Percent(){ return this._o2Percent; }

   //
   //
   //
   public double temperture(){ return this._temperature; }

   //
   //
   //
   public double tolerance(){ return this._tolerance; }

   //
   //
   //
   public String type(){ return this._type; }

   //
   //
   //
   public String toString(){
      String value = new String("Payload: ");
      value += "\nCrew:              "+this.crew();
      value += "\nCurrent Weight:    "+this.currentWeight();
      value += "\nDry Weight:        "+this.dryWeight();
      value += "\nError:             "+this.error();
      value += "\nIs Error:          "+this.isError();
      value += "\nIs Occupied:       "+this.isOccumpied();
      value += "\nMax Weight:        "+this.maxWeight();
      value += "\nModel:             "+this.model();
      value += "\nOxygen:            "+this.o2Percent()+"%";
      value += "\nTemperature:       "+this.temperature();
      value += "\nTolerance:         "+this.tolerance();
      value += "\nType:              "+this.type();
      return value;
   }
}
//////////////////////////////////////////////////////////////////////
