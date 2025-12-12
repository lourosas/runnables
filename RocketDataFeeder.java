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

   private int                     _currentStage;
   private int                     _numStages;
   //Read In
   private RocketData              _initRocketData;
   //Calculated
   private RocketData              _calcRocketData;

   private LaunchStateSubstate     _stateSubstate;
   private Object                  _obj; //Threading
   private Thread                  _t0;

   private boolean                 _toStart;
   //Sigleton Implmentation
   private static DataFeeder       _instance;

   //This is going have to be a LIST!!!  Based on stages!!!!
   private List<StageDataFeeder>   _stages;
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

      _currentStage    = 1;
      _numStages       = -1;
      _initRocketData  = null;
      _calcRocketData  = null;
      _stateSubstate   = null;
      _obj             = null;
      _t0              = null;
      _toStart         = false;
      //Singleton
      _instance        = null;
      _stages          = null;
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
      this.setUpThread();
   }

   //
   //
   //
   private double calculateCurrentWeight(){
      //If the System not initialized, no current weight at the moment
      double currentWeight = Double.NaN;
      if(this._stateSubstate != null){}
      return currentWeight;
   }

   //
   //
   //
   private void grabNumberOfStages(String file)throws IOException{
      System.out.println(file);
   }

   //
   //
   //
   private void initializeRocketData(String file)throws IOException{
      try{
         String mdl = null;       int    stg = -1;
         double eW  = Double.NaN; double lW  = Double.NaN;;
         double tol = Double.NaN;
         

         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String, String> ht = read.readRocketInfo();
         mdl = ht.get("model");
         try{ stg = Integer.parseInt(ht.get("stages")); }
         catch(NumberFormatException nfe){ stg = -1; }
         try{ eW = Double.parseDouble(ht.get("empty_weight")); }
         catch(NumberFormatException nfe){ eW = Double.NaN; }
         try{ lW = Double.parseDouble(ht.get("loaded_weight")); }
         catch(NumberFormatException nfe){ lW = Double.NaN; }
         try{ tol = Double.parseDouble(ht.get("tolerance")); }
         catch(NumberFormatException nfe){ tol = Double.NaN; }
         //Grab the Stage Data
         List<StageData> sd = this.monitorStages(stg);
         double calW = this.calculateCurrentWeight();
         RocketData rd = new GenericRocketData(mdl,
                                               this._currentStage,stg,
                                               eW,lW,calW,false,null,
                                               sd,tol);
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         this._initRocketData = null;
         throw ioe;
      }
   }

   //
   //
   //
   private void initializeStageData(String file)throws IOException{}

   //
   //
   //
   private boolean isPathFile(String file)throws IOException{
      boolean isPath = false;
      System.out.println(file);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         //Test Print
         System.out.println(read.readPathInfo().get("parameter"));
         isPath = true;
      }
      catch(IOException ioe){
         isPath = false;
         ioe.printStackTrace();
         //Do more stuff
         throw ioe;
      }
      catch(NullPointerException e){
         isPath = false;
         e.printStackTrace();
      }
      finally{
         return isPath;
      }
   }

   //
   //
   //
   private List<StageData> monitorStages(int numStages){
      /*
      List<StageData> stageData = new LinkedList<StageData>();
      for(int i = 0; i < numStages; ++i){
         stageData.add(this._stageDF.monitor());
      }
      return stageData;
      */
      return null; //Stop Gap for the time...
   }

   //
   //
   //
   private void setUpThread(){
      this._obj   = new Object();
      this._t0    = new Thread(this);
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
      
      //this._stageDF = StageDataFeeder.instance();
      //this._stageDF.initialize(file);

      String rocketFile = file;
      if(isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         rocketFile = read.readPathInfo().get("rocket");
      }
      this.grabNumberOfStages(rocketFile);
      this.initializeStageData(file);
      this.initializeRocketData(rocketFile);
   }

   //
   //
   //
   public Object monitor(){
      synchronized(this._obj){}
      //Temp For now
      return null;
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      this._stateSubstate = stateSubstate;
      //this._stageDF.setStateSubstate(this._stateSubstate);
      System.out.println(this._stateSubstate);
   }

   /////////////////Runnable Interface Implementattion////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
