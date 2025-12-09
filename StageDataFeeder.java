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

   //Read In....
   private StageData              _initStageData;
   //Derived
   private StageData              _calcStageData;

   private LaunchStateSubstate    _stateSubstate;
   private Object                 _obj;
   private Thread                 _t0;

   private FuelSystemData         _fuelSystemData;

   private int                    _engines;
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

      _initStageData        = null;
      _calcStageData        = null;
      _stateSubstate        = null;
      _obj                  = null;
      _t0                   = null;
      _fuelSystemData       = null;
      _engines              = -1;
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

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   static public DataFeeder instance(){}

   //////////////////////////Private Methods//////////////////////////
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
   public void addDataFeeder(DataFeeder feeder){}

   //
   //
   //
   public void initialize(String file)throws IOException{}

   //
   //
   //
   public Object monitor(){
      //null for now
      return null;
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){}

   /////////////////Runnable Interface Implementation/////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
