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

public class GenericPump implements Pump, Runnable{
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State INIT      = null; 
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null;

   private boolean _kill;
   private int     _stage;
   private boolean _start;
   private int     _tank;

   private DataFeeder          _feeder;
   private List<ErrorListener> _errorListeners;
   private LaunchStateSubstate _state;
   private Object              _obj;
   private PumpData            _pumpData;
   private PumpData            _measuredPumpData;
   private Thread              _rt0;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;
      
      _kill                = false;
      _stage               = -1;
      _start               = false;
      _tank                = -1;

      _feeder              = null;
      _errorListeners      = null;
      _state               = null;
      _obj                 = null;
      _pumpData            = null;
      _measuredPumpData    = null;
      _rt0                 = null;
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
      this._obj = new Object();
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private String flowError(double flow){
      double ul        = Double.NaN;
      double ll        = Double.NaN;
      double tolerance = Double.NaN;
      String error     = null;
      PumpData pd      = this.myPumpData();

      try{
         tolerance   = pd.tolerance();
         if(!Double.isNaN(flow) && !Double.isNaN(tolerance)){
            //Separate out by State
            if(this._state.state() == INIT){
               //INIT there should be NO FLOW through the pump
               //ANY FLOW through the pump is an error!!!
               ll = tolerance - 1.;
               ul = 1 - tolerance;
            }
            else if(this._state.state() == PRELAUNCH){}
            if(flow < ll || flow > ul){
               error  = new String("Stage "+pd.stage());
               error += "Tank: "+pd.index()+" Pump Flow Error: ";
               if(flow < ll){
                  error += "too low";
               }
               else if(flow > ul){
                  error += "too high";
               }
            }
         }
      }
      catch(NullPointerException npe){
         error  = new String(npe.getMessage());
         error += "\nStage "+pd.stage()+" Tank "+pd.index();
         error += " Pump Error Unknown";
      }
      finally{
         return error;
      }
   }

   //
   //
   //
   private void isError(double flow, double temperature){
      boolean isError  = false;
      String  error    = new String();
      String flowError = this.flowError(flow);
      String tempError = this.temperatureError(temperature);
      if(flowError != null){
         error   += " " + flowError;
         isError  = true;
      }
      if(tempError != null){
         error  += " " + tempError;
         isError = true;
      }
      if(isError){
         int    idx  = this._measuredPumpData.index();
         int    stg  = this._measuredPumpData.stage(); 
         double tol  = this._measuredPumpData.tolerance();
         String typ  = this._measuredPumpData.type();
         PumpData pd = new GenericPumpData(error,flow,idx,isError,
                                           stg,temperature,tol,typ);
         synchronized(this._obj){
            this._measuredPumpData = pd;
         }
      }
   }


   //The Fuel temperature in the pump MUST be within rage
   //REGARLESS of State!!!
   //
   //
   private String temperatureError(double temp){
      double ul        = Double.NaN;
      double ll        = Double.NaN;
      double tolerance = Double.NaN;
      String error     = null;
      PumpData pd      = this.myPumpData();
      
      try{
         tolerance  = pd.tolerance();
         if(!Double.isNaN(temp) && !Double.isNaN(tolerance)){
            //Separate out by State
            if(this._state.state() == INIT){
               //Since there is nothing in the Tank..and thuns,
               //nothing in the pump, no need to worry about tol
               ul = 373.15; //Boiling Point of Water in K
               ll = 273.15; //Freezing Point of Water in K
            }
            else if(this._state.state() == PRELAUNCH){}
            if(temp < ll || temp > ul){
               error  = new String("Stage "+pd.stage());
               error += "Tank: "+pd.index();
               error += " Pump Temerature Error: ";
               if(temp < ll){
                  error += " too low";
               }
               else if(temp > ul){
                  error += " too high";
               }
            }
         }
      }
      catch(NullPointerException npe){
         error  = new String(npe.getMessage());
         error += "\nStage "+pd.stage()+" Tank "+pd.index();
         error += " Pump Error Unknown";
      }
      finally{
         return error;
      }
   }

   //The flow is measured in Liters/sec...
   //
   //
   private double measureFlow(){
      double flow = 0.;
      try{
         PumpData pd = this.myPumpData();
         flow        = pd.flow();
      }
      catch(NullPointerException npe){
         //Temporary until actual Hardware is available...
         flow = this._pumpData.flow();
      }
      finally{
         return flow;
      }
   }

   //
   //
   //
   private double measureTemperature(){
      double temperature = Double.NEGATIVE_INFINITY;
      try{
         PumpData pd = this.myPumpData();
         temperature = pd.temperature();
      
      }
      catch(NullPointerException npe){
         //Temporary until actual Hardware is available...
         temperature = this._pumpData.temperature();
      }
      finally{
         return temperature;
      }
   }

   //
   //
   //
   private void monitorPump(){
      //Measure the Current Flow
      double flow = this.measureFlow();
      //Measure the Temperature
      double temp = this.measureTemperature();
      this.setUpPumpData(flow, temp);
      this.isError(flow, temp);

   }


   //
   //
   //
   private PumpData myPumpData()throws NullPointerException{
      return null;
      /*
       * Need to redo to fit the new DataFeeder Interface
      PumpData pumpData = null;
      try{
         RocketData            rd = this._feeder.rocketData();
         List<StageData>     list = rd.stages();
         Iterator<StageData>   it = list.iterator();
         boolean            found = false;
         while(!found && it.hasNext()){
            StageData sd = it.next();
            if(sd.stageNumber() == this._stage){
               FuelSystemData     fsd    = sd.fuelSystemData();
               List<PumpData>     pdList = fsd.pumpData();
               Iterator<PumpData> p_it   = pdList.iterator();
               while(p_it.hasNext() && !found){
                  PumpData pd = p_it.next();
                  if(pd.index() == this._tank){
                     pumpData = pd;
                     found    = true;
                  }
               }
            }
         }
         if(!found){
            throw new NullPointerException();
         }
      }
      catch(NullPointerException npe){
         pumpData = null;
         throw npe;
      }
      finally{
         return pumpData;
      }
      */
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

   //This this going to need to change!!!!
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
               double rate = Double.parseDouble(ht.get("rate"));
               Double d   = Double.parseDouble(ht.get("temperature"));
               double temp = d;
               d = Double.parseDouble(ht.get("tolerance"));
               double tol = d;
               PumpData pd = new GenericPumpData(null,  //Error
                                                 rate,  //rate
                                                 tank,  //Tank Number
                                                 false, //isError
                                                 stage, //Stage
                                                 temp,  //Temperature
                                                 tol,   //Tolerance
                                                 null   //Fuel Type
                                                 );
               this._pumpData = pd;
            }
         }
         catch(NumberFormatException nfe){
            this._pumpData = null;
         }
      }
   }

   //
   //
   //
   private void setUpPumpData(double flow, double temp){
      PumpData      pd = null;
      int         tank = this._pumpData.index(); //Tank Number
      int        stage = this._pumpData.stage();
      double tolerance = this._pumpData.tolerance();
      String type      = this._pumpData.type();

      pd = new GenericPumpData(null,       //error
                               flow,       //rate
                               tank,       //tank
                               false,      //isError
                               stage,      //stage
                               temp,       //temperature
                               tolerance,  //tolerance
                               type        //Fuel Type
                               );
      synchronized(this._obj){
         this._measuredPumpData = pd;
      }
   }

   //
   //
   //
   private void setUpThread(){
      String name = new String("Pump: "+this._stage+", "+this._tank);
      this._rt0 = new Thread(this,name);
      this._rt0.start();
   }

   ///////////////////Pump Interface Implementation///////////////////
   //
   //
   //
   public PumpData monitor(){
      synchronized(this._obj){
         return this._measuredPumpData;
      }
   }

   //
   //
   //
   public void initialize(String file)throws IOException{
      if((this._stage > 0) && (this._tank > 0)){
         this.pumpData(file);
      }
      this._state = new LaunchStateSubstate(INIT,null,null,null);
      this._start = true;
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

   /////////////////////////Runnable Interface////////////////////////
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
               this.monitorPump();
               if(this._state.state() == INIT){
                  //For later determination
                  Thread.sleep(15000);
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
