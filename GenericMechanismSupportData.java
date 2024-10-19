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

public class GenericMechanismSupportData 
implements MechanismSupportData{
   private double           _angle; //In Radians!!!
   private String           _error;
   private ForceVector      _forceVector;
   private int              _id;
   private boolean          _isError;
   private double           _measuredForce;

   {
      _angle         = Double.NaN;
      _error         = null;
      _forceVector   = null;
      _id            = -1;
      _isError       = false;
      _measuredForce = Double.NaN;
   };
   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   public GenericMechanismSupportData
   (
      double an,
      String er,
      ForceVector fv,
      int id,
      boolean iserr,
      double mf
   ){
      this._angle         = an;
      this._error         = er;
      this._forceVector   = fv;
      this._id            = id;
      this._isError       = iserr;
      this._measuredForce = mf;
   }

   /////////MechanismSupportData Interface Implementation/////////////
   //
   //
   //
   public double angle(){
      return this._angle;
   }

   //
   //
   //
   public String error(){
      return this._error;
   }

   //
   //
   //
   public ForceVector forceVector(){
      return this._forceVector;
   }

   //
   //
   //
   public int id(){
      return this._id;
   }

   //
   //
   //
   public boolean isError(){
      return this._isError;
   }

   //
   //
   //
   public double measuredForce(){
      return this._measuredForce;
   }

   //
   //
   //
   public String toString(){
      String string = this.id()+"\n"+this.angle()+"rad\n";
      string       += this.isError()+":"+this.error()+"\n";
      string       += this.forceVector().toString()+"\n";
      string       += this.measuredForce();
      
      return string;
   }
}
//////////////////////////////////////////////////////////////////////
