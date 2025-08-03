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
   private void isError(){
      double  edge      = Double.NaN;
      double  ul        = Double.NaN;
      double  ll        = Double.NaN;
      double  lim       = 1. - this._tolerance;
      boolean inputGood = !Double.isNaN(this._emptyWeight);
      inputGood        &= !Double.isNaN(this._loadedWeight);
      boolean measGood  = !Double.isNaN(this._calculatedWeight);

      this._isError = false;
      this._error   = new String();

      if(inputGood && measGood){
         if(this._state.state() == INIT){
            ll = this._emptyWeight*this._tolerance;
            ul = this._emptyWeight*(2-this._tolerance);
            if(this._emptyWeight < 0){
               double temp = ul;
               ul = ll;
               ll = temp;
            }
         }
         else if(this._state.state() == PRELAUNCH){
         }
         if(this._calculatedWeight<ll || this._calculatedWeight>ul){
            this._isError = true;
            String error  = new String("Calculated Weight: ");
            if(this._calculatedWeight < ll){
               error += "too low";
            }
            else if(this._calculatedWeight > ul){
               error += "too high";
            }
            this.setError(error);
         }
      }
   }

   //
   //
   //
   private void measureWeight(List<StageData> sd/*,CasuledData cap*/){
      this._calculatedWeight = 0;
      try{
         Iterator<StageData> it = sd.iterator();
         while(it.hasNext()){
            this._calculatedWeight += it.next().weight();
         }
         //this._calculatedWeight += cap.weight(); //TBD
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         this._calculatedWeight = Double.NaN;
      }
   }

   //
   //
   //
   private RocketData monitorRocket(){
      RocketData data = null;
      try{
         Iterator<Stage> it   = this._stages.iterator();
         List<StageData> list = new LinkedList<StageData>();
         while(it.hasNext()){
            list.add(it.next().monitor());
         }
         this.measureWeight(list/*,this.capsuleData*/); //TBD
         this.isError();
         data = new GenericRocketData(this._model,
                                      this._currentStage,
                                      this._numberOfStages,
                                      this._emptyWeight,
                                      this._loadedWeight,
                                      this._calculatedWeight,
                                      this._isError,
                                      this._error,
                                      list,
                                      this._tolerance);
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         data = null;
      }
      finally{
         return data;
      }
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

   //
   //
   //
   private void setError(String errorType){
      this._error = new String();
      java.text.DecimalFormat df = null;
      df = new java.text.DecimalFormat("###,###,###.##");
      if(errorType.toUpperCase().contains("CALCULATED")){
         this._error  = errorType;
         this._error += "\nState:      "+this._state.state();
         String formatted = df.format(this._calculatedWeight);
         this._error += "\nCalculated: "+formatted;
         this._error += "\nEmpty:      "+this._emptyWeight;
         this._error += "\nLoaded:     "+this._loadedWeight;
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
      if(data.containsKey("tolerance")){
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
         stage.addErrorListener(this);
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
         try{
            //Add the Components
            Iterator<Stage> it = this._stages.iterator();
            while(it.hasNext()){
               Stage stage = (Stage)it.next();
               stage.addDataFeeder(this._feeder);
            }
         }
         catch(NullPointerException npe){
            //Should never get here!!!
            npe.printStackTrace();
         }
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
               if(this._isError){
                  this.alertErrorListeners();
               }
               RocketData rd = this.monitorRocket();
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
