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

public class GenericEngineData implements EngineData{
   private double         _currentTemp;
   private String         _error;
   private double         _exhaustRate;
   private boolean        _isError;
   private boolean        _isIgnited;
   private double         _fuelRate;
   private long           _model;

   {
      _currentTemp  = Double.NaN;
      _error        = null;
      _exhaustRate  = Double.NaN;
      _isError      = false;
      _isIgnited    = false;
      _fuelRate     = Double.NaN;
      _model        = -1;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericEngineData
   (
      double  exhaust,
      double  fuel,
      long    model,
      boolean isError,
      String  error,
      boolean isIgnited,
      double  temperature
   ){
      this.exhaustFlowRate(exhaust);
      this.fuelFlowRate(fuel);
      this.model(model);
      this.isError(isError);
      this.error(error);
      this.isIgnited(isIgnited);
      this.temperature(temperature);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void error(String error){
      this._error = error;   
   }

   //
   //
   //
   private void exhaustFlowRate(double rate){
      this._exhaustRate = rate;
   }

   //
   //
   //
   private void fuelFlowRate(double rate){
      this._fuelRate = rate;
   }

   //
   //
   //
   private void isError(boolean isError){
      this._isError = isError;
   }

   //
   //
   //
   private void isIgnited(boolean isIgnited){
      this._isIgnited = isIgnited;
   }

   //
   //
   //
   private void model(long model){
      this._model = model;
   }

   //
   //
   //
   private void temperature(double temp){
      this._currentTemp = temp;
   }

   /////////////EngineData Interface Implementation///////////////////
   //
   //
   //
   public String error(){ return this._error; }

   //
   //
   //
   public double exhaustFlowRate(){ return this._exhaustRate; }

   //
   //
   //
   public long model(){ return this._model; }

   //
   //
   //
   public boolean isError(){ return this._isError; }

   //
   //
   //
   public boolean isIgnited(){ return this._isIgnited; }
   
   //
   //
   //
   public double fuelFlowRate(){ return this._fuelRate; }

   //
   //
   //
   public double temperature(){ return this._currentTemp; }

   //
   //
   //
   public String toString(){
      String value = new String("Engine: \n");
      value += "Error? "+this.isError();
      if(this.isError()){
         value += "\nErrors: "+this.error();
      }
      value += "\nModel:               ";
      value += String.format("0x%X",this.model());
      value += "\nTemperature:         "+this.temperature();
      value += "\nExhaust Flow Rate:   "+this.exhaustFlowRate();
      value += "\nFuel Flow Rate:      "+this.fuelFlowRate();
      value += "\nIgnited:             "+this.isIgnited();
      return value;
   }
}
//////////////////////////////////////////////////////////////////////
