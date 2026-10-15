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

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private double calculateInitializedWeight(){
      double calcWeight = Double.NaN;
      try{
         StageData sd = (StageData)this._initializable.initialized();
         boolean found        = false;
         double  dryWeight    = sd.dryWeight();
         double  maxWeight    = sd.maxWeight();
         double  tolerance    = sd.tolerance();
         double  upperLim     = Double.NaN;
         double  lowerLim     = Double.NaN;
         Random  random       = new Random();
         while(!found){
            if(this._stateSubstate.state() == INIT){
               calcWeight = dryWeight + (random.nextDouble()*1000);
               tolerance -= 0.01;
               lowerLim   = dryWeight * tolerance;
               upperLim   = dryWeight * (2 - tolerance);
               if(calcWeight >= lowerLim && calcWeight <= upperLim){
                  found = true;
               }
            }
            else{
               found = true;
            }
         }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      return calcWeight;
   }

   //
   //
   //
   private void stageData(){
      String   err = null;  //Error
      boolean  isE = false;
      double   wgt = Double.NaN;
      StageData sd = null;
      //Put together the Stage Data from Initialized...
      try{
         sd = (StageData)this._initializable.initialized(); 
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      /*
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
                                  dw,    //Dry Weight
                                  err,   //Error
                                  mdl,   //Model
                                  isE,   //Is Error
                                  stg,   //Stage
                                  egs,   //Engines
                                  mw,    //Max Weight
                                  tol,   //Tolerance
                                  wgt,   //Calculated Weight
                                  null,  //Engines
                                  null); //Fuel System Data
         this._calcStageData = d;
      }
      */
      synchronized(this._obj){
         if(this._stateSubstate.state() == INIT){
            //Rather Simple, nothing really to measure handled
            //literally elsewhere
            wgt = this.calculateInitializedWeight();
         }
         StageData tsd = null;
         tsd = 
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
   }

   //
   //
   //
   public Object monitor(){
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
               this.stageData();
               check = false;
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
