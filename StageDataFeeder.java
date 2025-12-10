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

   private FuelSystemDataFeeder   _fuelSystemDataFeeder;
   //private List<EngineDataFeeder> _engines;

   private int                    _numEngines;
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
      _fuelSystemDataFeeder = null;
      _numEngines           = -1;
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

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void initializeEngines(String file)throws IOException{
      System.out.println("StageDataFeeder Engines: "+file);
   }

   //
   //
   //
   private void initializeFuelSystem(String file){
      System.out.println("StageDataFeeder FuelSystem: "+file);
   }

   //
   //
   //
   private void initializeStageData(String file){
      System.out.println("StageDataFeeder StageData: "+file);
   }

   //
   //
   //
   private boolean isPathFile(String file){
      System.out.println("StageDataFeeder Path: "+file);
      boolean isPath = false;
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = read.readPathInfo();
         if(read.readPathInfo().get("parameter") == null){
            throw new NullPointerException("Not a Path File");
         }
         isPath = true;
      }
      catch(IOException ioe){
         isPath = false;
         ioe.printStackTrace(); //Temporary
      }
      catch(NullPointerException npe){
         isPath = false;
         npe.printStackTrace(); //Temporary
      }
      finally{
         return isPath;
      }
   }

   //
   //
   //
   private void setEngineNumber(String file)throws IOException{
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LauchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst = read.readStageInfo();

      }
      catch(IOException ioe){
         ioe.printStackTrace();
         this._numEngines = -1;
         throw ioe;
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
   public void initialize(String file)throws IOException{
      //Stage Data File
      String stgFile = file;
      if(this.isPathFile(stgFile)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         stgFile = read.readPathInfo().get("stage");
      }
      this.setEngineNumber(stgFile);
      this.initializeEngines(file);
      this.initializeFuelSystem(file);
      this.initializeStageData(stgFile);
   }

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
