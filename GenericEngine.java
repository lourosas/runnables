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
   private LaunchStateSubstate.State INIT              = null;
   private LaunchStateSubstate.State PREL              = null;
   private LaunchStateSubstate.State IGNI              = null;
   private LaunchStateSubstate.State LAUN              = null;
   private LaunchStateSubstate.State ASCE              = null;
   private LaunchStateSubstate.PreLaunchSubstate SET   = null;
   private LaunchStateSubstate.PreLaunchSubstate CONT  = null;
   private LaunchStateSubstate.PreLaunchSubstate FUEL  = null;
   private LaunchStateSubstate.PreLaunchSubstate HOLD  = null;
   private LaunchStateSubstate.IgnitionSubstate  IGN   = null;
   private LaunchStateSubstate.IgnitionSubstate  BUP   = null;
   private LaunchStateSubstate.AscentSubstate    STG   = null;
   private LaunchStateSubstate.AscentSubstate    IGNE  = null;

   private boolean _kill;
   private int     _number; //Engine number in the Stage
   private int     _stage;

   private DataFeeder                _feeder;
   private List<ErrorListener>       _errorListeners;
   private List<SystemListener>      _systemListeners;
   private LaunchStateSubstate       _state;
   private Object                    _obj;
   private Thread                    _rt0;
   private EngineData                _engineData;
   private EngineData                _measEngineData;

   {
      INIT = LaunchStateSubstate.State.INITIALIZE;
      PREL = LaunchStateSubstate.State.PRELAUNCH;
      IGNI = LaunchStateSubstate.State.IGNITION;
      LAUN = LaunchStateSubstate.State.LAUNCH;
      ASCE = LaunchStateSubstate.State.ASCENT;
      SET  = LaunchStateSubstate.PreLaunchSubstate.SET;
      CONT = LaunchStateSubstate.PreLaunchSubstate.CONTINUE;
      FUEL = LaunchStateSubstate.PreLaunchSubstate.FUELING;
      HOLD = LaunchStateSubstate.PreLaunchSubstate.HOLD;
      IGN  = LaunchStateSubstate.IgnitionSubstate.IGNITION;
      BUP  = LaunchStateSubstate.IgnitionSubstate.BUILDUP;
      STG  = LaunchStateSubstate.AscentSubstate.STAGING;
      IGNE = LaunchStateSubstate.AscentSubstate.IGNITEENGINES; 

      _kill              = false;
      _number            = -1;
      _stage             = -1;
      _feeder            = null;
      _errorListeners    = null;
      _systemListeners   = null;
      _state             = null;
      _obj               = null;
      _rt0               = null;
      _engineData        = null;
      _measEngineData    = null;
   };

   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public GenericEngine(int number, int stage){
      this.setEngineNumber(number);
      this.setStageNumber(number);
      this._obj = new Object();
      this.setUpThread();
   }

   ////////////////////////////Private Methods////////////////////////
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

   //
   //
   //
   private void setEngineNumber(int engine){
      if(engine > -1){
         this._number = engine;
      }
   }

   //
   //
   //
   private void setStageNumber(int stage){
      if(stage > 0){
         this._stage = stage;
      }
   }

   ////////////////////Engine Interface Implementation////////////////
   //
   //
   //
   public EngineData monitor(){
      synchronized(this._obj){
         return this._measuredEngineData;
      }
   }

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
      EngineData data = null;
      this.measureExhaustFlow();
      this.measureTemperature();
      this.measureFuelFlow();
      this.determineIgnition();
      this.isError(PRELAUNCH);
      data = new GenericEngineData(this._stage,
                                   this._number, //Index
                                   this._measuredExhaustFlow,
                                   this._measuredFuelFlow,
                                   this._model,
                                   this._isError,
                                   this._error,
                                   this._isIgnited,
                                   this._measuredTemperature,
                                   this._tolerance);
      return data;
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
