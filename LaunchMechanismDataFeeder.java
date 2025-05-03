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
   private LaunchStateSubstate.State INIT             = null;
   private LaunchStateSubstate.State PREL             = null;
   private LaunchStateSubstate.State IGNI             = null;
   private LaunchStateSubstate.State LAUN             = null;
   private LaunchStateSubstate.State ASCE             = null;
   private LaunchStateSubstate.PreLaunchSubstate SET  = null;
   private LaunchStateSubstate.PreLaunchSubstate CONT = null;
   private LaunchStateSubstate.PreLaunchSubstate FUEL = null;
   private LaunchStateSubstate.PreLaunchSubstate HOLD = null;
   private LaunchStateSubstate.IgnitionSubstate  IGN  = null;
   private LaunchStateSubstate.IgnitionSubstate  BUP  = null;
   private LaunchStateSubstate.AscentSubstate    STG  = null;
   private LaunchStateSubstate.AscentSubstate    IGNE = null; 

   private LaunchStateSubstate _cond;

   private double _emptyWeight;
   private int    _holds;
   private double _holdsTolerance;
   private double _loadedWeight;
   private double _measuredWeight;
   private double _platformTolerance;
   private Random _random;

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

      _cond              = null;
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
   private void setHolds(Hashtable<String,String> ht){
      String hlds = ht.get("number_of_holds");
      try{
         this._holds = Integer.parseInt(hlds);
      }
      catch(NumberFormatException nfe){
         this._holds = 0;
      }
   }

   //
   //
   //
   private void setHoldsTolerance(Hashtable<String,String> ht){
      String htol = ht.get("holds_tolerance");
      try{
         this._holdsTolerance = Double.parseDouble(htol);
      }
      catch(NumberFormatException nfe){
         this._holdsTolerance = Double.NaN;
      }
   }

   //
   //
   //
   private void setTolerance(Hashtable<String,String> ht){
      String tol = ht.get("total_tolerance");
      try{
         this._platformTolerance = Double.parseDouble(tol);
      }
      catch(NumberFormatException nfe){
         this._platformTolerance = Double.NaN;
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
         this.setLoadedWeight(ht);
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
   public int numberOfStages(){ return 0; }

   //
   //
   //
   public double platformTolerance(){ return Double.NaN; }
   
   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate cond){
      this._cond = cond;
   }

   //
   //
   //
   public double weight(){
      System.out.println(this._cond);
      return Double.NaN;
   }
}
//////////////////////////////////////////////////////////////////////
