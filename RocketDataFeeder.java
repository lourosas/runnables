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

public class RocketDataFeeder implements DataFeeder, Runnable{
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

   private Initializable           _initializable;
   private RocketData              _calcRocketData;
   private LaunchStateSubstate     _stateSubstate;
   private Object                  _obj; //Threading
   private Thread                  _t0;

   private boolean                 _toStart;
   //Sigleton Implmentation
   private static DataFeeder       _instance;

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

      _calcRocketData  = null;
      _initializable   = null;
      _stateSubstate   = null;
      _obj             = null;
      _t0              = null;
      _toStart         = false;
      //Singleton
      _instance        = null;
   };

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   static public DataFeeder instance(){
      if(_instance == null){
         _instance = new RocketDataFeeder();
      }
      return _instance;
   }

   //////////////////////////Private Methods//////////////////////////
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   private RocketDataFeeder(){
      this._obj = new Object();
      this.setUpThread();
   }

   //
   //The Weight is the only thing that is actually measured in the
   //
   private double calculateInitializedWeight(){
      double  calcWeight   = Double.NaN;
      try{
         RocketData rd=(RocketData)this._initializable.initialized();
         boolean found        = false;
         double  emptyWeight  = rd.emptyWeight();
         double  loadedWeight = rd.loadedWeight();
         double  tolerance    = rd.tolerance();
         double  lowerLim     = Double.NaN;
         double  upperLim     = Double.NaN;
         Random random        = new Random();
         while(!found){
            if(this._stateSubstate.state() == INIT){
               calcWeight =emptyWeight + (random.nextDouble() * 1000);
               tolerance -= 0.01;
               lowerLim   = emptyWeight * tolerance;
               upperLim   = emptyWeight * (2 - tolerance);
            }
            if(calcWeight >= lowerLim && calcWeight <= upperLim){
               found = true;
            }
         }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.out.println("Exiting");
         System.exit(1);
      }
      return calcWeight;
   }

   //Put together the Rocket Data
   //
   //
   private void rocketData(){
      double     cw = Double.NaN; //Calculated Weight
      RocketData rd = null;
      synchronized(this._obj){
         if(this._stateSubstate.state() == INIT){
            cw = this.calculateInitializedWeight();
            //Put together the Rocket Data from Initialized...
            try{
               rd = (RocketData)this._initializable.initialized();
            }
            catch(ClassCastException cce){
               cce.printStackTrace();
               System.out.println("Exiting");
               System.exit(1);
            }
         }
         RocketData trd = null;
         trd = new GenericRocketData(rd.model(),
                                     rd.currentStage(),
                                     rd.numberOfStages(),
                                     rd.emptyWeight(),
                                     rd.loadedWeight(),
                                     cw, //Calculated Weight
                                     rd.isError(),
                                     rd.error(),
                                     rd.payloadData(),
                                     rd.stages(),
                                     rd.tolerance());
         this._calcRocketData = trd;
      }
   }

   //
   //
   //
   private void setUpThread(){
      this._t0    = new Thread(this, "Rocket Data Feeder");
      this._t0.start();
   }

   /////////////////////DataFeeder Implementation/////////////////////
   //
   //
   //
   public void addInitializable(Initializable initializable){
      synchronized(this._obj){
         this._initializable = initializable;
      }
   }

   //
   //
   //
   public Object monitor(){
      this.rocketData();
      //More to be done with this...possibly...
      synchronized(this._obj){
         return this._calcRocketData;
      }
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      this._stateSubstate = stateSubstate;
   }

   /////////////////Runnable Interface Implementattion////////////////
   //May not need...
   //
   //
   public void run(){
      try{
         int counter    = 0;
         boolean check  = false;
         int checkValue = -1;
         while(true){
            if(this._stateSubstate != null){
               if(this._stateSubstate.state() == INIT){
                  //In Initialize, check the Rocket System every
                  //Half Second...
                  checkValue = 500;
               }
               //more to come...
               if(counter++%checkValue == 0){
                  check   = true;
                  counter = 1;
               }
            }
            if(check){
               check = false;
               System.out.println(Thread.currentThread().getName());
               System.out.println(Thread.currentThread().getId());
               //This is absolutelty, positively redundant from the
               //monitor() method--but will keep for the time being
               //really do not need the threading...
               this.rocketData();
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
