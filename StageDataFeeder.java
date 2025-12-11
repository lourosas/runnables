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
      //this._engines = new LinkedList<EnginDataFeeder>()
      //...
      //...
   }

   //
   //
   //
   private void initializeFuelSystem(String file)throws IOException{
      System.out.println("StageDataFeeder FuelSystem: "+file);
      FuelSystemDataFeeder f = null;
      f = new FuelSystemDataFeeder(this._stage, this._numEngines);
      f.initialize(file);
      this._fuelSystemDataFeeder = f;
   }

   //
   //
   //
   private void initializeStageData(String file)throws IOException{
      double dw = Double.NaN; int stg = -1; long mod = Long.MIN_VALUE;
      String err = null; boolean isE = false; double mw = Double.NaN;
      int egs = -1; double tol = Double.NaN;
      double wgt = Double.NaN;//calculated
      //Need to grab engine data (currently null) and Fuel System Data
      //test print
      System.out.println("StageDataFeeder StageData: "+file);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst = read.readStageInfo();
         Iterator<Hashtable<String,String>> it = lst.iterator();
         while(it.hasNext()){
            Hashtable<String,String> ht = it.next();
            try{stg = Integer.parseInt(ht.get("number")); }
            catch(NumberFormatException nfe){ stg = -1; }
            if(stg == this._stage){
               //Do not need to get stage number, nor engines
               try{ dw = Double.parseDouble(ht.get("dryweight")); }
               catch(NumberFormatException npe){ dw = Double.NaN;}
               try{ mw = Double.parseDouble(ht.get("maxweight"));}
               catch(NumberFormatException npe){ mw = Double.NaN; }
               try{ mod = Long.parseLong(ht.get("model"),16); }
               catch(NumberFormatException nfe){ mod=Long.MIN_VALUE; }
               try{ tol = Double.parseDouble(ht.get("tolerance")); }
               catch(NumberFormatException nfe){ tol = Double.NaN; }
               egs = this._numEngines;
               /*
               List<EngineData> lst = new LinkedList();
               Iterator<EngineDataFeeder> it=this._engines.iterator();
               while(it.hasNext()){
                  lst.add(it.next().monitor());
               }
               */
               FuelSystemData f = null;
               f=(FuelSystemData)this._fuelSystemDataFeeder.monitor();
               this._initStageData = new GenericStageData(
                                                 dw,   //Dry Weight
                                                 err,  //Error
                                                 mod,  //Model
                                                 isE,  //Is Error
                                                 stg,  //Stage
                                                 egs,  //Engines
                                                 mw,   //Max Weight
                                                 tol,  //Tolerance
                                                 wgt,  //Calc Weight
                                                 null, //Engines
                                                 f);   //Fuel System
               System.out.println(this._initStageData);
            }
         }
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         this._initStageData = null;
         throw ioe;
      }
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
      int stage = -1; int engines = 1;
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst = read.readStageInfo();
         Iterator<Hashtable<String,String>> it = lst.iterator();
         while(it.hasNext()){
            Hashtable<String,String> ht = it.next();
            try{stage = Integer.parseInt(ht.get("number"));}
            catch(NumberFormatException npe){ stage = -1; }
            if(this._stage == stage){
               try{ engines=Integer.parseInt(ht.get("engines")); }
               catch(NumberFormatException npe){ engines = -1; }
               this._numEngines = engines;
            }
         }
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
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      this._fuelSystemDataFeeder.setStateSubstate(stateSubstate);
      /*
      Iterator<EngineDataFeeder> it = this._engines.iterator();
      while(it.hasNext()){
         it.next().setStateSubstate(stateSubstate);
      }
      */
      this._stateSubstate = stateSubstate;

   }

   /////////////////Runnable Interface Implementation/////////////////
   //
   //
   //
   public void run(){
      try{
         int counter = 0;
         while(true){
            if(this._stateSubstate != null){
               //this.measureFuelSystem();
               //this.measureEngines();
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
