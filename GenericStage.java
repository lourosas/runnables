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

public class GenericStage implements Stage, Runnable, ErrorListener{
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State INIT      = null; 
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null;

   private double              _calculatedWeight;
   private double              _dryweight;
   private List<Engine>        _engines;
   private String              _error;//currently, not using, but keep
   private List<ErrorListener> _errorListeners;
   private DataFeeder          _feeder;
   private FuelSystem          _fuelSystem;
   private boolean             _kill;
   private double              _maxWeight;
   private int                 _stageNumber;
   private long                _modelNumber;
   private int                 _totalEngines;
   private boolean             _isError;//Currently not using,but keep
   private Thread              _rt0;
   private boolean             _start;
   private LaunchStateSubstate _state;
   private double              _tolerance;
   private double              _weight; //Dry weight + wet weight

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _calculatedWeight = Double.NaN;
      _dryweight        = Double.NaN;
      _engines          = null;
      _error            = null;
      _errorListeners   = null;
      _feeder           = null;
      _fuelSystem       = null;
      _kill             = false;
      _maxWeight        = Double.NaN;
      _stageNumber      = -1;
      _modelNumber      = -1;
      _totalEngines     = -1;
      _isError          = false;
      _rt0              = null;
      _start            = false;
      _state            = null;
      _tolerance        = Double.NaN;
      _weight           = Double.NaN;
   };

   /////////////////////////////Constructor///////////////////////////
   //
   //
   //
   public GenericStage(int number){
      if(number > 0){
         this._stageNumber = number;
      }
      this.setUpThread();
   }

   ///////////////////////////Private Methods/////////////////////////
   //
   //
   //
   private void calculateWeight(){}

   //Calculated the weight of the entire stage...
   //
   //
   private void calculateWeight(FuelSystemData fsd){
      this._weight = this._dryweight;
      List<TankData> data = fsd.tankData();
      Iterator<TankData> it = data.iterator();
      while(it.hasNext()){
         //4.  Add the Weight to the tank Weight
         this._weight += it.next().weight();
      }
   }

   //
   //
   //
   private void engineData(String file)throws IOException{
      for(int i = 0; i < this._totalEngines; ++i){
         Engine engine = new GenericEngine(i+1,this._stageNumber);
         engine.initialize(file);
         try{
            this._engines.add(engine);
         }
         catch(NullPointerException npe){
            this._engines = new LinkedList<Engine>();
            this._engines.add(engine);
         }
      }
   }

   //
   //
   //
   private void fuelSystem(String file)throws IOException{
      int stage = this._stageNumber;
      int engs  = this._totalEngines;
      this._fuelSystem = new GenericFuelSystem(stage,engs);
      this._fuelSystem.initialize(file);
   }

   //
   //
   //
   private void setStageData
   (
      List<Hashtable<String,String>> data
   ){
      //System.out.println(data);
      //will need to figure out which Stage it is...pretty simple
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String,String> ht = data.get(i);
         try{
            String num = ht.get("number");
            if(Integer.parseInt(num) == this._stageNumber){
               this._totalEngines=Integer.parseInt(ht.get("engines"));
               double dw = Double.parseDouble(ht.get("dryweight"));
               this._dryweight = dw;
               double mw = Double.parseDouble(ht.get("maxweight"));
               this._maxWeight = mw;
               int v = Integer.parseUnsignedInt(ht.get("model"),16);
               this._modelNumber = Integer.toUnsignedLong(v);
               double tol = Double.parseDouble(ht.get("tolerance"));
               this._tolerance = tol;
            }
         }
         catch(NumberFormatException npe){}
      }
   }

   //
   //
   //
   private void setUpThread(){
      String name = new String("Generic Stage "+ this._stageNumber);
      this._rt0   = new Thread(this, name);
      this._rt0.start();
   }

   //
   //
   //
   private void stageData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setStageData(read.readStageInfo());
      }
   }

   ///////////////ErrorListener Interface Implementation//////////////
   //
   //
   //
   public void errorOccurred(ErrorEvent e){}

   ///////////////////Stage Interface Implementation//////////////////
   //
   //
   //
   public void addDataFeeder(DataFeeder feeder){
      if(feeder != null){
         this._feeder = feeder;
         //Add Components as needed
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
   public void initialize(String file)throws IOException{
      if(this._stageNumber > -1){
         this.stageData(file);
         this.engineData(file);
         this.fuelSystem(file);
         
         this._calculatedWeight = this._dryweight;

         this._state = new LaunchStateSubstate(INIT,null,null,null);
         this._start = true;
      }
   }

   //
   //
   //
   public StageData monitor(){
      StageData sd = null;
      try{
         //return null for the time being...TBD...
         //Component Data will be fucking fed in...
         System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
         System.out.print("GenericStage"+this._stageNumber);
         System.out.println(".monitor()");
         System.out.println(this._feeder);
         System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
      }
      catch(NullPointerException npe){
         
      }
      finally{
         return sd;
      }
   }

   //
   //
   //
   public StageData monitorPrelaunch(){
      boolean isError = false;
      String error    = null;
      //Part of the StageData
      List<EngineData> engineData = new LinkedList<EngineData>();
      System.out.println("<Stage>.engines: "+this._engines.size());
      Iterator<Engine> it = this._engines.iterator();
      while(it.hasNext()){
         Engine e = (Engine)it.next();
         EngineData d = e.monitorPrelaunch();
         engineData.add(d);
         if(d.isError()){
            if(!isError){ isError = true; }
            if(error == null){ error = new String(d.error());}
            else{ error += d.error(); }
         }
         //engineData.add(it.next().monitorPrelaunch());
      }
      //now, get the Fuel System Data
      FuelSystemData fsd = this._fuelSystem.monitorPrelaunch();
      //get the weight of the fuel and engines and add to it...
      this.calculateWeight(fsd);
      if(fsd.isError()){
         if(!isError){ isError = true; }
         if(error == null){ error = new String(fsd.error()); }
         else{ error += fsd.error(); }
      }
      StageData sd = new GenericStageData(this._dryweight,
                                          error,
                                          this._modelNumber,
                                          isError,
                                          this._stageNumber,
                                          this._engines.size(),
                                          this._maxWeight,
                                          this._tolerance,
                                          this._weight,
                                          engineData,
                                          fsd);
      return sd;
   }

   //
   //
   //
   public StageData monitorIgnition(){ return null; }

   //
   //
   //
   public StageData monitorLaunch(){ return null; }

   ////////////////Runnable Interface Implementation//////////////////
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
               //this.calculateWeight();
               //this.isError()
               //if(this._isError){
               //   this.aleatErrorListeners();
               //}
               if(this._state.state() == INIT){
                  //Temporary Prints NEED TO REMOVE!
                  System.out.print("****Stage: ");
                  System.out.println(Thread.currentThread().getName());
                  System.out.print("****Stage: ");
                  System.out.println(Thread.currentThread().getId());
                  Thread.sleep(10000);
               }
            }
            else{
               Thread.sleep(1);
            }
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
