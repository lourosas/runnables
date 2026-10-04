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

public class EngineMonitorable implements Monitorable{
   private EngineData  _engineData;
   private Object      _obj;

   {
      _engineData = null;
      _obj        = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public EngineMonitorable(){
      this._obj = new Object();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void appendError(Object error){
      try{
         int      eng  = this._engineData.engine();
         String   err  = this._engineData.error();
         double   exr  = this._engineData.exhaustFlowRate();
         boolean  isE  = this._engineData.isError();
         if(isE){
            err = this._engineData.error() + "\n" + (String)error;
         }
         else{
            err = (String)error;
            isE = true;
         }
         boolean  isI = this._engineData.isIgnited();
         double   ffr = this._engineData.fuelFlowRate();
         int      stg = this._engineData.stage();
         long     mod = this._engineData.model();
         double   temp= this._engineData.temperature();
         double   tol = this._engineData.tolerance();
         int      tot = this._engineData.total();
         EngineData ed= new GenericEngineData(eng,err,exr,isE,isI,ffr,
                                              mod,stg,temp,tol,tot);
         this._engineData = ed;
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
   }

   //
   //
   //
   private void setData(String type, Object data){
      String inputType = type.toUpperCase();
      if(inputType.contains("ERROR")){
         this.setError(data);
      }
   }

   //
   //
   //
   private void setData(String type, Object data, boolean toAppend){
      String inputType = type.toUpperCase();
      if(inputType.contains("ERROR")){
         if(toAppend){
            this.appendError(data);
         }
         else{
            this.setError(data);
         }
      }
   }

   //
   //
   //
   private void setError(Object error){
      try{
         int      eng = this._engineData.engine();
         String   err = (String)error;
         double   exr = this._engineData.exhaustFlowRate();
         boolean  isE = true;
         boolean  isI = this._engineData.isIgnited();
         double   ffr = this._engineData.fuelFlowRate();
         int      stg = this._engineData.stage();
         long     mod = this._engineData.model();
         double   temp= this._engineData.temperature();
         double   tol = this._engineData.tolerance();
         int      tot = this._engineData.total();
         EngineData ed= new GenericEngineData(eng,err,exr,isE,isI,ffr,
                                              mod,stg,temp,tol,tot);
         this._engineData = ed;

      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
   }

   ////////////////Monitor Interface Implementation///////////////////
   //
   //
   //
   public void addData(Object data){
      try{
         synchronized(this._obj){
            this._engineData = (EngineData)data;
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
   public void addError(String error, boolean toAppend){
      if(toAppend){
         this.setData("Error", error, toAppend);
      }
      else{
         this.setData("Error", error);
      }
   }

   //
   //
   //
   public Object monitor(){
      return this._engineData;
   }
}
//////////////////////////////////////////////////////////////////////
