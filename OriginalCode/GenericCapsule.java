/////////////////////////////////////////////////////////////////////
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

public class GenericCapsule implements Payload, Runnable{
   private static final int PRELAUNCH = -1;
   private static final int IGNITION  =  0;
   private static final int LAUNCH    =  1;

   private int      _crew;
   private double   _currenttemp;
   private double   _dryweight;
   private String   _error;
   private boolean  _isError;
   private double   _maxweight;
   private double   _measuredweight;
   private String   _model;
   private String   _type;

   {
      _crew           = -1;
      _currenttemp    = Double.NaN;
      _dryweight      = Double.NaN;
      _error          = null;
      _isError        = false;
      _maxweight      = Double.NaN;
      _measuredweight = Double.NaN;
      _model          = null;
      _type           = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericCapsule(){}

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void capsuleData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setCapsuleData(read.readPayloadInfo());
      }
   }

   //
   //
   //
   private void isError(int state){
      this._error   = null;  //Set to null
      this._isError = false; //Pre-set
      this.isTemperatureError(state);
      this.isMeasuredWeightError(state);
   }

   //
   //
   //
   private void isMeasuredWeightError(int state){
      if(state == PRELAUNCH){
         //In the Pre-Launch state, the payload weight should not
         //exceede the Maximum Allowed Weight...
         if(this._measuredweight > this._maxweight){
            this._isError = true;
            String s = new String("\nMeasured Weight Error");
            if(this._error == null){ this._error = new String(s); }
            else{ this._error += s; }
            this._error += "\nExpected Weight: ";
            this._error += "" + this._dryweight;
            this._error += "\nMaximum  Weight: ";
            this._error += "" + this._maxweight;
            this._error += "\nMeasured Weight: ";
            this._error += "" + this._measuredweight + "\n";
         }
      }
   }

   //
   //
   //
   private void isTemperatureError(int state){
      if(state == PRELAUNCH){
         //The payload should never get higher than the boiling point
         //of water...
         double err = 373.;
         if(this._currenttemp > err){
            this._isError = true;
            String s = new String("\nPre-Launch Temperature Error");
            if(this._error == null){
               this._error = new String(s);
            }
            else{
               this._error += s;
            }
            this._error += "\nCurrent Temperature: ";
            this._error += this._currenttemp;
         }
      }
   }

   //
   //
   //
   private void measureTemp(){
      //Put a stop gap in for now...set the temp at typical
      //temp...temp is set in Kelvin...
      double temp = 300; //300K
      this._currenttemp = temp;
   }

   //
   //
   //
   private void measureWeight(){
      //Stop gap for the time being...
      this._measuredweight = this._dryweight;
   }

   //
   //
   //
   private void setCapsuleData(Hashtable<String,String> data){
      try{
         this._model = data.get("model");
      }
      catch(NullPointerException npe){}
      try{
         this._type = data.get("type");
      }
      catch(NullPointerException npe){}
      try{
         int i = Integer.parseInt(data.get("crew"));
         if(i > 0){
            this._crew = i;
         }
      }
      catch(NumberFormatException nfe){}
      catch(NullPointerException npe){}
      try{
         double d = Double.parseDouble(data.get("dryweight"));
         if(d > 0.){
            this._dryweight = d;
         }
      }
      catch(NumberFormatException nfe){}
      catch(NullPointerException npe){}
      try{
         double d = Double.parseDouble(data.get("maxweight"));
         if(d > 0.){
            this._maxweight = d;
         }
      }
      catch(NumberFormatException nfe){}
      catch(NullPointerException npe){}
   }

   //////////////////Payload Interface Implementation/////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      this.capsuleData(file);
   }

   //
   //
   //
   public PayloadData monitorPrelaunch(){
      PayloadData data = null;
      this.measureTemp();
      this.measureWeight();
      this.isError(PRELAUNCH);
      return new GenericCapsuleData(this._crew,
                                    this._currenttemp,
                                    this._error,
                                    this._isError,
                                    this._dryweight,
                                    this._maxweight,
                                    this._measuredweight,
                                    this._model,
                                    this._type);
   }

   //
   //
   //
   public PayloadData monitorIgnition(){ return null; }

   //
   //
   //
   public PayloadData monitorLaunch(){ return null; }

   //
   //
   //
   public PayloadData monitorPostLaunch(){ return null; }

   /////////////////Runnable Interface Implementation/////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
