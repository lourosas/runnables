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

public class GenericPumpData implements PumpData{
   private String    _error;
   private double    _flow;
   private int       _index; //Which Pump in the Fuel System
   private boolean   _isError;
   private int       _stage;  //which stage
   private double    _temperature;
   private double    _tolerance;
   private String    _type; //Fuel type (TBD)

   {
      _error       = null;
      _flow        = Double.NaN;
      _index       = -1;
      _isError     = false;
      _stage       = -1;
      _temperature = Double.NaN;
      _tolerance   = Double.NaN;
      _type        = null;
   };

   /////////////////PumpData Interface Implementation/////////////////
   //
   //
   //
   public String error(){ return this._error; }

   //
   //
   //
   public double flow(){ return this._flow; }

   //
   //
   //
   public int index(){ return this._index; }

   //
   //
   //
   public boolean isError(){ return this._isError; }

   //
   //
   //
   public int stage

   //
   //
   //
   public double temperature(){ return this._temperature; }

   //
   //
   //
   public double tolerance(){ return this._tolerance; }

   //
   //
   //
   public String type(){ return this._type; }

   //
   //
   //
   public String toString(){
      String value = new String("\nPump:      "+this.index());
      value += "\nError?      "+this.isError();
      if(this.isError()){
                       value += "Error(s): "+this.error(); 
      }
      value += "\nFuel Flow:   "+this.flow();
      value += "\nFuel Type:   "+this.type();
      value += "\nTemperature: "+this.temperature();
   }
}
//////////////////////////////////////////////////////////////////////
