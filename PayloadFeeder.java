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

public class PayloadDataFeeder implements DataFeeder, Runnable{
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
   private PayloadData               _initPayloadData;
   //Calculated
   private PayloadData               _calcPayloadData;

   private LaunchStateSubstate       _stateSubstate;
   private Object                    _obj; //Threading
   private Random                    _random;
   private Thread                    _t0;

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

      _initPayloadData   = null;
      _calcPayloadData   = null;
      _stateSubstate     = null;
      _obj               = null;
      _random            = null;
      _t0                = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public PayloadFeeder(){
      this._random = new Random();
      this.setUpThread();
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private int getCrewNumberFromHashtable(Hashtable<String,String>ht){
      int crew = -1;
      try{
         crew = Integer.parseInt(ht.get("crew"));
      }
      catch(NumberFormatException nfe){
         crew = -1;
      }
      return crew;
   }

   //
   //
   //
   private double getDryWghtFromHashtable(Hashtable<String,String>ht){
      double dw = Double.NaN;
      try{
         dw = Double.parseDouble(ht.get("dryweight"));
      }
      catch(NumberFormatException nfe){
         dw = Double.NaN;
      }
      return dw;
   }

   //
   //
   //
   private double getMaxWghtFromHashtable(Hashtable<String,String>ht){
      double mw = Double.NaN;
      try{
         mw = Double.parseDouble(ht.get("maxweight"));
      }
      catch(NumberFormatException nfe){
         mw = Double.NaN;
      }
      return mw;
   }


   //
   //
   //
   private boolean getOccFromHashtable(Hashtable<String,String> ht){
      boolean isOccupied = false;
      isOccupied = Boolean.parseBoolean(ht.get("occupied"));
      return  isOccupied;
   }

   //
   //
   //
   private double getO2PercFromHashtable(Hashtable<String,String> ht){
      double percent = Double.NaN;
      try{
         percent = Double.parseDouble(ht.get("o2percent"));
      }
      catch(NumberFormatException npe){
         percent = Double.NaN;
      }
      return percent;
   }

   //
   //
   //
   private void initializePayloadData(String file){
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = read.readPayloadInfo();
         int crw     = this.getCrewNumberFromHashtable(ht);
         double dw   = this.getDryWghtFromHashtable(ht);
         boolean occ =this.getOccFromHashtable(ht);
         double mw   = this.getMaxWghtFromHashtable(ht);
         String mod  = ht.get("model");
         double o2P  = this.getO2PercFromHashtable(ht);
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         this._initPayloadData = null;
      }
   }

   //
   //
   //
   private boolean isPathFile(String file){
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
         npe.printStackTrace(); //Temporary
      }
      finally{
         return isPath;
      }
   }

   //
   //
   //
   private void measure(){}

   //
   //
   //
   private double measureAtmosphere(){
      //Used for O2 Percent
      return Double.NaN;
   }

   //
   //
   //
   private double measureWeight(){
      //Used for Current Weight
      return Double.NaN;
   }

   //
   //
   //
   private double measureTemperature(){
      //Temperature
      return Double.NaN;
   }

   //
   //
   //
   private void setUpThread(){
      this._obj = new Object();
      this._t0  = new Thread();
      this._t0.start();
   }

   ///////////////////DataFeeder Interface Methods////////////////////
   //
   //
   //
   public void addDataFeeder(DataFeeder feeder){}

   //
   //
   //
   public void initialize(String file) throws IOException{
      //Payload Data File
      String pldFile = file;
      if(this.isPathFile(pldFile)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(pldFile);
         pldFile = read.readPathInfo().get("payload");
      }
      this.initializePayloadData(pldFile);
   }

   //
   //
   //
   public Object monitor(){
      //Temporary...
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
   public void run(){
      try{
         int counter   = 0;
         boolean check = false;
         while(true){
            if(this._stateSubstate != null){
               if(this._stateSubstate.state() == INIT){
                  //In Initialize, the Payload should not be occupied
                  //if a capsule, but loaded if not carrying humans
                  //For the time being, check every three seconds
                  if(counter++%3000 == 0){
                     check = true;
                  }
               }
               if(check){
                  this.measure();
                  check = false;
                  counter = 1;
               }
            }
            thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}

//////////////////////////////////////////////////////////////////////
