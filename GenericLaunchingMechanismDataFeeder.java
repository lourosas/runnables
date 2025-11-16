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

public class GenericLaunchingMechanismDataFeeder implements
DataFeeder,Runnable{
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

   //Read In
   private LaunchingMechanismData     _initMechData;
   //Calculated
   private LaunchingMechanismData     _calcMechData;

   private LaunchStateSubstate        _stateSubstate;
   private Object                     _obj;
   private Thread                     _t0;

   private boolean                    _toStart;

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

      _initMechData   = null;
      _calcMechData   = null;
      _stateSubstate  = null;
      _obj            = null;
      _t0             = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public GenericLaunchingMechanismDataFeeder(){
      this.setUpThread();
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void initialzeLaunchingMechanismData(String file)
   throws IOException{
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<MechanismSupportData> l = null;
         l = this.initializeMechanismSupportData(file);
         Hashtable<String, String> ht = null;
         ht = read.readLaunchingMechanismInfo();
         int    mod = -1;         //Model
         int    nh  = -1;         //Number of Holds
         double tol = Double.NaN; //Total Tolerance
         try{ mod = Integer.parseInt(ht.get("model")); }
         catch(NumberFormatException nfe){ mod = -1; }
         try{ nh = Integer.parseInt(ht.get("number_of_holds")); }
         catch(NumberFormatException nfe){ nh = -1; }
         try{ tol = Double.parseDouble(ht.get("total_tolerance")); }
         catch(NumberFormatException nfe){ tol = Double.NaN; }
         LaunchingMechanismData mech = null;
         mech = new GenericLaunchingMechanismData(
                                null,      //error
                                nh,        //Number of Holds
                                false,     //Is Error
                                Double.NaN,//Calc Weight
                                mod,       //Model
                                this._stateSubstate,
                                tol,       //Total Tolerance
                                l);        //Mechanism Support List
         this._initMechData = mech;
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         this._initMechData = null;
         throw ioe;
      }
   }

   //
   //
   //
   private List<MechanismSupportData>
   initializeMechanismSupportData(String file) throws IOException{
      List<MechanismSupportData> list = null;
      list = new LinkedList<MechanismSupportData>();
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String, String> ht = null;
         ht = read.readLaunchingMechanismInfo();
         int    nh  = -1;         //Number of Holds
         double tol = Double.NaN; //Holds Tolerance
         try{
            nh = Integer.parseInt(ht.get("number_of_holds"));
         }
         catch(NumberFormatException nfe){
            nh = -1;
         }
         try{
            tol = Double.parseDouble(ht.get("holds_tolerance"));
         }
         catch(NumberFormatException nfe){
            tol = Double.NaN;
         }
         for(int i = 0; i < nh; ++i){
            MechanismSupportData msd = null;
            msd = new GenericMechanismSupportData(ha, //Hold Angle
                                           null,  //Error
                                           null,  //Force Vector
                                           i,     //ID
                                           false, //Is Error
                                           Double.NaN, //Meas Force
                                           tol   //Holds Tolerance
                                           );
            list.add(msd);
         }
      }
      catch(IOException ioe){
         list = null;
         ioe.printStackTrace();
         throw ioe;
      }
      finally{
         return list;
      }
   }

   //
   //
   //
   private void measureMechanismData(){
      double scale = Double.NaN;
      int min    = -1; int max    = -1;
      int mod    = -1; //Model
      int nh     = -1; //Number of Holds
      double tol = Double.NaN; //total tolerance

      //Uused to collect the Mechanism Support data
      List<MechanismSupportData> list = null;
      List<MechanismSupportData> temp = null;
      try{
         list = this.measureMechanismSupports();
         mod  = this._initMechData.model();
         nh   = this._initMechData.holds();
         tol  = this._initMechData.tolerance();
      }
      catch(NullPointerException npe){
         scale = Double.NaN; max = -1; min = -1;
         mod   = -1; nh = -1; tol = Double.NaN;
      }
      finally{
         synchronized(this._obj){
         }
      }
   }

   //
   //
   //
   private List<MechanismSupportData> measureMechanismSupports(){
      //Temporary
      return null;
   }

   //
   //
   //
   private void setUpThread(){
      this._obj = new Object();
      this._t0  = new Thread(this);
      this._t0.start();
   }

   //////////////////////DataFeeder Implementation////////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      try{
         this.initializeLaunchingMechanismData(file);
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         throw ioe;
      }
   }

   //
   //
   //
   public Object monitor(){
      //Temp for now...
      return null;
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate statesubstate){
      this._stateSubstate = stateSubstate;
   }

   ///////////////////Runnable Interface Implementation///////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            if(this._stateSubstate != null){
               //Measure all the Data needed
               //Measure all the Launching Mechanism Data
               this.measureMechanismData();
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
