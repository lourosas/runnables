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

import java.util.*;
import java.io.*;
import rosas.lou.runnables.*;

public class GenericTank implements Tank, Runnable{
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State INIT      = null; 
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null; 

   private boolean    _kill;
   private int        _stageNumber;
   private int        _tankNumber;
   private boolean    _start;

   private DataFeeder           _feeder;
   private List<ErrorListener>  _errorListeners; 
   private List<SystemListener> _systemListeners;
   private LaunchStateSubstate  _state;
   private Object               _obj;
   private Thread               _rt0;
   private TankData             _tankData;
   private TankData             _measuredTankData;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _kill             = false;
      _obj              = null;
      _stageNumber      = -1;
      _tankNumber       = -1;

      _feeder           = null;
      _errorListeners   = null;
      _measuredTankData = null;
      _rt0              = null;
      _state            = null;
      _systemListeners  = null;
      _tankData         = null;
   };

   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public GenericTank(int stage, int number){
      if(stage > 0 && number > 0){
         this._stageNumber = stage;
         this._tankNumber  = number;
         this._obj = new Object();
         this.setUpThread();
      }
   }

   ////////////////////////////Private Methods////////////////////////
   //
   //
   //
   private void alertErrorListeners(){
      String error = null;
      TankData td  = null;
      synchronized(this._obj){
         error = this._measuredTankData.error();
         td    = this._measuredTankData;
      }
      try{
         Iterator<ErrorListener> it = this._errorListeners.iterator();
         while(it.hasNext()){
            it.next().errorOccurred(new ErrorEvent(this,td,error));
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void alertSubscribers(){
      TankData            td = null;
      LaunchStateSubstate ss = this._state;
      
      String event = ss.state()+", "+ss.ascentSubstate();
      event += ", "+ss.ignitionSubstate()+", ";
      event += ss.prelaunchSubstate();
      synchronized(this._obj){
         td = this._measuredTankData;
      }
      try{
         Iterator<SystemListener> it = null;
         it = this._systemListeners.iterator();
         while(it.hasNext()){
            MissionSystemEvent mse = null;
            mse = new MissionSystemEvent(this,td,event,ss);
            it.next().update(mse);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private double calculateMassLossRate(){
      double mlr = Double.NaN;
      double den = Double.NaN;
      double er  = Double.NaN;
      try{
         if(this._feeder == null){
            throw new NullPointerException("No DateFeeder");
         }
         synchronized(this._obj){
            den = this._measuredTankData.density();
            er  = this._measuredTankData.emptyRate();
         }    
         mlr = den * er;
      }
      catch(NullPointerException npe){
         mlr = Double.NaN; //Temporary
      }
      finally{
         return mlr;
      }
   }

   //
   //
   //
   private double calculateWeight(){
      double cap       = Double.NaN;
      double den       = Double.NaN;
      double dryWeight = Double.NaN;
      double weight    = Double.NaN;
      double g         = 9.81;  //Acceleration of Gravity...
      try{
         if(this._feeder == null){
            throw new NullPointerException("No DataFeedeer");
         }
         synchronized(this._obj){
            cap       = this._measuredTankData.capacity();
            dryWeight = this._measuredTankData.dryWeight();
            den       = this._measuredTankData.density();
         }
         double mass = cap * den;
         weight      = (mass * g) + dryWeight;
      }
      catch(NullPointerException npe){
         //Default value for now...stop gap...until hardare can
         //be queried
         weight = this._tankData.dryWeight();
      }
      finally{
         return weight;
      }
   }

   //The only time Weight needs calculating is in Init and Prelaunch
   //States when completely full...making sure to conform to what the
   //to the tolerance
   private boolean checkCalculatedWeight(){
      boolean isError     = false;
      double  weight      = Double.NaN;
      double  tolerance   = Double.NaN;
      double  emptyWeight = Double.NaN;
      double  scale       = Double.NaN;
      double  value       = Double.NaN;
      synchronized(this._obj){
         weight      = this._measuredTankData.weight();
         tolerance   = this._measuredTankData.tolerance();
         emptyWeight = this._measuredTankData.dryWeight();
      }
      double min = Double.NaN; double max = Double.NaN;
      if(this._state.state() == INIT){
         min = emptyWeight*tolerance;
         max = emptyWeight*(2. - tolerance);
      }
      isError |= ((weight > max)||(weight < min));
      return isError;
   }


   //
   //
   //
   private boolean checkCapacity(){
      boolean isError  = false;
      double capacity  = Double.NaN;
      double tolerance = Double.NaN;
      synchronized(this._obj){
         capacity  = this._measuredTankData.capacity();
         tolerance = this._measuredTankData.tolerance();
      }
      double min = Double.NaN; double max = Double.NaN;

      //In the Initialization State, Capacity is 0 within error!
      //regardless of Substate...no negative capacity...
      if(this._state.state() == INIT){
         max = 1. - tolerance;
         isError |= (capacity > max);
      }
      return isError;
   }

   //
   //
   //
   private boolean checkEmptyRate(){
      boolean isError = false;
      double emptyRate = Double.NaN;
      double tolerance = Double.NaN;
      synchronized(this._obj){
          emptyRate  = this._measuredTankData.emptyRate();
          tolerance  = this._measuredTankData.tolerance();
      }
      double  min = Double.NaN; double max = Double.NaN;
      //In the Initialization State, Empty Rate is 0 within error
      if(this._state.state() == INIT){
         max = 1. - tolerance;
         isError |= (emptyRate > max);
      }
      return isError;
   }

   //Based on State, check: capacity, empty rate, mass loss rate,
   //temperature, caculated weight...with the measured tank data
   //
   private void checkErrors(){
      TankData td = null;
      synchronized(this._obj){
         td = this._measuredTankData;
      }
      String err = new String();
      double cap = td.capacity();  double den = td.density();
      double dw  = td.dryWeight(); double er  = td.emptyRate();
      String fue = td.fuel();      double mlr = td.massLossRate();
      long mod   = td.model();     int    nbr = td.number();
      int stg    = td.stage();     double temp= td.temperature();
      double tol = td.tolerance(); double wgt = td.weight();

      boolean isError = false;
      if(this.checkCapacity()){
         err += "Capacity Error\n";
         isError = true;
      }
      if(this.checkEmptyRate()){
         err += "Tank Empty Rate Error\n";
         isError = true;
      }
      if(this.checkMassLossRate()){
         err += "Empty Loss Rate Error\n";
         isError = true;
      }
      if(this.checkTemperature()){
         err += "Temperature Error\n";
         isError = true;
      }
      if(this.checkCalculatedWeight()){
         err += "Calculated Weight Error\n";
         isError = true;
      }
      if(isError){
         td = new GenericTankData(cap,     //Capacity
                                  den,     //Density
                                  dw,      //Dry Weight
                                  er,      //Empty Rate
                                  err,     //Error
                                  fue,     //Fuel
                                  isError, //Is Error
                                  mlr,     //Mass Loss Rate
                                  mod,     //Model
                                  nbr,     //Tanks Number
                                  stg,     //Stage
                                  temp,    //temperature
                                  tol,     //Tolerance
                                  wgt);    //Measured Weight
         synchronized(this._obj){
            this._measuredTankData = td;
         }
         this.alertErrorListeners();
      }
   }

   //
   //
   //
   private boolean checkMassLossRate(){
      boolean isError      = false;
      double  massLossRate = Double.NaN;
      double  tolerance    = Double.NaN;
      synchronized(this._obj){
         massLossRate = this._measuredTankData.massLossRate();
         tolerance    = this._measuredTankData.tolerance();
      }
      double min = Double.NaN; double max = Double.NaN;

      //In the Initialization State, Mass Loss Rate is 0 within error!
      //regardless of Substate...no negative Mass Loss Rate
      if(this._state.state() == INIT){
         max = 1. - tolerance;
         isError |= (massLossRate > max);
      }
      return isError;
   }

   //
   //
   //
   private boolean checkTemperature(){
      boolean isError     = false;
      double  temperature = Double.NaN;
      double  tolerance   = Double.NaN;
      synchronized(this._obj){
         temperature = this._measuredTankData.temperature();
         tolerance   = this._measuredTankData.tolerance();
      }
      double min = Double.NaN; double max = Double.NaN;
      //In the Initialization State, anything between the Freezing and
      //boiling point of water is acceptable
      if(this._state.state() == INIT){
         min = 273.15; max = 373.15;
         isError |= (temperature < min || temperature > max);
      }
      return isError;
   }

   //
   //
   //
   private void initializeTankDataJSON(String file)throws IOException{
      double cap = Double.NaN; double den = Double.NaN; int num = -1;
      double dw  = Double.NaN; double er  = Double.NaN; int stg = -1;
      String err = null; String fue = null; boolean isE = false;
      double mlr = Double.NaN; long mod = Long.MIN_VALUE;
      double temp = Double.NaN; double tol = Double.NaN;
      double wgt = Double.NaN;
      //Test Print
      System.out.println("Generic Tank: "+file);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst = read.readTankDataInfo();
         Iterator<Hashtable<String,String>> it = lst.iterator();
         while(it.hasNext()){
            Hashtable<String,String> ht = it.next();
            try{ stg = Integer.parseInt(ht.get("stage")); }
            catch(NumberFormatException nfe){ stg = -1; }
            try{ num = Integer.parseInt(ht.get("number")); }
            catch(NumberFormatException nfe){ num = -1;}
            if(this._stageNumber == stg && this._tankNumber == num){
               try{ cap = Double.parseDouble(ht.get("capacity"));}
               catch(NumberFormatException nfe){ cap = Double.NaN; }
               try{ den = Double.parseDouble(ht.get("density")); }
               catch(NumberFormatException nfe){ den = Double.NaN; }
               try{ dw = Double.parseDouble(ht.get("dryweight")); }
               catch(NumberFormatException nfe){ dw = Double.NaN; }
               try{ er = Double.parseDouble(ht.get("rate")); }
               catch(NumberFormatException  nfe){ er = Double.NaN; }
               fue = ht.get("fuel"); 
               try{ mod = Long.parseLong(ht.get("model"),16); }
               catch(NumberFormatException nfe){ mod=Long.MIN_VALUE; }
               try{ temp=Double.parseDouble(ht.get("temperature")); }
               catch(NumberFormatException nfe){ temp=Double.NaN; }
               try{ tol = Double.parseDouble(ht.get("tolerance")); }
               catch(NumberFormatException nfe){ tol = Double.NaN; } 
               this._tankData = new GenericTankData(
                                                cap,//Capcity
                                                den,//Density
                                                dw, //Dry Weight
                                                er, //Empty Rate
                                                err,//Error
                                                fue,//Fuel
                                                isE,//isError
                                                mlr,//Mass Loss Rate
                                                mod,//Model
                                                num,//Tank Number
                                                stg,//Stage
                                                temp,//Temperature
                                                tol,//Tolerance
                                                wgt);//Weight
            }
         }
      }
      catch(IOException ioe){
         this._tankData = null;
         throw ioe;
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
  private void measure(){
      try{
         if(this._feeder != null){
            RocketData rd = (RocketData)this._feeder.monitor(); 
            StageData  sd = rd.stage(this._stageNumber);
            FuelSystemData fsd = sd.fuelSystemData();
            List<TankData> lst = fsd.tankData();
            Iterator<TankData> it = lst.iterator();
            while(it.hasNext()){
               TankData td = it.next();
               int sn  = td.stage();
               int idx = td.number();
               if(sn == this._stageNumber && idx == this._tankNumber){
                  synchronized(this._obj){
                     this._measuredTankData = td;
                  }
               }
            }
         }
         else{
            throw new NullPointerException("No DataFeeder");
         }
      }
      catch(ClassCastException cce){
         try{
            synchronized(this._obj){
               TankData td = (TankData)this._feeder.monitor();
               this._measuredTankData = td;
            }
         }
         catch(ClassCastException e){
            cce.printStackTrace();
            throw new NullPointerException("No TankDataFeeder");
         }
      }
      catch(NullPointerException npe){
         synchronized(this._obj){
            npe.printStackTrace();
            this._measuredTankData = this._tankData;
         }
      }
   }

   //
   //
   //
   private void monitorTank(){
      this.measure();
      this.setUpTankData();//Needs a different name!!!
   }

   //
   //
   //
   private void setUpTankData(){
      String err  = null;
      double cap  = Double.NaN;
      double rate = Double.NaN;
      double temp = Double.NaN;
      //Directly measured
      synchronized(this._obj){
         cap = this._measuredTankData.capacity();
      }
      double den = this._tankData.density();
      double dw  = this._tankData.dryWeight();
      //Directly Measured
      synchronized(this._obj){
         rate = this._measuredTankData.emptyRate();
      }
      String fue = this._tankData.fuel();
      boolean isE= false;
      long   mdl = this._tankData.model();
      int    stg = this._tankData.stage();
      int    tnk = this._tankData.number();
      //Directly Measured
      synchronized(this._obj){
         temp = this._measuredTankData.temperature();
      }
      double tol = this._tankData.tolerance();
      //Calculated
      double mlr = this.calculateMassLossRate();
      double wgt = this.calculateWeight();
      GenericTankData td = new GenericTankData(
                                      cap,  //Capacity
                                      den,  //Density
                                      dw,   //Dry Weight
                                      rate, //Empty Rate
                                      err,  //Error
                                      fue,  //Fuel
                                      isE,  //Is Error
                                      mlr,  //Mass Loss Rate
                                      mdl,  //Model
                                      tnk,  //Tank Number
                                      stg,  //Stage
                                      temp, //Temperature
                                      tol,  //Tolerance
                                      wgt); //Weight
      synchronized(this._obj){
         this._measuredTankData = td;
      }
   }

   //
   //
   //
   private void setUpThread(){
      String name = new String("Tank: "+this._stageNumber+", ");
      name += this._tankNumber;
      this._rt0 = new Thread(this, name);
      this._rt0.start();
   }

   //
   //
   //
   private void tankData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         this.initializeTankDataJSON(file);
      }
   }

   ///////////////////////Tank Interface Methods//////////////////////
   //
   //
   //
   public TankData monitor(){
      synchronized(this._obj){
         return this._measuredTankData;
      }
   }

   //
   //
   //
   public void initialize(String file)throws IOException{
      if(this._stageNumber > 0 && this._tankNumber > 0){
         String tdFile = file;
         if(this.isPathFile(tdFile)){
            LaunchSimulatorJsonFileReader read = null;
            read = new LaunchSimulatorJsonFileReader(tdFile);
            tdFile = read.readPathInfo().get("tank");
         }
         this.tankData(tdFile);
      }
   }

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
      if(listener != null){
         try{
            this._systemListeners.add(listener);
         }
         catch(NullPointerException npe){
            this._systemListeners = new LinkedList<SystemListener>();
            this._systemListeners.add(listener);
         }
      }
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      this._state = stateSubstate;
   }

   //////////////////////////Runnble Interface////////////////////////
   //
   //
   //
   public void run(){
      try{
         int counter   = 0;
         boolean check = false;
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this._state != null){
               if(this._state.state() == INIT){
                  if(counter++%1000 == 0){
                     //For Initialize, check every second...
                     check = true;
                  }
               }
            }
            if(check){
               this.monitorTank();
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
