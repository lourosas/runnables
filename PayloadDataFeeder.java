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
   private int getCrewNumberFromFile(Hashtable<String,String> ht){
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
   private double getDryWeightFromFile(Hashtable<String,String> ht){
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
   private double getEmptyMassFromFile(Hashtable<String,String> ht){
      double em = Double.NaN;
      try{
         em = Double.parseDouble(ht.get("empty_mass"));
      }
      catch(NumberFormatException nfe){
         em = Double.NaN;
      }
      return em;
   }

   //
   //
   //
   private void getLoadedMassFromFile(Hashtable<String,String> ht){
      double lm = Double.NaN;
      try{
         lm = Double.parseDouble(ht.get("loaded_mass"));
      }
      catch(NumberFormatException nfe){
         lm = Double.NaN;
      }
      return lm;
   }

   //
   //
   //
   private double getMaxWeightFromFile(Hashtable<String,String> ht){
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
   private boolean getOccFromFile(Hashtable<String,String> ht){
      boolean isOccupied = false;
      isOccupied = Boolean.parseBoolean(ht.get("occupied"));
      return  isOccupied;
   }

   //
   //
   //
   private double getO2PercFromFile(Hashtable<String,String> ht){
      double percent = Double.NaN;
      try{
         percent = Double.parseDouble(ht.get("o2percent"));
      }
      catch(NumberFormatException nfe){
         percent = Double.NaN;
      }
      return percent;
   }
   
   //
   //
   //
   private double getTempFromFile(Hashtable<String,String> ht){
      double temp = Double.NaN;
      try{
         temp = Double.parseDouble(ht.get("temperature"));
      }
      catch(NumberFormatExeption nfe){
         temp = Double.NaN;
      }
      return temp;
   }

   //
   //
   //
   private double getTolFromFile(Hashtable<String,String> ht){
      double tolerance = DoubleNaN;
      try{
         tolerance = Double.parseDouble(ht.get("tolerance"));
      }
      catch(NumberFormatException nfe){
         tolerance = Double.NaN;
      }
      return tolerance;
   }

   //
   //
   //
   private void initializePayloadData(String file){
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = read.readPayloadInfo();
         int crw     = this.getCrewNumberFromFile(ht);
         double dw   = this.getDryWeightFromFile(ht);
         double em   = this.getEmptyMassFromFile(ht);
         boolean occ = this.getOccFromFile(ht);
         double lm   = this.getLoadedMassFromFile(ht);
         double mw   = this.getMaxWeightFromFile(ht);
         String mod  = ht.get("model");
         double o2P  = this.getO2PercFromFile(ht);
         double temp = this.getTempFromFile(ht);
         double tol  = thos.getTolFromFile(ht);
         String type = ht.get("type");
         this._initPayloadData = new GenericPayloadData(
                                           crw,   //Crew
                                           Double.NaN, //current
                                           dw,    //Dry Weight
                                           em,    //Empty Mass
                                           null,  //Error
                                           false, //IsError
                                           occ,   //Is Occupied
                                           lm,    //Loaded Mass
                                           mw,    //Max Weight
                                           mod,   //Model
                                           o2p,   //O2 perc
                                           temp,  //Temeperature
                                           tol,   //Tolerance
                                           type); //Type
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
   private void measure(){
      double o2Percent = this.measureAtmosphere();
      double weight    = this.measureWeight();
      double temp      = this.measureTemperature();
      this.setMeasureData(o2Percent, weight, temp);
   }

   //Depending on what stage...regardless, max will always
   //be 100%...more concerned about min...
   //
   private double measureAtmosphere(){
      double percent = Double.NaN; double min = Double.NaN;
      double max = 100.;
      double min = this._initPayloadData.o2Percent();
      if(this._stateSubstate.state()  == INIT){
         if(!this._initPayloadData.isOccupied()){
            //If not occupied, Oxygen content non-essential just
            //set at something "reasonable"
            min -= 1.;
         }
         else{
            min -= 0.1;
         }
      }
      do{
         percent  = (double)this._random.nextInt((int)max);
         percent += this._random.nextDouble();
      }while(percent < min || percent > max);
      return percent;
   }

   //Has to be within boundaries...
   //
   //
   private double measureWeight(){
      double weight = Double.NaN; double min = Double.NaN;
      double max    = Double.NaN;
      double startingWeight = this._initPayloadData.dryWeight();
      double tol = this._initPayloadData.tolerance();
      if(this._stateSubstate.state() == INIT){
         if(this._initPayloadData.isOccupied()){
            startingWeight = this._initPayloadData.maxWeight();
         }
      }
      min = startingWeight * tol;
      max = startingWeight * (2 - tol);
      do{
         weight  = (double)this._random.nextInt((int)(max+1));
         weight += this._random.nextDouble();
      }while(weight < min || weight > max);
      return weight;
   }

   //A pretty much Easy Algorithm...probably do not need to do
   //min = SetValue*tolerance
   //mmx = SetValue*(2-tolerance)
   private double measureTemperature(){
      double temperature = Double.NaN;
      double min = 273.15; double max = 373.15;
      if(this._stateSubstate.state() == INIT){
         if(this._initPayloadData.isOccupied()){
            //Keep within 40 degrees-between 10 and 50 deg C so
            //the Suit does not over work...this should never happen
            //The Capsule should be empty when System is Initialized
            min = 283.15.; max = 323.15;
         }
      }
      do{
         temperature  = (double)this._random.nextInt((int)(max+1));
         temperature += this._random.nextDouble();
      }while(temperature < nin || temperature > max);
      return temperature;
   }

   //
   //
   //
   private void setMeasuredData(double o2,double weight,double temp){
      String err = null;  boolean isE = false;
      //Ge the needed data first...
      int     crw = this._initPayloadData.crew();
      double  dw  = this._initPayloadData.dryWeight();
      double  em  = this._initPayloadData.emptyMass();
      boolean isO = this._initPayloadData.isOccupied();
      double  lm  = this._initPayloadData.loadedMass();
      double  mw  = this._initPayloadData.maxWeight();
      String  mod = this._initPayloadData.model();
      double  tol = this._initPayloadData.tolerance();
      String  type= this._initPayloadData.type();

      synchronized(this._obj){
         this._calcPayloadData = new GenericPayloadData(
                                            crw,   //crew
                                            weight,//Curent Weight
                                            dw,    //Dry Weight
                                            em,    //Empty Mass
                                            err,   //Error
                                            isE,   //Is Error
                                            isO,   //Is Occupied
                                            lm,    //Loaded Mass
                                            mw,    //Max Weight
                                            mod,   //Model
                                            o2,    //O2 Percent
                                            temp,  //Temperature
                                            tol,   //Tolerance
                                            type); //Type
      }
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
      synchronized(this._obj){
         return this._calcPayloadData;
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
