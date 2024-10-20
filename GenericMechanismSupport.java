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

public class GenericMechanismSupport implements MechanismSupport{
   private double         _angle; //In Radians!!!
   private double         _measuredAngle; //In Radians
   private int            _id;
   //What to measure periodically
   private double         _measuredForce;
   //Depends on the number of holds
   private double         _armForce;
   private String         _error;
   private boolean        _isError;
   private double         _tolerance;
   private ForceVector    _measuredVector;
   private ForceVector    _vector;

   {
      _angle            = Double.NaN;
      _measuredAngle    = Double.NaN;
      _id               = -1;
      _armForce         = Double.NaN;
      _measuredForce    = Double.NaN;
      _error            = null;
      _isError          = false;
      _tolerance        = Double.NaN;
      _measuredVector   = null;
      _vector           = null;
   };

   ////////////////////////Contructors////////////////////////////////
   //Pretty much all you will need...
   //
   //
   public GenericMechanismSupport(int id){
      this._id = id;
   }

   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private void data(Hashtable<String,String> rocket,
                Hashtable<String,String> mech){
      int holds = -1;
      if(mech.containsKey("number_of_holds")){
         try{
            String sHolds = mech.get("number_of_holds");
            holds = Integer.parseInt(sHolds);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }
      if(mech.containsKey("angle_of_holds")){
         try{
            String sAoHolds = mech.get("angle_of_holds");
            double degHolds = Double.parseDouble(sAoHolds);
            //Convert to Radians
            this._angle = ((Math.PI)/180.0 * degHolds);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }
      if(mech.containsKey("holds_tollerance")){
         try{
            String sToll = mech.get("holds_tollerance");
            this._tolerance = Double.parseDouble(sToll);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }
      if(rocket.containsKey("loaded_weight")){
         try{
            String sWeight = rocket.get("loaded_weight");
            double weight = Double.parseDouble(sWeight);
            weight /= holds;
            weight /= Math.sin(this._angle);
            //This is the weight that is calculated based on
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
   private boolean isAngleError(){
      boolean isError = false;
      double lim  = 1. - this._tolerance;
      double edge = this._angle * lim;
      double ll   = this._angle - edge;
      double ul   = this._angle + edge;
      if(this._measuredAngle < ll || this._measuredAngle > ul){
         isError = true;
      }
      return isError;
   }

   //Set the _isError boolean
   //call to determine/set the _error if _isError is true
   //
   private boolean isError(){
      this._isError = false; //Reset everytime...
      //Initialize the Error String every invocation, but only use it
      //upon actual error
      this._error   = new String();
      //mesure everything to make sure within tollerance...
      //If out of tollerance, flag as an error...
      //Start with the Angle Measurements
      if(this._angle!=Double.NaN && this._measuredAngle!=Double.NaN){
         this._isError = this.isAngleError();
         if(this._isError){
            this._error += "\nMeasured Angle Error: ";
            this._error += this._measuredAngle + ", Expected:  ";
            this._error += this._angle;
         }
      }
      if((this._measuredForce != Double.NaN) &&
         (this._armForce != Double.NaN)){
         this._isError = this.isForceError();
         if(this._isError){
            this._error += "\nMeasured Weight Error:  ";
            this._error += this._measuredForce + ", Expected: ";
            this._error += this._armForce;
         }
      }
      if((this._vector != null)&&(this._measuredVector != null)){
         this._isError = this.isVectorError();
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
   private boolean isVectorError(){
      double edge     = Double.NaN;
      double ul       = Double.NaN;
      double ll       = Double.NaN;
      boolean isError = false;
      double  lim     = 1. - this._tolerance;
      //first, check the i-hat direction
      //Will need to change for the different parts...
      edge = this._vector.x() * lim;
      if(this._vector.x() < 0.){
         edge = this._vector.x() * -lim;
      }
      ll   = this._vector.x() - edge;
      ul   = this._vector.x() + edge;
      if(this._measuredVector.x()<ll || this._measuredVector.x()>ul){
         isError = true;
      }
      edge = this._vector.y() * lim;
      if(this._vector.y() < 0.){
         edge = this._vector.y() * -lim;
      }
      ll   = this._vector.y() - edge;
      ul   = this._vector.y() + edge;
      if(this._measuredVector.y()<ll || this._measuredVector.y()>ul){
         isError = true;
      }
      edge = this._vector.z() * lim;
      if(this._vector.z() < 0.){
         edge = this._vector.z() * -lim;
      }
      ll   = this._vector.z() - edge;
      ul   = this._vector.z() + edge;
      if(this._measuredVector.z()<ll || this._measuredVector.z()>ul){
         isError = true;
      }
      edge        = this._vector.magnitude() * lim;
      if(this._vector.magnitude() < 0.){
         edge = this._vector.magnitude() * -lim;
      }
      ll          = this._vector.magnitude() - edge;
      ul          = this._vector.magnitude() + edge;
      double mMag = this._measuredVector.magnitude();
      if((mMag < ll) || (mMag > ul)){
         isError = true;
      }
      return isError;
   }

   //This is the measured downward weight, not related to the
   //ForceVector, but will eventually be used for comparison...
   //
   //
   private boolean isForceError(){
      boolean isError = false;
      double lim  = 1. - this._tolerance;
      double edge = this._armForce * lim;
      double ll   = this._armForce - edge;
      double ul   = this._armForce + edge;
      if(this._measuredForce < ll || this._measuredForce > ul){
         isError = true;
      }
      return isError;
   }

   //
   //
   //
   private MechanismSupportData measure(){
      this.measureAngle();
      this.measureArm();
      this.measureForceVector();
      //After measurements, find the error...
      this.isError();
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
      this._measuredAngle = this._angle;
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

   //////////////MechanismSupport Interface Implementation////////////
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
            this.data(rocketHt, mechHt);
            this.initializeForceVector();
         }
         catch(IOException ioe){}
      }
   }

   //
   //
   //
   public MechanismSupportData monitorPrelaunch(){
      return(this.measure());
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
   public String toString(){
      String string = this.getClass().getName()+" : "+this.id();
      string += "\n"+this._angle;
      string += "\n"+this._armForce+", "+this._measuredForce;
      string += "\n"+this._isError+", "+this._error;
      string += "\n"+this._tolerance+"\n"+this._vector;
      return string;
   }
}
//////////////////////////////////////////////////////////////////////
