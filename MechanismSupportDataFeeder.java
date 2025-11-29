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

public class MechanismSupportDataFeeder implements DataFeeder,
Runnable{
   private LaunchStateSubstate.State INIT              = null;
   private LaunchStateSubstate.State PREl              = null;
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

   //Read In
   private MechanismSupportData           _initSuppData;
   //Calculated
   private MechanismSupportData           _calcSuppData;

   private LaunchStateSubstate            _stateSubstate;
   private Object                         _obj; //Threading
   private Thread                         _t0;

   private boolean                        _toStart;
   //Singleton Implementation
   private static DataFeeder              _instance;

   {
      INIT = LaunchStateSubstate.INITIALIZE;
      PREL = LaunchStateSubstate.PRELAUNCH;
      IGNI = LaunchStateSubstate.IGNITION;
      LAUN = LaunchStateSubstate.LAUNCH;
      ASCE = LaunchStateSubstate.ASCENT;
      SET  = LaunchStateSubstate.PreLaunchSubstate.SET;
      CONT = LaunchStateSubstate.PreLaunchSubstate.CONTINUE;
      FUEL = LaunchStateSubstate.PreLaunchSubstate.FUELING;
      HOLD = LaunchStateSubstate.PreLaunchSubstate.HOLD;
      IGN  = LaunchStateSubstate.IgnitionSubstate.IGNITION;
      BUP  = LaunchStateSubstate.IgnitionSubstate.BUILDUP;
      STG  = LaunchStateSubstate.AscentSubstate.STAGING;
      IGNE = LaunchStateSubstate.AscentSubstate.IGNITEENGINES;

      _initSuppData  = null;
      _calcSuppData  = null;
      _stateSubstate = null;
      _obj           = null;
      _t0            = null;
      //Singleton
      _instance      = null;
   };

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   static public DataFeeder instance(){
      if(_intstance == null){
         _instance = new MechanismSupportDataFeeder();
      }
      return _instance;
   }

   //////////////////////////Private Methods//////////////////////////
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   private MechanismSupportDataFeeder(){
      this.setUpThread();
   }

   //
   //
   //
   private void setUpThread(){
      this._obj = new Object();
      this._t0  = new Thread(this);
      this._to.start();
   }

   //////////////////////DataFeeder Implementation////////////////////
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
      synchronized(this._obj){}
      //Temp for now
      return null;
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){}

   ///////////////////Runnable Interface Implementation///////////////
   //
   //
   //
   public void run(){}

}
//////////////////////////////////////////////////////////////////////
