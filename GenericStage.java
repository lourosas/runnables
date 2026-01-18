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

   private List<Engine>        _engines;
   private List<ErrorListener> _errorListeners;
   private DataFeeder          _feeder;
   private FuelSystem          _fuelSystem;
   private boolean             _kill;
   private Object              _obj;
   private Thread              _rt0;
   private boolean             _start;
   private StageData           _stageData;
   private StageData           _measStageData;
   private LaunchStateSubstate _state;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _engines          = null;
      _errorListeners   = null;
      _feeder           = null;
      _fuelSystem       = null;
      _kill             = false;
      _measStageData    = null;
      _obj              = null;
      _rt0              = null;
      _start            = false;
      _stageData        = null;
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
   private void initializeStageData
   (
      List<Hashtable<String,String>> data
   ){
      //will need to figure out which Stage it is...pretty simple
      this._stageData = null;  //Erase all previous data
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String,String> ht = data.get(i);
         try{
            String num = ht.get("number");
            if(Integer.parseInt(num) == this._stageNumber){
               //Total Engines
               int te       = Integer.parseInt(ht.get("engines"));
               //Dry Weight
               double dw    = Double.parseDouble(ht.get("dryweight"));
               //Max Weight
               double mw    = Double.parseDouble(ht.get("maxweight"));
               //Model Number
               String model = ht.get("model");
               long mn      = Integer.parseUnsignedInt(model,16);
               //Tolerance
               double tol   = Double.parseDouble(ht.get("tolerance"));
               //Stage Number
               int sn       = this._stageNumber;
               this._stageData = new GenericStageData(
                                       dw,    //Dry Weight
                                       null,  //Error (Init to NUll)
                                       mn,    //Model Number
                                       false, //isError Boolean
                                       sn,    //Stage Number
                                       te,    //Total Engines
                                       mw,    //Max Weight
                                       tol,   //Tolerance
                                       Double.NaN, //Calculated Weight
                                       null,  //Engine Data
                                       null   //Fuel System Data
                                       );
            }
         }
         catch(NumberFormatException npe){
            this._stageData = null;
         }
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
      FuelSystemData   fs  = this.monitorFuelSystem();
      double weight        = this.calculateWeight(eng, fs);
      boolean isError      = this.isError(weight);
      if(isError){
         error = this.error(weight);   
      }
      this.setUpStageData(eng,fs,weight,isError,error);
   }

   //
   //
   //
   private void setUpStageData
   (
      List<EngineData>   engines,
      FuelSystemData  fuelSystem,
      Double          calcWeight,
      boolean            isError,
      String               error
   ){
      synchronized(this._obj){
         double dw    = this._stageData.dryWeight();
         //Somehow, will need to set the error in addition
         double mw    = this._stageData.maxWeight();
         long   model = this._stageData.model();
         int    en    = this._stageData.numberOfEngines();
         int    sn    = this._stageData.stageNumber();
         double to    = this._stageData.tolerance();
         StageData sd = new GenericStageData(dw,    //Dry Weight
                                             error, //error
                                             model, //Model
                                             isError, //Is Error
                                             sn,    //Stage Number
                                             en,    //No. Engines
                                             mw,    //Max Weight
                                             to,    //Tolerance
                                             calcWeight, //Calc Weight
                                             engines,//Engines List
                                             fuelSystem//Fuel System
                                             );
         this._stageData = sd;
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
      if(this._stageNumber > -1){
         this.stageData(file);
         this.engineData(file);
         this.fuelSystem(file);
         this._state = new LaunchStateSubstate(INIT,null,null,null);
         this._start = true;
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
