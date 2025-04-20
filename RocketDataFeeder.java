//////////////////////////////////////////////////////////////////////
/*
Copyright 2024 Lou Rosas

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
   private double     _emptyWeight;
   private double     _loadedWeight;
   private DataFeeder _lmdFeeder;
   private double     _measuredWeight;
   private Random     _random;
   private int        _stages;

   {
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
         this._lmdFeeder = new LaunchingMechanismDataFeeder();
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
   public double weight(LaunchSimulatorStateSubstate.State state){
      return Double.NaN;
   }
}
//////////////////////////////////////////////////////////////////////
