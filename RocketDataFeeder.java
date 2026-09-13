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
   //
   //
   private void monitorInitialize(){
      System.out.println(this._initializable.initialized());
      try{
         RocketData rd=(RocketData)this._initializable.initialized();
         System.out.println(rd);
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.out.println("Exiting");
         System.exit(1);
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
      //More testing real quick
      if(this._stateSubstate != null){
         if(this._stateSubstate.state() == INIT){
            this.monitorInitialize();
         }
      }
      //More to be done with this...definitely...
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
         int counter   = 0;
         boolean check = false;
         while(true){
            if(this._stateSubstate != null){
               if(this._stateSubstate.state() == INIT){
                  //In Initialize, check the Rocket System every
                  //Half Second...
                  if(counter++%500 == 0){
                     check   = true;
                     counter = 1;
                  }
               }
            }
            if(check){
               check = false;
               //Test Prints
               System.out.println(Thread.currentThread().getName());
               System.out.println(Thread.currentThread().getId());
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
