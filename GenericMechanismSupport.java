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
import rosas.lou.runnables.*;

public class GenericMechanismSupport implements MechanismSupport,
Runnable{
   private LaunchStateSubstate.State INIT       = null;
   private LaunchStateSubstate.State PRELAUNCH  = null;
   private LaunchStateSubstate.State IGNITION   = null;
   private LaunchStateSubstate.State LAUNCH     = null;

   private double              _angle; //In Radians!!!
   private List<ErrorListener> _errorListeners;
   private double              _measuredAngle; //In Radians
   private int                 _id;
   //What to measure periodically
   private double              _measuredForce;
   //Depends on the number of holds
   private double              _armForce;
   private String              _error;
   private boolean             _isError;
   private double              _tolerance;

   private DataFeeder          _feeder;
   private ForceVector         _measuredVector;
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
      _measuredAngle    = Double.NaN;
      _id               = -1;
      _armForce         = Double.NaN;
      _measuredForce    = Double.NaN;
      _error            = null;
      _isError          = false;
      _tolerance        = Double.NaN;
      _measuredVector   = null;
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
            double degHolds = Double.parseDouble(aAoHolds);
            //Convert to Radians
            this._angle = ((Math.PI)/180. * degHolds);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }
      this.initializeForce(holds);
      this.initializeForceVector();
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
   private boolean isError(int state){
      this._isError = false; //Reset everytime...
      //Initialize the Error String every invocation, but only use it
      //upon actual error
      this._error   = new String();
      //mesure everything to make sure within tollerance...
      //If out of tollerance, flag as an error...
      //Start with the Angle Measurements
      if(this._angle!=Double.NaN && this._measuredAngle!=Double.NaN){
         this._isError = this.isAngleError(state);
         if(this._isError){
            this._error += "\nMeasured Angle Error: ";
            this._error += this._measuredAngle + ", Expected:  ";
            this._error += this._angle;
         }
      }
      if((this._measuredForce != Double.NaN) &&
         (this._armForce != Double.NaN)){
         this._isError = this.isForceError(state);
         if(this._isError){
            this._error += "\nMeasured Weight Error:  ";
            this._error += this._measuredForce + ", Expected: ";
            this._error += this._armForce;
         }
      }
      if((this._vector != null)&&(this._measuredVector != null)){
         this._isError = this.isVectorError(state);
         if(this._isError){
            this._error += "\nMeasured Vector Error: ";
            this._error += this._measuredVector + "\n\t\tExpected: ";
            this._error += this._vector;
         }
      }
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
   private MechanismSupportData measure(int state){
      this.measureAngle();
      this.measureArm();
      this.measureForceVector();
      //After measurements, find the error...
      this.isError(state);
      MechanismSupportData data = null;
      data = new GenericMechanismSupportData(this._measuredAngle,
                                             this._error,
                                             this._measuredVector,
                                             this._id,
                                             this._isError,
                                             this._measuredForce);
      return data;
   }

   //
   //
   //
   private void measureAngle(){
      //Make this more complex based on release...for now, just
      //get something working
      try{
         this._measuredAngle = this._feeder.angleOfHolds();
      }
      catch(NullPointerException npe){
         this._measuredAngle = this._angle;
      }
   }

   //
   //
   //
   private void measureForceVector(){
      //Make this more complex based on release...for now, just
      //get something working
      double x = this._vector.x();
      double y = this._vector.y();
      double z = this._vector.z();
      this._measuredVector = new GenericForceVector(x,y,z);
   }

   //Measure the force on the Arm...
   //
   //
   private void measureArm(){
      this._measuredForce = this._armForce;
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
            //this.data(rocketHt, mechHt);
            this.initializeHold(mechHt, rocketHt);
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
      string += "\n"+this._armForce+", "+this._measuredForce;
      string += "\n"+this._isError+", "+this._error;
      string += "\n"+this._tolerance+"\n"+this._vector;
      return string;
   }

   /////////////////Runnable Interface Implementation/////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
