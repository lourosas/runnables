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

public class PumpDataFeeder implements DataFeeder, Runnable{
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
   private PumpData            _initPumpData;
   //Derived
   private PumpData            _calcPumpData;

   private LaunchStateSubstate _stateSubstate;
   private Object              _obj;
   private Random              _random;
   private Thread              _t0;

   private int                 _stage; //Stage Number
   private int                 _tank;  //Tank Number

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

      _initPumpData      = null;
      _calcPumpData      = null;
      _stateSubstate     = null;
      _obj               = null;
      _random            = null;
      _t0                = null;
      _stage             = -1;
      _tank              = -1;
   };

   ////////////////////////////Contructors////////////////////////////
   //
   //
   //
   public PumpDataFeeder(int stage, int tank){
      this._random = new Random();
      this.setStageNumber(stage);
      this.setTankNumber(tank);
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void initializePumpData(String file)throws IOException{
      String err = null; double flw = Double.NaN; //Derived
      int tnk = this._tank;  boolean isE = false;
      int stg = this._stage; double temp = Double.NaN;
      double tol = Double.NaN; String ft = null; //Fuel Type
      //Test Print
      System.out.println("Pump Data:  "+file);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst = read.readPumpDataInfo();
         Iterator<Hashtable<String,String>> it = lst.iterator();
         while(it.hasNext()){
            Hashtable<String,String> ht = it.next();
            //First, get the Stage and Tank
            try{ stg = Integer.parseInt(ht.get("stage")); }
            catch(NumberFormatException nfe){ stg = -1; }
            try{ tnk = Integer.parseInt(ht.get("tanknumber"));}
            catch(NumberFormatException nfe){ tnk = -1; }
            if(this._stage == stg && this._tank == tnk){
               try{ flw = Double.parseDouble(ht.get("rate")); }
               catch(NumberFormatException nfe){ flw = Double.NaN; }
               try{ temp = Double.parseDouble(ht.get("temperature"));}
               catch(NumberFormatException nfe){ temp = Double.NaN; }
               try{ tol = Double.parseDouble(ht.get("tolerance")); }
               catch(NumberFormatException nfe){ tol = Double.NaN; }
               this._initPumpData = new GenericPumpData(
                                            err,  //error
                                            flw,  //Flow Rate
                                            tnk,  //Tank
                                            isE,  //Is Error
                                            stg,  //Stage
                                            temp, //Temperature
                                            tol,  //tolerance
                                            ft);
               System.out.println(this._initPumpData);
            }
         }
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         this._initPumpData = null;
         throw ioe;
      }
   }

   //
   //
   //
   private boolean isPathFile(String file){
      System.out.println("Pump Data: "+file);
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
         ioe.printStackTrace();  //Temporary
         throw ioe;
      }
      catch(NullPointerException npe){
         isPath = false;
         npe.printStackTrace();  //Temporary
      }
      finally{
         return isPath;
      }
   }
   
   //
   //
   //
   private void setMeasuredData(double flow, double temp){
      String err = null; //Error
      String ft  = null; //Fuel Type
      //Get the appropriate data from the initialized pump data...
      //error is not determined by the DataFeeder...
      //Flow and Temp are determined...
      int tnk    = this._initPumpData.index();
      int stg    = this._initPumpData.stage();
      double tol = this._initPumpData.tolerance();
      synchronized(this._obj){
         int round = (int)(flow*100);
         flow      = round*0.01;
         round     = (int)(temp*100);
         temp      = round*0.01;
         this._calcPumpData = new GenericPumpData(
                                          err,   //error
                                          flow,  //flow
                                          tnk,   //Tank Number
                                          false, //is Error
                                          stg,   //Stage
                                          temp,  //Temperature
                                          tol,   //Tolerance
                                          ft);   //Fuel Type
      }
   }

   //
   //
   //
   private double setFlow(){
      double flow = Double.NaN; double scale = Double.NaN;
      double min  = Double.NaN; double max   = Double.NaN;
      if(this._stateSubstate.state() == INIT){
         //In the Initialize State, the Pump should not be pumping!
         //Regardless of Substate...and cannot have a Negative flow...
         max = 1. - this._initPumpData.tolerance();
         do{
            flow = this._random.nextDouble();
         }while(flow > max);
      }
      return flow;
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
   private void setTankNumber(int tank){
      if(tank > 0){
         this._tank = tank;
      }
   }

   //
   //
   //
   private double setTemp(){
      double temp = Double.NaN; double min = Double.NaN;
      double max  = Double.NaN;
      if(this._stateSubstate.state() == INIT){
         //Temperature is insignificant in the Initialize State...
         //put between the boiling and freezing point of water...
         do{
            min = 273.; max = 373.;
            temp  = (double)this._random.nextInt((int)(max + 1));
            temp += this._random.nextDouble();
         }while(temp < min || temp > max);
      }
      return temp;
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
   public void addDataFeeder(DataFeeder feeeder){}

   //
   //
   //
   public void initialize(String file)throws IOException{
      //Pump Data File
      String pdFile = file;
      if(this.isPathFile(pdFile)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(pdFile);
         pdFile = read.readPathInfo().get("pump");
      }
      this.initializePumpData(pdFile);
   }

   //
   //
   //
   public Object monitor(){
      synchronized(this._obj){
         return this._calcPumpData;
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
                  //In Initialize State, set the data every second
                  //(regardless of Substate)
                  //Test Prints
                  if(counter++%1000 == 0){
                     check = true;
                  }
               }
               if(check){
                  double flow = this.setFlow();
                  double temp = this.setTemp();
                  this.setMeasuredData(flow, temp);
                  check = false;
                  //temp prints--remove
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
