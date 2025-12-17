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
import java.math.BigDecimal;
import java.math.RoundingMode;
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
   private Random                 _random;
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
      _calcTankData     = null;
      _stateSubstate    = null;
      _obj              = null;
      _t0               = null;
      _random           = null;
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
      this._random = new Random();
      this.setUpNumber(number);
      this.setUpStage(stage);
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void initializeTankData(String file)throws IOException{
      double cap = Double.NaN; double den = Double.NaN; int num = -1;
      double dw  = Double.NaN; double er  = Double.NaN; int stg = -1;
      String err = null; String fue = null; boolean isE = false;
      long mod = Long.MIN_VALUE; double temp = Double.NaN;
      double tol = Double.NaN; double wgt = Double.NaN;
      //Start a test print
      System.out.println("Tank Data: "+file);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst = read.readTankDataInfo();
         Iterator<Hashtable<String,String>> it = lst.iterator();
         while(it.hasNext()){
            Hashtable<String,String> ht = it.next();
            try{ stg = Integer.parseInt(ht.get("stage")); }
            catch(NumberFormatException nfe){ stg = -1; }
            try{ num = Integer.parseInt(ht.get("number")); }
            catch(NumberFormatException nfe){ num = -1; }
            if(this._stage == stg && this._number == num){
               try{ cap = Double.parseDouble(ht.get("capacity"));}
               catch(NumberFormatException nfe){ cap = Double.NaN; }
               try{ den = Double.parseDouble(ht.get("density")); }
               catch(NumberFormatException nfe){ den = Double.NaN; }
               try{ dw = Double.parseDouble(ht.get("dryweight")); }
               catch(NumberFormatException nfe){ dw = Double.NaN; }
               try{ er = Double.parseDouble(ht.get("rate")); }
               catch(NumberFormatException  nfe){ er = Double.NaN; }
               fue = ht.get("fuel"); 
               try{ mod = Long.parseLong(ht.get("model"),16); }
               catch(NumberFormatException nfe){ mod=Long.MIN_VALUE; }
               try{ temp=Double.parseDouble(ht.get("temperature")); }
               catch(NumberFormatException nfe){ temp=Double.NaN; }
               try{ tol = Double.parseDouble(ht.get("tolerance")); }
               catch(NumberFormatException nfe){ tol = Double.NaN; }
               //Weight needs calculation...init to Double.NaN!!
               this._initTankData = new GenericTankData(
                                                cap,//Capcity
                                                den,//Density
                                                dw, //Dry Weight
                                                er, //Empty Rate
                                                err,//Error
                                                fue,//Fuel
                                                isE,//isError
                                                mod,//
                                                num,
                                                stg,
                                                temp,
                                                tol,
                                                wgt);
            }
         }
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         this._initTankData = null;
         throw ioe;
      }
   }

   //
   //
   //
   private boolean isPathFile(String file)throws IOException{
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
   private double setCapacity(){
      double capacity = Double.NaN; double scale = Double.NaN;
      double min      = Double.NaN; double max   = Double.NaN;
      if(this._stateSubstate.state() == INIT){
         //In the Initialize State, the Capacity should be 0!
         //regardless of Substate...and no negative capacity...
         max = 1. - this._initTankData.tolerance();
         do{
            capacity = this._random.nextDouble();
         }while(capacity > max);
      }
      capacity = Math.round(capacity * 100.)/100.;
      return capacity;
   }

   //
   //
   //
   private double setEmptyRate(){
      double rate = Double.NaN; double scale = Double.NaN;
      double min  = Double.NaN; double max   = Double.NaN;
      if(this._stateSubstate.state() == INIT){
         //In the Initialize State, no Empty Rate regardless of
         //Substate...and no negative rate
         max = 1. - this._initTankData.tolerance();
         do{
            rate = this._random.nextDouble();
         }while(rate > max);
      }
      rate     = Math.round(rate * 100.)/100.;
      return rate;
   }

   //
   //
   //
   private void setMeasuredData
   (
     double capacity,
     double rate,
     double temp
   ){
      String err = null; //Error
      //Get the needed initialized data...error is not determined by
      //the DataFeeder...temperature, rate and capacity are determined
      double den  = this._initTankData.density();
      double dw   = this._initTankData.dryWeight();
      String fue  = this._initTankData.fuel();
      boolean isE = false;
      long   mdl  = this._initTankData.model();
      int    tnk  = this._initTankData.number();
      int    stg  = this._initTankData.stage();
      double tol  = this._initTankData.tolerance();
      double wgt  = Double.NaN;//Weght--Dervifed by the Tank

      synchronized(this._obj){
         this._calcTankData = new GenericTankData(
                                       capacity, //capacity
                                       den,      //density
                                       dw,       //dry weight
                                       rate,     //empty rate
                                       err,      //error
                                       fue,      //fuel
                                       isE,      //isError
                                       mdl,      //model
                                       tnk,      //tank num
                                       stg,      //stage
                                       temp,     //Temperature
                                       tol,      //Tolerance
                                       wgt);     //Weight
      }
   }

   //
   //
   //
   private double setTemp(){
      double temp = Double.NaN;  double min   = Double.NaN;
      double max  = Double.NaN;  double scale = Double.NaN;
      if(this._stateSubstate.state() == INIT){
         //Temperature is insignificant in the Initialize State...
         //put between the boiling and freezing point of water...
         do{
            min   = 273; max = 373;
            temp  = (double)this._random.nextInt((int)(max + 1));
            temp += this._random.nextDouble();
         }while(temp < min || temp > max);
      }
      temp = Math.round(temp*100.)/100.;
      return temp;
   }

   //
   //
   //
   private void setUpNumber(int tankNumber){
      this._number = tankNumber > 0 ? tankNumber : -1;
   }

   //
   //
   //
   private void setUpStage(int stageNumber){
      this._stage = stageNumber > 0 ? stageNumber : -1;
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
         tdFile = read.readPathInfo().get("tank");
      }
      this.initializeTankData(tdFile);
   }

   //
   //
   //
   public Object monitor(){
      synchronized(this._obj){
         return this._calcTankData;
      }
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      this._stateSubstate = stateSubstate;
   }

   /////////////////Runnable Interface Implementation/////////////////
   //
   //
   //
   public void run(){
      int counter   = 0;
      boolean check = false;
      try{
         while(true){
            if(this._stateSubstate != null){
               if(this._stateSubstate.state() == INIT){
                  if(counter++%1000 == 0){
                     //In Initialize State, set the data every second
                     //regardless of substate
                     check = true;
                  }
               }
            }
            if(check){
               double capacity = this.setCapacity();
               double rate     = this.setEmptyRate();
               double temp     = this.setTemp();
               this.setMeasuredData(capacity, rate, temp);
               check = false;
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }

}
//////////////////////////////////////////////////////////////////////
