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

import java.util.*;
import java.lang.*;
import rosas.lou.runnables.*;

public abstract class LaunchMechanismData{
   double              _angle;
   String              _error;
   double              _holdingForce;
   boolean             _isError;
   double              _measuredWeight;
   int                 _number; //Hold number
   double              _tolerance;

   {
      _angle          = Double.NaN;
      _error          = null;
      _holdingForce   = Double.NaN;
      _isError        = false;
      _measuredWeight = Double.NaN;
      _number         = -1;
      _tolerance      = Double.NaN;
   };

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public double angle(){ return this._angle; }

   //
   //
   //
   public String error(){ return this._error; }

   //
   //
   //
   public double holdingForce(){ return this._holdingForce; }

   //
   //
   //
   public boolean isError(){ return this._isError; }

   //
   //
   //
   public double measuredWeight(){ return this._measuredWeight; }

   //Hold number
   //
   //
   public int number(){ return this._number; }

   //
   //
   //
   public double tolerance(){ return this._tolerance; }

   //
   //
   //
   public String toString(){
      String data = new String("\n Launch Mechnanism Data");
      data += "\n-----------------------------------------";
      data += "\nHold:            "+this.number();
      double a = this.angle();
      data += "\nAngle:           "+String.format("%.2f",a);
      double hf = this.holdingForce();
      data += "\nHold Force:      "+String.format("%.2f",hf);
      double mw = this.measuredWeight();
      data += "\nMeasured Weight: "+String.format("%.2f",mw);
      double tol= this.tolerance();
      data += "\nTolerance:       "+String.format("%.2f",tol);
      data += "\nIs Error:        "+this.isError();
      data += "\nError:           "+this.error();
      return data;
   }

   /////////////////////////Protected Methods/////////////////////////
   //
   //
   //
   protected void angle(double agl){ this._angle = agl; }

   //
   //
   //
   protected void error(String err){ this._error = err; }

   //
   //
   //
   protected void holdingForce(double holdForce){
      this._holdingForce = holdForce;
   }

   //
   //
   //
   protected void isError(boolean isE){ this._isError = isE; }

   //
   //
   //
   protected void measuredWeight(double weight){
      this._measuredWeight = weight;
   }

   //Hold Number
   //
   //
   protected void number(int number){ this._number = number; }

   //
   //
   //
   protected void tolerance(double tolerance){
      this._tolerance = tolerance;
   }
}
//////////////////////////////////////////////////////////////////////
