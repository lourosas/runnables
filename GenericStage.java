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

   private double              _calculatedWeight;//Dry+Wet Weight
   private List<Engine>        _engines;
   private List<ErrorListener> _errorListeners;
   private DataFeeder          _feeder;
   private FuelSystem          _fuelSystem;
   private boolean             _kill;
   private Object              _obj;
   private Thread              _rt0;
   private boolean             _start;
   private StageData           _stageData;
   private int                 _stageNumber;
   private LaunchStateSubstate _state;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _calculatedWeight = Double.NaN;
      _engines          = null;
      _errorListeners   = null;
      _feeder           = null;
      _fuelSystem       = null;
      _kill             = false;
      _obj              = null;
      _rt0              = null;
      _start            = false;
      _stageData        = null;
      _stageNumber      = -1;
      _state            = null;
   };

   /////////////////////////////Constructor///////////////////////////
   //
   //
   //
   public GenericStage(int number){
      if(number > 0){
         this._stageNumber = number;
      }
      this._obj = new Object();
      this.setUpThread();
   }

   ///////////////////////////Private Methods/////////////////////////
   //This whole thing will FUCKING CHANGE!! As Design/Development
   //Progresses!!!
   //
   private double calculateWeight
   (
      List<EngineData> list,
      FuelSystemData   fuelSystem
   ){
      synchronized(this._obj){
         double calcWeight = Double.NaN;
         try{
            //Need to get the entire Rocket Data...
            RocketData rd = this._feeder.rocketData();
            //From there, get the StageData!!!
            List<StageData> sdl    = rd.stages();
            Iterator<StageData> it = sdl.iterator();
            while(it.hasNext()){
               StageData sd = it.next();
               if(sd.stageNumber() == this._stageData.stageNumber()){
                  try{
                     //THIS IS A COMPLETELY TEMPORARY WAY of getting 
                     //the STAGE WEIGHT!!!  It WILL BE CALCULATED FROM
                     //the FuelSystemData! as well as the DryWeight
                     //AND the ENGINE WEIGHT!!!  It will NEED TO BE
                     //Calculated!!
                     //MONITOR ALL THE WAY DOWN!!!
                     calcWeight = sd.weight();
                  }
                  catch(NullPointerException npe){
                     //For the time being!!
                     calcWeight = this._stageData.dryWeight();
                  }
               }
            }
         }
         catch(NullPointerException npe){
            //A fix for the time being--when not being simulated,
            //but ACTUALLY Meeasured!!!
            calcWeight = this._stageData.weight();
         }
         finally{
            return calcWeight;
         }
      }
   }

   //Calculated the weight of the entire stage...
   //
   //
   private void calculateWeight(FuelSystemData fsd){
   }

   //
   //
   //
   private void engineData(String file)throws IOException{
      for(int i = 0; i < this._stageData.numberOfEngines(); ++i){
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
   String error(double weight){
      String  error      = null;
      synchronized(this._obj){
         double  dw         = this._stageData.dryWeight();
         double  mw         = this._stageData.maxWeight();
         double  tol        = this._stageData.tolerance();
         boolean isError    = false;
         double  edge       = Double.NaN;
         double  ul         = Double.NaN;
         double  ll         = Double.NaN;
         double  lim        = 1. - tol;
         lim                = Math.round(lim*100.)/100.;
         boolean inputGood  = !Double.isNaN(dw) && !Double.isNaN(mw);
         boolean measGood   = !Double.isNaN(weight);

         if(inputGood && measGood){
            if(this._state.state() == INIT){
               ll = dw * tol;
               ul = dw * (2. - tol);
            }
            else if(this._state.state() == PRELAUNCH){}
            if(weight < ll || weight > ul){
               error = new String("Calculated Weight:  ");
               if(weight < ll){
                  error += "too low";
               }
               else if(weight > ul){
                  error += "too high";
               }
            }
         }
      }
      return error;
   }

   //
   //
   //
   private boolean  isError(double weight){
      boolean isError = false;
      synchronized(this._obj){
         double  dw         = this._stageData.dryWeight();
         double  mw         = this._stageData.maxWeight();
         double  tol        = this._stageData.tolerance();
         double  edge       = Double.NaN;
         double  ul         = Double.NaN;
         double  ll         = Double.NaN;
         double  lim        = 1. - tol;
         lim                = Math.round(lim*100.)/100.;
         boolean inputGood  = !Double.isNaN(dw) && !Double.isNaN(mw);
         boolean measGood   = !Double.isNaN(weight);

         if(inputGood && measGood){
            if(this._state.state() == INIT){
               ll = dw * tol;
               ul = dw *(2.-tol);
            }
            else if(this._state.state() == PRELAUNCH){}
            if(weight < ll || weight > ul){
               isError = true;
            }
         }
      }
      return isError;
   }

   //
   //
   //
   private void fuelSystem(String file)throws IOException{
      int stage = this._stageData.stageNumber();
      int engs  = this._stageData.numberOfEngines();
      this._fuelSystem = new GenericFuelSystem(stage,engs);
      this._fuelSystem.initialize(file);
   }

   //
   //
   //
   private List<EngineData> monitorEngines(){
      synchronized(this._obj){
         //For the Time Being return null
         return null;
      }
   }

   private FuelSystemData monitorFuelSystem(){
      synchronized(this._obj){
         //For the Time Being, reutrn null
         return null;
      }
   }

   //
   //
   //
   private void monitorStage(){
      String         error = null;
      List<EngineData> eng = this.monitorEngines();
      FuelSystemData   fs  = this.monitorFuelSystem();
      double weight        = this.calculateWeight(eng, fs);
      boolean isError      = this.isError(weight);
      if(isError){
         this.error(weight);   
      }
      this.setStageData(eng,fs,weight,isError,error);
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
      this._stageData = null;  //Erase all previous data
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String,String> ht = data.get(i);
         try{
            String num = ht.get("number");
            if(Integer.parseInt(num) == this._stageNumber){
               //Total Engines
               int te       = Integer.parseInt(ht.get("engines"));
               //Dry Weight
               double dw    = Double.parseDouble(ht.get("dryweight"));
               //Max Weight
               double mw    = Double.parseDouble(ht.get("maxweight"));
               //Model Number
               String model = ht.get("model");
               long mn      = Integer.parseUnsignedInt(model,16);
               //Tolerance
               double tol   = Double.parseDouble(ht.get("tolerance"));
               //Stage Number
               int sn       = this._stageNumber;
               this._stageData = new GenericStageData(
                                       dw,    //Dry Weight
                                       null,  //Error (Init to NUll)
                                       mn,    //Model Number
                                       false, //isError Boolean
                                       sn,    //Stage Number
                                       te,    //Total Engines
                                       mw,    //Max Weight
                                       tol,   //Tolerance
                                       Double.NaN, //Calculated Weight
                                       null,  //Engine Data
                                       null   //Fuel System Data
                                       );
            }
         }
         catch(NumberFormatException npe){
            this._stageData = null;
         }
      }
   }

   //
   //
   //
   private void setStageData
   (
      List<EngineData>   engines,
      FuelSystemData  fuelSystem,
      Double          calcWeight,
      boolean            isError,
      String               error
   ){
      double dw       = this._stageData.dryWeight();
      //Somehow, will need to set the error in addition
      double mw       = this._stageData.maxWeight();
      long   model    = this._stageData.model();
      int    en       = this._stageData.numberOfEngines();
      int    sn       = this._stageData.stageNumber();
      double to       = this._stageData.tolerance();
      StageData sd = new GenericStageData(dw,    //Dry Weight
                                          error, //error
                                          model, //Model
                                          isError, //Is Error
                                          sn,    //Stage Number
                                          en,    //No. Engines
                                          mw,    //Max Weight
                                          to,    //Tolerance
                                          calcWeight, //Calc Weight
                                          engines,//Engines List
                                          fuelSystem//Fuel System
                                          );
      this._stageData = sd;
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
         this._state = new LaunchStateSubstate(INIT,null,null,null);
         this._start = true;
      }
   }

   //
   //
   //
   public StageData monitor(){
      //This needs to be fucking fixed!!!!
      synchronized(this._obj){
         //return null for the time being...TBD...
         //Component Data will be fucking fed in...
         System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
         System.out.print("GenericStage"+this._stageNumber);
         System.out.println(".monitor()");
         System.out.println(this._feeder);
         System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
         return this._stageData;
      }
   }

   //
   //
   //
   public StageData monitorPrelaunch(){
      /* NO LONGER NEEDED!!!!
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
                                          this._calculatedWeight,
                                          engineData,
                                          fsd);
      return sd;
      */
      return null;
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
               this.monitorStage();
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
