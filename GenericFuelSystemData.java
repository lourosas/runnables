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

public class GenericFuelSystemData extends FuelSystemData{
   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericFuelSystemData
   (
      int            engines,
      int            stage,
      List<PipeData> pipeData,
      List<PumpData> pumpData,
      List<TankData> tankData
   ){
      this.engines(engines);
      this.stage(stage);
      this.pipes(pipeData);
      this.pumps(pumpData);
      this.tanks(tankData);
      this.errors();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void errors(){
      try{
         Iterator<PipeData> it = this._pipes.iterator();
         while(it.hasNext()){
            PipeData pd = (PipeData)it.next();
            if(pd.isError()){
               if(!this._isError){ this._isError = true;}
               if(this._error == null){
                  this._error = new String(pd.error());
               }
               else{ this._error += pd.error(); }
            }
         }
      }
      catch(NullPointerException npe){}
      try{
         Iterator<PumpData> it = this._pumps.iterator();
         while(it.hasNext()){
            PumpData pd = (PumpData)it.next();
            if(pd.isError()){
               if(!this._isError){ this._isError = true; }
               if(this._error == null){
                  this._error = new String(pd.error());
               }
               else{ this._error += pd.error(); }
            }
         }
      }
      catch(NullPointerException npe){}
      try{
         Iterator<TankData> it = this._tanks.iterator();
         while(it.hasNext()){
            TankData td = (TankData)it.next();
            if(td.isError()){
               if(!this._isError){ this._isError = true; }
               if(this._error == null){
                  this._error = new String(td.error());
               }
               else{ this._error += td.error(); }
            }
         }
      }
      catch(NullPointerException npe){}
   }
}
//////////////////////////////////////////////////////////////////////
