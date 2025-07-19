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
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State INIT      = null;
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null;

   private String             _error;
   private boolean            _isError;
   private int                _numberOfStages;
   //Accumulation of all the Weight of the Stages!!!
   //Get rid of Calculated Weight
   private double              _calculatedWeight;
   private int                 _currentStage;
   private double              _emptyWeight;
   private List<ErrorListener> _errorListeners;
   private DataFeeder          _feeder;
   private boolean             _kill;
   private double              _loadedWeight;
   private String              _model;
   private Thread              _rt0;
   private List<Stage>         _stages;
   private boolean             _start;
   private LaunchStateSubstate _state;
   private List<SystemListener>_systemListeners;
   private double              _tolerance;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _calculatedWeight = Double.NaN;
      _currentStage     = -1;
      _emptyWeight      = Double.NaN;
      _error            = null;
      _errorListeners   = null;
      _feeder           = null;
      _kill             = false;
      _isError          = false;
      _numberOfStages   = -1;
      _loadedWeight     = Double.NaN;
      _model            = null;
      _rt0              = null;
      _stages           = null;
      _start            = false;
      _state            = null;
      _systemListeners  = null;
      _tolerance        = Double.NaN;
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public GenericRocket(){
      this.setUpThread();
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void alertErrorListeners(){}

   //
   //
   //
   private void alertSystemListeners(){}

   //
   //
   //
   private void calculateWeight(){
      //do this for the time being...
      //this._calculatedWeight = this._emptyWeight;
      this._calculatedWeight = 0.;
      Iterator<Stage> it     = this._stages.iterator();
      try{
         while(it.hasNext()){
            Stage s                 = it.next();
            StageData sd            = s.monitor();
            this._calculatedWeight += sd.weight();
         }
         System.out.print("====Rocket Weight: ");
         System.out.println(this._calculatedWeight);
      }
      catch(NullPointerException npe){
         //Temporary for the time being
         npe.printStackTrace();
         this._calculatedWeight = Double.NaN;
      }
   }

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
   private void isCalculatedWeightError
   (
      LaunchStateSubstate.State state
   ){
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
   private void isCurrentStageError(LaunchStateSubstate.State state){
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

   //
   //
   //
   private void isError(){
      double  edge      = Double.NaN;
      double  ul        = Double.NaN;
      double  ll        = Double.NaN;
      double  lim       = 1. - this._tolerance;
      boolean inputGood = !Double.isNaN(this._emptyWeight);
      inputGood        &= !Double.isNaN(this._loadedWeight);
      boolean measGood  = !Double.isNaN(this._calculatedWeight);
      //Test Prints to remove!!
      System.out.print("+++++Generic Rocket: ");
      System.out.print(this._emptyWeight+", "+this._loadedWeight);
      System.out.println(", "+this._calculatedWeight);
      System.out.println(inputGood+", "+measGood);
      System.out.println(this._state.state());
      if(inputGood && measGood){
      }

   }

   //For the given State, check to see if there is an error
   //
   //
   private void isError(LaunchStateSubstate.State state){
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
      if(data.contains("tolerance")){
         try{
            double t = Double.parseDouble(data.get("tolerance"));
            this._tolerance = t;
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
   }

   //
   //
   //
   private void setUpThread(){
      this._rt0 = new Thread(this, "Generic Rocket");
      this._rt0.start();
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
      if(feeder != null){
         this._feeder = feeder;
      }
   }

   //
   //
   //
   public void addErrorListener(ErrorListener listener){
      try{
         this._errorListeners.add(listener);
      }
      catch(NullPointerException npe){
         this._errorListeners = new LinkedList<ErrorListener>();
         this._errorListeners.add(listener);
      }
   }

   //
   //
   //
   public void addSystemListener(SystemListener listener){
      try{
         this._systemListeners.add(listener);
      }
      catch(NullPointerException npe){
         this._systemListeners = new LinkedList<SystemListener>();
         this._systemListeners.add(listener);
      }
   }

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
      //As with all components, the initialization phase assumes NO
      //fuel loaded, THEREFORE, the Rocket (calculated or not) is at
      //empty weight...this is PRIOR to actually measuring the System
      //which is about three seconds...or, whenever the Thread starts
      //up--so something is available...
      this._calculatedWeight = this._emptyWeight;
      this._state = new LaunchStateSubstate(INIT,null,null,null);
      this._start = true;
   }


   //
   //
   //
   public RocketData monitor(){
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
      return this._numberOfStages;
   }
   ///////////////Runnable Interface Implementation///////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this._start){
               //Everthing else comes directly from the Stages
               this.calculateWeight();
               this.isError();
               if(this._isError){
                  this.alertErrorListeners();
               }
               this.alertSystemListeners();
               if(this._state.state() == INIT){
                  //Temporary Prints, need to remove...
                  System.out.print("*****Rocket: ");
                  System.out.println(Thread.currentThread().getName());
                  System.out.print("*****Rocket: ");
                  System.out.println(Thread.currentThread().getId());
                  Thread.sleep(10000);
               }
            }
            else{
               Thread.sleep(1);
            }
         }
      }
      catch(InterruptedException ie){}
      catch(NullPointerException npe){
         npe.printStackTrace();
         System.exit(0);
      }
   
   }
}
//////////////////////////////////////////////////////////////////////
