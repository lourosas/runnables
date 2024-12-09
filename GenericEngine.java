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

public class GenericEngine implements Engine, Runnable{
   private static final int PRELAUNCH = -1;
   private static final int IGNITION  =  0;
   private static final int LAUNCH    =  1;

   private String  _error;
   private double  _exhaustFlow;
   private double  _measuredExhaustFlow;
   private double  _fuelFlow;
   private double  _measuredFuelFlow;
   private boolean _isError;
   private boolean _isIgnited;
   private long    _model;
   private int     _number;
   private int     _stage;
   private double  _temperature;
   private double  _measuredTemperature;
   private double  _tolerance;

   {
      _error               = null;
      _exhaustFlow         = Double.NaN;
      _measuredExhaustFlow = Double.NaN;
      _fuelFlow            = Double.NaN;
      _measuredFuelFlow    = Double.NaN;
      _isError             = false;
      _isIgnited           = false;
      _model               = -1;
      _number              = -1;
      _stage               = -1;
      _temperature         = Double.NaN;
      _measuredTemperature = Double.NaN;
      _tolerance           = Double.NaN;
   };

   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public GenericEngine(int number, int stage){
      if(number > 0){
         this._number = number;
      }
      if(stage > 0){
         this._stage = stage;
      }
   }

   ////////////////////////////Private Methods////////////////////////
   //Temp comments:  In truth, there is no way to actually determine
   //Ignition...based on the data, can "surmise" if ignition has
   //occured based on what is measureable:  exhaust rate, fuel rate,
   //temperature and compare to what is pre-entered...
   private void determineIgnition(){
      this._isIgnited = false;  //Pre-Set the value...
      double limit = this._exhaustFlow * this._tolerance;
      //All three have to fit the criteria as dictated by the 
      if(this._measuredExhaustFlow >= limit){
         limit = this._fuelFlow * this._tolerance;
         if(this._measuredFuelFlow >= limit){
            limit = this._temperature * this._tolerance;
            if(this._measuredTemperature >= limit){
               this_isIgnited = true;
            }
         }
      }
      
      
   }

   //
   //
   //
   private void engineData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setEngineData(read.readEngineDataInfo());
      }
   }

   //
   //
   //
   private void isExhaustFlowError(int state){}

   //
   //
   //
   private void isFuelFlowError(int state){}

   //
   //
   //
   private void isTemperatureError(int state){}

   //
   //
   //
   private void isError(int state){
      this._isError = false; //Preset the error condition
      this.isExhaustFlowError(state);
      this.isFuelFlowError(state);
      this.isTemperatureError(state);
      if(state == PRELAUNCH){

      }
   }

   //
   //
   //
   private void measureExhaustFlow(){
      //@TBD need to determine how to "Measure"
      //for the time being, cludge the value
      this._measuredExhaustFlow = 0.;
   }

   //
   //
   //
   private void measureFuelFlow(){
      //Stop Gap for the time being
      this._measuredFuelFlow = 0.;
   }

   //
   //
   //
   private void measureTemperature(){
      //Stop gap for the time being
      this._measuredTemperature = this._temperature;
   }

   //
   //
   //
   private void setEngineData
   (
      List<Hashtable<String,String>> data
   ){
      System.out.println(this._stage);
      System.out.println(this._number);
      System.out.println(data);
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String, String> ht = data.get(i);
         System.out.println(ht);
         try{
            String num = ht.get("stage");
            if(Integer.parseInt(num) == this._stage){
               int x = Integer.parseUnsignedInt(ht.get("model"),16);
               this._model = Integer.toUnsignedLong(x);
               //System.out.println(String.format("0x%X",this._model));
               double d = Double.parseDouble(ht.get("exhaust_flow"));
               this._exhaustFlow = d;
               d = Double.parseDouble(ht.get("fuel_flow"));
               this._fuelFlow = d;
               d = Double.parseDouble(ht.get("temperature"));
               this._temperature = d;
               d = Double.parseDouble(ht.get("tolerance"));
               this._tolerance = d;
               System.out.println(this._tolerance);
            }
         }
         catch(NumberFormatException nfe){}
      }
   }

   ////////////////////Engine Interface Implementation////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      if(this._number > -1 && this._stage > -1){
         this.engineData(file);
      }
   }

   //
   //
   //
   public EngineData monitorPrelaunch(){
      this.measureExhaustFlow();
      this.measureTemperature();
      this.measureFuelFlow();
      this.determineIgnition();
      this.isError(PRELAUNCH);
      return null;
   }

   //
   //
   //
   public EngineData monitorIgnition(){
      return null;
   }

   //
   //
   //
   public EngineData monitorLaunch(){
      return null;
   }

   //////////////////Runnable Interface Implementation////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
