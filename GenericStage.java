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
   private void alertErrorListeners(){}

   //
   //
   //
   private void alertSubscribers(){}

   //
   //
   //
   private void checkErrors(){}

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
      synchronized(this._obj){
         //For the Time Being return null
         return null;
      }
   }

   //
   //
   //
   private FuelSystemData monitorFuelSystem(){
      FuelSystemData fsd = null;
      synchronized(this._obj){
         fsd = this._fuelSystem.monitor();
      }
      return fsd;
   }

   //
   //
   //
   private void monitorStage(){
      String         error = null;
      List<EngineData> eng = this.monitorEngines();
      FuelSystemData    fs = this.monitorFuelSystem();
      /*
      double weight        = this.calculateWeight(eng, fs);
      boolean isError      = this.isError(weight);
      if(isError){
         error = this.error(weight);   
      }
      this.setUpStageData(eng,fs,weight,isError,error);
      */
   }

   //
   //
   //
   private void setUpEngines(String file)throws IOException{
      try{
         int numEngines = this._stageData.numberOfEngines();
         int stage      = this._stageData.stageNumber();
         for(int i = 0; i < numEngines; ++i){
            Engine e = new GenericEngine(i,stage);
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
   private void setUpFuelSystem(String file)throws IOException{
      try{
         int stage   = this._stageData.stageNumber();
         int engines = this._stageData.numberOfEngines();
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
   private void setUpThread(){
      String name = new String("Generic Stage "+ this._stageNumber);
      this._rt0   = new Thread(this, name);
      this._rt0.start();
   }

   //
   //
   //
   private void stageData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.initializeStageData(read.readStageInfo());
      }
   }

   ///////////////ErrorListener Interface Implementation//////////////
   //
   //
   //
   public void errorOccurred(ErrorEvent e){}

   ///////////////////Stage Interface Implementation//////////////////
   //
   //
   //
   public StageData monitor(){
      //This needs to be fucking fixed!!!!
      synchronized(this._obj){
         //return null for the time being...TBD...
         //Component Data will be fucking fed in...
         System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
         System.out.print("GenericStage"+this._stageNumber);
         System.out.println(".monitor()");
         System.out.println(this._feeder);
         System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
         return this._stageData;
      }
   }


   //
   //
   //
   public void initialize(String file)throws IOException{
      if(this._stageNumber > 0){
         String gsFile = file;
         String enFile = file;
         String fsFile = file;
         if(this.isPathFile(gsFile)){
            LaunchSimulatorJsonFileReader read = null;
            read = new LaunchSimulatorJsonFileReader(gsFile);
            gsFile = read.readPathInfo().get("stage");
            enFile = read.readPathInfo().get("engine");
         }
         this.stageData(gsFile);
         this.setUpEngines(enFile);
         this.setUpFuelSystem(file);
         //Somehow, need to finalize
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
   public void addSystemListener(SystemListener listener){}

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate state){} 
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
