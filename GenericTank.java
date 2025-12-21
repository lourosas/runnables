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
   private double calculateMassLossRate(){
      double mlr = 0.;
      try{
         if(this._feeder == null){
            throw new NullPointerException("No DateFeeder");
         }
         double den = this._measuredTankData.density();
         double er  = this._measuredTankData.emptyRate();    
         mlr = den * er;
      }
      catch(NullPointerException npe){
         mlr = this._tankData.massLossRate();
      }
      finally{
         return mlr;
      }
   }

   //
   //
   //
   private double calculateWeight(){
      double weight = 0.;
      double g      = 9.81;  //Acceleration of Gravity...
      try{
         if(this._feeder == null){
            throw new NullPointerException("No DataFeedeer");
         }
         double cap       = this._measuredTankData.capacity();
         double dryWeight = this._measuredTankData.dryWeight();
         double den       = this._measuredTankData.density();
         double mass      = cap * den;
         weight           = (mass * g) + dryWeight;
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

   //
   //
   //
   private String capacityError(double capacity){
      double edge      = Double.NaN;
      double ul        = Double.NaN;
      double ll        = Double.NaN;
      double tolerance = Double.NaN;
      String error     = null;
      try{
         //I should this make this a fucking method!!!
         TankData td = this.myTankData();
         tolerance   = td.tolerance();
         if(!Double.isNaN(capacity) && !Double.isNaN(tolerance)){
            //Will need to separate this out by state!!
            if(this._state.state() == INIT){
               //Durring the INIT, the TANK SHOULD BE EMPTY!!
               ll = tolerance - 1.;
               ul = 1 - tolerance;
            }
            else if(this._state.state() == PRELAUNCH){}
            if(capacity < ll || capacity > ul){
               error = new String("Measured Capacity: "+capacity);
               if(capacity < ll){
                  error += " too low";
               }
               else if (capacity > ul){
                  error += " too high";
               }
            }
         }
      }
      catch(NullPointerException npe){
         error     = new String(npe.getMessage());
         error    += ":  Capacity Error Unknown";
      }
      finally{
         return error;
      }
   }

   //
   //
   //
   private double convertToMass(double volume){
      //Convert from Liters to cubic meters...
      //Multiply by density to get mass...
      double mass = (volume/1000)*this._tankData.density();
      return mass;
   }

   //Take a volume and convert to weight (Newtons)
   //Volume in Liters
   //
   private double convertToWeight(double volume){
      double g = 9.81;
      //Convert from Liters to cubic meters...
      //Multiply by desnsity to get mass...
      //Multiply by g to get weight-->F = ma...
      double weight = (volume/1000)*this._tankData.density()*g;
      return weight;
   }

   //
   //
   //
   private String flowError(double rate){
      double edge      = Double.NaN;
      double ul        = Double.NaN;
      double ll        = Double.NaN;
      double tolerance = Double.NaN;
      String error     = null;
      try{
         TankData td = this.myTankData();
         tolerance   = td.tolerance();
         if(!Double.isNaN(rate) && !Double.isNaN(tolerance)){
            //Separate out by state Tank instance determines this...
            if(this._state.state() == INIT){
               //Durring INIT, rate should be 0!!!
               ll = tolerance - 1.;
               ul = 1 - tolerance;
            }
            else if(this._state.state() == PRELAUNCH){}
            if(rate < ll || rate > ul){
               error = new String("Measured Flow Rate: "+rate);
               if(rate < ll){
                  error += " too low";
               }
               else if(rate > ul){
                  error += " too high";
               }
            }
         }
      }
      catch(NullPointerException npe){
         error  = new String(npe.getMessage());
         error += ":  Rate Error Unknown";
      }
      finally{
         return error;
      }
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
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst = read.readTankInfo();
         Iterator<Hashtable<String,String>> it = lst.iterator();
         while(it.hasNext()){
            Hashtable<String,String> ht = it.next();
            try{ stg = Integer.parseInt(ht.get("stage")); }
            catch(NumberFormatException nfe){ stg = -1; }
            try{ num = Integer.parseInt(ht.get("number")); }
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

   //Depricated!!!  Do not use!!!  Keep for the Code...then delete!!!
   //
   //
   private void isError
   (
      double capacity,
      double emptyRate,
      double temperature,
      double weight
   ){
      boolean isError = false;
      String  error   = new String();
      //Determine the Error For all the Measured data...
      String capError = this.capacityError(capacity);
      if(capError != null){
         error   = " " + capError;
         isError = true;
      }
      String flowError = this.flowError(emptyRate);
      if(flowError != null){
         error  += " "+flowError;
         isError = true;
      }
      String tempError = this.temperatureError(temperature);
      if(tempError != null){
         error  += " " + tempError;
         isError = true;
      }
      String weightError = this.weightError(weight);
      if(weightError != null){
         error += " " + weightError;
         isError = true;
      }
      if(isError){
         double cap  = this._measuredTankData.capacity();
         double den  = this._measuredTankData.density();
         double dw   = this._measuredTankData.dryWeight();
         double er   = this._measuredTankData.emptyRate();
         String fuel = this._measuredTankData.fuel();
         int    tn   = this._measuredTankData.number();
         int    sn   = this._measuredTankData.stage();
         double te   = this._measuredTankData.temperature();
         double to   = this._measuredTankData.tolerance();
         double we   = this._measuredTankData.weight();
         TankData td = new GenericTankData(cap,den,dw,er,error,
                                           fuel,isError,tn,sn,te,
                                           to,we);
         synchronized(this._obj){
            this._measuredTankData = td;
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
         ioe.printStackTrace();  //Temporary
         throw ioe;
      }
      catch(NullPointerException npe){
         isPath = false;
         npe.printStackTrace(); //Temporary
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
               TankData td = it.next().stage();
               int sn  = td.stage();
               int idx = td.number();
               if(sn == this._stageNumber && idx == this._tankNumber){
                  this._measuredTankData = td;
               }
            }
         }
         else{
            throw new NullPointerException("No DataFeeder");
         }
      }
      catch(ClassCastException cce){
         try{
            this._measuredTankData = (TankData)this._feeder.monitor();
         }
         catch(ClassCastException cce){
            cce.printStackTrace();
            throw new NullPointerException("No TankDataFeeder");
         }
      }
      catch(NullPointerException npe){
         this._measuredTankData = this._tankData;
      }
   }

   //
   //
   //
   private void monitorTank(){
      this.measure();
      double wgt = this.calculateWeight();
      double mlr = this.calculateMassLossRate();
      this.setUpTankData(wgt, mlr);//Needs a different name!!!
   }

   //
   //
   //
   private void setUpTankData
   (
      //double capacity,
      //double emptyRate,
      //double temp,
      //double weight
      double weight,
      double massLossRate
   ){
      //measure the weight and empty rate
      //grab from the current tank data (not initialized tank data)
      //the capacity and weight...
      //basically, getting, calculating the derived quantities...
      TankData td = null;
      double den  = this._tankData.density();
      double dw   = this._tankData.dryWeight();
      String fuel = this._tankData.fuel();
      int    num  = this._tankData.number();
      int    stage= this._tankData.stage();
      double tol  = this._tankData.tolerance();
      //What is measured...
      double cap  = this._measuredTankData.capacity();
      double er   = this._measuredTankData.emptyRate();
      double temp = this._measuredTankData.temperature();
      double wgt  = weight;
      double mlr  = massLossRate;
      
      td = new GenericTankData(cap,  //Measure Capacity
                               den,       //Density
                               dw,        //Dry Weight
                               er, //Empty Rate
                               null,      //error (TBD)
                               fuel,      //fuel type
                               false,     //no errors (yet)
                               num,       //tank number
                               stage,     //Current Stage
                               temp,      //measured temperature
                               tol,       //tolerance
                               wgt     //measured weight
                               );
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

   //Fuel Temp MUST be the same regargless of State!!!
   //
   //
   private String temperatureError(double temp){
      double ll        = Double.NaN;
      double ul        = Double.NaN;
      double tolerance = Double.NaN;
      String error     = null;
      try{
         TankData td = this.myTankData();
         tolerance = td.tolerance();
         if(!Double.isNaN(temp) && !Double.isNaN(tolerance)){
            //Separate out by State
            if(this._state.state() == INIT){
               //Since there is nothing in the tank...ul, ll could be
               //anything within reason--no need to worry about tol
               ul = 373.15; //Boiling pt of water in K
               ll = 273.15; //Freezing pt of water in K
            }
            else if(this._state.state() == PRELAUNCH){}
            if(temp < ll || temp > ul){
               error = new String("Measured Temperature:  "+temp);
               if(temp < ll){
                  error += " too low";
               }
               else if(temp > ul){
                  error += " too high";
               }
            }
         }
      }
      catch(NullPointerException npe){
         error  = new String(npe.getMessage());
         error += ": Temperature Error Unknown";
      }
      finally{
         return error;
      }
   }

   //
   //
   //
   private  String weightError(double weight){
      double ll        = Double.NaN;
      double ul        = Double.NaN;
      double tolerance = Double.NaN;
      double refWeight = Double.NaN; //Referenced
      String error     = null;
      try{
         TankData td = this.myTankData();
         tolerance   = td.tolerance();
         refWeight   = td.weight(); //Get weight from Feeder
         boolean inputGood = !Double.isNaN(weight);
         inputGood &= !Double.isNaN(tolerance);
         inputGood &= !Double.isNaN(refWeight);
         if(inputGood){
            //Compare Calculated Weight to the referenced weight
            ll = refWeight*tolerance;
            ul = refWeight*(2-tolerance);
            if(weight < ll || weight > ul){
               error = new String("Measured Weight: "+weight);
               if(weight < ll){
                  error += "too low expected:  ";
               }
               else if(weight > ul){
                  error += "too high expected:  ";
               }
               //This is the current weight the tank should be
               error += refWeight;
            }
         }
      }
      catch(NullPointerException npe){}
      finally{
         return error;
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
                  if(counter++ == 1000){
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
