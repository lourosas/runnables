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

public class GenericPipe implements Pipe, Runnable{
   private static boolean TOPRINT = true;
   
   private LaunchStateSubstate.State INIT      = null; 
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null;

   private String   _error;
   private boolean  _isError;
   private boolean  _kill;
   private int      _tank;  //Tank Number (1,2)
   private int      _stage; //Stage Number (1...total stages)
   private boolean  _start;
   //Pipe Number for the Tank (1,2)--corresponds to the engine...
   private int      _number; 
   private double   _rate;
   private double   _measuredRate;
   private double   _temperature;
   private double   _measuredTemperature;
   private double   _tolerance;

   private DataFeeder          _feeder;
   private List<ErrorListener> _errorListeners;
   private LaunchStateSubstate _state;
   private Object              _obj;
   private PipeData            _pipeData;
   private PipeData            _measuredPipeData;
   private Thread              _rt0;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _error               = null;
      _isError             = false;
      _kill                = false;
      _tank                = -1;
      _stage               = -1;
      _start               = false;
      _number              = -1;
      _rate                = Double.NaN;
      _measuredRate        = Double.NaN;
      _temperature         = Double.NaN;
      _measuredTemperature = Double.NaN;
      _tolerance           = Double.NaN;

      _feeder              = null;
      _errorListeners      = null;
      _state               = null;
      _obj                 = null;
      _pipeData            = null;
      _measuredPipeData    = null;
      _rt0                 = null;
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
         //Essentially, this is the Rocket Engine the Pipe Feeds...
         this._number = number;
      }
      this._obj = new Object();
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void isError(){
      this._error    = null;
      this._isError  = false;
      this.isFlowError();
      this.isTemperatureError();
   }

   //
   //
   //
   private void isFlowError(){
      if(this._state.state() == INIT){}
      else if(this._state.state() == PRELAUNCH){
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
   private void isTemperatureError(){
      if(this._state.state() == INIT){}
      else if(this._state.state() == PRELAUNCH){
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
   private void monitorPipe(){
      PipeData pd = null;
      this.measureFlow();
      this.measureTemperature();
      this.isError();
      pd = new GenericPipeData(this._error,
                               this._measuredRate,
                               this._number, //Pipe Number
                               this._isError,
                               this._stage,
                               this._tank,   //Tank Number
                               this._measuredTemperature,
                               this._tolerance,
                               null);   
      synchronized(this._obj){
         this._pipeData = pd;
      }
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
      //This will need to change similar to GenericStage!
      //Use PipeData
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

   //
   //
   //
   private void setUpThread(){
      String name = new String("Pipe: "+this._stage+", "+this._tank);
      name += ", "+this._number;
      this._rt0 = new Thread(this, name);
      this._rt0.start();
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
         this._state = new LaunchStateSubstate(INIT,null,null,null);
         this._start = true;
      }
   }

   //
   //
   //
   public PipeData monitor(){
      synchronized(this._obj){
         return this._measuredPipeData;
      }
   }

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
   public void addErrorListener(ErrorListener listener){}

   ///////////////////Runnable Interface Implementation///////////////
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
               //this.monitorPipe();
               if(this._state.state() == INIT){
                  //For later determination
                  Thread.sleep(7500);
               }
            }
            else{
               Thread.sleep(1);
            }
         }
      }
      catch(InterruptedException ie){}
      catch(NullPointerException npe){
         //Should NEVER GET HERE!!!
         npe.printStackTrace();
         System.exit(0);
      }
   }
}
//////////////////////////////////////////////////////////////////////
