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

public class GenericPipe implements Pipe{
   private static final int PRELAUNCH = -1;
   private static final int IGNITION  =  0;
   private static final int LAUNCH    =  1;

   private String   _error;
   private boolean  _isError;
   private int      _tank;  //Tank Number (1,2)
   private int      _stage; //Stage Number (1...total stages)
   //Pipe Number for the Tank (1,2)--corresponds to the engine...
   private int      _number; 
   private double   _rate;
   private double   _measuredRate;
   private double   _temperature;
   private double   _measuredTemperature;
   private double   _tolerance;

   {
      _error               = null;
      _isError             = false;
      _tank                = -1;
      _stage               = -1;
      _number              = -1;
      _rate                = Double.NaN;
      _measuredRate        = Double.NaN;
      _temperature         = Double.NaN;
      _measuredTemperature = Double.NaN;
      _tolerance           = Double.NaN;
   };

   ///////////////////////////Constructor/////////////////////////////
   //Tank Number
   //Stage Number
   //Pipe Number
   public GenericPipe(int tank, int stage, int number){
      if(tank > 0){
         this._tank = tank;
      }
      if(stage > 0){
         this._stage = stage;
      }
      if(number > 0){
         this._number = number;
      }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void isError(int state){
      this._error    = null;
      this._isError  = false;
      this.isFlowError(state);
      this.isTemperatureError(state);
   }

   //
   //
   //
   private void isFlowError(int state){
      if(state == PRELAUNCH){
         //At Prelaunch, there litterally better not be any Flow!
         double err = 0.05;
         if(this._measuredRate >= err){
            double er      = this._measuredRate;
            //convert to m^3
            double ercubic = this._measuredRate/1000;
            this._isError  = true;
            String s = new String("\nPipe Pre-Launch Error:  Flow");
            if(this._error == null){
               this._error = new String(s);
            }
            else{
               this._error += s;
            }
            this._error += "\nMeasured Flow:     " + er;
            this._error += "\nMeasured Flow m^3: " + ercubic;
         }
      }
   }

   //
   //
   //
   private void isTemperatureError(int state){
      if(state == PRELAUNCH){
         double ul = this._temperature*(2-this._tolerance);
         double ll = this._temperature*this._tolerance;
         double m  = this._measuredTemperature;
         if(m > ul){
            this._isError = true;
            String s = new String("\nPipe Temperature Error: ");
            if(this._error == null){
               this._error = new String(s);
            }
            else{
               this._error += s;
            }
            this._error += "\nRequired Temp: "+this._temperature;
            this._error += "\nMeasured Temp: "+m;
         }
      }
   }

   //The flow is measured in Liters/sec...converted to m^3/sec
   //
   //
   private void measureFlow(){
      //Stop gap for now...for Prelaunch, there should be NO Flow!
      this._measuredRate = 0.;
   }

   //
   //
   //
   private void measureTemperature(){
      //Stop Gap for the time being...
      this._measuredTemperature = this._temperature;
   }

   //
   //
   //
   private void pipeData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setPipeData(read.readPipeDataInfo());
      }
   }

   //
   //
   //
   private void setPipeData(List<Hashtable<String,String>> data ){
      System.out.println("Pipes: "+data);
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String,String> ht = data.get(i);
         try{
            int tk     = this._tank;
            int st     = this._stage;
            int num    = this._number;
            int tank   = Integer.parseInt(ht.get("tanknumber"));
            int stage  = Integer.parseInt(ht.get("stage"));
            if((tk == tank) && (st == stage)){
               System.out.println("Engine:  "+this._number);
               System.out.println(ht);
               this._rate = Double.parseDouble(ht.get("rate"));
               Double d = Double.parseDouble(ht.get("temperature"));
               this._temperature = d;
               d = Double.parseDouble(ht.get("tolerance"));
               this._tolerance = d;
            }
         }
         catch(NumberFormatException nfe){}
      }
   }

   ////////////////////Pipe Interface Implementation//////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      int tank  = this._tank;
      int stage = this._stage;
      int num   = this._number;
      if((tank > 0) && (stage > 0) && (num > 0)){
         this.pipeData(file);
      }
   }

   //
   //
   //
   public PipeData monitorPrelaunch(){
      PipeData data = null;
      this.measureFlow();
      this.measureTemperature();
      this.isError(PRELAUNCH);
      data = new GenericPipeData(
                                 this._error,
                                 this._measuredRate,
                                 this._number, //Pipe Number
                                 this._isError,
                                 this._stage,
                                 this._tank,   //Tank Number
                                 this._measuredTemperature,
                                 this._tolerance,
                                 null);
      return data;
   }

   //
   //
   //
   public PipeData monitorIgnition(){ return null; }

   //
   //
   //
   public PipeData monitorLaunch(){ return null; }
}
//////////////////////////////////////////////////////////////////////
