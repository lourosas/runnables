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

public abstract class RocketData{
   //Initialization, Launch, Countdown
   protected double                 _calculatedWeight;
   protected int                    _currentStage;
   protected double                 _emptyWeight;
   protected String                 _error;
   protected boolean                _isError;
   protected double                 _loadedWeight;
   protected String                 _model;
   protected int                    _numberOfStages;
   protected PayloadData            _payloadData;
   protected List<StageData>        _stageData;
   protected double                 _tolerance;

   {
      _calculatedWeight = Double.NaN;
      _currentStage     = -1;
      _emptyWeight      = Double.NaN;
      _error            = null;
      _isError          = false;
      _loadedWeight     = Double.NaN;
      _model            = null;
      _numberOfStages   = -1;
      _payloadData      = null;
      _stageData        = null;
      _tolerance        = Double.NaN;
   };
   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public double calculatedWeight(){
      return this._calculatedWeight;
   }

   //
   //
   //
   public int currentStage(){
      return this._currentStage;
   }

   //
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
   public PayloadData payloadData(){
      return this._payloadData;
   }

   //
   //
   //
   public StageData stage(int stage){
      try{
         return this._stageData.get(stage - 1);
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
   public double tolerance(){
      return this._tolerance;
   }

   public String toString(){
      String value = new String("\nRocket Data: ");
      value += "\nEmpty Weight: "+this.emptyWeight();
      value += "\nError? "+this.isError();
      if(this.isError()){
         value += " Error(s): "+this.error();
      }
      value += "\nLoaded Weight:     "+this.loadedWeight();
      value += "\nCalculated Weight: "+this.calculatedWeight();
      value += "\nModel:             "+ this.model();
      value += "\nCurrent Stage:     "+this.currentStage();
      value += "\nTolerance:         "+this.tolerance();
      value += "\n"+this.payloadData();
      value += "\nNumber of Stages:  "+this.numberOfStages();
      try{
         for(int i = 0; i < this._stageData.size(); ++i){
            value += this._stageData.get(i).toString();
         }
      }
      catch(NullPointerException npe){
         value += "\nnull";
      }
      return value; 
   }

   ////////////////////////Protected Methods//////////////////////////
   //
   //
   //
   protected void calculatedWeight(double weight){
      this._calculatedWeight = weight;
   }

   //
   //
   //
   protected void currentStage(int stage){
      if(stage > 0){
         this._currentStage = stage;
      }
   }

   //
   //
   //
   protected void emptyWeight(double weight){
      this._emptyWeight = weight;
   }

   //
   //
   //
   protected void error(String error){
      this._error = error;
   }

   //
   //
   //
   protected void isError(boolean isError){
      this._isError = isError;
   }

   //
   //
   //
   protected void loadedWeight(double loadedWeight){
      this._loadedWeight = loadedWeight;
   }

   //
   //
   //
   protected void model(String model){
      this._model = model;
   }

   //
   //
   //
   protected void numberOfStages(int num){
      this._numberOfStages = num;
   }

   //
   //
   //
   protected void payloadData(PayloadData pd){

      this._payloadData = pd;
   }

   //
   //
   //
   protected void stages(List<StageData> data){
      if(data != null){
        this._stageData = data;
      }
   }

   //
   //
   //
   protected void tolerance(double tolerance){
      if(tolerance > 0.){
         this._tolerance = tolerance;
      }
   }
}
//////////////////////////////////////////////////////////////////////
