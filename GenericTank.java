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
   private double  _measuredWeight; //Measured Fuel Weigt in Newtons
   private String  _error;
   private String  _fuel;
   private long    _model;
   private double  _emptyRate; //Liters/Sec
   private double  _emptyRateWeight; //N/s
   private boolean _isError;
   private int     _stageNumber;
   private int     _tankNumber;
   private double  _temperature;
   private double  _measuredTemperature;
   private double  _tolerance;

   {
      _capacity            = Double.NaN;
      _density             = Double.NaN;
      _measuredCapacity    = Double.NaN;
      _error               = null;
      _fuel                = null;
      _measuredWeight      = Double.NaN;
      _model               = -1;
      _emptyRate           = Double.NaN;
      _isError             = false;
      _stageNumber         = -1;
      _tankNumber          = -1;
      _temperature         = Double.NaN;
      _measuredTemperature = Double.NaN;
      _tolerance           = Double.NaN;
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
   private void isCapacityError(int state){
      double g = 9.81;
      if(state == PRELAUNCH){
         //Durring Prelaunch, the capacity must be with in tollerance,
         //since there should be NO FLOW in the Tank in Pre-Launch!
         double limit  = this._capacity * this._tolerance;
         //F = ma measured in Newtons...weight capcity
         double weight = (capLimit/1000)*this._density*g;
         if(this._measuredCapacity > capLimit){
            isError  = true;
            String s = new String("\nPre-Launch Error: Tank too low");
         }
      }
   }

   //
   //
   //
   private void isError(int state){
      this._isError = false;
      this.isCapacityError(PRELAUNCH);
      this.isFlowError(PRELAUNCH);
      this.isTemperatureError(PRELAUNCH);
   }

   //
   //
   //
   private void isFlowError(int state){}

   //
   //
   //
   private void isTemperatureError(int state){}

   //The Capacity is measured in liters--converted into m^3
   //
   //
   private void measureCapacity(){
      double g = 9.81;
      //Stop Gap for the time being...
      this._measuredCapacity = this._capacity;
      double mass = (this._measuredCapacity/1000)*this._density;
      this._measuredWeight = mass*g;  //F = ma in Newtons!!!!
   }

   //
   //
   //
   private void measureEmptyRate(){
      double g = 9.81;
      //Need to figure out how to measure
      this._emptyRate = 0;
      double mass = (this._emptyRate/1000.)*this._density;
      this._emptyRateWeight = mass*g; //F = ma in Newtons
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
      System.out.println("Fuel "+this._fuel);
      this.measureCapacity();
      this.measureEmptyRate();
      this.measureTemperature();
      this.isError(PRELAUNCH);
      return null;
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
