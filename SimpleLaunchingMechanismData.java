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

public class SimpleLaunchingMechanismData
implements LaunchingMechanismData{
   int           _id;
   ForceVector   _direction;
   double        _force;
   boolean       _isError;
   String        _error;

   {
      _id        =   -1;
      _direction = null;
      _force     = -1.;
      _isError   = true;
      _error     = null;
   }

   /**/
   public SimpleLaunchingMechanismData
   (
      ForceVector direction,
      double  force,
      int     id,
      boolean isError,
      String  error
   ){
      this._direction = direction;
      this._force     = force;
      this._id        = id;
      this._isError   = isError;
      this._error     = new String(error);
   }

   //////////LaunchingSupportMechanismData Implementation/////////////
   /**/
   public ForceVector direction(){
      return this._direction;
   }

   /**/
   public boolean error(){
      return this._isError;
   }

   /**/
   public String errorReason(){
      return this._error;
   }

   /**/
   public double force(){
      return this._force;
   }

   public int id(){
      return this._id;
   }

   /**/
   public String toString(){
      String s = new String();

      s += "id:\t\t\t"+this.id()+"\n";
      s += "force:\t\t"+this.force()+"\n";
      s += "direction:\t"+this.direction()+"\n";
      s += "error:\t\t"+this.error()+"\n";
      s += "reson:\t\t"+this.errorReason()+"\n";

      return s;
   }
}
//////////////////////////////////////////////////////////////////////
