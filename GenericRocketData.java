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

public class GenericRocketData implements RocketData{
   //Initialization, Launch, Countdown
   private double                 _emptyWeight;
   private String                 _error;
   private boolean                _isError;
   private double                 _loadedWeight;
   private String                 _model;
   private int                    _numberOfStages;
   private List<StageData>        _stageData;

   {
      _emptyWeight     = Double.NaN;
      _error           = null;
      _isError         = false;
      _loadedWeight    = Double.NaN;
      _model           = null;
      _numberOfStages  = -1;
      _stageData       = null;
   };

   //////////////////////////Constructor//////////////////////////////
   //
   //
   //
   public GenericRocketData
   (
      String          model,
      int             numberOfStages,
      double          emptyWeight,
      double          loadedWeight,
      boolean         isError,
      String          error,
      List<StageData> stages
   ){
      this.model(model);
      this.numberOfStages(numberOfStages);
      this.emptyWeight(emptyWeight);
      this.loadedWeight(loadedWeight);
      this.isError(isError);
      this.error(error);
      this.stages(stages);
   }

   ////////////////////////////Private Methods////////////////////////
   //////////////RocketData Inteface Implementation///////////////////
   //
   //
   //
   public String error(){
      return this._error;
   }

   //
   //
   //
   public double emptyWeight(){
      return this._emptyWeight;
   }

   //
   //
   //
   public boolean isError(){
      return this._isError;
   }

   //
   //
   //
   public double loadedWeight(){
      return this._loadedWeight;
   }

   //
   //
   //
   public String model(){
      return this._model;
   }

   //
   //
   //
   public int numberOfStages(){
      return this._numberOfStages;
   }

   //
   //
   //
   public StageData stage(int stage){
      try{
         return this._stageData.get(stage);
      }
      catch(IndexOutOfBoundsException oob){
         return null;
      }
      catch(NullPointerException npe){
         return null;
      }
   }

   //
   //
   //
   public List<StageData> stages(){
      return this._stageData;
   }

   //
   //
   //
   public String toString{
      String value = new String("Empty Weight: "+this.emptyWeight());
      value += "\nError? "+this.isError();
      if(this.isError()){
         value += " Error(s): "+this.error();
      }
      value += "\nLoaded Weight: "+this.loadedWeight();
      value += "\nModel: "+ this.model();
      value += "\nNumber of Stages" + this.numberOfStages();
      try{
         for(int i = 0; i < this._stageData.size(); ++i){
            value += this._stageData.get(i).toString();
         }
      }
      catch(NullPointerException npe){}

      return value;
   }
}
//////////////////////////////////////////////////////////////////////
