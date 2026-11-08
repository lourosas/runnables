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

public class PipeDataFeeder implements DataFeeder, Runnable{
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
   private PipeData            _calcPipeData;
   private LaunchStateSubstate _stateSubstate;
   private Object              _obj;
   private Thread              _t0;

   private int                 _number; //Engine Number
   private int                 _stage;
   private int                 _tank;  //Tank number...

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
   
      _calcPipeData       = null;
      _initializable      = null;
      _stateSubstate      = null;
      _obj                = null;
      _t0                 = null;
      _number             = -1; //Engine
      _stage              = -1;
      _tank               = -1;
   };
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //number = Engine Number
   public PipeDataFeeder(int stage, int tank, int number){
      this.setStageNumber(stage);
      this.setTankNumber(tank);
      this.setNumber(number);
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private double calculateFlowInitialized(){
      double flowRate = Double.NaN;
      try{
         PipeData pd = (PipeData)this._initializable.initialized();
         //Initialization:  NO FLOW RATE
         flowRate          = 0.;
         boolean found     = false;
         double  lowerLim  = flowRate;
         //Liters/Sec:  needs to be VERY LOW!!  Will probably need
         //to change the upper limit to something more realistic
         //Better be at most ml/sec!
         double upperLim   = 0.001;
         Random random     = new Random();
         while(!found){
            if(this._stateSubstate.state() == INIT){
               flowRate = lowerLim + (random.nextDouble());
               if(flowRate >= lowerLim && flowRate <= upperLim){
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
      return flowRate;
   }

   //
   //
   //
   private double calculateTempInitialized(){
      double temp = Double.NaN;
      try{
         PipeData pd = (PipeData)this._initializable.initialized();
         //Temperature is insignificant in the Initialize State...
         //Can literally return anything within reason...since
         //measured in Kelvin, keep between 273-373...
         boolean found     = false;
         double  lowerLim  = 273.15;
         double  upperLim  = 373.15;
         Random  random    = new Random();
         int     seed      = 374; //Above the Boiling point of H2O
         while(!found){
            if(this._stateSubstate.state() == INIT){
               temp = random.nextInt(seed) + random.nextDouble();
               if(temp >= lowerLim && temp <= upperLim){
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
      return temp;
   }

   //
   //
   //
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
   private void setMeasuredData(double flow, double temp){
      PipeData pd = null;
      try{
         pd = (PipeData)this._initializable.initialized();
         PipeData tpd = null;
         tpd = new GenericPipeData(pd.error(),
                                   pd.isError(),
                                   this._number,
                                   flow,
                                   this._stage,
                                   this._tank,
                                   temp,
                                   pd.tolerance(),
                                   pd.type());
         synchronized(this._obj){
            this._calcPipeData = tpd;
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
   private double setTemp(){
      double temp = Double.NaN;
      synchronized(this._obj){
         if(this._stateSubstate.state() == INIT){
            temp = this.calculateTempInitialized();
         }
         return temp;
      }
   }

   //Associated Engine Number
   //
   //
   private void setNumber(int number){
      if(number > 0){
         this._number = number;
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
   private void setUpThread(){
      this._obj  = new Object();
      this._t0   = new Thread(this);
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
      this.setMeasuredData(flow,temp);
      synchronized(this._obj){
         return this._calcPipeData;
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
                  //In Initialize State, set data every second
                  //(regardless of Substate)
                  if(counter++%1000 == 0){
                     //Counter should just roll over...
                     check = true;
                  }
               }
               if(check){
                  //Test Prints--will be removed soon!
                  double flow = this.setFlow();
                  double temp = this.setTemp();
                  this.setMeasuredData(flow, temp);
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
