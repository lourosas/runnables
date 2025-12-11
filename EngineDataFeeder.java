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

public class EngineDataFeeder implements DataFeeder, Runnable{
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

   //Read in...
   private EngineData          _initEngineData;
   //Derived
   private EngineData          _calcEngineData;

   private LaunchStateSubstate _stateSubstate;
   private Object              _obj;
   private Thread              _t0;

   private int                 _number; //Current Engine Number
   private int                 _stage;  //Current Rocket Stage

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

      _initEngineData     = null;
      _calcEngineData     = null;
      _stateSubstate      = null;
      _obj                = null;
      _t0                 = null;
      _number             = -1;
      _stage              = -1;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public EngineDataFeeder(int stage, int number){
      this.setStageNumber(stage);
      this.setEngineNumber(number);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void initializeEngineData(String file)throws IOException{
      int stg = -1;  int num = this._number; double exf = Double.NaN;
      double ff = Double.NaN; long mod = Long.MIN_VALUE;
      boolean isE = false; String err = null; boolean isIg = false;
      double temp = Double.NaN; double tol = Double.NaN;
      //Test Print
      System.out.println("EngineDataFeeder: "+file);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst=read.readEngineDataInfo();
         Iterator<Hashtable<String,String>> it = lst.iterator();
         while(it.hasNext()){
            Hashtable<String,String> ht = it.next();
            try{ stg = Integer.parseInt(ht.get("stage")); }
            catch(NumberFormatException npe){ stg = -1; }
            if(stg == this._stage){
               try{ exf=Double.parseDouble(ht.get("exhaust_flow")); }
               catch(NumberFormatException nfe){ exf = Double.NaN; }
               try{ ff = Double.parseDouble(ht.get("fuel_flow"));}
               catch(NumberFormatException nfe){ ff = Double.NaN; }
               try{ mod = Long.parseLong(ht.get("nodel"), 16); }
               catch(NumberFormatException nfe){ mod=Long.MIN_VALUE; }
               try{temp=Double.parseDouble(ht.get("temperature"));}
               catch(NumberFormatException nfe){ temp = Double.NaN; }
               try{ tol = Double.parseDouble(ht.get("tolerance")); }
               catch(NumberFormatException nfe){ tol = Double.NaN; }
               EngineData e = new GenericEngineData(
                                          stg,  //Stage
                                          num,  //number
                                          exf,  //Exhaust Flow Rate
                                          ff,   //Fuel Flow Rate
                                          mod,  //Model
                                          isE,  //isError
                                          err,  //error
                                          isIg, //is ignited
                                          temp, //Temperature
                                          tol); //Tolerance
               this._initEngineData = e;
               System.out.println(this._initEngineData);
            }
         }
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         this._initEngineData = null;
         throw ioe;
      }
   }

   //
   //
   //
   private boolean isPathFile(String file){
      System.out.println("EngingDataFeeder Path: "+file);
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
   private void setEngineNumber(int number){
      if(number > -1){
         this._number = number;
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

   /////////////////////DataFeeder Implementation/////////////////////
   //
   //
   //
   public void addDataFeeder(DataFeeder feeder){}

   //
   //
   //
   public void initialize(String file)throws IOException{
      //Engine Data File
      String engFile = file;
      if(this.isPathFile(engFile)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         engFile = read.readPathInfo().get("engine");
      }
      this.initializeEngineData(engFile);
   }

   //
   //
   //
   public Object monitor(){
      synchronized(this._obj){}
      //null for now
      return null;
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      this._stateSubstate = stateSubstate;
   }

   ////////////////Runnable Interface Implementation//////////////////
   //
   //
   //
   public void run(){}
}

//////////////////////////////////////////////////////////////////////
