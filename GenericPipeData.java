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
import rosas.lou.runnables

public class GenericPipeData implements PipeData{
   private String   _error;
   private double   _flow;
   private int      _index; //Number of current pipe
   private boolean  _isError;
   private double   _temp;
   private String   _type; //Fuel Type

   {
      _error   = null;
      _flow    = Double.NaN;
      _index   = -1;
      _isError = false;
      _temp    = Double.NaN;
      _type    = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericPipeData
   (
      String   error,
      double   flow,
      int      index,
      boolean  isError,
      double   temp,
      String   fuelType
   ){
      this.error(error);
      this.flow(flow);
      this.index(index);
      this.isError(isError);
      this.temp(temp);
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
      if(flow > -1.){
         this._flow = flow;
      }
   }

   //
   //
   //
   private void index(int index){
      if(index > 0){
         this._index = index;
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
   private void temp(double temperature){
      this._temp = temperature;
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
   public int index(){ return this._index }

   //
   //
   //
   public boolean isError(){ return this._isError; }

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
      String value = new String("\nPipe:         "+this.index());
      value += "\nFuel:        "+this.type();
      value += "\nError?       "+this.isError();
      if(this.isError()){
         value += "\nError(s)       "+this.error();
      }
      value += "\nFuel Type:   "+this.type();
      value += "\nTempearture: "+this.temp();
   }
}
//////////////////////////////////////////////////////////////////////
