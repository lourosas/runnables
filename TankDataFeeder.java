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

   //Read In
   private TankData               _initTankData;
   //Calculated
   private TankData               _calcTankData;

   private LaunchStateSubstate    _stateSubstate;
   private Object                 _obj; //Threading
   private Thread                 _t0;

   private boolean                _start;
   //Singleton Implementation
   private static TankDataFeeder  _instance;

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

      _initTankData     = null;
      _caclTankData     = null;
      _stateSubstate    = null;
      _obj              = null;
      _t0               = null;
      _start            = false;
      _instance         = null;
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
   private void initializeTankData(String file)throws IOException{
      //Start a test print
      System.out.println(file);
   }

   //
   //
   //
   private boolean isPathFile(String file)throws IOException{
      boolean isPath = false;
      System.out.println(file); //Fucking test print
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         //Test Print!
         System.out.println(read.readPathInfo().get("paramenter"));
         isPath = true;
      }
      catch(IOException ioe){
         isPath = false;
         ieo.printStackTrace(); //Temporary
         //Do more stuff
         throw ioe;
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
   private void setUpNumber(int tankNumber){
      this._number = tankNumber > -1 ? tankNumber : -1;
   }

   //
   //
   //
   private void setUpStage(int stageNumber){
      this._stage = stageNumber > -1 ? stageNumber : -1;
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
   public void addDataFeeder(DataFeeder feeder){}

   //
   //
   //
   public void initialize(String file)throws IOException{
      //Tank Data File
      String tdFile = file;
      if(this.isPathFile(tdFile)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(tdFile);
         tdFile = read.readPathInfo().get("stage");
      }
      this.initializeTankData(tdFile);
   }

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

   /////////////////Runnable Inteface Implementation//////////////////
   //
   //
   //
   public void run(){}

}
//////////////////////////////////////////////////////////////////////
