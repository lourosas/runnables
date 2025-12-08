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

public class PipeDataFeeder implements DataFeeder, Runnable{
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
   private PipeData            _initPipeData;
   //Derived
   private PipeData            _calcPipeData;

   private LaunchStateSubstate _stateSubstate;
   private Object              _obj;
   private Thread              _t0;

   private int                 _number; //Engine Number
   private int                 _stage;
   private int                 _tank;  //Tank number...

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
   
      _initPipeData       = null;
      _calcPipeData       = null;
      _stateSubstate      = null;
      _obj                = null;
      _t0                 = null;
      _number             = -1; //Engine
      _stage              = -1;
      _tank               = -1;
   };
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public PipeDataFeeder(int stage, int tank, int number){
      this.setStageNumber(stage);
      this.setTankNumber(tank);
      this.setNumber(number);
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void initializePipeData(String file){
      String err = null; double flw = Double.NaN; //Derived
      int num = this._number; boolean isE = false; int stg = -1;
      int tnk = -1; double temp = Double.NaN;
      double tol = Double.NaN; String ft = null;//Fuel Type
      //Test Print
      System.out.println("Pipe Data: "+file); //Test Print
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst = read.readPipeDataInfo();
         Iterator<Hashtable<String,String>> it = lst.iterator();
         while(it.hasNext()){
            Hashtable<String,String> ht = it.next();
            //First, get Stage and Tank
            try{ stg = Integer.parseInt(ht.get("stage")); }
            catch(NumberFormatException npe){ stg = -1; }
            try{ tnk = Integer.parseInt(ht.get("tanknumber")); }
            catch(NumberFormatException npe){ tnk = -1; }
            if(this._stage == stg && this._tank == tnk){
               try{ flw = Double.parseDouble(ht.get("rate"));}
               catch(NumberFormatException nfe){ flw = Double.NaN; }
               try{ temp = Double.parseDouble(ht.get("temperature"));}
               catch(NumberFormatException nfe){ temp = Double.NaN; }
               try{ tol = Double.parseDouble(ht.get("tolerance")); }
               catch(NumberFormatException nfe){ tol = Double.NaN; }
               this._initPipeData = new GenericPipeData(
                                             err, //error
                                             flw, //Flow
                                             num, //engine num
                                             isE, //is Error
                                             stg, //stage
                                             tnk, //tank
                                             temp,//temperature
                                             tol, //tolerance
                                             ft);
               System.out.println(this._initPipeData);
            }
         }
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         this._initPipeData = null;
      }

   }

   //
   //
   //
   private boolean isPathFile(String file){
      System.out.println("Pipe Data: "+file);
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
   private void setNumber(int number){
      this._number = number;
   }

   //
   //
   //
   private void setStageNumber(int stage){
      this._stage = stage;
   }

   //
   //
   //
   private void setTankNumber(int tank){
      this._tank = tank;
   }

   //
   //
   //
   private void setUpThread(){
      this._obj  = new Object();
      this._t0   = new Thread(this);
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
      //Pipe Data File
      String pdFile = file;
      if(this.isPathFile(pdFile)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(pdFile);
         pdFile = read.readPathInfo().get("tank");
      }
      this.initializePipeData(pdFile);
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
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      this._stateSubstate = stateSubstate;
   }

   /////////////////Runnable Interface Implementation/////////////////
   //
   //
   //
   public void run(){
      int counter = 0;
      try{
         while(true){
            if(this._stateSubstate != null){
               //this.measureData();
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
