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

public class LaunchMechanismDataFeeder implements DataFeeder{
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

   private double _emptyWeight;
   private int    _holds;
   private double _holdsTolerance;
   private double _loadedWeight;
   private double _measuredWeight;
   private double _platformTolerance;
   private Random _random;

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

      _emptyWeight       = Double.NaN;
      _holds             = 0;
      _holdsTolerance    = Double.NaN;
      _loadedWeight      = Double.NaN;
      _measuredWeight    = Double.NaN;
      _platformTolerance = Double.NaN;
      _random            = null;
   };
   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public LaunchMechanismDataFeeder(){
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
   public double holdsTolerance(){ return Double.NaN; }

   //
   //
   //
   public void initialize(String file){
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = null;
         ht = read.readRocketInfo();
         this.setEmptyWeight(ht);
         this.setLoadedWeith(ht);
         ht = read.readLaunchingMechanismInfo();
         this.setHolds(ht);
         this.setTolerance(ht);
         this.setHoldsTolerance(ht);
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
   public int numberOfHolds(){ return 0; }

   //
   //
   //
   public int numberOfStage(){ return 0; }

   //
   //
   //
   public double platformTolerance(){ return Double.NaN; }

   //
   //
   //
   public double weight(LaunchSimulatorStateSubstate cond){
      return Double.NaN;
   }
}
//////////////////////////////////////////////////////////////////////
