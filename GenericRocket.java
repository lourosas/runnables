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
import java.io.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class GenericRocket implements Rocket, Runnable, ErrorListener{
   private LaunchStateSubstate.State INIT      = null;
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null;

   private String      _error;
   private boolean     _isError;
   private int         _numberOfStages;
   //Accumulation of all the Weight of the Stages!!!
   //Get rid of Calculated Weight
   private double      _calculatedWeight;
   private int         _currentStage;
   private DataFeeder  _feeder;
   private double      _emptyWeight;
   private double      _loadedWeight;
   private List<Stage> _stages;
   private String      _model;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _calculatedWeight = Double.NaN;
      _currentStage     = -1;
      _error            = null;
      _feeder           = null;
      _isError          = false;
      _numberOfStages   = -1;
      _emptyWeight      = Double.NaN;
      _loadedWeight     = Double.NaN;
      _model            = null;
      _stages           = null;
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public GenericRocket(){}

   /////////////////////////Private Methods///////////////////////////
   //Calculate the Agregate weight of all the Stages...for comparison
   //THIS HAS GOT TO GO AWAY!!!  Rely COMPLETELY ON THE FEEDER!!!
   //
   private void calculateWeight(List<StageData> data){
      this._calculatedWeight = 0.;
      //Everything but the fuel
      this._calculatedWeight += this._emptyWeight;
      Iterator<StageData> it = data.iterator();
      while(it.hasNext()){
         StageData sd = it.next();
         this._calculatedWeight += sd.weight();
         //The Dry Weight already taken into account in the dry weight
         this._calculatedWeight -= sd.dryWeight();
      }
   }

   //
   //
   //
   private void isCalculatedWeightError(int state){
      if(state == PRELAUNCH){
         double tolerance = .95; //95% of loaded weight
         double wl = this._loadedWeight * tolerance;
         double ul = this._loadedWeight + wl;
         double ll = this._loadedWeight - wl;
         if(this._calculatedWeight<ll || this._calculatedWeight>ul){
            if(this._error == null){
               this._error = new String("\nCalculated Weight Error");
            }
            else{
               this._error += "\nCalculated Weight Error";
            }
            this._error += "\nCalculated Weight: ";
            this._error += ""+this._calculatedWeight;
            this._error += "\nLoaded Weight: " + this._loadedWeight;
            this._error += "\n";
            this._isError = true;
         }
      }
   }

   //
   //
   //
   private void isCurrentStageError(int state){
      if(state == PRELAUNCH){
         if(this._currentStage != 1){
            if(this._error == null){
               this._error = new String("\nStage Reporting Error");
            }
            else{
               this._error += "\nStage Reporting Error";
            }
            this._error += "\nReporting Stage: "+this._currentStage;
            this._error += "\nExpected Stage: 1\n";
            this._isError = true;
         }
      }
   }

   //For the given State, check to see if there is an error
   //
   //
   private void isError(int state){
      this.isCurrentStageError(state);
      this.isCalculatedWeightError(state);
      //more to come as needed...     
   }

   //
   //
   //
   private void rocketData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setRocketData(read.readRocketInfo());
      }
   }

   //Set up the data....
   //
   //
   private void setRocketData(Hashtable<String,String> data){
      //the JSON data is all lower case...
      if(data.containsKey("model")){
         this._model = data.get("model");
      }
      if(data.containsKey("stages")){
         try{
            this._numberOfStages=Integer.parseInt(data.get("stages"));
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
      if(data.containsKey("empty_weight")){
         try{
            double v = Double.parseDouble(data.get("empty_weight"));
            this._emptyWeight = v;
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
      if(data.containsKey("loaded_weight")){
         try{
            double v = Double.parseDouble(data.get("loaded_weight"));
            this._loadedWeight = v;
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
   }

   //
   //
   //
   private void stageData(String file)throws IOException{
      this._stages = new LinkedList<Stage>();
      for(int i = 0; i < this._numberOfStages; ++i){
         //For simplicity, stages need to be positive numbers...
         GenericStage stage = new GenericStage(i+1);
         //Initialize the stage
         stage.initialize(file);
         this._stages.add(stage);
      }
   }

   ////////////////////ErrorListener Implementation///////////////////
   //
   //
   //
   public void errorOccurred(ErrorEvent e){}

   //////////////////Rocket Interface Implementation//////////////////
   //
   //
   //
   public void addDataFeeder(DataFeeder feeder){
      this._feeder = feeder;
   }

   //
   //
   //
   public void addErrorListener(ErrorListner listener){}

   //
   //
   //
   public void addSystemListener(SystemListener listener){}

   //
   //
   //
   public int currentStage(){
      return -1;
   }

   //
   //
   //
   public void initialize(String file)throws IOException{
      //For initialization, always set the current stage to 1 (the
      //first stage)...
      //This is order dependent, consider changing...
      this._currentStage = 1;
      this.rocketData(file);
      this.stageData(file);
   }


   //
   //
   //
   public RocketData montitor(){
      return null;
   }

   //
   //
   //
   public RocketData monitorInitialization(){
      //@TODO Monitor Initialization for all the Stages and
      //capture the data!!!!!!
      /* NEEDS TO CHANGE!!!
      List<StageData> stageData = new LinkedList<StageData>();
      try{
         Iterator<Stage> it = this._stages.iterator();
         while(it.hasNext()){
            Stage s = it.next();
            StageData sd = s.monitorPrelaunch();
            stageData.add(sd);
            //Filter up the stage data...
            if(sd.isError()){
               this._isError = true;
               if(this._error == null){
                  this._error=new String(sd.error());
               }
               else{ this._error += sd.error(); }
            }
         }
      }
      catch(NullPointerException npe){}
      //Go ahead and calculate the weight of the rocket by calculating
      //the weight of each stage
      this.calculateWeight(stageData);
      //Determine if there is an error-->COMPARE WEIGHT!!!
      this.isError(PRELAUNCH);
      return new GenericRocketData(this._model,
                                    this._currentStage,
                                    this._numberOfStages,
                                    this._emptyWeight,
                                    this._loadedWeight,
                                    this._calculatedWeight,
                                    this._isError,
                                    this._error,
                                    stageData);
      */
      return null;
   }

   //
   //
   //
   public RocketData monitorPrelaunch(){
      return null;
   }

   //
   //
   //
   public RocketData monitorIgnition(){
      return null;
   }

   //
   //
   //
   public RocketData monitorLaunch(){
      return null;
   }

   //
   //
   //
   public RocketData monitorPostlaunch(){
      return null;
   }

   //
   //
   //
   public int totalStages(){
      return this._stages;
   }
   ///////////////Runnable Interface Implementation///////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
