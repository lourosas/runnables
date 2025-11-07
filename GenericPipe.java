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

   private boolean  _kill;
   private int      _tank;  //Tank Number (1,2)
   private int      _stage; //Stage Number (1...total stages)
   private boolean  _start;
   //Pipe Number for the Tank (1,2)--corresponds to the engine...
   private int      _number; 
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

      _kill                = false;
      _tank                = -1;
      _stage               = -1;
      _start               = false;
      _number              = -1; //Engine
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
   private String flowError(double flow){
      double ll          = Double.NaN;
      double ul          = Double.NaN;
      String error       = null;
      double tolerance   = Double.NaN;
      PipeData pd        = this.myPipeData();
      try{
         tolerance = pd.tolerance();
         if(!Double.isNaN(flow) && !Double.isNaN(tolerance)){
            if(this._state.state() == INIT){
               //INIT there should be NO FLOW through the pipe
               //ANY FLOW throgh the pipe is an error!!!
               ll = tolerance - 1.;
               ul = 1. - tolerance;
            }
            else if(this._state.state() == PRELAUNCH){}
            if(flow < ll || flow > ul){
               error  = new String("Stage "+pd.stage());
               error += " Tank "+pd.tank()+" Pipe "+pd.number();
               error += " Pipe Flow Error: ";
               if(flow < ll){
                  error += "too low";
               }
               else{
                  error += "too high";
               }
            }
         }
      }
      catch(NullPointerException npe){
         error  = new String(npe.getMessage());
         error += "\nStage "+pd.stage()+" Tank "+pd.tank();
         error += " Number "+pd.number();
         error += " Pipe Error Unknown";
      }
      finally{
         return error;
      }
   }

   //
   //
   //
   private void isError(double flow, double temp){
      boolean isError    = false;
      String  error      = new String();
      String  flowError  = this.flowError(flow);
      String  tempError  = this.temperatureError(temp);
      if(flowError != null){
         error  += " " + flowError;
         isError = true;
      }
      if(tempError != null){
         error   += " " + tempError;
         isError  = true;
      }
      if(isError){
         int     number= this._measuredPipeData.number();
         int     stage = this._measuredPipeData.stage();
         int     tank  = this._measuredPipeData.tank();
         double  tol   = this._measuredPipeData.tolerance();
         String  type  = this._measuredPipeData.type();
         PipeData pd   = new GenericPipeData(error,flow,number,isError,
                                             stage,tank,temp,tol,type);
         synchronized(this._obj){
            this._measuredPipeData = pd;
         }
      }
   }


   //The flow is measured in Liters/sec...converted to m^3/sec
   //
   //
   private double measureFlow(){
      double flow = 0.;
      try{
         PipeData pd = this.myPipeData();
         flow        = pd.flow();
      }
      catch(NullPointerException npe){
         //Temporary until actual hardware is available...
         flow = this._pipeData.flow();
      }
      finally{
         return flow;
      }
   }

   //
   //
   //
   private double measureTemperature(){
      double temp = Double.NEGATIVE_INFINITY;
      try{
         PipeData pd = this.myPipeData();
         temp        = pd.temperature();
      }
      catch(NullPointerException npe){
         //Temporary until actual hardware is available...
         temp = this._pipeData.temperature();
      }
      finally{
         return temp;
      }
   }

   //
   //
   //
   private void monitorPipe(){
      //Measure the Current Flow
      double flow = this.measureFlow();
      //Measure the Temperature
      double temp = this.measureTemperature();
      this.setUpPipeData(flow, temp);
      this.isError(flow, temp);

   }

   //
   //
   //
   private PipeData myPipeData()throws NullPointerException{
      /*
       * This needs redoing to fit into the new DataFeeder
       * Implementation
      PipeData pipeData = null;
      try{
         RocketData          rd = this._feeder.rocketData();
         List<StageData>   list = rd.stages();
         Iterator<StageData> it = list.iterator();
         boolean          found = false;
      }
      catch(NullPointerException npe){
         pipeData = null;
         throw npe;
      }
      finally{
         return pipeData;
      }
      */
      return null;
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
            int tk     = this._tank;   //Tank Number
            int st     = this._stage;  //Stage Number
            int num    = this._number; //Engine Number
            int tank   = Integer.parseInt(ht.get("tanknumber"));
            int stage  = Integer.parseInt(ht.get("stage"));
            if((tk == tank) && (st == stage)){
               System.out.println("Engine:  "+num);
               System.out.println(ht);
               double rate=Double.parseDouble(ht.get("rate"));
               double temp=Double.parseDouble(ht.get("temperature"));
               double tol = Double.parseDouble(ht.get("tolerance"));
               PipeData pd = new GenericPipeData(null,  //Error
                                                 rate,  //Flow
                                                 num,   //Engine Number
                                                 false, //isError
                                                 st,    //Stage Number
                                                 tk,    //Tank Number
                                                 temp,  //Temperature
                                                 tol,   //Tolerance
                                                 null   //Fuel Type
                                                 );
               this._pipeData = pd;
            }
         }
         catch(NumberFormatException nfe){
            this._pipeData = null;
         }
      }
   }

   //
   //
   //
   private void setUpPipeData(double flow, double temp){
      PipeData  pd        = null;
      int number          = this._pipeData.number(); //Engine Number
      int stage           = this._pipeData.stage();
      int tank            = this._pipeData.tank();   //Tank Number
      double tolerance    = this._pipeData.tolerance();
      String fuelType     = this._pipeData.type();   //Fuel Type

      pd = new GenericPipeData(null,       //error
                               flow,       //flow
                               number,     //Engine Number
                               false,      //isError
                               stage,      //Stage Number
                               tank,       //Tank Number
                               temp,       //Temperature
                               tolerance,  //Tolerance
                               fuelType    //Fuel Type (can be null)
                               );
      synchronized(this._obj){
         this._measuredPipeData = pd;
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

   //
   //
   //
   private String temperatureError(double temp){
      double ll        = Double.NaN;
      double ul        = Double.NaN;
      double tolerance = Double.NaN;
      String error     = null;
      PipeData pd      = this.myPipeData();

      try{
         if(!Double.isNaN(temp) && !Double.isNaN(tolerance)){
            if(this._state.state() == INIT){
               //Since there is NOTHING in the Tank, there should be
               //NOTHING in the Pipe!!!
               ul = 373.15; //Boiling point of Water in K
               ll = 273.15; //Freezing point of Water in K
            }
            else if(this._state.state() == PRELAUNCH){}
            if(temp < ll || temp > ul){
               error  = new String("Stage "+pd.stage());
               error += " Tank "+pd.tank()+" Pipe "+pd.number();
               error += " Pipe Temperature Error: ";
               if(temp < ll){
                  error += " too low ";
               }
               else{
                  error += "too high ";
               }
            }
         }
      }
      catch(NullPointerException npe){
         error  = new String(npe.getMessage());
         error += "\nStage "+pd.stage()+" Tank "+pd.tank();
         error += " Number "+pd.number();
         error += " Pipe Error Unknown";
      }
      finally{
         return error;
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
               this.monitorPipe();
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
