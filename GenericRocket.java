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

public class GenericRocket implements Rocket, Runnable{
   private static final int PRELAUNCH = -1;
   private static final int IGNITION  =  0;
   private static final int LAUNCH    =  1;

   private String      _error;
   private boolean     _isError;
   private int         _numberOfStages;
   //Accumulation of all the Weight of the Stages!!!
   private double      _calculatedWeight;
   private int         _currentStage;
   private double      _emptyWeight;
   private double      _loadedWeight;
   private List<Stage> _stages;
   private String      _model;

   {
      _calculatedWeight = Double.NaN;
      _currentStage     = -1;
      _error            = null;
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
   //
   //
   private void calculateWeight(List<StageData> data){
      this._calculatedWeight = 0.; //Clear out the weight
      Iterator<StageData> it = data.iterator();
      //Do a test print, first...
      while(it.hasNext()){
         this._calculatedWeight += it.next().weight();
      }
      //Now add weight for a simple and hypothetical (small) payload!!
      this._calculatedWeight += 17658;
      //For the time being, just set the Calculated Weight to the
      //Loaded Weight
      //this._calculatedWeight = this._loadedWeight;
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
      this.isCurrentStageError(PRELAUNCH);
      this.isCalculatedWeightError(PRELAUNCH);
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

   //////////////////Rocket Interface Implementation//////////////////
   //
   //
   //
   /*
   public double emptyWeight(){
      return this._emptyWeight;
   }
   */

   //
   //
   //
   public void initialize(String file)throws IOException{
      //For initialization, always set the current stage to 1 (the
      //first stage)...
      this._currentStage = 1;
      this.rocketData(file);
      this.stageData(file);
   }

   //
   //
   //
   /*
   public double loadedWeight(){
      return this._loadedWeight;
   }
   */

   //
   //
   //
   /*
   public String model(){
      return this._model;
   }
   */

   //
   //
   //
   public RocketData monitorInitialization(){
      //@TODO Monitor Initialization for all the Stages and
      //capture the data!!!!!!
      List<StageData> stageData = new LinkedList<StageData>();
      try{
         Iterator<Stage> it = this._stages.iterator();
         while(it.hasNext()){
            stageData.add(it.next().monitorPrelaunch());
         }
      }
      catch(NullPointerException npe){}
      //Go ahead and calculate the weight of the rocket by calculating
      //the weight of each stage
      this.calculateWeight(stageData);
      //Determine if there is an error-->COMPARE WEIGHT!!!
      this.isError(PRELAUNCH);
      return new GenericRocketData(this._model,
                                    this._numberOfStages,
                                    this._emptyWeight,
                                    this._loadedWeight,
                                    this._calculatedWeight,
                                    this._isError,
                                    this._error,
                                    stageData);
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
   /*
   public int stages(){
      return this._stages;
   }
   */

   ///////////////Runnable Interface Implementation///////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
