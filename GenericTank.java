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

import java.util.*;
import java.io.*;
import rosas.lou.runnables.*;

public class GenericTank implements Tank{
   private static final int PRELAUNCH = -1;
   private static final int IGNITION  =  0;
   private static final int LAUNCH    =  1;

   private double  _capacity; //In Liters
   private double  _density;
   private double  _measuredCapacity; //In Liters
   private String  _error;
   private String  _fuel;
   private long    _model;
   private double  _emptyRate; //Liters/Sec
   private double  _measuredEmptyRate; //Liters/Sec
   private boolean _isError;
   private int     _stageNumber;
   private int     _tankNumber;
   private double  _temperature;
   private double  _measuredTemperature;
   private double  _tolerance;

   {
      _capacity                = Double.NaN;
      _density                 = Double.NaN;
      _measuredCapacity        = Double.NaN;
      _error                   = null;
      _fuel                    = null;
      _model                   = -1;
      _emptyRate               = Double.NaN;
      _measuredEmptyRate       = Double.NaN;
      _isError                 = false;
      _stageNumber             = -1;
      _tankNumber              = -1;
      _temperature             = Double.NaN;
      _measuredTemperature     = Double.NaN;
      _tolerance               = Double.NaN;
   };

   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public GenericTank(int stage, int number){
      if(stage > 0){
         this._stageNumber = stage;
      }
      if(number > 0){
         this._tankNumber  = number;
      }
   }

   ////////////////////////////Private Methods////////////////////////
   //
   //
   //
   private double convertToMass(double volume){
      //Convert from Liters to cubic meters...
      //Multiply by density to get mass...
      double weight = (volume/1000)*this._density;
      return weight;
   }

   //Take a volume and convert to weight (Newtons)
   //Volume in Liters
   //
   private double convertToWeight(double volume){
      double g = 9.81;
      //Convert from Liters to cubic meters...
      //Multiply by desnsity to get mass...
      //Multiply by g to get weight-->F = ma...
      double weight = (volume/1000)*this._density*g;
      return weight;
   }

   //
   //
   //
   private void isCapacityError(int state){
      double g = 9.81;
      if(state == PRELAUNCH){
         //Durring Prelaunch, the capacity must be with in tolerance,
         //since there should be NO FLOW in the Tank in Pre-Launch!
         double limit  = this._capacity * this._tolerance;
         //F = ma measured in Newtons...weight capcity
         double weight = this.convertToWeight(this._capacity);
         double mw     = this.convertToWeight(this._measuredCapacity);
         double mc     = this._measuredCapacity;
         if(this._measuredCapacity < limit){
            this._isError  = true;
            String s = new String("\nPre-Launch Error: Tank too low");
            if(this._error == null){
               this._error = new String(s);
            }
            else{
               this._error += s;
            }
            this._error += "\nMeasured Capacity: "+mc;
            this._error += "\nExpected Capacity: "+this._capacity;
            this._error += "\nMeasured Weight:   "+mw;
            this._error += "\nExpected Weight:   "+weight;
         }
      }
   }

   //
   //
   //
   private void isError(int state){
      this._error   = null;
      this._isError = false;
      this.isCapacityError(PRELAUNCH);
      this.isFlowError(PRELAUNCH);
      this.isTemperatureError(PRELAUNCH);
   }

   //
   //
   //
   private void isFlowError(int state){
      if(state == PRELAUNCH){
         //At prelaunch, there literally better not be ANY flow!
         double err = 0.05;
         if(this._measuredEmptyRate >= err){
            double er = this._measuredEmptyRate;
            this._isError = true;
            double mass = this.convertToMass(this._measuredEmptyRate);
            String s = new String("\nPre-Launch Error: Flow");
            if(this._error == null){
               this._error = new String(s);
            }
            else{
               this._error += s;
            }
            this._error += "\nMeasured Flow:      " + er;
            this._error += "\nMeasured Mass Loss: " + mass;
         }
      }
   }

   //Fuel Temp MUST be the same regargless of State!!!
   //
   //
   private void isTemperatureError(int state){
      double ul = this._temperature*(2 - this._tolerance);
      double ll = this._temperature*this._tolerance;
      double m  = this._measuredTemperature;
      //Error out based on tradtional limit ranges...
      if(m < ll || m > ul){
         this._isError = true;
         String s = new String("Tempearture Error:  ");
         if(this._error == null){
            this._error = new String(s);
         }
         else{
            this._error += s;
         }
         this._error += "\nRequired Temp:  "+this._temperature;
         this._error += "\nMeasured Temp:  "+m;
      }
   }

   //The Capacity is measured in liters--converted into m^3
   //
   //
   private void measureCapacity(){
      double g = 9.81;
      //Stop Gap for the time being...
      this._measuredCapacity = this._capacity;
   }

   //
   //
   //
   private void measureEmptyRate(){
      //Need to figure out how to measure
      this._measuredEmptyRate = 0;
   }

   //
   //
   //
   private void measureTemperature(){
      //Stop Gap for now
      this._measuredTemperature = this._temperature;
   }

   //
   //
   //
   private void setTankData
   (
      List<Hashtable<String,String>> data
   ){
      //Get the Stage and Tank numbers for comparison
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String,String> ht = data.get(i);
         try{
            int stage = Integer.parseInt(ht.get("stage"));
            int num   = Integer.parseInt(ht.get("number"));
            if((stage==this._stageNumber) && (num==this._tankNumber)){
               int x = Integer.parseUnsignedInt(ht.get("model"),16);
               this._model = Integer.toUnsignedLong(x);
               double d = Double.parseDouble(ht.get("capacity"));
               this._capacity = d;
               d = Double.parseDouble(ht.get("density"));
               this._density = d;
               this._fuel = ht.get("fuel");
               d = Double.parseDouble(ht.get("rate"));
               this._emptyRate = d;
               d = Double.parseDouble(ht.get("temperature"));
               this._temperature = d;
               d = Double.parseDouble(ht.get("tolerance"));
               this._tolerance = d;
            }
         }
         catch(NumberFormatException nfe){}
      }
   }

   //
   //
   //
   private void tankData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setTankData(read.readTankDataInfo());
      }
   }

   ///////////////////////Tank Interface Methods//////////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      if(this._stageNumber > 0 && this._tankNumber > 0){
         this.tankData(file);
      }
   }

   //
   //
   //
   public TankData monitorPrelaunch(){ 
      TankData tankData = null;
      this.measureCapacity();
      this.measureEmptyRate();
      this.measureTemperature();
      this.isError(PRELAUNCH);
      tankData = new GenericTankData(
                              this._measuredCapacity,
                              this._density,
                              this._measuredEmptyRate,
                              this._error,
                              this._fuel,
                              this._isError,
                              this._tankNumber,
                              this._measuredTemperature);
      return tankData;
   }

   //
   //
   //
   public TankData monitorIgnition(){ return null; }

   //
   //
   //
   public TankData monitorLaunch(){ return null; }
}
//////////////////////////////////////////////////////////////////////
