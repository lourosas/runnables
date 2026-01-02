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
   private long    _model;
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
      _model             = Long.MIN_VALUE;
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
   public GenericEngine(int number, int stage, String model){
      this.setEngineNumber(number);
      this.setModel(model);
      this.setStageNumber(number);
      this._obj = new Object();
      this.setUpThread();
   }

   ////////////////////////////Private Methods////////////////////////
   //
   //
   //
   private void engineData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         this.initializeEngDataINI(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         this.initializeEngDataJSON(file);
      }
   }

   //
   //
   //
   private void initializeEngDataINI(String file)throws IOException{}

   //
   //
   //
   private void initializeEngDataJSON(String file)throws IOException{
      long mod   = Long.MIN_VALUE;  double exh  = Double.NaN;
      double ff  = Double.NaN;      double temp = Double.NaN;
      int stg    = -1;              double tol  = Double.NaN;
      //Test Print
      System.out.println("Generic Engine: "+file);
      System.out.println(this._stage+", "+this._number);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst=read.readEngineDataInfo();
         Iterator<Hashtable<String,String>> it = lst.iterator();
         while(it.hasNext()){
            Hashtable<String,String> ht = it.next();
            try{ mod = Long.parseLong(ht.get("model"),16); }
            catch(NumberFormatException nfe){ mod = Long.MIN_VALUE; }
            try{ stg = Integer.parseInt(ht.get("stage")); }
            catch(NumberFormatException nfe){ stg = -1; }
         }
      }
      catch(IOException ioe){
         this._engineData = null;
         throw ioe;
      }
   }

   //
   //
   //
   private boolean isPathAndFile(String file)throws IOException{
      boolean isPath = false;
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         if(read.readPathInfo().get("parameter") == null){
            throw new NullPointerException("Not a Path File");
         }
         isPath = true;
      }
      catch(IOException ioe){
         isPath = false;
         ioe.printStackTrace(); //Temporary
         throw ioe;
      }
      catch(NullPointerException npe){
         isPath = false;
      }
      catch(Exception e){
         e.printStackTrace(); //Shold never get here
         isPath = false;
      }
      finally{
         return isPath;
      }
   }

   //
   //
   //
   private void setEngineNumber(int number){
      if(number > -1){
         this._number = number;
      }
   }

   //
   //
   //
   private void setModel(String model){
      try{
         this._model = Long.parseLong(model, 16);
      }
      catch(NumberFormatException nfe){
         this._model = Long.MIN_VALUE;
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

   //
   //
   //
   private void setUpThread(){
      int num = this._number;
      int stg  = this._stage;
      String name = new String("Engine: "+stg+", "+num);
      this._rt0 = new Thread(this, name);
      this._rt0.start();
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
         String engFile = file;
         if(this.isPathFile(engFile)){
            LaunchSimulatorJsonFileReader read = null;
            read = new LaunchSimulatorJsonFileReader(engFile);
            engFile = read.readPathInfo().get("engine");
         }
         this.engineData(engFile);
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
   public void addErrorListener(ErrorListener listener){
      try{
         if(listener != null){
            this._errorListeners.add(listener);
         }
      }
      catch(NullPointerExcepetion npe){
         this._errorListeners = new LinkedList<ErrorListeners>();
         this._errorListeners.add(listener);
      }
   }

   //
   //
   //
   public void addSystemListener(SystemListener listener){
      try{
         if(listener != null){
            this._systemListeners.add(listener);
         }
      }
      catch(NullPointerException npe){
         this._systemListeners = new LinkedList<SystemListener>();
         this._systemListeners.add(listener);
      }
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      this._state = stateSubstate;
   }

   //////////////////Runnable Interface Implementation////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
