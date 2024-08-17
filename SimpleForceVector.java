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

public class SimpleForceVector implements ForceVector{
   private double _x;
   private double _y;
   private double _z;
   private double _magnitude;

   {
      _x         = -0.;
      _y         = -0.;
      _z         = -0.;
      _magnitude = -0.;
   }; 

   ////////////////////////////Constructors///////////////////////////
   /**/
   public SimpleForceVector(double x, double y, double z){
      this.magnitude(x, y, z);
   }

   ///////////////////////////Private Methods/////////////////////////
   /**/
   private void magnitude(double x, double y, double z){
      this._x = x;
      this._y = y;
      this._z = z;
      double square=(this._x*this._x+this._y*this._y+this._z*this._z);
      this._magnitude = Math.sqrt(square);
   }

   //////////////////////ForceVector Implementation///////////////////
   /**/
   public double x(){
      return this._x;
   }

   /**/
   public double y(){
      return this._y;
   }

   public double z(){
      return this._z;
   }

   public double maginitude(){
      return this._magnitude();
   }

   public String toString(){
      String s = new String;

      return s;
   }
}
//////////////////////////////////////////////////////////////////////
