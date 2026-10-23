//////////////////////////////////////////////////////////////////////
/*
Copyright 2026 Lou Rosas

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

public class FuelSystemDataFeeder implements DataFeeder, Runnable{
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
   private FuelSystemData       _calcFuelSystemData;
   private Initializable        _initializable;
   private LaunchStateSubstate  _stateSubstate;
   private Object               _obj;
   private Thread               _t0;
   
   private int                  _stage;
   private int                  _engines;

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
   
      _calcFuelSystemData        = null;
      _initializable             = null;
      _stateSubstate             = null;
      _obj                       = null;
      _t0                        = null;
      _stage                     = -1;
      _engines                   = -1;
   };
   
   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public FuelSystemDataFeeder(int stage, int engines){
      this.setEngineNumber(engines);
      this.setStageNumber(stage);
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void fuelSystemData(){
      //Stub out for the moment
   }

   //
   //
   //
   private void setEngineNumber(int engines){
      if(engines > 0){
         this._engines = engines;
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
      this._obj   = new Object();
      this._t0    = new Thread(this);
      this._t0.start();
   }

   ////////////////////DataFeeder Implementation Methods//////////////
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
      this.fuelSystemData();
      synchronized(this._obj){
         return this._calcFuelSystemData;
      }
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
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
                  //Monitor every half-second
                  if(counter++%500 == 0){
                     check   = true;
                     counter = 1;
                  }
               }
            }
            if(check){
               this.fuelSystemData();
               check = false;
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}
