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
import java.io.IOException;
import rosas.lou.runnables.*;

public class GenericPump implements Pump{
   private static final int PRELAUNCH = -1;
   private static final int IGNITION  =  0;
   private static final int LAUNCH    =  1;

   private String  _error;
   private boolean _isError;
   private int     _stage;
   private int     _tank;
   private double  _rate;
   private double  _measuredRate;
   private double  _temperature;
   private double  _measuredTemperature;
   private double  _tolerance;

   {
      _error               = null;
      _isError             = false;
      _stage               = -1;
      _tank                = -1;
      _rate                = Double.NaN;
      _measuredRate        = Double.NaN;
      _temperature         = Double.NaN;
      _measuredTemperature = Double.NaN;
      _tolerance           = Double.NaN;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericPump(int stage, int tank){
      if(stage > 0){
         this._stage = stage;
      }
      if(tank > 0){
         this._tank = tank;
      }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void isError(int state){
     this._error   = null;
     this._isError = false;
     this.isFlowError(state);
     this.isTemperatureError(state);
   }

   //
   //
   //
   private void isFlowError(int state){
      if(state == PRELAUNCH){
         //At prelaunch, there literally better not be ANY Flow!!!
      }
   }

   //
   //
   //
   private void isTemperatureError(int state){
      if(state == PRELAUNCH){}
   }

   //
   //
   //
   private void isTemperatureError(int state){}

   //The flow is measured in Liters/sec...converted to m^3/sec
   //
   //
   private void measureFlow(){
      //Stop gap for now...for Prelaunch, there should be NO flow...
      this._measuredRate = 0.;
   }

   //
   //
   //
   private void measureTemperature(){
      //Stop gap for now...
      this._measuredTemperature = this._temperature;
   }

   //
   //
   //
   private void pumpData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setPumpData(read.readPumpDataInfo());
      }
   }

   //
   //
   //
   private void setPumpData(List<Hashtable<String,String>> data){
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String,String> ht = data.get(i);
         try{
            int stage = Integer.parseInt(ht.get("stage"));
            int tank  = Integer.parseInt(ht.get("tanknumber"));
            if((this._tank == tank) && (this._stage == stage)){
               System.out.println("Pump: "+ht);
               this._rate = Double.parseDouble(ht.get("rate"));
               Double d   = Double.parseDouble(ht.get("temperature"));
               this._temperature = d;
               d = Double.parseDouble(ht.get("tolerance"));
               this._tolerance = d;
            }
         }
         catch(NumberFormatException nfe){}
      }
   }

   ///////////////////Pump Interface Implementation///////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      if((this._stage > 0) && (this._tank > 0)){
         this.pumpData(file);
      }
   }

   //
   //
   //
   public PumpData monitorPrelaunch(){
      PumpData data = null;
      this.measureFlow();
      this.measureTemperature();
      this.isError(PRELAUNCH);
      return data;
   }

   //
   //
   //
   public PumpData monitorIgnition(){ return null; }

   //
   //
   //
   public PumpData monitorLaunch(){ return null; }
}
//////////////////////////////////////////////////////////////////////
