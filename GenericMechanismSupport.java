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

   private List<ErrorListener> _errorListeners;
   private int                 _id;
   private String              _error;
   private boolean             _isError;
   private boolean             _kill;
   private double              _setAngle;
   private double              _setArmForce;
   private ForceVector         _setForceVector;
   private boolean             _start;
   private double              _tolerance;

   private DataFeeder          _feeder;
   private LaunchStateSubstate _state;
   private MechanismSupportData _supportData;
   private Thread              _rt0;
   
   //Derived
   private double              _angle; //In Radians!!!
   //Depends on the number of holds
   private double              _armForce;
   private ForceVector         _vector;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _errorListeners   = null;
      _id               = -1;
      _error            = null;
      _isError          = false;
      _kill             = false;
      _setAngle         = Double.NaN;
      _setArmForce      = Double.NaN;
      _setForceVector   = null;
      _start            = false;
      _tolerance        = Double.NaN;
      _feeder           = null;
      _state            = null;
      _rt0              = null;
      _supportData      = null;

      _angle            = Double.NaN;
      _armForce         = Double.NaN;
      _vector           = null;
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
   //
   //
   //
   private void alertErrorListeners(){
      //System.out.print("GenericMechanismSupport.alertErrorListener()");
      //System.out.println("  "+this._error);
      ErrorEvent e = new ErrorEvent(this, this._error);
      try{
         Iterator<ErrorListener> it = null;
         it = this._errorListeners.iterator();
         while(it.hasNext()){
            it.next().errorOccurred(e);
         }
      }
      catch(NullPointerException npe){
         //Should NEVER get here
         npe.printStackTrace();
      }
   }

   //
   //
   //
   private void initializeHold
   (
      Hashtable<String,String> mech,
      Hashtable<String,String> rocket
   ){
      //Need to make this more fucking robust!!!
      this.setAngle(mech,rocket);
      this.setArmForce(mech,rocket);
      this.setForceVector(mech,rocket);
   }

   //
   //
   //
   private boolean isAngleError(){
      double  edge     = Double.NaN;
      double  ll       = Double.NaN;
      double  ul       = Double.NaN;
      double  lim      = Double.NaN;
      boolean isError  = false;

      boolean measGood = (this._angle != Double.NaN);
      measGood &= (this._armForce != Double.NaN);
      measGood &= (this._vector != null);
      if(measGood){
         lim = 1. - this._tolerance;
         ll  = this._setAngle * this._tolerance;
         ul  = this._setAngle * (2 - this._tolerance);
         if(this._angle < ll || this._angle > ul){
            isError = true;
            this.setError("Angle Error");
         }
      }
      return isError;
   }

   //Set the _isError boolean
   //call to determine/set the _error if _isError is true
   //
   private void isError(){
      this._isError  = false; //Reset everytime...
      this._error    = null;  //Reset everytime...
      this._isError |= this.isAngleError();
      this._isError |= this.isForceError();
      this._isError |= this.isVectorError();
   }

   //This is the measured downward weight, not related to the
   //ForceVector, but will eventually be used for comparison...
   //
   //
   private boolean isForceError(){
      double  edge    = Double.NaN;
      double  ll      = Double.NaN;
      double  ul      = Double.NaN;
      double  lim     = Double.NaN;
      boolean isError = false;

      boolean measGood = (this._angle != Double.NaN);
      measGood &= (this._armForce != Double.NaN);
      measGood &= (this._vector != null);
      if(measGood){
         lim = 1. - this._tolerance;
         ll  = this._setArmForce * this._tolerance;
         ul  = this._setArmForce * (2 - this._tolerance);
         if(this._armForce < ll || this._armForce > ul){
            isError = true;
            this.setError("Force Error");
         }
      }
      return isError;
   }

   //Need to measure the Vector Tolerances, in addition to the
   //Magnitude Tollerances in addtion to the Z-direction force vs.
   //the _measuredWeight-->those two need to be in tolerance, as well
   //
   private boolean isVectorError(){
      double  edge    = Double.NaN;
      double  ul      = Double.NaN;
      double  ll      = Double.NaN;
      double  lim     = Double.NaN;
      boolean isError = false;

      boolean measGood = (this._angle != Double.NaN);
      measGood &= (this._armForce != Double.NaN);
      measGood &= (this._vector != null);
      if(measGood){
         double set = this._setForceVector.x();
         double mea = this._vector.x();
         isError    = this.isVectorError(set,mea);
         if(isError){
            this.setError("Vector Error: x");
         }
         set     = this._setForceVector.y();
         mea     = this._vector.y();
         isError = this.isVectorError(set,mea);
         if(isError){
            this.setError("Vector Error: y");
         } 
         set     = this._setForceVector.z();
         mea     = this._vector.z();
         isError = this.isVectorError(set, mea);
         if(isError){
            this.setError("Vector Error: z");
         }
         set     = this._setForceVector.magnitude();
         mea     = this._vector.magnitude();
         isError = this.isVectorError(set, mea);
         if(isError){
            this.setError("Magnitude Error");
         }
      }
      return isError;
   }

   //
   //
   //
   private boolean isVectorError(double set, double mea){
      double edge      = Double.NaN;
      double ll        = Double.NaN;
      double ul        = Double.NaN;
      double lim       = Double.NaN;
      boolean isError  = false;
      
      lim = 1. - this._tolerance;
      ll  = set * this._tolerance;
      ul  = set * (2 - this._tolerance);
      if(set < 0){
         double temp = ul;
         ul          = ll;
         ll          = temp;
      }
      if(mea < ll || mea > ul){
         isError = true;
      }
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
   }

   //
   //
   //
   private void measureForceVector(){
      double x = Double.NaN;
      double y = Double.NaN;
      double z = Double.NaN;
      this._vector = null;
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

   //Sets the _setAngle property
   //
   //
   private void setAngle
   (
      Hashtable<String,String> mech,
      Hashtable<String,String> rocket
   ){
      if(Double.isNaN(this._tolerance)){
         this.tolerance(mech,rocket);
      }
      if(mech.containsKey("angle_of_holds")){
         try{
            String sAoHolds = mech.get("angle_of_holds");
            double degHolds = Double.parseDouble(sAoHolds);
            //Convert to Radians
            this._setAngle = ((Math.PI)/180.) * degHolds;
         }
         catch(NumberFormatException nfe){
            this._setAngle = Double.NaN;
         }
         catch(NullPointerException  npe){}
      }
   }

   //Set the _setArmForce Property
   //
   //
   private void setArmForce
   (
      Hashtable<String,String> mech,
      Hashtable<String,String> rocket
   ){
      int holds = -1;
      if(this._setAngle == Double.NaN){
         this.setAngle(mech,rocket);
      }
      if(Double.isNaN(this._tolerance)){
         this.tolerance(mech,rocket);
      }
      if(mech.containsKey("number_of_holds")){
         try{
            String sHolds = mech.get("number_of_holds");
            holds         = Integer.parseInt(sHolds);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }
      if(rocket.containsKey("empty_weight")){
         try{
            String sWeight = rocket.get("empty_weight");
            double weight  = Double.parseDouble(sWeight);
            weight /= holds;
            weight /= Math.sin(this._setAngle);
            //This is the weight that is calculated based on the
            //initialization data...
            this._setArmForce = weight;
         }
         catch(NumberFormatException nfe){
            this._setArmForce = Double.NaN;
         }
         catch(NullPointerException npe){}
      }

   }

   //
   //
   //
   private void setError(String errorType){
      if(this._error == null){
         this._error = new String();
      }
      if(errorType.toUpperCase().contains("ANGLE")){
         this._error += "\n";
         this._error += "Mechanism Support: "+this.id()+"\n";
         this._error += "Error\nMeasured Angle: "+this._angle;
         this._error += " rad\nExpected Angle: "+this._setAngle;
      }
      else if(errorType.toUpperCase().contains("FORCE")){
         this._error += "\nMechanism Support: "+this.id()+"\n";
         this._error += "Error\nMeasured Arm Force: "+this._armForce;
         this._error += " rad\nExpected Arm Force: ";
         this._error += this._setArmForce;
      }
      else if(errorType.toUpperCase().contains("VECTOR")){
         this._error += "\nMechanism Support: "+this.id()+"\n";
         this._error += "Error\nForce Direction: ";
         if(errorType.toUpperCase().contains("X")){
            this._error += "X direction\nMeasured: ";
            this._error += this._vector.x() + "\nExpected: ";
            this._error += this._setForceVector.x();
         }
         else if(errorType.toUpperCase().contains("Y")){
            this._error += "Y direction\nMeasured: ";
            this._error += this._vector.y() + "\nExpected: ";
            this._error += this._setForceVector.y();
         }
         else if(errorType.toUpperCase().contains("Z")){
            this._error += "Z direction\nMeasured: ";
            this._error += this._vector.z() + "\nExpected: ";
            this._error += this._setForceVector.z();
         }
      }
      else if(errorType.toUpperCase().contains("MAGNITUDE")){
         this._error += "\nMagnitude\nMeasured: ";
         this._error += this._vector.magnitude()+"\nExpected: ";
         this._error += this._setForceVector.magnitude();
      }
   }

   //Set the _setForceVector
   //
   //
   private void setForceVector
   (
      Hashtable<String,String> mech,
      Hashtable<String,String> rocket
   ){
      double x = Double.NaN;
      double y = Double.NaN;
      double z = Double.NaN;
      if(this._setAngle == Double.NaN){
         this.setAngle(mech,rocket);
      }
      if(this._setArmForce == Double.NaN){
         this.setArmForce(mech,rocket);
      }
      if(Double.isNaN(this._tolerance)){
         this.tolerance(mech,rocket);
      }
      if(this.id() == 0){
         x = this._setArmForce*Math.cos(this._setAngle);
         y = 0;
      }
      else if(this.id() == 1){
         x = 0;
         y = this._setArmForce*Math.cos(this._setAngle);
      }
      else if(this.id() == 2){
         x = (-1.)*this._setArmForce*Math.cos(this._setAngle);
         y = 0;
      }
      else if(this.id() == 3){
         x = 0;
         y = (-1)*this._setArmForce*Math.cos(this._setAngle);
      }
      else{}
      z = (-1)*this._setArmForce*Math.sin(this._setAngle);
      this._setForceVector = new GenericForceVector(x,y,z);
   }

   //
   //
   //
   private void setUpMechanismSupportData(){
      MechanismSupportData msd = null;
      msd = new GenericMechanismSupportData(this._angle,
                                            this._error,
                                            this._vector,
                                            this._id,
                                            this._isError,
                                            this._armForce);
      this._supportData = msd;
   }

   //
   //
   //
   private void setUpThread(){
      this._rt0 = new Thread(this, "Mechanism Support" + this._id);
      this._rt0.start();
   }

   //Swt the this._tolerance property
   //
   //
   private void tolerance
   (
      Hashtable<String,String> mech,
      Hashtable<String,String> rocket
   ){
      if(mech.containsKey("holds_tolerance")){
         try{
            String sTolerance = mech.get("holds_tolerance");
            double tolerance  = Double.parseDouble(sTolerance);
            this._tolerance   = tolerance;
         }
         catch(NumberFormatException npe){
            this._tolerance = Double.NaN;
         }
         catch(NullPointerException npe){}
      } 
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
   public MechanismSupportData monitor(){
      return this._supportData;
   }
   //
   //
   //
   public MechanismSupportData monitorInitialization(){
      return this._supportData;
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
      DecimalFormat df = new DecimalFormat("###.##");
      df.setRoundingMode(RoundingMode.HALF_UP);
      String string = this.getClass().getName()+" : "+this.id();
      string += "\n"+df.format(this._angle);
      string += "\n"+df.format(this._armForce)+", ";
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
               if(this._isError){
                  this.alertErrorListeners();
               }
               else{
                  //Save off the data as needed...
                  this.setUpMechanismSupportData();
               }
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
