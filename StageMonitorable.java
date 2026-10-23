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

public class StageMonitorable implements Monitorable{
   private Object      _obj;
   private StageData   _stageData;

   {
      _obj       = null;
      _stageData = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public StageMonitorable(){
      this._obj = new Object();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setError(String error, boolean toAppend){
      double            dw = this._stageData.dryWeight();
      double            dm = this._stageData.dryMass();
      String           err = this._stageData.error();
      long             mod = this._stageData.model();
      boolean          isE = this._stageData.isError();
      if(toAppend && isE){
         err += "\n" + error;
      }
      else{
         err = error;
         isE = true;
      }
      int              sgn = this._stageData.stageNumber();
      int              nme = this._stageData.numberOfEngines();
      double            lm = this._stageData.loadedMass();
      double            mw = this._stageData.maxWeight();
      double           tol = this._stageData.tolerance();
      double           wgt = this._stageData.weight();
      List<EngineData> enl = this._stageData.engineData();
      FuelSystemData   fsd = this._stageData.fuelSystemData();
      StageData sd = new GenericStageData(dw,dm,err,mod,isE,sgn,nme,
                                          lm,mw,tol,wgt,enl,fsd);
      this._stageData = sd;
   }

   //
   //
   //
   private void setUpEngineData(Object data){
      try{
         double            dw = this._stageData.dryWeight();
         double            dm = this._stageData.dryMass();
         String           err = this._stageData.error();
         long             mod = this._stageData.model();
         boolean          isE = this._stageData.isError();
         int              sgn = this._stageData.stageNumber();
         int              nme = this._stageData.numberOfEngines();
         double            lm = this._stageData.loadedMass();
         double            mw = this._stageData.maxWeight();
         double           tol = this._stageData.tolerance();
         double           wgt = this._stageData.weight();
         List<EngineData> enl = this._stageData.engineData();
         FuelSystemData   fsd = this._stageData.fuelSystemData();
         //If the data is null, clear out the Engine data:  adding
         //fresh data...do not append the data to the Engine Data List
         if(data == null){
            //set the List to Null
            enl = null;
         }
         else{
            EngineData ed = null;
            try{
               ed = (EngineData)data;
            }
            catch(ClassCastException cce){
               cce.printStackTrace();
               System.exit(1);
            }
            try{
               enl.add(ed);
            }
            catch(NullPointerException npe){
               enl = new LinkedList<EngineData>();
               enl.add(ed);
            }
         }
         StageData sd = new GenericStageData(dw,dm,err,mod,isE,sgn,
                                             nme,lm,mw,tol,wgt,enl,
                                             fsd);
         this._stageData = sd;
      }
      catch(NullPointerException npe){}
   }

   ////////////////Monitorable Interface Implementation///////////////
   //
   //
   //
   public void addData(Object data){
      try{
         synchronized(this._obj){
            this._stageData = (StageData)data;  
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
   public void addData(String type, Object data){
      if(type.toUpperCase().contains("ENGINE")){
         //Set up the Engine Data...
         this.setUpEngineData(data);
      }
      else if(type.toUpperCase().contains("FUEL SYSTEM")){}
   }

   //
   //
   //
   public void addError(String error, boolean toAppend){
      if(toAppend){
          this.setError(error, toAppend);
      }
      else{
         this.setError(error, false);
      }
   }

   //
   //
   //
   public Object monitor(){
      synchronized(this._obj){
         return this._stageData;
      }
   }
}
//////////////////////////////////////////////////////////////////////
