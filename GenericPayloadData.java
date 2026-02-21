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
   private double          _currentWeight;
   private double          _dryWeight;
   private String          _error;
   private boolean         _isError;
   private boolean         _isOccupied;
   private double          _maxWeight;
   private String          _model;
   private double          _o2Percent;
   private double          _temperature;
   private double          _tolerance;
   private String          _type;

   {
      _crew           = -1;
      _currentWeight  = Double.NaN;
      _dryWeight      = Double.NaN;
      _error          = null;
      _isError        = false;
      _isOccupied     = false;
      _maxWeight      = Double.NaN;
      _model          = null;
      _o2Percent      = Double.NaN;
      _temperature    = Double.NaN;
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
      String  error,
      boolean isError,
      boolean isOccupied,
      double  maxWeight,
      String  model,
      double  o2Percent,
      double  temperature,
      String  type
   ){}

   //////////////////////////Private Methods//////////////////////////
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
