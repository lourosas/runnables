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

public class GenericStage implements Stage, Runnable, ErrorListener{
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State INIT      = null; 
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null;

   private List<Engine>          _engines;
   private List<ErrorListener>   _errorListeners;
   private List<SystemListener>  _systemListeners;
   private DataFeeder            _feeder;
   private FuelSystem            _fuelSystem;
   private boolean               _kill;
   private Object                _obj;
   private Thread                _rt0;
   private boolean               _start;
   private StageData             _stageData;
   private StageData             _measStageData;
   private int                   _stageNumber;
   private LaunchStateSubstate   _state;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _engines          = null;
      _errorListeners   = null;
      _systemListeners  = null;
      _feeder           = null;
      _fuelSystem       = null;
      _kill             = false;
      _measStageData    = null;
      _obj              = null;
      _rt0              = null;
      _start            = false;
      _stageData        = null;
      _stageNumber      = -1;
      _state            = null;
   };

   /////////////////////////////Constructor///////////////////////////
   //
   //
   //
   public GenericStage(int number){
      if(number > 0){
         this._stageNumber = number;
      }
      this._obj = new Object();
      this.setUpThread();
   }

   ///////////////////////////Private Methods/////////////////////////
   //
   //
   //
   private void alertErrorListeners(){
      String error    = null;
      StageData sd    = null;
      synchronized(this._obj){
         sd    = this._measStageData;
         error = sd.error();
      }
      try{
         Iterator<ErrorListener> it = this._errorListeners.iterator();
         while(it.hasNext()){
            it.next().errorOccurred(new ErrorEvent(this,sd,error));
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void alertSubscribers(){
      StageData sd           = null;
      LaunchStateSubstate ss = this._state;

      String event = ss.state()+", "+ss.ascentSubstate();
      event += ", "+ss.ignitionSubstate()+", ";
      event += ss.prelaunchSubstate();
      synchronized(this._obj){
         sd = this._measStageData;
      }
      try{
         MissionSystemEvent mse = null;
         mse = new MissionSystemEvent(this,sd, event,ss);
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
   private boolean checkEngineDataErrors(){
      boolean isError = false;
      List<EngineData> list = null;
      synchronized(this._obj){
         list = this._measStageData.engineData();
      }
      try{
         Iterator<EngineData> it = list.iterator();
         while(it.hasNext()){
            isError |= it.next().isError();
         }
      }
      catch(NullPointerException npe){}
      return isError;
   }

   //
   //
   //
   private void checkErrors(){
      String err      = new String();
      boolean isError = false;
      if(this.checkMeasuredWeightError()){
         err += "Capacity Error\n";
         isError = true;
      }
      if(this.checkEngineDataErrors()){
         err += "Engine Data Errors\n";
         isError = true;
      }
      if(this.checkFuelSystemDataErrors()){
         err += "Fuel System Data Errors\n";
         isError = true;
      }
      if(isError){
         StageData sd = null;
         sd = this.setUpStageData(Double.NaN,null,null,err,isError);
         synchronized(this._obj){
            this._measStageData = sd;
         }
      }
   }

   //
   //
   //
   private boolean checkFuelSystemDataErrors(){
      boolean isError = false;
      FuelSystemData fsd = null;
      synchronized(this._obj){
         fsd = this._measStageData.fuelSystemData();
      }
      try{
         isError = fsd.isError();
      }
      catch(NullPointerException npe){}
      return isError;
   }

   //
   //
   //
   private boolean checkMeasuredWeightError(){
      double min      = Double.NaN;
      double max      = Double.NaN;
      StageData sd    = null;
      synchronized(this._obj){
         sd = this._measStageData;
      }
      double dryWeight = this._stageData.dryWeight();
      double tolerance = this._stageData.tolerance();
      double weight    = sd.weight();
      if(this._state.state() == INIT){
         min = dryWeight * tolerance;
         max = dryWeight * (2 - tolerance);
      }
      return ((weight < min)||(weight > max));
   }

   //
   //
   //
   private double computeStageWeight(StageData sd){
      Double weight         = sd.dryWeight();
      FuelSystemData fsd    = sd.fuelSystemData();
      List<TankData> list   = fsd.tankData();
      Iterator<TankData> it = list.iterator();
      while(it.hasNext()){
         TankData td = it.next();
         weight += (td.weight() - td.dryWeight());
      }
      return weight;
   }

   //
   //
   //
   private List<Hashtable<String,String>>
   getStageDataFromFile(String file)throws IOException{
      List<Hashtable<String,String>> list = null;
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         list = read.readStageInfo();
      }
      return list;
   }

   //
   //
   //
   private void initializeEngines(String file)throws IOException{
      try{
         int numEngines = this._stageData.numberOfEngines();
         int stage      = this._stageData.stageNumber();
         for(int i = 0; i < numEngines; ++i){
            Engine e = new GenericEngine(i, stage);
            e.initialize(file);
            try{
               this._engines.add(e);
            }
            catch(NullPointerException npe){
               this._engines = new LinkedList<Engine>();
               this._engines.add(e);
            }
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //
   //
   //
   private void initializeFuelSystem(String file)throws IOException{
      try{
         int stage        = this._stageData.stageNumber();
         int engines      = this._stageData.numberOfEngines();
         this._fuelSystem = new GenericFuelSystem(stage,engines);
         this._fuelSystem.initialize(file);
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //
   //
   //
   private void initializeStage(String file)throws IOException{
      String gsFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         gsFile = read.readPathInfo().get("stage");
      }
      this.initializeStageData(this.getStageDataFromFile(gsFile));
   }

   //
   //
   //
   private void initializeStageData
   (
      List<Hashtable<String,String>> data
   ){
      int stg = -1;  int te = -1;  double dw = Double.NaN;
      long mdl= Long.MIN_VALUE; double mw = Double.NaN;
      double tol = Double.NaN; double cw = Double.NaN;
      String err = null; boolean isE = false;
      List<EngineData> ed = null; FuelSystemData fsd = null;
      this._stageData = null;  //Erase all previous data
      Iterator<Hashtable<String,String>> it = data.iterator();
      while(it.hasNext()){
         Hashtable<String,String> ht = it.next();
         try{ stg = Integer.parseInt(ht.get("number"));}
         catch(NumberFormatException nfe){ stg = -1; }
         if(stg == this._stageNumber){
            try{ te = Integer.parseInt(ht.get("engines")); }
            catch(NumberFormatException nfe){ te = -1; }
            try{ dw = Double.parseDouble(ht.get("dryweight")); }
            catch(NumberFormatException nfe){ dw = Double.NaN; }
            try{ mw = Double.parseDouble(ht.get("maxweight")); }
            catch(NumberFormatException nfe){ mw = Double.NaN; }
            try{ mdl = Integer.parseUnsignedInt(ht.get("model"),16); }
            catch(NumberFormatException nfe){ mdl = -1; }
            try{ tol = Double.parseDouble(ht.get("tolerance")); }
            catch(NumberFormatException nfe){ tol = Double.NaN; }
            this._stageData = new GenericStageData(
                                    dw,    //Dry Weight
                                    err,   //Error (Init to NUll)
                                    mdl,   //Model Number
                                    isE,   //isError Boolean
                                    stg,   //Stage Number
                                    te,    //Total Engines
                                    mw,    //Max Weight
                                    tol,   //Tolerance
                                    cw,    //Calculated Weight
                                    ed,    //Engine Data
                                    fsd);  //Fuel System Data
         }
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
   private List<EngineData> monitorEngines(){
      List<EngineData> ed = null;
      try{
         ed = new LinkedList<EngineData>();
         synchronized(this._obj){
            Iterator<Engine> it = this._engines.iterator();
            while(it.hasNext()){
               ed.add(it.next().monitor());
            }
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         ed = null;
      }
      finally{
         return ed;
      }
   }

   //
   //
   //
   private FuelSystemData monitorFuelSystem(){
      FuelSystemData fsd = null;
      try{
         synchronized(this._obj){
            fsd = this._fuelSystem.monitor();
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         fsd = null;
      }
      finally{
         return fsd;
      }
   }

   //
   //
   //
   private void monitorStage(){
      String         error = null;
      //Might not need to do this...possibly delete
      List<EngineData> eng = this.monitorEngines();
      FuelSystemData   fsd = this.monitorFuelSystem();
      StageData         sd = null;
      try{
         if(this._feeder != null){
            RocketData rd = (RocketData)this._feeder.monitor();
            sd = (StageData)rd.stage(this._stageNumber);
         }
         else{
            throw new NullPointerException("No DataFeeder");
         }
      }
      catch(ClassCastException cce){
         try{
            sd = (StageData)this._feeder.monitor();
         }
         catch(ClassCastException e){
            e.printStackTrace();
            throw new NullPointerException("No StageDataFeeder");
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         sd = this._stageData;
      }
      finally{
         try{
            double weight = this.computeStageWeight(sd);
            //Set up the Stage Data that is measured/monitored
            sd = this.setUpStageData(weight,eng,fsd,error,false);
         }
         catch(NullPointerException npe){
            //In some respects, wait until it is initialized...
            //May need to come up with a better solution...
            sd = this._stageData;
         }
         finally{
            synchronized(this._obj){
               this._measStageData = sd;
            }
         }
      }
   }

   //
   //
   //
   private StageData setUpStageData
   (
      double           weight,
      List<EngineData> list,
      FuelSystemData   fsd,
      String           error,
      boolean          isError
   ){
      StageData sd = null;
      synchronized(this._obj){
         sd = this._measStageData;
      }
      try{
         double dw  = this._stageData.dryWeight();
         long   mdl = this._stageData.model();
         int    num = this._stageData.stageNumber();
         int    ens = this._stageData.numberOfEngines();
         double mw  = this._stageData.maxWeight();
         double tol = this._stageData.tolerance();
         if(Double.isNaN(weight) && list == null && fsd == null){
            if(isError || (error != null && error.length() > 0)){
               weight = sd.weight();
               list   = sd.engineData();
               fsd    = sd.fuelSystemData();
            }
         }
         sd = new GenericStageData(dw,     //Dry Weight
                                   error,  //Error
                                   mdl,    //Model
                                   isError,//Is Error
                                   num,    //Stage Number
                                   ens,    //Total Engines
                                   mw,     //Max Weight
                                   tol,    //Tollerance
                                   weight, //Calculated Weight
                                   list,   //Engine Data
                                   fsd);   //Fuel System Data
      }
      catch(NullPointerException npe){
         sd = this._stageData;
      }
      return sd;
   }

   //
   //
   //
   private void setUpThread(){
      String name = new String("Generic Stage "+ this._stageNumber);
      this._rt0   = new Thread(this, name);
      this._rt0.start();
   }

   ///////////////ErrorListener Interface Implementation//////////////
   //This is when the Instance is fed Errors from the Components...
   //TBD...
   //
   public void errorOccurred(ErrorEvent e){}

   ///////////////////Stage Interface Implementation//////////////////
   //
   //
   //
   public StageData monitor(){
      synchronized(this._obj){
         return this._measStageData;
      }
   }


   //
   //
   //
   public void initialize(String file)throws IOException{
      if(this._stageNumber > 0){
         this.initializeStage(file);
         this.initializeEngines(file);
         this.initializeFuelSystem(file);
      }
   }

   //
   //
   //
   public void addDataFeeder(DataFeeder feeder){
      if(feeder != null){
         this._feeder = feeder;
         //Add Components as needed
         try{
            this._fuelSystem.addDataFeeder(this._feeder);
         }
         catch(NullPointerException npe){
            //Should never get here
            npe.printStackTrace();
         }
         //Add the Data Feeder for the Engines eventually
         try{
            Iterator<Engine> it = this._engines.iterator();
            while(it.hasNext()){
               it.next().addDataFeeder(this._feeder);
            }
         }
         catch(NullPointerException npe){
            //Should never get here
            npe.printStackTrace();
         }
      }
   }

   //
   //
   //
   public void addErrorListener(ErrorListener listener){
      try{
         if(listener != null){
            this._errorListeners.add(listener);
         }
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
         if(listener != null){
            this._systemListeners.add(listener);
         }
      }
      catch(NullPointerException npe){
         this._systemListeners = new LinkedList<SystemListener>();
         this._systemListeners.add(listener);
      }
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate state){
      this._state = state;
      try{
         this._fuelSystem.setStateSubstate(this._state);
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      try{
         Iterator<Engine> it = this._engines.iterator();
         while(it.hasNext()){
            it.next().setStateSubstate(this._state);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   } 

   ////////////////Runnable Interface Implementation//////////////////
   //
   //
   //
   public void run(){
      try{
         int     count = 0;
         boolean check= false;
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this._state != null){
               if(this._state.state() == INIT){
                  //In the Initialization Stage, check every 2 seconds
                  if(count++%2000 == 0){
                     check = true;
                     count = 1;  //Reset the Counter
                  }
               }
            }
            if(check){
               this.monitorStage();
               this.checkErrors();
               this.alertSubscribers();
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
