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
   private LaunchingMechanismData   _mechData;
   private RocketData               _rocketData;

   //Set Data
   private LaunchStateSubstate _cond;
   private Object              _obj;
   private Random              _random;
   private Thread              _rt0;
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
      //_angleOfHolds              = -1;
      //_emptyWeight               = Double.NaN;
      //_holdsTolerance            = Double.NaN;
      //_loadedWeight              = Double.NaN;
      _mechData                  = null;
      //_numberOfHolds             = 0;
      _obj                       = null;
      //_platformTolerance         = Double.NaN;
      _random                    = null;
      _rocketData                = null;
      //_stages                    = 0;
      _rt0                       = null;
      //Calculated--these two NO LONGER NEEDED!!!
      //_holdAngle                 = Double.NaN;
      //_weight                    = Double.NaN;
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
         List<MechanismSupportData> l = null;
         int    m  = -1;       //Model
         int    nh = -1;         //Number of Holds
         double ha = Double.NaN; //Holds Angle
         double ho = Double.NaN; //Holds Tolerance
         double tt = Double.NaN; //Total Tolerance
         try{m = Integer.parseInt(ht.get("model"));}
         catch(NumberFormatException npe){ m = -1;}
         try{nh = Integer.parseInt(ht.get("number_of_holds"));}
         catch(NumberFormatException nfe){nh = -1;}
         try{ha = Double.parseDouble(ht.get("angle_of_holds"));}
         catch(NumberFormatException nfe){ha = Double.NaN;}
         try{ho = Double.parseDouble(ht.get("holds_tolerance"));}
         catch(NumberFormatException nfe){ho = Double.NaN;}
         try{tt = Double.parseDouble(ht.get("total_tolerance"));}
         catch(NumberFormatException nfe){tt = Double.NaN;}
         l = new LinkedList<MechanismSupportData>();
         for(int i = 0; i < nh; ++i){
            MechanismSupportData msd = null;
            msd = new GenericMechanismSupportData(ha,   //Hold Angle
                                                  null, //Error
                                                  null, //Force Vec
                                                  i,    //ID
                                                  false,//error
                                                  Double.NaN,//Force
                                                  ho);  //holds tol
            l.add(msd);
         }
         LaunchingMechanismData mech = null;
         mech = new GenericLaunchingMechanismData(
                                         null,       //error
                                         nh,         //number of holds
                                         false,      //Is Error
                                         Double.NaN, //Calc Weight
                                         m,          //model
                                         this._cond, //State/Substate
                                         tt,         //Total Tolerance
                                         l);         //Mech Supp List
         this._mechData = mech;
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
         double ew = Double.NaN;  //Empty  Weight
         double lw = Double.NaN;  //Loaded Weight
         int    ns = -1;          //Number of Stages
         String m  = null;        //Model
         double t  = Double.NaN;  //Tolerance
         try{ew = Double.parseDouble(ht.get("empty_weight"));}
         catch(NumberFormatException nfe){ew = Double.NaN;}
         try{lw = Double.parseDouble(ht.get("loaded_weight"));}
         catch(NumberFormatException nfe){lw = Double.NaN;}
         try{ns = Integer.parseInt(ht.get("stages"));}
         catch(NumberFormatException nfe){ns = -1;}
         m = ht.get("model");
         try{Double.parseDouble(ht.get("tolerance"));}
         catch(NumberFormatException nfe){t = Double.NaN; }
         //Try to set up everything...
         this._rocketData = new GenericRocketData(m, //model
                                                 -1, //current stage
                                                  ns,//No. of Stages
                                                  ew,//empty
                                                  lw,//loaded
                                                  Double.NaN,//calc W
                                                  false,//isError
                                                  null,//Error
                                                  null,//stages List
                                                  t);//Tolerance
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
   private void setMechanism(){
      double scale = Double.NaN;
      int min   = -1; int max   = -1;
      int value = -1; int  m    = -1; //Model
      int nh       = -1;         //Number of holds
      double  ah   = Double.NaN; //Angle of Holds
      double  ht   = Double.NaN; //Holds Tolerance
      double  tt   = Double.NaN; //Total Tolerance
      
      List<MechanismSupportData> l = null; //Mechanism Support Data
      List<MechanismSupportData> t = null;

      try{
         //Set the Mechanism Data
         synchronized(this){
            l  = new LinkedList<MechanismSupportData>();
            m  = this._mechData.model();
            nh = this._mechData.holds();
            tt = this._mechData.tolerance();
            //Grab the supports!!!
            t = this._mechData.supportData();
            Iterator<MechanismSupportData> it = t.iterator();
            while(it.hasNext()){
               int id = it.next().id();
               ah     = it.next().angle();
               if(this._cond.state() == INIT){
                  scale = 0.01;
               }
               min   = (int)(ah*(1-scale));
               max   = (int)(ah*(1+scale));
               value = this._random.nextInt(max - min +1) + min; 
               ah    = (double)value;
               MechanismSupportData msd = null;
               msd = new GenericMechanismSupportData(ah,
                                                     null,
                                                     null,
                                                     id,
                                                     false,
                                                     Double.NaN,
                                                     ht);
               l.add(msd);
            }
         }
      }
      catch(NullPointerException npe){
         m = -1; nh = -1; ah = Double.NaN; ht = Double.NaN;
         tt = Double.NaN; l  = null;
      }
      finally{
         synchronized(this._obj){
            LaunchingMechanismData md = null;
            md = new GenericLaunchingMechanismData(
                                       null,      //error
                                       nh,        //number of holds
                                       false,     //Is Error
                                       Double.NaN,//Calc Weight
                                       m,         //model
                                       this._cond,//State/Substate
                                       tt,        //total tolerance
                                       l);        //Mech Supp List
            this._mechData = md;
         }
      }
   }

   /*Separate out the components...definitely easier and a task I
    * have started to undertake
   //
   //
   //
   private void setMechanismSupports(){}
   */

   //
   //
   //
   private void setRocket(){
      double scale = Double.NaN;
      int min = -1;int max   = -1;int value = -1; String m = "";
      int st  = -1;double ew = Double.NaN;double lw = Double.NaN;
      double to = Double.NaN;double weight= Double.NaN;
      try{
         //set the rocket weight as needed
         synchronized(this._obj){
            m  = this._rocketData.model();
            st = this._rocketData.numberOfStages();
            ew = this._rocketData.emptyWeight();
            lw = this._rocketData.loadedWeight();
            to = this._rocketData.tolerance();
            if(this._cond.state() == INIT){
               scale = 0.01;
               min   = (int)(ew*(1-scale));
               max   = (int)(ew*(1+scale));
            }
            value  = (this._random.nextInt(max - min +1) + min);
            weight = (double)value;
         }
      }
      catch(NullPointerException npe){
         m  = ""; st = -1; ew = Double.NaN; lw = Double.NaN;
         value = -1; to = Double.NaN; weight = Double.NaN;
      }
      finally{
         synchronized(this._obj){
            RocketData rd = null;
            //rd = new GenericRocketData(...);
            rd = new GenericRocketData(m,       //model
                                       -1,      //Current Stage
                                       st,      //Number of Stages
                                       ew,      //empty weight
                                       lw,      //loaded weight
                                       weight,  //Calculated weight
                                       false,   //Is Error
                                       null,    //Error
                                       null,    //Stages List
                                       to);     //tolerance
            this._rocketData = rd;
         }
      }
   }

   //
   //
   //
   private void setUpThread(){
      this._rt0 = new Thread(this, "Generic System Data Feeder");
      this._rt0.start();
   }

   ////////////////DataFeeder Interface Implmentation/////////////////
   //
   //
   //
   /*
   public double angleOfHolds(){
      return this._angleOfHolds;
   }
   */

   //
   //
   //
   /*
   public double emptyWeight(){
      return this._emptyWeight;
   }
   */

   //
   //
   //
   /*
   public double holdAngle(){
      synchronized(this._obj){
         return this._holdAngle;
      }
   }
   */

   //
   //
   //
   /*
   public double holdsTolerance(){
      return this._holdsTolerance;
   }
   */

   //
   //
   //
   public void initialize(String file)throws IOException{
      try{
         this.readRocketData(file);
         this.readLaunchingMechanismData(file);
         //this.readTankData(file);
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         throw ioe;
      }
   }

   //
   //
   //
   public LaunchingMechanismData launchMechData(){
      return _mechData;
   }

   //
   //
   //
   /*
   public double loadedWeight(){
      //I am "thinking" the loaded weight and empty weight should
      //be consistent
      return this._loadedWeight;
   }
   */

   //
   //
   //
   /*
   public int numberOfHolds(){
      return this._numberOfHolds;
   }
   */

   //
   //
   //
   /*
   public int numberOfStages(){
      return this._stages;
   }
   */

   //
   //
   //
   /*
   public double platformTolerance(){
      //Should remain consistant...
      return this._platformTolerance;
   }
   */

   //
   //
   //
   public RocketData rocketData(){
      return this._rocketData;
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
   /*
   public double weight(){
      synchronized(this._obj){
         return this._weight;
      }
   }
   */

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
            //this.setMechanismSupports();
            //this.setStage();
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
