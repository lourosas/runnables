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

public class TankMonitorable implements Monitorable{
   private TankData _tankData;
   private Object    _obj;

   {
      _tankData = null;
      _obj      = null;
   };

   ////////////////////////////Constructors///////////////////////////
   ///
   ///
   ///
   public TankMonitorable(){
      this._obj = new Object();
   }

   //
   //
   //
   private void setData(String type, Object data, boolean toAppend){
      String inputType = type.toUpperCase();
      if(inputType.contains("ERROR")){
         this.setError(data,toAppend);
      }
   }

   //
   //
   //
   private void setError(Object data, boolean toAppend){
      boolean toA = toAppend;
      try{
         double  cap  = this._tankData.capacity();
         double  den  = this._tankData.density();
         double  dw   = this._tankData.dryWeight();
         double  er   = this._tankData.emptyRate();
         String  err  = this._tankData.error();
         String  fue  = this._tankData.fuel();
         boolean isE  = this._tankData.isError();
         double  mlr  = this._tankData.massLossRate();
         long    mdl  = this._tankData.model();
         int     num  = this._tankData.number();
         int     stg  = this._tankData.stage();
         double  temp = this._tankData.temperature();
         double  tol  = this._tankData.tolerance();
         double  wgt  = this._tankData.weight();
         if(toAppend && isE){
            err = err + "\n" + (String)data;
         }
         else{
            err = (String)data;
            isE = true;
         }
         TankData td = new GenericTankData(cap,den,dw,er,err,fue,isE,
                                        mlr,mdl,num,stg,temp,tol,wgt);
         synchronized(this._obj){
            this._tankData = td;
         }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
   }

   //////////////////Monitor Interface Implementation/////////////////
   ///
   ///
   ///
   public void addData(Object data){
      try{
         synchronized(this._obj){
            this._tankData = (TankData)data;
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
      this.setData("Error",error,toAppend);
   }

   //
   //
   //
   public Object monitor(){
      return this._tankData;
   }
}
//////////////////////////////////////////////////////////////////////
