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

public class GenericSystemDataFeeder implements DataFeeder{
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
   private double              _angleOfHolds;
   private double              _emptyWeight;
   private double              _holdsTolerance;
   private double              _loadedWeight;
   private double              _measuredWeight;
   private int                 _numberOfHolds;
   private double              _platformTolerance;
   private Random              _random;
   private int                 _stages;
   private double              _weight;

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
      
      _cond                      = null;
      _angleOfHolds              = -1;
      _emptyWeight               = Double.NaN;
      _holdsTolerance            = Double.NaN;
      _loadedWeight              = Double.NaN;
      _measuredWeight            = Double.NaN;
      _numberOfHolds             = 0;
      _platformTolerance         = Double.NaN;
      _random                    = null;
      _stages                    = 0;
      _weight                    = Double.NaN;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public GenericSystemDataFeeder(){
      this._random = new Random();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setEmptyWeight(Hashtable<String,String> ht){
      String ew = ht.get("empty_weight");
      try{
         this._emptyWeight = Double.parseDouble(ew);
      }
      catch(NumberFormatExceptions nfe){
         this._emptyWeight = Double.NaN;
      }
   }

   //
   //
   //
   private void setHoldsAngle(Hashtable<String,String> ht){
      String ha = ht.get("angle_of_holds");
      try{
         this._angleOfHolds = Integer.parseInt(ha);
      }
      catch(NumberFormatException nfe){
         this._angleOfHolds = -1;
      }
   }

   //
   //
   //
   private void setHoldsTolerance(Hashtable<String,String> ht){}

   //
   //
   //
   private void setLoadedWeight(Hashtable<String,String> ht){}

   //
   //
   //
   private void setNumberOfHolds(Hashtable<String,String> ht){}

   //
   //
   //
   private void setPlatformTolerance(Hashtable<String,String> ht){}

   //
   //
   //
   private void setStages(Hashtable<String,String> ht){}

   ////////////////DataFeeder Interface Implmentation/////////////////
   //
   //
   //
   public double angleOfHolds(){
      return this._angleOfHolds;
   }

   //
   //
   //
   public double emptyWeight(){
      return this._emptyWeight;
   }

   //
   //
   //
   public double holdsTolerance(){
      return this._holdsTolerance;
   }

   //
   //
   //
   public double initialize(String file){
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = null;
         //Set up the Rocket Data
         ht = read.readRocketInfo();
         this.setEmptyWeight(ht);
         this.setLoadedWeight(ht);
         this.setStages(ht);
         //Set up the Launching Mechanism Data
         ht = read.readLaunchingMechanismInfo();
         this.setHoldsAngle(ht);
         this.setNumberOfHolds(ht);
         this.setPlatformTolerance(ht);
         this.setHoldsTolerance(ht);
      }
      catch(IOException ioe){
         ioe.printStackTrace();
      }
   }
}
//////////////////////////////////////////////////////////////////////
