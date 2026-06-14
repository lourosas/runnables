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

public abstract class EngineData{
   private int     _engine;  //Current Engine Number
   private String  _error;
   private double  _exhaustFlowRate;
   private boolean _isError;
   private boolean _isIgnited;
   private double  _fuelFlowRate;
   private long    _model;
   private int     _stage; //Stage the Engine is part
   private double  _temperature;
   private double  _tolerance;
   private int     _total; //Total Number of engines:model&stage

   {
      _engine          = -1;
      _error           = null;
      _exhaustFlowRate = Double.NaN;
      _isError         = false;
      _isIgnited       = false;
      _fuelFlowRate    = Double.NaN;
      _model           = -1;
      _stage           = -1;
      _temperature     = Double.NaN;
      _total           = -1;
   };
   
   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public int engine(){ return this._engine; }
   //
   //
   //
   public String  error(){ return this._error; }

   //
   //
   //
   public double  exhaustFlowRate(){ return this._exhaustFlowRate; }

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
   public double  fuelFlowRate(){ return this._fuelFlowRate; }

   //
   //
   //
   public long    model(){ return this._model; }

   //
   //
   //
   public int     stage(){ return this._stage; }

   //
   //
   //
   public double  temperature(){ return this._temperature; }

   //
   //
   //
   public double  tolerance(){ return this._temperature; }

   //
   //
   //
   public int     total(){ return this._total; }

   //
   //
   //
   public String  toString(){
      String data = new String("\nEngineData: ");
      data += "\n------------------------------";
      data += "\nEngine:            "+this.engine();
      data += "\nIs Error:          "+this.isError();
      data += "\nError:             "+this.error();
      data += "\nExhaust Flow Rate: "+this.exhaustFlowRate();
      data += "\nIs Ignited:        "+this.isIgnited();
      data += "\nFuel Flow Rate:    "+this.fuelFlowRate();
      long mod = this.model();
      data += "\nModel:             "+String.format("0x%%X",mod);
      data += "\nStage:             "+this.stage();
      data += "\nTemperature:       "+this.temperature();
      data += "\nTolerance:         "+this.tolerance();
      data += "\nTotal Engines:     "+this.total();

      return data;
   }

   /////////////////////////Protected Methods/////////////////////////
   //
   //
   //
   protected void engine(int engine){
      if(engine > -1){
         this._engine = engine;
      }
   }

   //
   //
   //
   protected void error(String error){ this._error = error; }

   //
   //
   //
   protected void exhaustFlowRate(double flowRate){
      if(flowRate >= 0.){
         this._exhaustFlowRate = flowRate;
      }
   }

   //
   //
   //
   protected void isError(boolean isErr){ this._isError = isErr; }

   //
   //
   //
   protected void isIgnited(boolean isIgn){ this._isIgnited = isIgn; }

   //
   //
   //
   protected void fuelFlowRate(double flowRate){
      if(flowRate >= 0.){
         this._fuelFlowRate = flowRate;
      }
   }

   //
   //
   //
   protected void model(long mod){
      this._model = mod;
   }

   //
   //
   //
   protected void stage(int stage){
      if(stage > -1){
         this._stage = stage;
      }
   }

   //
   //
   //
   protected void temperature(double temp){
      this._temperature = temp;
   }

   //
   //
   //
   protected void tolerance(double tol){
      if(tol >= 0. && tol <= 1.){
         this._tolerance = tol;
      }
   }

   //
   //
   //
   protected void total(int tot){
      if(tot > 0){
         this._total = tot;
      }
   }
}
//////////////////////////////////////////////////////////////////////
