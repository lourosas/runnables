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

public abstract class PumpData{
   private String  _error;
   private boolean _isError;
   private double  _rate;
   private int     _stage;
   private int     _tankNumber;
   private double  _temperature;
   private double  _tolerance;

   {
      _error       = null;
      _isError     = false;
      _rate        = Double.NaN;
      _stage       = -1;
      _tankNumber  = -1;
      _temperature = Double.NaN;
      _tolerance   = Double.NaN;
   };

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public String  error(){ return this._error; }

   //
   //
   //
   public boolean isError(){ return this._isError; }
   
   //
   //
   //
   public double  rate(){ return this._rate; }

   //
   //
   //
   public int     stage(){ return this._stage; }

   //
   //
   //
   public int     tankNumber(){ return this._tankNumber; }

   //
   //
   //
   public double  temperature(){ return this._temperature; }

   //
   //
   //
   public double  tolerance(){ return this._tolerance; }

   //
   //
   //
   public String   toString(){
      String data = new String("\nTankData");
      data += "\n------------------------------";
      data += "\nIs Error                "+this.isError();
      data += "\nerror:                  "+this.error();
      double rate = this.rate();
      data += "\nrate:                   "+String.format("%.2f",rate);
      data += "\nstage:                  "+this.stage();
      data += "\ntank:                   "+this.tankNumber();
      double t = this.temperature();
      data += "\ntemperture:             "+String.format("%.2f",t);
      data += "\ntolerance:              "+this.tolerance();

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
   protected void stage(int st){
      if(st > 0){
         this._stage = st;
      }
   }

   //
   //
   //
   protected void tankNumber(int tn){
      if(tn > 0){
         this._tankNumber = tn;
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
}
//////////////////////////////////////////////////////////////////////
