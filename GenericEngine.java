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
   public GenericEngine(int number, int stage){
      this.setEngineNumber(number);
      this.setStageNumber(stage);
      this._obj = new Object();
      this.setUpThread();
   }

   ////////////////////////////Private Methods////////////////////////
   //
   //
   //
   private void alertErrorListeners(){}

   //
   //
   //
   private void alertSubscribers(){}

   //
   //
   //
   private boolean checkExhaustFlow(){
      boolean isError   = false;
      double  ef        = Double.NaN;
      double  min       = Double.NaN;
      double  max       = Double.NaN;
      double  tolerance = Double.NaN;
      
      synchronized(this._obj){
         ef = this._measuredEngineData.exhaustFlowRate();
         tolerance = this._measuredEngineData.tolerance();
      }

      return isError;
   }

   //
   //
   //
   private void checkErrors(){
      String  err     = new String();
      boolean isError = false;
      if(this.checkExhaustFlow()){}
      if(this.checkFuelFlow()){}
      if(this.checkTemperature()){}
   }

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
   //
   private int[] grabEngineCounts(List<Hashtable<String,String>> lst){
      int stg = -1; int tot = -1; int[] index = new int[lst.size()];
      int count = 0;
      Iterator<Hashtable<String,String>> it = lst.iterator();
      while(it.hasNext()){
         Hashtable<String,String> ht = it.next();
         try{ stg = Integer.parseInt(ht.get("stage")); }
         catch(NumberFormatException nfe){ stg = -1; }
         if(stg == this._stage){
            try{ tot = Integer.parseInt(ht.get("total")); }
            catch(NumberFormatException nfe){ tot = -1; }
            if(tot > 0){
               if(count == 0){
                  index[count] = tot;
               }
               else{
                  index[count] = index[count - 1] + tot;
               }
               ++count;
            }
         }
      }
      return index;
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
      int num = this._number;
      //Test Print
      System.out.println("Generic Engine: "+file);
      System.out.println(this._stage+", "+this._number);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst=read.readEngineDataInfo();
         int[] index = this.grabEngineCounts(lst);
         int count     = 0;
         boolean found = false;
         for(int i = 0; i < lst.size(); ++i){
            Hashtable<String,String> ht = lst.get(i);
            try{ stg = Integer.parseInt(ht.get("stage")); }
            catch(NumberFormatException nfe){ stg = -1; }
            if(stg == this._stage){
               if(index[count] > 0 && num < index[count] && !found){
                  this.setInitializedData(ht);
                  found = true;
               }
               else{
                  ++count;
               }
            }
         }
      }
      catch(IOException ioe){
         ioe.printStackTrace();
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
   private void measure(){
      try{
         if(this._feeder != null){
            RocketData rd      = (RocketData)this._feeder.monitor();
            StageData  sd      = rd.stage(this._stage);

            List<EngineData>    lst = sd.engineData();
            Iterator<EngineData> it = lst.iterator();
            while(it.hasNext()){
               EngineData ed = it.next();
               int  sn  = ed.stage();
               int  idx = ed.index();
               long mdl = ed.model();
               if(sn == this._stage && idx == this._number){
                  if(mdl = this._model){
                     synchronized(this._obj){
                        this._measEngineData = ed;
                     }
                  }
               }
            }
         }
         else{
            throw new NullPointerException("No DataFeeder");
         }
      }
      catch(ClassCastException cce){
         try{
            synchronized(this._obj){
               EngineData ed = (EngineData)this._feeder.monitor();
               this._measEngineData = ed;
            }
         }
         catch(ClassCastException e){
            e.printStackTrace();
            throw new NullPointerException("No TankDataFeeder");
         }
      }
      catch(NullPointerException npe){
         synchronized(this._obj){
            this._measEngineData = this._engineData;
         }
      }
   }

   //
   //
   //
   private void monitorEngine(){
      this.measure();
      //Do not even do this!! No need to!!!
      this.setUpEngineData();
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
   private void setInitializedData(Hashtable<String,String> ht){
      int stg = -1; int num = this._number; int tot = -1;
      double exf = Double.NaN; String err = null;
      double ff  = Double.NaN; long mod = Long.MIN_VALUE;
      boolean isE = false; boolean isIg = false;
      double temp = Double.NaN; double tol = Double.NaN;
      try{ stg = Integer.parseInt(ht.get("stage")); }
      catch(NumberFormatException nfe){ stg = -1; }
      try{ exf = Double.parseDouble(ht.get("exhaust_flow")); }
      catch(NumberFormatException nfe){ exf = Double.NaN; }
      try{ ff = Double.parseDouble(ht.get("fuel_flow")); }
      catch(NumberFormatException nfe){ ff = Double.NaN; }
      try{ mod = Long.parseLong(ht.get("model"), 16); }
      catch(NumberFormatException nfe){ mod = Long.MIN_VALUE; }
      try{ temp = Double.parseDouble(ht.get("temperature")); }
      catch(NumberFormatException nfe){ temp = Double.NaN; }
      try{ tol = Double.parseDouble(ht.get("tolerance")); }
      catch(NumberFormatException nfe){ tol = Double.NaN; }
      try{ tot = Integer.parseInt(ht.get("total")); }
      catch(NumberFormatException nfe){ tot = -1; }
      //Stage, Index, exhaust flow, fuel flow, Model, isError,
      //error, is Ignited, temperature, tolerance
      //total engines (of the give model number);
      EngineData e = new GenericEngineData(stg, num, exf,
                                           ff,  mod, isE,
                                           err, isIg, temp,
                                           tol, tot);
      this._engineData = e;
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
   private void setUpEngineData(){}

   //
   //
   //
   private void setUpThread(){
      int num = this._number;
      int stg = this._stage;
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
         return this._measEngineData;
      }
   }

   //
   //
   //
   public void initialize(String file)throws IOException{
      if(this._number > -1 && this._stage > -1){
         String engFile = file;
         if(this.isPathAndFile(engFile)){
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
      catch(NullPointerException npe){
         this._errorListeners = new LinkedList<ErrorListener>();
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
   public void run(){
      try{
         int counter   = 0;
         boolean check = false;
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this._state != null){
               if(this._state.state() == INIT){
                  if(counter++%5000 == 0){
                     //For Initialize, check every 5 seconds...
                     check = true;
                     counter = 1; //Reset the Counter
                  }
               }
            }
            if(check){
               this.monitorEngine();
               this.checkErrors();
               this.alertSubscribers();
               check = false;
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
      catch(NullPointerException npe){
         npe.printStackTrace();
         System.exit(0);
      }
   }
}
//////////////////////////////////////////////////////////////////////
