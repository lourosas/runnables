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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.*;
import java.time.*;
import java.time.format.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class TankDataFeeder implements DataFeeder, Runnable{
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

   //Calculated
   private Initializable          _initializable;
   private TankData               _calcTankData;
   private LaunchStateSubstate    _stateSubstate;
   private Object                 _obj; //Threading
   private Thread                 _t0;

   private int                    _stage;
   private int                    _number;

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

      _calcTankData     = null;
      _initializable    = null;
      _stateSubstate    = null;
      _obj              = null;
      _t0               = null;
      _stage            = -1;
      _number           = -1;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TankDataFeeder(int stage, int number){
      this.setUpNumber(number);
      this.setUpStage(stage);
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private double calculateEmptyRateInitialized(){
      double emptyRate = Double.NaN;
      try{
         TankData td = (TankData)this._initializable.initalized();
         //Initialization:  should be NO empty rate!!!
         emptyRate         = 0.;
         boolean found     = false;
         double  tolerance = td.tolerance();
         double  lowerLin  = emptyRate;
         //Liters per Sec:  needs to be VERY LOW!  Will Probably
         //need to change the upper limit to something more
         //realistic
         //Better be at most milliliter/sec!!!
         double upperLimit = 0.001;

      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      return emptyRate;
   }

   //
   //
   //
   private double calculateMassLossRate(double rate, double den){
      return rate*den;  //L/s*kg/L= kg/s
   }
   //
   //
   //
   private double calculateWeight(double cap, double den, double dw){
      double g = 9.81;
      return cap*den*g + dw;
   }

   //
   //
   //
   private double setCapacity(){
      synchronized(this._obj){
         double capacity = Double.NaN;

         return capacity;
      }
   }

   //
   //
   //
   private double setEmptyRate(){
      synchronized(this._obj){
         double emptyRate = Double.NaN;
         if(this._stateSubstate.state() == INIT){
            emptyRate = this.calculateEmptyRateInitialized();
         }
         return emptyRate;
      }
   }

   //
   //
   //
   private void setMeasuredData(double cap,double rate,double temp){
      synchronized(this._obj){}
   }

   //
   //
   //
   private double setTemp(){
      synchronized(this._obj){
         double temp = Double.NaN;

         return temp;
      }
   }

   //
   //
   private void setUpNumber(int tankNumber){
      this._number = tankNumber > 0 ? tankNumber : -1;
   }

   //
   //
   //
   private void setUpStage(int stageNumber){
      this._stage = stageNumber > 0 ? stageNumber : -1;
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
      double cap = this.setCapacity();
      double rte = this.setEmptyRate();
      double tmp = this.setTemp();
      this.setMeasuredData(cap, rte, tmp);
      synchronized(this._obj){
         return this._calcTankData;
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
      boolean check   = false;
      int     counter = 0;
      int     compare = -1;
      try{
         while(true){
            if(this._stateSubstate != null){
               if(this._stateSubstate.state() == INIT){
                  //In Initialize State, set the data every second
                  //regardless of substate
                  compare = 1000;
               }
               if((compare > 0) && (counter++%compare == 0)){
                  check   = true;
                  counter = 1;
               }
            }
            if(check){
               double capacity = this.setCapacity();
               double rate     = this.setEmptyRate();
               //Mass Loss Rate
               double temp     = this.setTemp();
               this.setMeasuredData(capacity, rate, temp);
               check = false;
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }

}
//////////////////////////////////////////////////////////////////////
