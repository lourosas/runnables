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
   private double      _force;
   private ForceVector _forceVector;
   private String      _error;
   private boolean     _isError;
   private int         _id;

   {
      _force       = -1.;
      _forceVector = null;
      _error       = null;
      _isError     = true;
      _id          = -1;
   };
   /**/
   public SimpleLaunchingMechanismData
   (
      ForceVector direction,
      double  force,
      int     id,
      boolean isError,
      String  error
   ){
      this._forceVector = direction;
      this._force       = force;
      this._id          = id;
      this._isError     = isError;
      this._error       = error;
   }

   //////////LaunchingSupportMechanismData Implementation/////////////
   /**/
   public ForceVector direction(){
      return this._forceVector;
   }

   /**/
   public String error(){
      return this._error;
   }

   /**/
   public boolean isError(){
      return this._isError;
   }

   /**/
   public double force(){
      return this._force;
   }

   /**/
   public int id(){ 
      return this._id;
   }

   /**/
   public String toString(){
      String ret = new String();
      ret += "\nMechanism:  "+this.id();
      ret += "\nMagnitude:  "+this.force();
      ret += "\nDirection:  "+this.direction();
      ret += "\nIs Error:   "+this.isError();
      ret += "\nErrors:     "+this.error() +"\n";

      return ret;
   }

}
//////////////////////////////////////////////////////////////////////
