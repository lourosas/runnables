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

public class EngineDataFeeder implements DataFeeder, Runnable{
   private LaunchStateSubstate.State             INIT  = null;
   private LaunchStateSubstate.State             PREL  = null;
   private LaunchStateSubstate.State             IGNI  = null;
   private LaunchStateSubstate.State             LAUN  = null;
   private LaunchStateSubstate.State             ASCE  = null;
   private LaunchStateSubstate.PreLaunchSubstate SET   = null;
   private LaunchStateSubstate.PreLaunchSubstate CONT  = null;
   private LaunchStateSubstate.PreLaunchSubstate FUEL  = null;
   private LaunchStateSubstate.PreLaunchSubstate HOLD  = null;
   private LaunchStateSubstate.IgnitionSubstate  IGN   = null;
   private LaunchStateSubstate.IgnitionSubstate  BUP   = null;
   private LaunchStateSubstate.AscentSubstate    STG   = null;
   private LaunchStateSubstate.AscentSubstate    IGNE  = null;

   private Initializable       _initializable;
   private EngineData          _calcEngineData;
   private LaunchStateSubstate _stateSubstate;
   private Object              _obj;
   private Thread              _t0;

   private int                 _stage;
   private int                 _number;

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

      _calcEngineData = null;
      _initializable  = null;
      _stateSubstate  = null;
      _obj            = null;
      _t0             = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //Might not need the eng. no. nor stage--get from the RocketData!
   //
   //
   public EngineDataFeeder(int number, int stage){
      this._obj = new Object();
      this.setEngineNumber(number);
      this.setStageNumber(stage);
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private double calculateExhaustFlowInitialized(){
      double exhaustFlow = Double.NaN;
      try{
         EngineData ed=(EngineData)this._initializable.initialized();
         //Initialization, should be NO exhaust flow!!
         exhaustFlow       = 0.;
         boolean found     = false;
         double  tolerance = ed.tolerance();
         double  lowerLim  = exhaustFlow; //should be 0
         //Liters per Sec needs to be VERY LOW!!!
         //Will probably need to change to be more realistic
         double  upperLim  = 0.001; //A milliliter per sec!!!
         Random  random    = new Random();
         while(!found){
            if(this._stateSubstate.state() == INIT){
               exhaustFlow = lowerLim + (random.nextDouble());
               if(exhaustFlow >= lowerLim && exhaustFlow <= upperLim){
                  found = true;
               }
            }
            else{ //Safeguard!  Should NEVER happen!!
               found = true;
            }
         }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }

      return exhaustFlow;
   }

   //
   //
   //
   private double calculateFuelFlowInitialized(){
      double fuelFlow = Double.NaN;
      try{
         EngineData ed=(EngineData)this._initializable.initialized();
         //Initialization:  should be NO fuel flow!!!
         fuelFlow          = 0.;
         boolean found     = false;
         double  tolerance = ed.tolerance();
         double  lowerLim  = fuelFlow; //0
         //Liters per Sec:  needs to be VERY LOW!
         //Will probably need to change upper limit to something more
         //realistic
         //Better be at most a milliliter/sec!!!!
         double  upperLim  = 0.001;
         Random  random    = new Random();
         while(!found){
            if(this._stateSubstate.state() == INIT){
               fuelFlow = lowerLim + (random.nextDouble());
               if(fuelFlow >= lowerLim && fuelFlow <= upperLim){
                  found = true;
               }
            }
            else{
               found = true;  //Safeguard!!  Should NEVER happen!!
            }
         }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      return fuelFlow;
   }

   //
   //
   //
   private void setEngineNumber(int num){
      if(num > -1){
         this._number = num;
      }
   }

   //
   //
   //
   private double setExhaustFlow(){ 
      double exhaustFlow = Double.NaN;
      if(this._stateSubstate.state() == INIT){
         exhaustFlow = this.calculateExhaustFlowInitialized();
      }
      //will need to add logic to include other states as needed...
      return exhaustFlow;
   }

   //
   //
   //
   private double setFuelFlow(){
      double fuelFlow = Double.NaN;
      if(this._stateSubstate.state() == INIT){
         fuelFlow = this.calculateFuelFlowInitialized();
      }
      return fuelFlow;
   }

   //Exhaust Flow, Fuel Flow, Temperature
   //
   //
   private void setMeasuredData(double ef,double ff, double temp){}

   //
   //
   //
   private void setStageNumber(int stg){
      if(stg > -1){
         this._stage = stg;
      }
   }


   //
   //
   //
   private double setTemp(){
      double temp = Double.NaN;
      
      return temp;
   }

   //
   //
   //
   private void setUpThread(){
      this._t0 = new Thread(this, "Engine Data Feeder");
      this._t0.start();
   }

   /////////////////////DataFeeder Implementation/////////////////////
   //
   //
   //
   public void addInitializable(Initializable initializable){}

   //
   //
   //
   public Object monitor(){
      double exFlow = this.setExhaustFlow();
      double flFlow = this.setFuelFlow();
      double temp   = this.setTemp();
      this.setMeasuredData(exFlow,flFlow,temp);
      synchronized(this._obj){
         return this._calcEngineData;
      }
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      this._stateSubstate = stateSubstate;
   }

   ////////////////Runnable Interface Implementation//////////////////
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
                  //In Initialize, query at 1/10 a second
                  if(counter++%100 == 0){
                     check = true;
                  }
               }
            }
            if(check){
               //This is absolutely, positively redundant!  Just look
               //at the monitor() method!! For the time being, will
               //keep--in truth, do not need to threading
               double exhFlow    = this.setExhaustFlow();
               double fuelFlow   = this.setFuelFlow();
               double temp       = this.setTemp();
               this.setMeasuredData(exhFlow, fuelFlow, temp); 
               check = false;
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}

//////////////////////////////////////////////////////////////////////
