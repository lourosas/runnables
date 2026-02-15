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

   private LaunchStateSubstate.State INIT                      = null;
   private LaunchStateSubstate.State PRELAUNCH                 = null;
   private LaunchStateSubstate.State IGNITION                  = null;
   private LaunchStateSubstate.State LAUNCH                    = null;
   private LaunchStateSubstate.PreLaunchSubstate SET           = null;
   private LaunchStateSubstate.PreLaunchSubstate CONT          = null;
   private LaunchStateSubstate.PreLaunchSubstate FUEL          = null;
   private LaunchStateSubstate.PreLaunchSubstate HOLD          = null;
   private LaunchStateSubstate.IgnitionSubstate  IGN           = null;
   private LaunchStateSubstate.IgnitionSubstate  BUP           = null;
   private LaunchStateSubstate.AscentSubstate    STG           = null;
   private LaunchStateSubstate.AscentSubstate    IGNE          = null;

   //Accumulation of all the Weight of the Stages!!!
   //Get rid of Calculated Weight
   private int                 _currentStage;
   private List<ErrorListener> _errorListeners;
   private List<SystemListener>_systemListeners;
   private DataFeeder          _feeder;
   private boolean             _kill;
   private Object              _obj;
   private Thread              _rt0;
   private List<Stage>         _stages;
   private boolean             _start;
   private LaunchStateSubstate _state;
   private RocketData          _rocketData;
   private RocketData          _measRocketData;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;
      SET       = LaunchStateSubstate.PreLaunchSubstate.SET;
      CONT      = LaunchStateSubstate.PreLaunchSubstate.CONTINUE;
      FUEL      = LaunchStateSubstate.PreLaunchSubstate.FUELING;
      HOLD      = LaunchStateSubstate.PreLaunchSubstate.HOLD;
      IGN       = LaunchStateSubstate.IgnitionSubstate.IGNITION;
      BUP       = LaunchStateSubstate.IgnitionSubstate.BUILDUP;
      STG       = LaunchStateSubstate.AscentSubstate.STAGING;
      IGNE      = LaunchStateSubstate.AscentSubstate.IGNITEENGINES;

      _currentStage     = -1;
      _errorListeners   = null;
      _feeder           = null;
      _obj              = null;
      _rt0              = null;
      _stages           = null;
      _start            = false;
      _state            = null;
      _errorListeners   = null;
      _systemListeners  = null;
      _rocketData       = null;
      _measRocketData   = null;
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public GenericRocket(){
      this._obj = new Object();
      this.setUpThread();
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void alertErrorListeners(){
      String error  = null;
      RocketData rd = null;
      synchronized(this._obj){
         rd    = this._measRocketData;
         error = rd.error();
      }
      try{
         Iterator<ErrorListener> it = this._errorListeners.iterator();
         while(it.hasNext()){
            it.next().errorOccurred(new ErrorEvent(this,rd,error));
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void alertSubscribers(){
      RocketData rd          = null;
      LaunchStateSubstate ss = this._state;

      String event = ss.state()+", "+ss.ascentSubstate();
      event += ", "+ss.ignitionSubstate()+", ";
      event += ss.prelaunchSubstate();
      synchronized(this._obj){
         rd = this._measRocketData;
      }
      try{
         MissionSystemEvent mse = null;
         mse = new MissionSystemEvent(this,rd,event,ss);
         Iterator<SystemListener> it = null;
         it = this._systemListeners.iterator();
         while(it.hasNext()){
            it.next().update(mse);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void checkErrors(){
      String err        = new String();
      boolean isError   = false;
      if(isError = this.checkMeasurementWeightError()){
         err+= "Measured Weight Error\n";
      }
      if(isError = this.checkStageErrors()){
         err += "Stage Errors\n";
      }
      if(isError){
         RocketData rd = null;
         rd = this.setUpRocketData(Double.NaN,null,err,isError);
         synchronized(this._obj){
            this._measRocketData = rd;
         }
         this.alertErrorListeners();
      }
   }

   //
   //
   //
   private boolean checkMeasurementWeightError(){
      double min    = Double.NaN;
      double max    = Double.NaN;
      double weight = Double.NaN;
      synchronized(this._obj){
         weight = this._measRocketData.calculatedWeight();
      }
      double dryWeight = this._rocketData.emptyWeight();
      double tolerance = this._rocketData.tolerance();
      if(this._state.state() == INIT){
         min = dryWeight * tolerance;
         max = dryWeight * (2 - tolerance);
      }
      return ((weight < min) || (weight > max));
   }
   
   //
   //
   //
   private boolean checkStageErrors(){
      boolean isError      = false;
      List<StageData> list = null;
      synchronized(this._obj){
         list = this._measRocketData.stages();
      }
      try{
         Iterator<StageData> it = list.iterator();
         while(it.hasNext()){
            isError = it.next().isError();
         }
      }
      catch(NullPointerException npe){
         isError = false;
      }
      return isError;
   }

   //
   //
   //
   private double computeRocketWeight
   (
      RocketData      rd,
      List<StageData> list
   ){
      double weight = 0.; //Of course, start with a 0 weight
      //Get the current weight of the payload and the weights of the
      //stages and add them together...do not try to get all cute...
      //weight += {get the capsule weight}--to be monitored!! TBD
      Iterator<StageData> it = list.iterator();
      while(it.hasNext()){
         StageData sd = it.next();
         weight += sd.weight();
      }
      return weight;
   }

   //
   //
   //
   private void initializeRocket(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         //this.setRocketData(read.readRocketInfo());
         this.initializeRocketDataJSON(read.readRocketInfo());
      }
   }

   //
   //
   //
   private void initializeRocketDataJSON(Hashtable<String,String> ht){
      String mdl = null; int cs = this._currentStage;
      int ns = -1; double ew = Double.NaN; double lw = Double.NaN;
      double cw = Double.NaN;  boolean isE = false; String err = null;
      List<StageData> lst = null; double tol = Double.NaN;
      //the JSON data is all lower case...
      try{ mdl = ht.get("model");}
      catch(NullPointerException npe){}
      try{ ns  = Integer.parseInt(ht.get("stages")); }
      catch(NumberFormatException nfe){ ns = -1; }
      catch(NullPointerException  npe){ ns = -1; }
      try{ ew = Double.parseDouble(ht.get("empty_weight")); }
      catch(NumberFormatException nfe){ ew = Double.NaN; }
      catch(NullPointerException  npe){ ew = Double.NaN; }
      try{ lw = Double.parseDouble(ht.get("loaded_weight")); }
      catch(NumberFormatException nfe){ lw = Double.NaN; }
      catch(NullPointerException  npe){ lw = Double.NaN; }
      try{ tol = Double.parseDouble(ht.get("tolerance")); }
      catch(NumberFormatException nfe){ lw = Double.NaN; }
      catch(NullPointerException  npe){ lw = Double.NaN; }
      this._rocketData = new GenericRocketData(mdl,//Model
                                               cs, //Current Stage
                                               ns, //No. of Stages
                                               ew, //Empty Weight
                                               lw, //Loaded Weight
                                               cw, //Calculated Weight
                                               isE,//Is Error
                                               err,//Error String
                                               lst,//Stages List
                                               tol);//Tollerance
   }

   //
   //
   //
   private void initializeStage(String file)throws IOException{
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      Hashtable<String,String> ht = read.readRocketInfo();
      int numStages = -1;
      try{ numStages = Integer.parseInt(ht.get("stages")); }
      catch(NullPointerException  npe){}
      catch(NumberFormatException nfe){}
      this._stages = new LinkedList<Stage>();
      for(int i = 0; i < numStages; ++i){
         //For simplicity, stages need to be positive numbers...
         GenericStage stage = new GenericStage(i+1);
         //Initialize the stage
         stage.initialize(file);
         stage.addErrorListener(this);
         this._stages.add(stage);
      }
   }

   //
   //
   //
   private boolean isPathFile(String file)throws IOException{
      boolean isPath = false;
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         if(read.readPathInfo().get("parameter") == null){
            throw new NullPointerException("Not a Path File");
         }
         isPath = true;
      }
      catch(IOException ioe){
         isPath = false;
         throw ioe;
      }
      catch(NullPointerException npe){
         isPath = false;
      }
      finally{
         return isPath;
      }
   }

   //
   //
   //
   private void monitorRocket(){
      String error        = null;
      //Might not need...possibly delete...
      List<StageData> lst = this.monitorStage();
      RocketData rd       = null;
      try{
         if(this._feeder != null){
            rd = (RocketData)this._feeder.monitor();
         }
         else{
            throw new NullPointerException("No Data Feeder");
         }
      }
      catch(ClassCastException cce){
         throw new NullPointerException("No Data Feeder");
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         rd = this._rocketData;
      }
      finally{
         try{
            double weight = this.computeRocketWeight(rd,lst);
            rd = this.setUpRocketData(weight,lst,error,false);
         }
         catch(NullPointerException npe){
            rd = this._rocketData;
         }
         finally{
            synchronized(this._obj){
               this._measRocketData = rd;
            }
         }
      }
   }

   //
   //
   //
   private List<StageData> monitorStage(){
      List<StageData> list = null;
      try{
         list = new LinkedList<StageData>();
         synchronized(this._obj){
            Iterator<Stage> it = this._stages.iterator();
            while(it.hasNext()){
               list.add(it.next().monitor());
            }
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         list = null;
      }
      finally{
         return list;
      }
   }

   //
   //
   //
   private RocketData setUpRocketData
   (
      double          weight,
      List<StageData> list,
      String          error,
      boolean         isError
   ){
      RocketData rd = null;
      synchronized(this._obj){
         rd = this._measRocketData;
      }
      try{
         int    cs  = rd.currentStage(); //This has to be most current
         double ew  = this._rocketData.emptyWeight();
         double lw  = this._rocketData.loadedWeight();
         String mdl = this._rocketData.model();
         int    ns  = this._rocketData.numberOfStages();
         double tol = this._rocketData.tolerance();
         if(Double.isNaN(weight) && list == null){
            if(isError || (error!=null && error.length() > 0)){
               weight = rd.calculatedWeight();
               list   = rd.stages();
            }
         }
         rd = new GenericRocketData(mdl,    //model
                                    cs,     //Current Stage
                                    ns,     //Number of Stages
                                    ew,     //Empty Weight
                                    lw,     //Loaded Weight
                                    weight, //Calculated Weight
                                    isError,
                                    error,  //error
                                    list,   //Stages
                                    tol);   //Tolerance
      }
      catch(NullPointerException npe){
         rd = this._rocketData;
      }
      finally{
         return rd;
      }
   }

   //
   //
   //
   private void setUpThread(){
      this._rt0 = new Thread(this, "Generic Rocket");
      this._rt0.start();
   }

   ////////////////////ErrorListener Implementation///////////////////
   //TBD...
   //
   //
   public void errorOccurred(ErrorEvent e){}

   //////////////////Rocket Interface Implementation//////////////////
   //
   //
   //
   public RocketData monitor(){
      synchronized(this._obj){
         return this._measRocketData;
      }
   }

   //
   //
   //
   public void initialize(String file)throws IOException{
      String rFile = file;
      String sFile = file;
      this._currentStage = 1;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         rFile = read.readPathInfo().get("rocket");
         sFile = read.readPathInfo().get("stage");
      }
      //Real Simple for initialization...
      this.initializeRocket(rFile);
      this.initializeStage(sFile);
   }

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
      int cs = -1;
      try{
         this._measRocketData.currentStage();
      }
      catch(NullPointerException npe){
         cs = this._currentStage;     
      }
      return cs;
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate state){
      this._state = state;
      try{
         Iterator<Stage> it = this._stages.iterator();
         while(it.hasNext()){
            it.next().setStateSubstate(this._state);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //
   //
   //
   public int totalStages(){
      int ts = -1;
      try{
         this._rocketData.numberOfStages();
      }
      catch(NullPointerException npe){}
      return ts;
   }
   ///////////////Runnable Interface Implementation///////////////////
   //
   //
   //
   public void run(){
      try{
         int     count = 0;
         boolean check = false;
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this._state != null){
               if(this._state.state() == INIT){
                  //In the Initialization Stage, check every
                  //10 Seconds
                  if(count++%10000 == 0){
                     check = true;
                     count = 1; //Reset the Counter
                  }
               }
            }
            if(check){
              System.out.println("\nGR 1\n+++++++++++++++++++++++");
              System.out.print("Rocket: ");
              System.out.println(Thread.currentThread().getName());
              System.out.print("Rocket: ");
              System.out.println(Thread.currentThread().getId());
              //Eventually perform all of this...
              this.monitorRocket();
              this.checkErrors();
              this.alertSubscribers();
              System.out.println("+++++++++++++++++++++++\nGR 2\n");
              check = false;
            }
            Thread.sleep(1);
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
