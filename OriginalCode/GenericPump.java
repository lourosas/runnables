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
import java.io.IOException;
import rosas.lou.runnables.*;

public class GenericPump implements Pump, Runnable{
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State INIT      = null; 
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null;

   private boolean _kill;
   private int     _stage;
   private boolean _start;
   private int     _tank;

   private DataFeeder            _feeder;
   private List<ErrorListener>   _errorListeners;
   private List<SystemListener>  _systemListeners;
   private LaunchStateSubstate   _state;
   private Object                _obj;
   private PumpData              _pumpData;
   private PumpData              _measuredPumpData;
   private Thread                _rt0;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;
      
      _kill                = false;
      _stage               = -1;
      _start               = false;
      _tank                = -1;

      _feeder              = null;
      _errorListeners      = null;
      _systemListeners     = null;
      _state               = null;
      _obj                 = null;
      _pumpData            = null;
      _measuredPumpData    = null;
      _rt0                 = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericPump(int stage, int tank){
      if(stage > 0){
         this._stage = stage;
      }
      if(tank > 0){
         this._tank = tank;
      }
      this._obj = new Object();
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void alertErrorListeners(){
      String error = null;
      PumpData pd  = null;
      synchronized(this._obj){
         pd     = this._measuredPumpData;
         error  = pd.error();
      }
      try{
         Iterator<ErrorListener> it = this._errorListeners.iterator();
         while(it.hasNext()){
            it.next().errorOccurred(new ErrorEvent(this, pd, error));
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void alertSubscribers(){
      PumpData            pd = null;
      LaunchStateSubstate ss = this._state;

      String event = ss.state() + ", " + ss.ascentSubstate();
      event += ", " + ss.ignitionSubstate() + ", ";
      event += ss.prelaunchSubstate();
      synchronized(this._obj){
         pd = this._measuredPumpData;
      }
      try{
         Iterator<SystemListener> it = null;
         it = this._systemListeners.iterator();
         while(it.hasNext()){
            MissionSystemEvent mse = null;
            mse = new MissionSystemEvent(this,pd,event,ss);
            it.next().update(mse);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void checkErrors(){
      boolean  isError = false;
      PumpData pd      = null;
      synchronized(this._obj){
         pd = this._measuredPumpData;
      }
      String err = new String();
      double flw = pd.flow(); double temp = pd.temperature();
      double tol = pd.tolerance();  String type = pd.type();
      
      if(this.checkFlow()){
         err    += "\nFlow Rate Error";
         isError = true;
      }
      if(this.checkTemperature()){
         err    += "\nTemperature Error";
         isError = true;
      }
      if(isError){
         pd = new GenericPumpData(
                                   err,         //Error
                                   flw,         //Flow
                                   this._tank,  //Tank
                                   isError,     //Error
                                   this._stage, //Stage
                                   temp,        //Temperature
                                   tol,         //Tolerance
                                   type);       //Type
         synchronized(this._obj){
            this._measuredPumpData = pd;
         }
         this.alertErrorListeners();
      }
   }

   //
   //
   //
   private boolean checkFlow(){
      boolean isError   = false;
      double  flow      = Double.NaN;
      double  tolerance = Double.NaN;
      synchronized(this._obj){
         flow =      this._measuredPumpData.flow();
         tolerance = this._measuredPumpData.tolerance();
      }
      double min = Double.NaN; double max = Double.NaN;
      //In the Initialization State, nothing should be flowing w/in
      //Tolerance
      if(this._state.state() == INIT){
         max = 1. - tolerance;
         isError |= (flow > max);
      }
      return  isError;
   }

   //
   //
   //
   private boolean checkTemperature(){
      boolean isError     = false;
      double  temperature = Double.NaN;
      double  tolerance   = Double.NaN;
      synchronized(this._obj){
         temperature = this._measuredPumpData.temperature();
         tolerance   = this._measuredPumpData.tolerance();
      }
      double min = Double.NaN; double max = Double.NaN;
      //In the Initialization State, anything between the Freezing and
      //Boiling point of water is fine
      if(this._state.state() == INIT){
         min = 273.15; max = 373.15;
         isError |= (temperature < min || temperature > max);
      }
      return isError;
   }

   //
   //
   //
   private void initializePumpDataJSON(String file)throws IOException{
      double flo  = Double.NaN; int idx = -1; int stg = -1;
      double temp = Double.NaN; double tol = Double.NaN;
      String type = null; String err = null; boolean isE = false;
      //Test Print
      System.out.println("Generic Pump: "+file);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst = read.readPumpDataInfo();
         Iterator<Hashtable<String,String>> it = lst.iterator();
         while(it.hasNext()){
            Hashtable<String,String> ht = it.next();
            try{ idx = Integer.parseInt( ht.get("tanknumber")); }
            catch(NumberFormatException nfe){ idx = -1; }
            try{ stg = Integer.parseInt( ht.get("stage")); }
            catch(NumberFormatException nfe){ stg = -1; }
            if(this._stage == stg && this._tank == idx){
               try{ flo = Double.parseDouble(ht.get("rate")); }
               catch(NumberFormatException nfe){ flo = Double.NaN; }
               try{ temp = Double.parseDouble(ht.get("temperature")); }
               catch(NumberFormatException nfe){ temp = Double.NaN; }
               try{ tol = Double.parseDouble(ht.get("tolerance")); }
               catch(NumberFormatException nfe){ tol = Double.NaN; }
               this._pumpData = new GenericPumpData(
                                              err,  //Error
                                              flo,  //Rate
                                              idx,  //Number
                                              isE,  //Is Error
                                              stg,  //Stage
                                              temp, //Temp
                                              tol,  //Tol
                                              type);//Fuel Type
            }
         }
      }
      catch(IOException ioe){
         this._pumpData = null;
         throw ioe;
      }
   }

   //
   //
   //
   private boolean isPathFile(String file) throws IOException{
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
         ioe.printStackTrace();
         throw ioe;
      }
      catch(NullPointerException npe){
         isPath = false;
      }
      catch(Exception e){
         e.printStackTrace();
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
            StageData  sd = rd.stage(this._stage);
            FuelSystemData fsd = sd.fuelSystemData();
            List<PumpData> lst = fsd.pumpData(); 
            Iterator<PumpData> it = lst.iterator();
            while(it.hasNext()){
               PumpData pd = it.next();
               int sn  = pd.stage();
               int idx = pd.index();
               if(this._stage == sn && this._tank == idx){
                  synchronized(this._obj){
                     this._measuredPumpData = pd;
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
               PumpData pd = (PumpData)this._feeder.monitor();
               this._measuredPumpData = pd;
            }
         }
         catch(ClassCastException e){
            e.printStackTrace();
            throw new NullPointerException("No PumpDataFeeder");
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         synchronized(this._obj){
            this._measuredPumpData = this._pumpData;
         }
      }
   }

   //
   //
   //
   private void monitorPump(){
      this.measure();
   }

   //
   //
   //
   private void pumpData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         this.initializePumpDataJSON(file);
      }
   }

   //
   //
   //
   private void setUpThread(){
      String name = new String("Pump: "+this._stage+", "+this._tank);
      this._rt0 = new Thread(this,name);
      this._rt0.start();
   }

   ///////////////////Pump Interface Implementation///////////////////
   //
   //
   //
   public PumpData monitor(){
      synchronized(this._obj){
         return this._measuredPumpData;
      }
   }

   //
   //
   //
   public void initialize(String file)throws IOException{
      if((this._stage > 0) && (this._tank > 0)){
         String pdFile = file;
         if(this.isPathFile(pdFile)){
            LaunchSimulatorJsonFileReader read = null;
            read = new LaunchSimulatorJsonFileReader(pdFile);
            pdFile = read.readPathInfo().get("tank");
         }
         this.pumpData(pdFile);
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
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      this._state = stateSubstate;
   }

   /////////////////////////Runnable Interface////////////////////////
   //
   //
   //
   public void run(){
      int counter   = 0;
      boolean check = false;
      try{
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this._state != null){
               if(this._state.state() == INIT){
                  if(counter++%500 == 0){
                     check = true;
                     counter = 1; //reset the counter
                  }
               }
            }
            if(check){
               this.monitorPump();
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
