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
import java.math.*;
import rosas.lou.runnables.*;

public class GenericMechanismSupport implements MechanismSupport,
Runnable{
   private LaunchStateSubstate.State INIT       = null;
   private LaunchStateSubstate.State PRELAUNCH  = null;
   private LaunchStateSubstate.State IGNITION   = null;
   private LaunchStateSubstate.State LAUNCH     = null;

   private double              _angle; //In Radians!!!
   //Depends on the number of holds
   private double              _armForce;
   private List<ErrorListener> _errorListeners;
   private int                 _id;
   private String              _error;
   private boolean             _isError;
   private boolean             _kill;
   private boolean             _start;
   private double              _tolerance;

   private DataFeeder          _feeder;
   private ForceVector         _vector;
   private LaunchStateSubstate _state;
   private MechanismSupportData _supportData;
   private Thread              _rt0;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _angle            = Double.NaN;
      _errorListeners   = null;
      _id               = -1;
      _armForce         = Double.NaN;
      _error            = null;
      _isError          = false;
      _kill             = false;
      _start            = false;
      _tolerance        = Double.NaN;
      _vector           = null;
      _feeder           = null;
      _state            = null;
      _rt0              = null;
      _supportData      = null;
   };

   ////////////////////////Contructors////////////////////////////////
   //Pretty much all you will need...
   //
   //
   public GenericMechanismSupport(int id){
      this._id = id;
      this.setUpThread();
   }

   ////////////////////////Private Methods////////////////////////////
   //Grab the force on the Arm
   //
   //
   private void initializeForce
   (
      int                      holds,
      Hashtable<String,String> rocket
   ){
      if(rocket.containsKey("loaded_weight")){
         try{
            String sWeight = rocket.get("loaded_weight");
            double weight  = Double.parseDouble(sWeight);
            weight /= holds;
            weight /= Math.sin(this._angle);
            //This is the weight that is calculated based on the
            //initialization data...
            this._armForce = weight;
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }
   }

   //Initialze the ForceVector
   //
   //
   private void initializeForceVector(){
      double x = Double.NaN;
      double y = Double.NaN;
      double z = Double.NaN;
      if(this.id() == 0){
         x = this._armForce*Math.cos(this._angle);
         y = 0.;
      }
      else if(this.id() == 1){
         x = 0.;
         y = this._armForce*Math.cos(this._angle);
      }
      else if(this.id() == 2){
         x = (-1.)*this._armForce*Math.cos(this._angle);
         y = 0;
      }
      else if(this.id() == 3){
         x = 0.;
         y = (-1.)*this._armForce*Math.cos(this._angle);
      }
      else{}
      z = (-1.)*this._armForce*Math.sin(this._angle);
      this._vector = new GenericForceVector(x,y,z);
   }

   //
   //
   //
   private void initializeHold
   (
      Hashtable<String,String> mech,
      Hashtable<String,String> rocket
   ){
      int holds = this.initializeHoldData(mech);
      this.initializeForce(holds, rocket);
      this.initializeForceVector();
   }

   //
   //
   //
   private int initializeHoldData(Hashtable<String,String> mech){
      int holds = -1;
      if(mech.containsKey("number_of_holds")){
         try{
            String sHolds = mech.get("number_of_holds");
            holds         = Integer.parseInt(sHolds);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }
      if(mech.containsKey("angle_of_holds")){
         try{
            String sAoHolds = mech.get("angle_of_holds");
            double degHolds = Double.parseDouble(sAoHolds);
            //Convert to Radians
            this._angle = ((Math.PI)/180. * degHolds);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }     
      return holds;
   }

   //
   //
   //
   private boolean isAngleError(int state){
      boolean isError = false;
      double lim  = 1. - this._tolerance;
      double edge = this._angle * lim;
      double ll   = this._angle - edge;
      double ul   = this._angle + edge;
      //Account for the State to determine errors
      /*
      if(state  == PRELAUNCH){
         if(this._measuredAngle < ll || this._measuredAngle > ul){
            isError = true;
         }
      }
      */
      return isError;
   }

   //Set the _isError boolean
   //call to determine/set the _error if _isError is true
   //
   private boolean isError(){
      this._isError = false; //Reset everytime...
      this._error   = null;
      return this._isError;
   }

   //Need to measure the Vector Tolerances, in addition to the
   //Magnitude Tollerances in addtion to the Z-direction force vs.
   //the _measuredWeight-->those two need to be in tolerance, as well
   //
   private boolean isVectorError(int state){
      double edge     = Double.NaN;
      double ul       = Double.NaN;
      double ll       = Double.NaN;
      boolean isError = false;
      double  lim     = 1. - this._tolerance;
      //Account for the state to determine errors...
      /*
      if(state == PRELAUNCH){
         //first, check the i-hat direction
         //Will need to change for the different parts...
         edge = this._vector.x() * lim;
         if(this._vector.x() < 0.){
            edge = this._vector.x() * -lim;
         }
         ll = this._vector.x() - edge;
         ul = this._vector.x() + edge;
         if(this._measuredVector.x()<ll||this._measuredVector.x()>ul){
            isError = true;
         }
         edge = this._vector.y() * lim;
         if(this._vector.y() < 0.){
            edge = this._vector.y() * -lim;
         }
         ll = this._vector.y() - edge;
         ul = this._vector.y() + edge;
         if(this._measuredVector.y()<ll||this._measuredVector.y()>ul){
            isError = true;
         }
         edge = this._vector.z() * lim;
         if(this._vector.z() < 0.){
            edge = this._vector.z() * -lim;
         }
         ll = this._vector.z() - edge;
         ul = this._vector.z() + edge;
         if(this._measuredVector.z()<ll||this._measuredVector.z()>ul){
            isError = true;
         }
         edge = this._vector.magnitude() * lim;
         if(this._vector.magnitude() < 0.){
            edge = this._vector.magnitude() * -lim;
         }
         ll = this._vector.magnitude() - edge;
         ul = this._vector.magnitude() + edge;
         double mMag = this._measuredVector.magnitude();
         if((mMag < ll) || (mMag > ul)){
            isError = true;
         }
      }
      */
      return isError;
   }

   //This is the measured downward weight, not related to the
   //ForceVector, but will eventually be used for comparison...
   //
   //
   private boolean isForceError(int state){
      boolean isError = false;
      double lim  = 1. - this._tolerance;
      double edge = this._armForce * lim;
      double ll   = this._armForce - edge;
      double ul   = this._armForce + edge;
      //Account for state to determine errors...
      /*
      if(state == PRELAUNCH){
         if(this._measuredForce < ll || this._measuredForce > ul){
            isError = true;
         }
      }
      */
      return isError;
   }

   //
   //
   //
   private void measureAngle(){
      //Make this more complex based on release...for now, just
      //get something working
      try{
         double degHolds = this._feeder.holdAngle();
         //Convert to Radians
         this._angle = ((Math.PI)/180. * degHolds);
      }
      catch(NullPointerException npe){
         //Measure Directly!!!
         this._angle = this._angle;
      }
   }

   //Measure the force on the Arm...
   //
   //
   private void measureArmForce(){
      try{
         double weight  = this._feeder.weight();
         int    holds   = this._feeder.numberOfHolds();
         weight        /= holds;
         weight        /= Math.sin(this._angle);
         this._armForce = weight;
      }
      catch(NullPointerException npe){
         //Put this in as a stop gap for the time being...
         this._armForce = this._armForce;
      }
      //TBD!!!
   }

   //
   //
   //
   private void measureForceVector(){
      double x = Double.NaN;
      double y = Double.NaN;
      double z = Double.NaN;
      switch(this.id()){
         case 0:
            x = this._armForce*Math.cos(this._angle);
            y = 0.;
            break;
         case 1:
            x = 0.;
            y = this._armForce*Math.cos(this._angle);
            break;
         case 2:
            x = (-1.)*this._armForce*Math.cos(this._angle);
            y = 0;
            break;
         case 3:
            x =  0.;
            y = (-1.)*this._armForce*Math.cos(this._angle);
            break;
         default: ;
      }
      z = (-1.)*this._armForce*Math.sin(this._angle);
      this._vector = new GenericForceVector(x,y,z);
   }

   //
   //
   //
   private void setUpThread(){
      this._rt0 = new Thread(this, "Mechanism Support" + this._id);
      this._rt0.start();
   }

   //////////////MechanismSupport Interface Implementation////////////
   //
   //
   //
   public void addDataFeeder(DataFeeder feeder){
      if(feeder != null){
         this._feeder = feeder;
      }
   }

   //
   //
   //
   public void addErrorListener(ErrorListener e){
      try{
         this._errorListeners.add(e);
      }
      catch(NullPointerException npe){
         this._errorListeners = new LinkedList<ErrorListener>();
         this._errorListeners.add(e);
      }
   }

   //
   //
   //
   public int id(){
      return this._id;
   }

   //
   //
   //
   public void initialize(String file){
      if(file.toUpperCase().contains("INI")){}
      else if(file.toUpperCase().contains("JSON")){
         try{
            LaunchSimulatorJsonFileReader read = null;
            read = new LaunchSimulatorJsonFileReader(file);
            Hashtable<String,String> rocketHt = null;
            rocketHt = read.readRocketInfo();
            Hashtable<String,String> mechHt = null;
            mechHt = read.readLaunchingMechanismInfo();
            this.initializeHold(mechHt, rocketHt);
            this._state=new LaunchStateSubstate(INIT,null,null,null);
            //Once Initialized, can start the monitoring...
            this._start = true;
         }
         catch(IOException ioe){}
      }
   }

   //
   //
   //
   public MechanismSupportData monitorData(){
      return null;
   }
   //
   //
   //
   public MechanismSupportData monitorInitialization(){
      return null;
   }

   //
   //
   //
   public MechanismSupportData monitorPrelaunch(){
      return null;
   }

   //
   //
   //
   public MechanismSupportData monitorIgnition(){
      return null;
   }

   //
   //
   //
   public MechanismSupportData monitorLaunch(){
      return null;
   }

   //
   //
   //
   public MechanismSupportData monitorPostlaunch(){
      return null;
   }

   //
   //
   //
   public String toString(){
      String string = this.getClass().getName()+" : "+this.id();
      string += "\n"+this._angle;
      string += "\n"+this._armForce+", ";
      string += "\n"+this._isError+", "+this._error;
      string += "\n"+this._tolerance+"\n"+this._vector;
      return string;
   }

   /////////////////Runnable Interface Implementation/////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this._start){
               this.measureAngle();
               this.measureArmForce();
               this.measureForceVector();
               this.isError();
               if(this._state.state() == INIT){
                  Thread.sleep(5000);
                  //Thread.sleep(10);
               }
            }
            else{
               //Monitor every 10^-3 seconds
               Thread.sleep(1);
            }
         }
      }
      catch(InterruptedException ie){}
      catch(NullPointerException npe){
         npe.printStackTrace();
         System.exit(0); //Not really needed...better way to implement
      }
   }
}
//////////////////////////////////////////////////////////////////////
