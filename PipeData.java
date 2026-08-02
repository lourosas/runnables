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

public abstract class PipeData{
   private String   _error;
   private boolean  _isError;
   private int      _number;
   private double   _rate;
   private int      _stage;
   private int      _tank;
   private double   _temperature;
   private double   _tolerance;
   private String   _type;

   {
      _error        = null;
      _isError      = false;
      _number       = -1;
      _rate         = -1;
      _stage        = -1;
      _tank         = -1;
      _temperature  = Double.NaN;
      _tolerance    = Double.NaN;
      _type         = null;
   };

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public String   error(){ return this._error; }

   //
   //
   //
   public boolean  isError(){ return this._isError; }

   //
   //
   //
   public int      number(){ return this._number; }

   //
   //
   //
   public double   rate(){ return this._rate; }

   //
   //
   //
   public int      stage(){ return this._stage; }

   //
   //
   //
   public int      tank(){ return this._tank; }

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
   //
   public String   type(){ return this._type; }

   //
   //
   //
   public String   toString(){
      String data = new String("\nPipe Data");
      data += "\n------------------------------";
      data += "\nIs Error:                 "+this.isError();
      data += "\nerror:                    "+this.error();
      data += "\nto Engine:                "+this.number();
      data += "\nrate:                     "+this.rate();
      data += "\nstage:                    "+this.stage();
      data += "\ntank:                     "+this.tank();
      double t = this.temperature();
      data += "\ntemperature:              "+String.format("%.2f",t);
      data += "\nFuel Type:                "+this.type();
      return data;
   }

   /////////////////////////Protected Methods/////////////////////////
   //
   //
   //
   protected void error(String err){ this._error = err; }

   //
   //
   //
   protected void isError(boolean isErr){ this._isError = isErr; }

   //The Pipe going to the particular engine
   //
   //
   protected void number(int num){
      if(num > 0){
         this._number = num;
      }
   }

   //
   //
   //
   protected void rate(double rate){
      if(rate >= 0.){
         this._rate = rate;
      }
   }

   //
   //
   //
   protected void stage(int stage){
      if(stage > 0){
         this._stage = stage;
      }
   }

   //Tank Number associated with the pipe
   //
   //
   protected void tank(int tankNumber){
      if(tankNumber > 0){
         this._tank = tankNumber;
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
      if(tol >= 0.){
         this._tolerance = tol;
      }
   }

   //This can be null...
   //
   //
   protected void type(String fuelType){
      this._type = fuelType;
   }
}
//////////////////////////////////////////////////////////////////////
