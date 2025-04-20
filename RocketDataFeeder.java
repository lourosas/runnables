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

public class RocketDataFeeder implements DataFeeder{
   private LaunchSimulatorStateSubstate.State INIT             = null;
   private LaunchSimulatorStateSubstate.State PREL             = null;
   private LaunchSimulatorStateSubstate.State IGNI             = null;
   private LaunchSimulatorStateSubstate.State LAUN             = null;
   private LaunchSimulatorStateSubstate.State ASCE             = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate SET  = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate CONT = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate FUEL = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate HOLD = null;
   private LaunchSimulatorStateSubstate.IgnitionSubstate  IGN  = null;
   private LaunchSimulatorStateSubstate.IgnitionSubstate  BUP  = null;
   private LaunchSimulatorStateSubstate.AscentSubstate    STG  = null;
   private LaunchSimulatorStateSubstate.AscentSubstate    IGNE = null;

   private double     _emptyWeight;
   private double     _loadedWeight;
   private DataFeeder _lmdFeeder; //LaunchMechanismDataFeeder
   private double     _measuredWeight;
   private Random     _random;
   private int        _stages;
   private LaunchSimulatorStateSubstate _cond;

   {
      INIT = LaunchSimulatorStateSubstate.State.INITIALIZE;
      PREL = LaunchSimulatorStateSubstate.State.PRELAUNCH;
      IGNI = LaunchSimulatorStateSubstate.State.IGNITION;
      LAUN = LaunchSimulatorStateSubstate.State.LAUNCH;
      ASCE = LaunchSimulatorStateSubstate.State.ASCENT;
      SET  = LaunchSimulatorStateSubstate.PreLaunchSubstate.SET;
      CONT = LaunchSimulatorStateSubstate.PreLaunchSubstate.CONTINUE;
      FUEL = LaunchSimulatorStateSubstate.PreLaunchSubstate.FUELING;
      HOLD = LaunchSimulatorStateSubstate.PreLaunchSubstate.HOLD;
      IGN  = LaunchSimulatorStateSubstate.IgnitionSubstate.IGNITION;
      BUP  = LaunchSimulatorStateSubstate.IgnitionSubstate.BUILDUP;
      STG  = LaunchSimulatorStateSubstate.AscentSubstate.STAGING;
      IGNE =LaunchSimulatorStateSubstate.AscentSubstate.IGNITEENGINES;

      _cond           = null;
      _emptyWeight    = Double.NaN;
      _loadedWeight   = Double.NaN;
      _lmdFeeder      = null;
      _measuredWeight = Double.NaN;
      _random         = null;
      _stages         = 0;
   };
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public RocketDataFeeder(){
      this._random = new Random();
   }

   ///////////////////////////Public Methods//////////////////////////

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setEmptyWeight(Hashtable<String,String> ht){
      String ew = ht.get("empty_weight");
      try{
         this._emptyWeight = Double.parseDouble(ew);
      }
      catch(NumberFormatException nfe){
         this._emptyWeight = Double.NaN;
      }
   }

   //
   //
   //
   private void setLoadedWeight(Hashtable<String,String> ht){
      String lw = ht.get("loaded_weight");
      try{
         this._loadedWeight = Double.parseDouble(lw);
      }
      catch(NumberFormatException nfe){
         this._loadedWeight = Double.NaN;
      }
   }

   ////////////////DataFeeder Interface Implementation////////////////
   //
   //
   //
   public double angleOfHolds(){ return Double.NaN; }

   //
   //
   //
   public double emptyWeight(){ return Double.NaN; }

   //
   //
   //
   //
   public double holdsTolerance(){ return Double.NaN; }

   //
   //
   //
   public void initialize(String file){
      try{
         this._lmdFeeder = new LaunchMechanismDataFeeder();
         this._lmdFeeder.initialize(file);
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = null;
         ht = read.readRocketInfo();
         this.setEmptyWeight(ht);
         this.setLoadedWeight(ht);
      }
      catch(IOException ioe){ ioe.printStackTrace(); }
   }

   //
   //
   //
   public double loadedWeight(){ return Double.NaN; }

   //
   //
   //
   public int numberOfHolds(){ return -1; }

   //
   //
   //
   public int numberOfStages(){ return -1; }

   //
   //
   //
   public double platformTolerance(){ return Double.NaN; }

   //
   //
   //
   public void setStateSubstate(LaunchSimulatorStateSubstate cond){
      try{
         this._cond = cond;
         this._lmdFeeder.setStateSubstate(cond);
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   public double weight(){
      return Double.NaN;
   }
}
//////////////////////////////////////////////////////////////////////
