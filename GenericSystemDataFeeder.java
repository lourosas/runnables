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

public class GenericSystemDataFeeder implements DataFeeder,Runnable{
   public class InnerRocketData{
      private String model        = null;
      private int    stages       = 0;
      private double emptyWeight  = Double.NaN;
      private double loadedWeight = Double.NaN;
      private double measWeight   = Double.NaN;//Measured Weight
      private double tolerance    = Double.NaN;
      public InnerRocketData
      (
         String m,
         String st,
         String ew,
         String lw,
         String to
      ){
         this(m,st,ew,lw,to,Double.NaN);
      }
      public InnerRocketData
      (
         String model,
         String stages,
         String emptyWeight,
         String loadedWeight,
         String tolerance,
         double measuredWeight

      ){
         this.model(model);
         this.stages(stages);
         this.emptyWeight(emptyWeight);
         this.loadedWeight(loadedWeight);
         this.tolerance(tolerance);
         this.measuredWeight(measuredWeight);
      }
      public double emptyWeight(){return this.emptyWeight;}
      public double loadedWeight(){return this.loadedWeight;}
      public double measuredWeight(){return this.measuredWeight;}
      public String model(){return this.model;}
      public double tolerance(){return this.tolerance;}
      public int stages(){return this.stages;}
      private void model(String m){ this.model = m; }
      private void stages(String st){
         try{
            this.stages = Integer.parseInt(st);
         }
         catch(NumberFormatException nfe){this.stages = -1;}
      }
      private void emptyWeight(String ew){
         try{
            this.emptyWeight = Double.parseDouble(ew);
         }
         catch(NumberFormatException nfe){
            this.emptyWeight = Double.NaN;
         }
      }
      private void loadedWeight(String lw){
         try{
            this.loadedWeight = Double.parseDouble(lw);
         }
         catch(NumberFormatException nfe){
            this.emptyWeight = Double.NaN;
         }
      }
      private void measuredWeight(double mw){
         this.measuredWeight = mw;
      }
      private void tolerance(string t){
         try{
            this.tolerance = Double.parseDouble(t);
         }
         catch(NumberFormatException nfe){
            this.tolerace = Double.NaN;
         }
      }
   }

   public class InnerLaunchingMechanismData{
      private String   model          = null;
      private int      holds          = -1;
      private double   holdsAngle     = Double.NaN;
      private double   holdsTolerance = Double.NaN;
      private double   measuredAngle  = Double.NaN;
      private double   totalTolerance = Double.NaN;

      public InnerLaunchingMechanismData
      (
         String m,
         String a,
         String h,
         String hT,
         String tT
      ){
         this(m,a,h,hT,tT,Double.NaN);
      }
      public InnerLaunchingMechanismData
      (
         String model,
         String angle,
         String holds,
         String hTolerance,
         String tTolerance,
         double measuredAngle
      ){
         this.model(model);
         this.holdsAngle(angle);
         this.holds(holds);
         this.holdsTolerance(hTolerance);
         this.totalTolerance(tTolerance);
         this.measuredAngle(measuredAngle);
      }
      public int holds(){return this.holds;}
      public double holdsAngle(){return this.holdsAngle;}
      public double holdsTolerance{return this.holdsTolerance;}
      public double measuredAngle(){return this.measuredAngle;}
      public String model(){return this.model;}
      public double totalTolerance(){return this.totalTolerance;}
      private void holds(String holds){
         try{
            this.holds = Integer.parseInt(holds);
         }
         catch(NumberFormatException nfe){ this.holds = -1;}
      }
      private void holdsAngle(String angle){
         try{
            this.holdsAngle = Double.parseDouble(angle);
         }
         catch(NumberFormatException nfe){this.holdsAngle=Double.NaN;}
      }
      private void holdsTolerance(String ht){
         try{
            this.holdsTolerance = Double.parseDouble(ht);
         }
         catch(NumberFormatException nfe){
            this.holdsTolerance = Double.NaN;
         }
      }
      private void measuredAngle(Double ma){
         this.measuredAngle = ma;
      }
      private void model(String model){
         this.model = model;
      }
      private void totalTolerance(String tt){
         try{
            this.totalTolerance = Double.parseDouble(tt);
         }
         catch(NumberFormatException nfe){
            this.totalTolerance = Double.NaN;
         }
      }
   }

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

   //dont need if have weight!
   //Measured Data
   private double                   _weight;
   private double                   _holdAngle;

   //Set Data
   private LaunchStateSubstate _cond;
   private Object              _obj;
   private Random              _random;
   private Thread              _rt0;

   //Inner Classes
   private InnerRocketData             _rocketData;
   private InnerLaunchingMechanismData _mechData;

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
      _mechData                  = null;
      _numberOfHolds             = 0;
      _obj                       = null;
      _platformTolerance         = Double.NaN;
      _random                    = null;
      _rocketData                = null;
      _stages                    = 0;
      _rt0                       = null;
      //Calculated--these two NO LONGER NEEDED!!!
      _holdAngle                 = Double.NaN;
      _weight                    = Double.NaN;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public GenericSystemDataFeeder(){
      this._random = new Random();
      //Grab the Monitor for the Threads...
      this._obj    = new Object();
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //KEEP
   //
   //
   private void readLaunchingMechanismData(String file)
   throws IOException{
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = null;
         ht = read.readLaunchingMechanismInfo();
         String m  = ht.get("model");
         String ha = ht.get("angle_of_holds");
         String nh = ht.get("number_of_holds");
         String ho = ht.get("holds_tolerance");
         String tt = ht.get("total_tolerance");
         this._mechData=new InnerLaunchingMechanismData(m,ha,nh,ho,tt);
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         throw ioe;
      }
   }

   //KEEP
   //
   //
   private void readRocketData(String file)throws IOException{
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = null;
         ht = read.readRocketInfo();
         //Now try to set everything up...
         String ew = ht.get("empty_weight");
         String lw = ht.get("loaded wieght");
         String st = ht.get("stages");
         String m  = ht.get("model");
         String t  = ht.get("tolerance");
         this._rocketData = new InnerRocketData(m,st,ew,lw,t);
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         throw ioe;
      }
   }

   //KEEP
   //
   //
   private void readTankData(String file){}

   //
   //
   //
   private void setAngle(){
      double scale = Double.NaN;
      int    min   = -1;
      int    max   = -1;
      int    value = -1;
      //Set Angle
      try{
         synchronized(this._obj){
            if(this._cond.state() == INIT){
               //scale = 0.025;
               scale = 0.01;
               min   = (int)(this.angleOfHolds()*(1-scale));
               max   = (int)(this.angleOfHolds()*(1+scale));
               value = this._random.nextInt(max - min + 1) + min;
            }
            this._holdAngle = value;
         }
      }
      catch(NullPointerException npe){
         synchronized(this._obj){
            this._holdAngle = Double.NaN;
         }
      }
   }

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
   private void setHoldsTolerance(Hashtable<String,String> ht){
      String hldt = ht.get("holds_tolerance");
      try{
         this._holdsTolerance = Double.parseDouble(hldt);
      }
      catch(NumberFormatException nfe){
         this._holdsTolerance = Double.NaN;
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

   //
   //
   //
   private void setMechanism(){
      double scale = Double.NaN;
      int    min   = -1;
      int    max   = -1;
      int    value = -1;
      double angle = Double.NaN;
      String m  = "";
      String nh = "";
      String ah = "";
      String ht = "";
      String tt = "";
      try{
         //Set the Mechanism Data
         synchronized(this){
            m  = this._mechData.model();
            nh = this._mechData.holds() + "";
            ah = this._mechData.holdsAngle() + "";
            ht = this._mechData.holdsTolerance() + "";
            tt = this._mechData.totalTolerance() + "";
            if(this._cond.state() == INIT){
               scale        = 0.01;
               double angle = this._mechData.holdsAngle();
               min   = (int)(angle*(1-scale));
               max   = (int)(angle*(1+scale));
               value = this._random.nextInt(max - min + 1) + min;
            }
            angle = (double)value;
         }
      }
      catch(NullPointerException npe){
         m      = "";
         nh     = "";
         ah     = "";
         ht     = "";
         tt     = "";
         angle  = Double.NaN;
      }
      finally{
         synchronized(this._obj){
            InnerLaunchingMechanismData md = null;
            md = new InnerLaunchingMechansismData(m,nh,ah,ht,tt,angle);
            this._mechData = md;
         }
      }
   }

   //
   //
   //
   private void setNumberOfHolds(Hashtable<String,String> ht){
      String nh = ht.get("number_of_holds");
      try{
         this._numberOfHolds = Integer.parseInt(nh);
      }
      catch(NumberFormatException nfe){
         this._numberOfHolds = -1;
      }
   }

   //
   //
   //
   private void setPlatformTolerance(Hashtable<String,String> ht){
      String pt = ht.get("total_tolerance");
      try{
         this._platformTolerance = Double.parseDouble(pt);
      }
      catch(NumberFormatException nfe){
         this._platformTolerance = Double.NaN;
      }
   }

   //
   //
   //
   private void setRocket(){
      double scale = Double.NaN;
      int    min   = -1;
      int    max   = -1;
      int    value = -1;
      double weight= Double.NaN;
      String m  = "";
      String st = "";
      String ew = "";
      String lw = "";
      String to = "";
      try{
         //set the rocket weight as needed
         synchronized(this._obj){
            m  = this._rocketData.model();
            st = this._rocketData.stages() + "";
            ew = this._rocketData.emptyWeight() + "";
            lw = this._rocketData.loadedWeight() + "";
            to = this._rocketData.tolerance() + "";
            if(this._cond.state() == INIT){
               scale = 0.01;
               double emptyWeight = this._rocketData.emptyWeight();
               min   = (int)(emptyWeight*(1-scale));
               max   = (int)(emptyWeight*(1+scale));
               value = (this._random.nextInt(max - min +1) + min;
            }
            weight = (double)value;
         }
      }
      catch(NullPointerException npe){
         m      = "";
         st     = "";
         ew     = "";
         lw     = "";
         to     = "";
         weight = Double.NaN;
      }
      finally{
         synchronized(this._obj){
            InnerRocketData rd = null;
            rd = new InnerRocketData(m,st,ew,lw,to,weight);
            this._rocketData = rd;
         }
      }
   }

   //
   //
   //
   private void setStages(Hashtable<String,String> ht){
      String st = ht.get("stages");
      try{
         this._stages = Integer.parseInt(st);
      }
      catch(NumberFormatException nfe){
         this._stages = -1;
      }
   }

   private void setUpThread(){
      this._rt0 = new Thread(this, "Generic System Data Feeder");
      this._rt0.start();
   }

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
   public double holdAngle(){
      synchronized(this._obj){
         return this._holdAngle;
      }
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
   public void initialize(String file)throws IOException{
      try{
         this.readRocketData(file);
         this.readLaunchingMechanismData(file);
         /*
         this.readTankData(file);
         */
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         throw ioe;
      }
   }

   //
   //
   //
   public double loadedWeight(){
      //I am "thinking" the loaded weight and empty weight should
      //be consistent
      return this._loadedWeight;
   }

   //
   //
   //
   public int numberOfHolds(){
      return this._numberOfHolds;
   }

   //
   //
   //
   public int numberOfStages(){
      return this._stages;
   }

   //
   //
   //
   public double platformTolerance(){
      //Should remain consistant...
      return this._platformTolerance;
   }

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
      synchronized(this._obj){
         return this._weight;
      }
   }

   //
   //
   //
   public String toString(){
      String s = new String(""+this.getClass().getName());
      /*  Definitely going to NEED TO CHANGE...
      s += "\nHolds Angle:       " + this.angleOfHolds();
      s += "\nEmpty Weight:      " + this.emptyWeight();
      s += "\nHolds Tolerance:   " + this.holdsTolerance();
      s += "\nLoaded Weight:     " + this.loadedWeight();
      s += "\nHolds:             " + this.numberOfHolds();
      s += "\nStages:            " + this.numberOfStages();
      s += "\nPlatform Tolerance " + this.platformTolerance();
      s += "\nWeight:            " + this.weight();
      */
      return s;
   }

   //////////////////Runnable Interface Implementation////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            //Do this right!!!
            this.setRocket();
            //this.setMechanism();
            //this.setStage();
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
