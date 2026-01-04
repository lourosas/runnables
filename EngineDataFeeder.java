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
   private Random              _random;
   private Thread              _t0;

   private boolean             _isIgnited;
   private long                _model;
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
      _random             = null;
      _t0                 = null;
      _isIgnited          = false;
      _model              = Long.MIN_VALUE;
      _number             = -1;
      _stage              = -1;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public EngineDataFeeder(int stage, int number){
      this._random = new Random();
      this.setStageNumber(stage);
      this.setEngineNumber(number);
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void initializeEngineData(String file)throws IOException{
      int stg = -1;  int num = this._number; double exf = Double.NaN;
      double ff = Double.NaN; long mod = Long.MIN_VALUE;
      boolean isE = false; String err = null; boolean isIg = false;
      double temp = Double.NaN; double tol = Double.NaN; int tot = -1;
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
            try{ tot = Integer.parseInt(ht.get("total"));}
            catch(NumberFormatException nfe){ tot = -1;}
            //this is not the way...I need to come up with a better
            //way...
            if(this._number >= tot){
               int more = tot;
               ht = it.next();
               try{ stg = Integer.parseInt(ht.get("stage")); }
               catch(NumberFormatException npe){ stg = -1; }
               try{ tot = Integer.parseInt(ht.get("total"));}
               catch(NumberFormatException nfe){ tot = -1;}
               tot += more;
            }
            if(stg == this._stage && this._number < tot){
               try{ exf=Double.parseDouble(ht.get("exhaust_flow")); }
               catch(NumberFormatException nfe){ exf = Double.NaN; }
               try{ ff = Double.parseDouble(ht.get("fuel_flow"));}
               catch(NumberFormatException nfe){ ff = Double.NaN; }
               try{ mod = Long.parseLong(ht.get("model"), 16); }
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
                                          tol,  //Tolerance
                                          tot); //Total
               this._initEngineData = e;
               System.out.println(this._initEngineData);
            }
            else{
               tot += more;
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
   private double setExhaustFlow(){
      double exhaustFlow = Double.NaN; double scale = Double.NaN;
      double min         = Double.NaN; double max   = Double.NaN;
      if(this._stateSubstate.state() == INIT){
         //In the Initialize State, the Flows should be 0!
         //regardless of Substate
         max = 1. - this._initEngineData.tolerance();
         do{
            exhaustFlow = this._random.nextDouble();
         }while(exhaustFlow > max);
      }
      exhaustFlow = Math.round(exhaustFlow * 100.)*0.01;
      return exhaustFlow;
   }

   //
   //
   //
   private double setFuelFlow(){
      double fuelFlow = Double.NaN;  double scale = Double.NaN;
      double min      = Double.NaN;  double max   = Double.NaN;
      if(this._stateSubstate.state() == INIT){
         //In the Initialization State, no Fuel Flow...
         //regardless of Substate...and not negative fuel flow
         max = 1. - this._initEngineData.tolerance();
         do{
            fuelFlow = this._random.nextDouble();
         }while(fuelFlow > max);
      }
      fuelFlow = Math.round(fuelFlow * 100.)*0.01;
      return fuelFlow;
   }

   //
   //
   //
   private void setMeasuredData
   (
      double exhaustFlow,
      double fuelFlow,
      double temp
   ){
      String  err = null; //Error
      boolean isE = false;
      //Get the needed initialzed data...error is not determined by
      //the DataFeeder...exhaust flow, fuel flow, temperature are...
      long    mdl = this._initEngineData.model();
      int     idx = this._initEngineData.index();
      int     stg = this._initEngineData.stage();
      double  tol = this._initEngineData.tolerance();
      boolean ign = this._isIgnited;
      int     tot = this._initEngineData.total();
      synchronized(this._obj){
         EngineData e = new GenericEngineData(
                                    stg,   //Stage
                                    idx,   //number
                                    exhaustFlow,
                                    fuelFlow,
                                    mdl,   //Model
                                    isE,   //Is Error
                                    err,   //error
                                    ign,   //is Ignited
                                    temp,  //Temperature
                                    tol,   //Tolerance 
                                    tot);  //Total
         this._calcEngineData = e;
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
   private double setTemp(){
      double temp = Double.NaN;  double scale = Double.NaN;
      double min  = Double.NaN;  double max   = Double.NaN;
      if(this._stateSubstate.state() == INIT){
         //Temperature is insignificant in the initialization state...
         //put between the boiling and freezing point of water...
         min = 273; max = 373;
      }
      do{
         temp  = (double)this._random.nextInt((int)(max + 1));
         temp += this._random.nextDouble();
      }while(temp < min || temp > max);
      temp = Math.round(temp * 100)*0.01;
      return temp;
   }

   //
   //
   //
   private void setUpThread(){
      this._obj = new Object();
      this._t0  = new Thread(this);
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
      synchronized(this._obj){
         return this._calcEngineData;
      }
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
   public void run(){
      try{
         int counter   = 0;
         boolean check = false;
         while(true){
            if(this._stateSubstate != null){
               if(this._stateSubstate.state() == INIT){
                  //In Initialize, query at 1/10 a second
                  if(counter++%100 == 0){
                     check = true;
                  }
               }
            }
            if(check){
               double exhFlow    = this.setExhaustFlow();
               double fuelFlow   = this.setFuelFlow();
               double temp       = this.setTemp();
               this.setMeasuredData(exhFlow, fuelFlow, temp); 
               check = false;
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}

//////////////////////////////////////////////////////////////////////
