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

public class RocketMonitorable implements Monitorable{
   private RocketData    _rocketData;

   {
      _rocketData = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public RocketMonitorable(){}

   //////////////////////////Private Methods//////////////////////////
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
   private void setError(Object error){
      try{
         String  mod         = this._rocketData.model();
         int     stg         = this._rocketData.currentStage();
         int     stgs        = this._rocketData.numberOfStages();
         double  ew          = this._rocketData.emptyWeight();
         double  lw          = this._rocketData.loadedWeight();
         double  cw          = this._rocketData.calculatedWeight();
         boolean isE         = true;
         String  err         = (String)error;
         PayloadData pd      = this._rocketData.payloadData();
         List<StageData> lst = this._rocketData.stages();
         double  tol         = this._rocketData.tolerance();
         RocketData rd       = new GenericRocketData(mod,stg,stgs,ew,
                                            lw,cw,isE,err,pd,lst,tol);
         this._rocketData = rd;
      }
      catch(ClassCastException cce){}
   }

   ////////////////Monitorable Interface Implementation///////////////
   //
   //
   //
   public void addData(Object data){
      try{
         this._rocketData = (RocketData)data;
      }
      catch(ClassCastException cce){
         //This print out is probably going to stay
         cce.printStackTrace();
         this._rocketData = null;
      }
   }

   //
   //
   //
   public void addData(String type, Object data){
      RocketData rd = null;
      //Continue on...
      //
      if(type.toUpperCase().contains("STAGE")){
         //TBD...set the Stage Data!!
      }
   }

   //
   //
   //
   public void addError(String error){
      this.setData("Error", error);
   }

   //
   //
   //
   public Object monitor(){
      return this._rocketData;
   }
}
//////////////////////////////////////////////////////////////////////
