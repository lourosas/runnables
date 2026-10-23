//////////////////////////////////////////////////////////////////////
/*
Copyright 2026 Lou Rosas

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
import java.io.*;
import rosas.lou.runnables.*;

public class FuelSystemMonitorable implements Monitorable{
   private Object         _obj;
   private FuelSystemData _fuelSystemData;

   {
      _obj            = null;
      _fuelSystemData = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public FuelSystemMonitorable(){
      this._obj = new Object();
   }

   //////////////////////////Private Methiods/////////////////////////

   ////////////////Monitorable Interface Implementation///////////////
   //
   //
   //
   public void addData(Object data){
      try{
         synchronized(this._obj){
            this._fuelSystemData = (FuelSystemData)data;
         }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
   }

   //
   //
   //
   public void addData(String type, Object data){}

   //
   //
   //
   public void addError(String error, boolean toAppend){}

   //
   //
   //
   public Object monitor(){
      synchronized(this._obj){
         return this._fuelSystemData;
      }
   }
}


//////////////////////////////////////////////////////////////////////
