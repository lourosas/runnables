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

      _currentStage    = -1;
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

   //Technically, do not need this...will keep here "just in case"
   //The rest of the components should have the data and the System
   //is responsible for calculating the weight...
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
      System.out.println("path: "+file);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = read.readRocketInfo();
         System.out.println(ht);
         try{
            this._numStages = Integer.parseInt(ht.get("stages"));
         }
         catch(NumberFormatException nfe){
            this._numStages = -1;
         }
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         this._numStages = -1;
         throw ioe;
      }
   }

   //
   //
   //
   private void initializeRocketData(String file)throws IOException{
      try{
         String mdl  = null;       int    stg = -1;
         double eW   = Double.NaN; double lW  = Double.NaN;;
         double tol  = Double.NaN; this._currentStage = 1;
         double calW = Double.NaN;

         int cs = this._currentStage;

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
         RocketData rd = new GenericRocketData(
                                           mdl, //Model
                                           cs,  //Current Stage
                                           stg, //Number of Stages
                                           eW,  //Empty Weight
                                           lW,  //Loaded Weight
                                           calW,//Calculated Weight
                                           false,//Is Error
                                           null,//Error String
                                           sd,  //Stage Data
                                           tol);//Tolerance
         this._initRocketData = rd;
         System.out.println(this._initRocketData);
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
   private void initializeStageData(String file)throws IOException{
      System.out.println(file);
      this._stages = new LinkedList<StageDataFeeder>();
      for(int i = 0; i < this._numStages; ++i){
         //Since Zero based loop, and there is no "Stage 0", the Stage
         //is One ahead of the Loop Counter
         StageDataFeeder f = new StageDataFeeder(i+1);
         f.initialize(file);
         this._stages.add(f);
      }
   }

   //
   //
   //
   private boolean isPathFile(String file)throws IOException{
      boolean isPath = false;
      System.out.println(file);
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
      List<StageData> stageData = new LinkedList<StageData>();
      Iterator<StageDataFeeder> it = this._stages.iterator();
      while(it.hasNext()){
         try{
            stageData.add((StageData)it.next().monitor());
         }
         catch(ClassCastException cce){
            stageData.add(null);
         }
      }
      return stageData;
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
      String rocketFile = file;
      if(this.isPathFile(file)){
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
      Iterator<StageDataFeeder> it = this._stages.iterator();
      while(it.hasNext()){
         it.next().setStateSubstate(stateSubstate);
      }
      this._stateSubstate = stateSubstate;
   }

   /////////////////Runnable Interface Implementattion////////////////
   //
   //
   //
   public void run(){
      try{
         int counter = 0;
         while(true){
            if(this._stateSubstate != null){
               //this.measureStages();
               //this.measureRocket();
               //Test Prints
               if(counter++%1000 == 0){
                  System.out.println(Thread.currentThread().getName());
                  System.out.println(Thread.currentThread().getId());
               }
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
