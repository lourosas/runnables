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

   //Read In...
   private FuelSystemData       _initFuelSystemData;
   //Derived
   private FuelSystemData       _calcFuelSystemData;

   private LaunchStateSubstate  _stateSubstate;
   private Object               _obj;
   private Thread               _t0;

   private List<PipeDataFeeder> _pipes;
   private List<PumpDataFeeder> _pumps;
   private List<TankDataFeeder> _tanks;
   
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
   
      _initFuelSystemData        = null;
      _calcFuelSystemData        = null;
      _stateSubstate             = null;
      _obj                       = null;
      _t0                        = null;
      _pipes                     = null;
      _pumps                     = null;
      _tanks                     = null;
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
   private void initializePipes(String file)throws IOException{}

   //
   //
   //
   private void initializePumps(String file)throws IOException{
      int totalPumps = this._tanks; //One Pump for each tank...
      this._pumps = new LinkedList<PumpDataFeeder>();
      for(int i = 0; i < totalPumps; ++i){
         PumpDataFeeder f = new PumpDataFeeder(this._stage, i);
         f.initialize(file);
         this._pumps.add(f);
      }
   }

   //
   //
   //
   private void initializeTanks(String file)throws IOException{}

   //
   //
   //
   private void setEnginNumber(int engines){
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
   public void addDataFeeder(DataFeeder feeder){}

   //
   //
   //
   public void initialize(String file)throws IOException{
      this.initializePipes(file);
      this.initializePumps(file);
      this.initializeTanks(file);
   }

   //
   //
   //
   public Object monitor(){
      synchronized(this._obj){}
      //temp for now
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
