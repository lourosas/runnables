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

public class GenericPipeData implements PipeData{
   private String   _error;
   private double   _flow;
   private int      _number; //Number of current pipe
   private boolean  _isError;
   private int      _stage;
   private int      _tank;
   private double   _temp;
   private double   _tolerance;
   private String   _type; //Fuel Type

   {
      _error     = null;
      _flow      = Double.NaN;
      _number    = -1;
      _isError   = false;
      _stage     = -1;
      _tank      = -1;
      _temp      = Double.NaN;
      _tolerance = Double.NaN;
      _type      = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericPipeData
   (
      String   error,
      double   flow,
      int      number,
      boolean  isError,
      int      stage,
      int      tank,
      double   temp,
      double   tolerance,
      String   fuelType //Can be null
   ){
      this.error(error);
      this.flow(flow);
      this.number(number);
      this.isError(isError);
      this.stage(stage);
      this.tank(tank);
      this.temp(temp);
      this.tolerance(tolerance);
      this.type(fuelType);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void error(String er){
      this._error = er;
   }

   //
   //
   //
   private void flow(double flow){
      if(flow >= 0.){
         this._flow = flow;
      }
   }

   //
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
   private void isError(boolean error){
      this._isError = error;
   }

   //
   //
   //
   private void stage(int stg){
      if(stg > 0){
         this._stage = stg;
      }
   }

   //
   //
   //
   private void tank(int tk){
      if(tk > 0){
         this._tank = tk;
      }
   }

   //
   //
   //
   private void temp(double temperature){
      this._temp = temperature;
   }

   //
   //
   //
   private void tolerance(double tolerance){
      this._tolerance = tolerance;
   }

   //
   //
   //
   private void type(String fuelType){
      this._type = fuelType;
   }

   /////////////////PipeData Interface Implementation/////////////////
   //
   //
   //
   public String error(){ return this._error; }

   //The fuel flow in the Pipe
   //
   //
   public double flow(){ return this._flow; }

   //Pipe connected to the Tank and Engine
   //
   //
   public int number(){ return this._number; }

   //
   //
   //
   public boolean isError(){ return this._isError; }

   //
   //
   //
   public int stage(){ return this._stage; }

   //
   //
   //
   public int tank(){ return this._tank; }

   //
   //
   //
   public double tolerance(){ return this._tolerance; }

   //
   //
   //
   public double temp(){ return this._temp; }

   //Return the Fuel Type in the Pipe
   //
   //
   public String type(){ return this._type; }

   //
   //
   //
   public String toString(){
      String value = new String("\nPipe:         "+this.number());
      value += "\nStage:        "+this.stage();
      value += "\nTank:         "+this.tank();
      value += "\nEngine:       "+this.number();
      value += "\nError?        "+this.isError();
      if(this.isError()){
         value += "\nError(s) "+this.error();
      }
      value += "\nFuel Flow:    "+this.flow();
      if(this._type != null){value += "\n Fuel Type:   "+this.type();}
      value += "\nTempearture:  "+this.temp();
      value += "\nTolerance:    "+this.tolerance();
      return value;
   }
}
//////////////////////////////////////////////////////////////////////
