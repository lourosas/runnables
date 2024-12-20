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

public class GenericCapsuleData implements PayloadData{
   private int         _crew;
   private double      _currentTemp;
   private String      _error;
   private boolean     _isError;
   private double      _mesuredWeight;
   private String      _model;
   private String      _type;

   {
      _crew           = -1;
      _currentTemp    = Double.NaN;
      _error          = null;
      _isError        = false;
      _measuredWeight = Double.NaN;
      _model          = null;
      _type           = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericCapsuleData
   (
      int     crew,
      double  temp,
      String  error,
      boolean isError,
      double  weight,
      String  model,
      String  type
   ){
      this.crew(crew);
      this.currentTemp(temp);
      this.error(error);
      this.isError(isError);
      this.measuredWeight(weight);
      this.model(model);
      this.type(type);
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void crew(int crw){
      if(crw > 0){
         this._crew = crw;
      }
   }

   //
   //
   //
   private void currentTemp(double temp){
      this._currentTemp = temp;
   }

   //
   //
   //
   private void error(String error){
      this._error = error;
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
   private void measuredWeight(double wgt){
      if(wgt > 0.){
         this._measuredWeight = wgt;
      }
   }

   //
   //
   //
   private void model(String model){
      this._model = model;
   }

   //
   //
   //
   private void type(String type){
      this._type = type;
   }

   ////////////////PayloadData Interface Implementation///////////////
   //
   //
   //
   public int crew(){ return this._crew; }
}
//////////////////////////////////////////////////////////////////////
