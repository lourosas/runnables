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

public class PumpDataFeeder implements DataFeeder, Runnable{
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
   private Initializable       _initializable;
   private PumpData            _calcPumpData;
   private LaunchStateSubstate _stateSubstate;
   private Object              _obj;
   private Thread              _t0;

   private int                 _stage; //Stage Number
   private int                 _tank;  //Tank Number

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

      _calcPumpData      = null;
      _initializable     = null;
      _stateSubstate     = null;
      _obj               = null;
      _t0                = null;
      _stage             = -1;
      _tank              = -1;
   };

   ////////////////////////////Contructors////////////////////////////
   //
   //
   //
   public PumpDataFeeder(int stage, int tank){
      this.setStageNumber(stage);
      this.setTankNumber(tank);
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   ///Actually, more of the Pump Rate than the Flow Rate
   ///
   ///
   private double calculateFlowInitialized(){
      double pumpRate = Double.NaN;
      return pumpRate;
   }

   //
   //
   //
   private double calculateTempInitialized(){
      double temp = Double.NaN;
      return temp;
   }

   ///
   ///
   ///
   private double setFlow(){
      double flow = Double.NaN;
      synchronized(this._obj){
         if(this._stateSubstate.state() == INIT){
            flow = this.calculateFlowInitialized();
         }
         return flow;
      }
   }

   //
   //
   //
   private void setMeasureableData(double flow, double temp){
      PumpData pd = null;
      try{
         pd = (PumpData)this._initializable.initialized();
         PumpData tpd = null;
         tpd = new GenericPumpData(pd.error(),
                                   pd.isError(),
                                   flow,
                                   this._stage,
                                   this._tank,
                                   temp,
                                   pd.tolerance());
         synchronized(this._obj){
            this._calcPumpData = tpd;
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
   private void setStageNumber(int stage){
      if(stage > 0){
         this._stage = stage;
      }
   }

   //
   //
   //
   private void setTankNumber(int tank){
      if(tank > 0){
         this._tank = tank;
      }
   }

   //
   //
   //
   private double setTemp(){
      double temp = Double.NaN;
      synchronized(this._obj){
         if(this._stateSubstate.state() == INIT){
            temp = this.calculateTempInitialized();
         }
         return temp;
      }
   }

   //
   //
   //
   private void setUpThread(){
      this._obj    = new Object();
      this._t0     = new Thread(this);
      this._t0.start();
   }

   ////////////////////DataFeeder Interface Methods///////////////////
   //
   //
   //
   public void addInitializable(Initializable init){
      this._initializable = init;
   }

   //
   //
   //
   public Object monitor(){
      double flow = this.setFlow();
      double temp = this.setTemp();
      this.setMeasureableData(flow,temp);
      synchronized(this._obj){
         return this._calcPumpData;
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
      int counter   = 0;
      boolean check = false;
      try{
         while(true){
            if(this._stateSubstate != null){
               if(this._stateSubstate.state() == INIT){
                  //In Initialize State, set the data every second
                  //(regardless of Substate)
                  if(counter++%1000 == 0){
                     check = true;
                  }
               }
               if(check){
                  double flow = this.setFlow();
                  double temp = this.setTemp();
                  this.setMeasureableData(flow, temp);
                  check = false;
               }
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
