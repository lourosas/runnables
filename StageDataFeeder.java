//////////////////////////////////////////////////////////////////////
/*
Copyright 2025 Lou Rosas

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
import java.text.*;
import java.time.*;
import java.time.format.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class StageDataFeeder implements DataFeeder, Runnable{
   private LaunchStateSubstate.State INIT              = null;
   private LaunchStateSubstate.State PREL              = null;
   private LaunchStateSubstate.State IGNI              = null;
   private LaunchStateSubstate.State LAUN              = null;
   private LaunchStateSubstate.State ASCE              = null;
   private LaunchStateSubstate.PreLaunchSubstate SET   = null;
   private LaunchStateSubstate.PreLaunchSubstate CONT  = null;
   private LaunchStateSubstate.PreLaunchSubstate FUEL  = null;
   private LaunchStateSubstate.PreLaunchSubstate HOLD  = null;
   private LaunchStateSubstate.IgnitionSubstate  IGN   = null;
   private LaunchStateSubstate.IgnitionSubstate  BUP   = null;
   private LaunchStateSubstate.AscentSubstate    STG   = null;
   private LaunchStateSubstate.AscentSubstate    IGNE  = null;

   //Derived
   private StageData              _calcStageData;
   private Initializable          _initializable;
   private LaunchStateSubstate    _stateSubstate;
   private Object                 _obj;
   private Thread                 _t0;

   private FuelSystemDataFeeder   _fuelSystemDataFeeder;
   private List<EngineDataFeeder> _engines;

   private String                 _initializableInitFile;
   private int                    _stage;

   {
      INIT = LaunchStateSubstate.State.INITIALIZE;
      PREL = LaunchStateSubstate.State.PRELAUNCH;
      IGNI = LaunchStateSubstate.State.IGNITION;
      LAUN = LaunchStateSubstate.State.LAUNCH;
      ASCE = LaunchStateSubstate.State.ASCENT;
      SET  = LaunchStateSubstate.PreLaunchSubstate.SET;
      CONT = LaunchStateSubstate.PreLaunchSubstate.CONTINUE;
      FUEL = LaunchStateSubstate.PreLaunchSubstate.FUELING;
      HOLD = LaunchStateSubstate.PreLaunchSubstate.HOLD;
      IGN  = LaunchStateSubstate.IgnitionSubstate.IGNITION;
      BUP  = LaunchStateSubstate.IgnitionSubstate.BUILDUP;
      STG  = LaunchStateSubstate.AscentSubstate.STAGING;
      IGNE = LaunchStateSubstate.AscentSubstate.IGNITEENGINES;

      _calcStageData        = null;
      _initStageData        = null;
      _stateSubstate        = null;
      _obj                  = null;
      _t0                   = null;

      _fuelSystemDataFeeder = null;
      _engines              = null;
      
      _numEngines           = -1;
      _initializableInitFile= null;
      _stage                = -1;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public StageDataFeeder(int currentStage){
      this.setStageNumber(currentStage);
      this.setUpThread();
   }

   //
   //
   //
   public StageDataFeeder(int currentStage, String initializeFile){
      this.setStageNumber(currentStage);
      this.setInitializableInitFile(initializedFile);
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void initializeEngineDataFeeder(){
      try{
         int stg = this._stage;
         StageData sd = (StageData)this._initializable.initialized();
         for(int i = 0; i < sd.numberOfEngines(); ++i){
            EngineDataFeeder edf = new EngineDataFeeder(i+1,stg);
            try{
               this._engines.add(edf);
            }
            catch(NullPointerException npe){
               this._engines = new LinkedList<EngineDataFeeder>();
               this._engines.add(edf);
            }
         }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
   }

   //
   //
   //
   private void initializeFuelSystemDataFeeder(){}

   //
   //
   //
   private List<EngineData> measureEngines(){
      List<EngineData> lst = null;
      try{
         List<EngineData> lst = new LinkedList<EngineData>();
         synchronized(this._obj){
            Iterator<EngineDataFeeder> it = this._engines.iterator();
            while(it.hasNext()){
               lst.add((EngineData)it.next().monitor());
            }
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();  //To Be removed
         lst = null;
      }
      return lst;
   }

   //
   //
   //
   private FuelSystemData measureFuelSystem(){
      FuelSystemData fsd = null;
      try{
         synchronized(this._obj){
            fsd = this._fuelSystemDataFeeder.monitor();
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace(); //To Be Removed
         fsd = null;
      }
      return fsd;
   }

   //
   //
   //
   private void setInitializableInitFile(String init){
      this._initializedInitFile = init;
   }

   //
   //
   //
   private void setMeasuredData
   (
      FuelSystemData   fsd,
      List<EngineData> engines
   ){
      String  err = null;  //Error
      boolean isE = false;
      double  wgt = Double.NaN;
      //Get the needed initialized data
      //NEEDS UPDATING!!!  All will come from the Initializable
      double  dw = this._initStageData.dryWeight();
      long   mdl = this._initStageData.model();
      int    stg = this._initStageData.stageNumber();
      int    egs = this._numEngines;
      double  mw = this._initStageData.maxWeight();
      double tol = this._initStageData.tolerance();

      synchronized(this._obj){
         StageData d = new GenericStageData(
                                  dw,      //Dry Weight
                                  err,     //Error
                                  mdl,     //Model
                                  isE,     //Is Error
                                  stg,     //Stage
                                  egs,     //Engines
                                  mw,      //Max Weight
                                  tol,     //Tolerance
                                  wgt,     //Calculated Weight
                                  engines, //Engines
                                  fsd);    //Fuel System Data
         this._calcStageData = d;
      }
   }
   
   //
   //
   //
   private void setStageNumber(int stage){
      if(stage > 0){
         this._stage = stage;
      }
   }
   //
   //
   //
   private void setUpThread(){
      this._obj     = new Object();
      this._t0      = new Thread(this);
      this._t0.start();
   }

   /////////////////////DataFeeder Implementation/////////////////////
   //
   //
   //
   public void addInitializable(Initializable initializable){
      this._initializable = initializable;
      //Set up this NOW
      this.initializeEngineDataFeeder();
      this.initializeFuelSystemDataFeeder();
   }

   //
   //
   //
   public Object monitor(){
      List<EngineData>  l = this.measureEngines();
      FuelSystemData    f = this.measureFuelSystemData();
      this.setMeasuredData(f, l);
      synchronized(this._obj){
         return this._calcStageData;
      }
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      this._fuelSystemDataFeeder.setStateSubstate(stateSubstate);
      Iterator<EngineDataFeeder> it = this._engines.iterator();
      while(it.hasNext()){
         it.next().setStateSubstate(stateSubstate);
      }
      this._stateSubstate = stateSubstate;

   }

   /////////////////Runnable Interface Implementation/////////////////
   //
   //
   //
   public void run(){
      try{
         int counter   = 0;
         boolean check = false;
         while(true){
            if(this._stateSubstate != null){
               if(this._stateSubstate.state() == INIT){
                  //Querry at every 1/10 of a second
                  if(counter++%100 == 0){
                     check = true;
                  }
               }
            }
            if(check){
               FuelSystemData   f = this.measureFuelSystem();
               List<EngineData> l = this.measureEngines();
               this.setMeasuredData(f, l);
               check = false;
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
