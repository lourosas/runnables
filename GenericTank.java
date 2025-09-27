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

import java.util.*;
import java.io.*;
import rosas.lou.runnables.*;

public class GenericTank implements Tank, Runnable{
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State INIT      = null; 
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null; 

   private boolean    _kill;
   private int        _stageNumber;
   private int        _tankNumber;
   private boolean    _start;

   private DataFeeder          _feeder;
   private List<ErrorListener> _errorListeners; 
   private LaunchStateSubstate _state;
   private Object              _obj;
   private Thread              _rt0;
   private TankData            _tankData;
   private TankData            _measuredTankData;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _kill                = false;
      _obj                 = null;
      _stageNumber         = -1;
      _tankNumber          = -1;
      _start               = false;

      _feeder           = null;
      _errorListeners   = null;
      _rt0              = null;
      _state            = null;
      _measuredTankData = null;
      _tankData         = null;
   };

   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public GenericTank(int stage, int number){
      if(stage > 0){
         this._stageNumber = stage;
      }
      if(number > 0){
         this._tankNumber  = number;
      }
      this._obj = new Object();
      this.setUpThread();
   }

   ////////////////////////////Private Methods////////////////////////
   //
   //
   //
   private double calculateWeight(){
      //Stop Gap for now...will need to calculate the weight (and
      //eventually the mass) based on the density and measured
      //volume...return the dry weight for the time being...
      return this._tankData.dryWeight();
   }

   //
   //
   //
   private String capacityError(){
      String error = null;
      //This will need to change...do not need to do this...
      //this is more about weight...
      /*
      double g = 9.81;
      if(this._state.state() == INIT){}
      if(this._state.state() == PRELAUNCH){
         //Durring Prelaunch, the capacity must be with in tolerance,
         //since there should be NO FLOW in the Tank in Pre-Launch!
         double limit  = this._capacity * this._tolerance;
         //F = ma measured in Newtons...weight capcity
         double weight = this.convertToWeight(this._capacity);
         double mw     = this.convertToWeight(this._measuredCapacity);
         double mc     = this._measuredCapacity;
         if(this._measuredCapacity < limit){
            this._isError  = true;
            String s = new String("\nPre-Launch Error: Tank too low");
            if(this._error == null){
               this._error = new String(s);
            }
            else{
               this._error += s;
            }
            this._error += "\nMeasured Capacity: "+mc;
            this._error += "\nExpected Capacity: "+this._capacity;
            this._error += "\nMeasured Weight:   "+mw;
            this._error += "\nExpected Weight:   "+weight;
         }
      }
      */
      return error;
   }

   //
   //
   //
   private double convertToMass(double volume){
      //Convert from Liters to cubic meters...
      //Multiply by density to get mass...
      double mass = (volume/1000)*this._tankData.density();
      return mass;
   }

   //Take a volume and convert to weight (Newtons)
   //Volume in Liters
   //
   private double convertToWeight(double volume){
      double g = 9.81;
      //Convert from Liters to cubic meters...
      //Multiply by desnsity to get mass...
      //Multiply by g to get weight-->F = ma...
      double weight = (volume/1000)*this._tankData.density()*g;
      return weight;
   }

   //
   //
   //
   private String flowError(){
      String error = null;
      /*
      if(this._state.state() == INIT){}
      else if(this._state.state() == PRELAUNCH){
         //At prelaunch, there literally better not be ANY flow!
         double err = 0.05;
         if(this._measuredEmptyRate >= err){
            double er = this._measuredEmptyRate;
            this._isError = true;
            double mass = this.convertToMass(this._measuredEmptyRate);
            String s = new String("\nTank Pre-Launch Error: Flow");
            if(this._error == null){
               this._error = new String(s);
            }
            else{
               this._error += s;
            }
            this._error += "\nMeasured Flow:      " + er;
            this._error += "\nMeasured Mass Loss: " + mass;
         }
      }
      */
      return error;
   }

   //
   //
   //
   private void isError(){
      boolean isError = false;
      String  error   = new String();
      //Determine the Error For all the Measured data...
      String capError = this.capacityError();
      if(capError != null){
         error   = capError;
         isError = true;
      }
      String flowError = this.flowError();
      if(flowError != null){
         error  +=  flowError;
         isError = true;
      }
      String tempError = this.temperatureError();
      if(tempError != null){
         error  += tempError;
         isError = true;
      }
      String weightError = this.weightError();
      if(weightError != null){
         error += weightError;
         isError = true;
      }
      if(isError){
         double cap  = this._measuredTankData.capacity();
         double den  = this._measuredTankData.density();
         double dw   = this._measuredTankData.dryWeight();
         double er   = this._measuredTankData.emptyRate();
         String fuel = this._measuredTankData.fuel();
         int    tn   = this._measuredTankData.number();
         int    sn   = this._measuredTankData.stage();
         double te   = this._measuredTankData.temperature();
         double to   = this._measuredTankData.tolerance();
         double we   = this._measuredTankData.weight();
         TankData td = new GenericTankData(cap,den,dw,er,error,
                                           fuel,isError,tn,sn,te,
                                           to,we);
         synchronized(this._obj){
            this._measuredTankData = td;
         }
      }
   }

   //The Capacity is measured in liters--converted into m^3
   //
   //
   private double measureCapacity(){
      double capacity =   0.;
      double g        = 9.81;
      try{
         RocketData        rd   = this._feeder.rocketData();
         List<StageData> list   = rd.stages();
         Iterator<StageData> it = list.iterator();
         while(it.hasNext()){
            StageData sd = it.next();
            if(sd.stageNumber() == this._stageNumber){
               FuelSystemData fsd      = sd.fuelSystemData();
               List<TankData> tdList   = fsd.tankData();
               Iterator<TankData> t_it = tdList.iterator(); 
               while(t_it.hasNext()){
                  TankData td = t_it.next();
                  if(td.number() == this._tankNumber){
                     //Get the current capacity
                     capacity = td.capacity();
                  }
               }
            }
         }
      }
      catch(NullPointerException npe){
         //Default the value for now...stop gap...until hardware
         //Can be queried
         capacity = this._tankData.capacity();
      }
      finally{
         return capacity;
      }
   }

   //
   //
   //
   private double measureEmptyRate(){
      double emptyRate = 0.;
      try{
         RocketData      rd     = this._feeder.rocketData();
         List<StageData> list   = rd.stages();
         Iterator<StageData> it = list.iterator();
         while(it.hasNext()){
            StageData sd = it.next();
            if(sd.stageNumber() == this._stageNumber){
               FuelSystemData fsd       = sd.fuelSystemData();
               List<TankData> tdList    = fsd.tankData();
               Iterator<TankData> t_it  = tdList.iterator();
               while(t_it.hasNext()){
                  TankData td = t_it.next();
                  if(td.number() == this._tankNumber){
                     //Get the current empty rate
                     emptyRate = td.emptyRate();
                  }
               }
            }
         }
      }
      catch(NullPointerException npe){
         //Stop Gap...until connected up to actual hardware...
         emptyRate = this._tankData.emptyRate();
      }
      finally{
         return emptyRate;
      }
   }

   //
   //
   //
   private double measureTemperature(){
      double temperature = 0.;
      try{
         RocketData       rd    = this._feeder.rocketData();
         List<StageData>  list  = rd.stages();
         Iterator<StageData> it = list.iterator();
         while(it.hasNext()){
            StageData sd = it.next();
            if(sd.stageNumber() == this._stageNumber){
               FuelSystemData fsd        = sd.fuelSystemData();
               List<TankData> tdList     = fsd.tankData();
               Iterator<TankData> t_it   = tdList.iterator();
               while(t_it.hasNext()){
                  TankData td = t_it.next();
                  if(td.number() == this._tankNumber){
                     //Get the current temprature
                     temperature = td.temperature();
                  }
               }
            }
         }
      }
      catch(NullPointerException npe){
         //Temporary Stop Gap...until can test with real HW...
         temperature = this._tankData.temperature();
      }
      finally{
         return temperature;
      }
   }

   //
   //
   //
   private void monitorTank(){
      double cap    = this.measureCapacity();
      double er     = this.measureEmptyRate();
      double temp   = this.measureTemperature();
      double weight = this.calculateWeight();
      //Determine the Error based on setting the data...
      this.setUpTankData(cap,er,temp,weight);
      this.isError();
   }

   //
   //
   //
   private void setTankData
   (
      List<Hashtable<String,String>> data
   ){
      //Get the Stage and Tank numbers for comparison
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String,String> ht = data.get(i);
         try{
            int stage = Integer.parseInt(ht.get("stage"));
            int num   = Integer.parseInt(ht.get("number"));
            if((stage==this._stageNumber) && (num==this._tankNumber)){
               double den = Double.parseDouble(ht.get("density"));
               int x = Integer.parseUnsignedInt(ht.get("model"),16);
               long model = Integer.toUnsignedLong(x);
               double cap = Double.parseDouble(ht.get("capacity"));
               double dw  = Double.parseDouble(ht.get("dryweight"));
               String fuel= ht.get("fuel");
               double rate= Double.parseDouble(ht.get("rate"));
               double temp=Double.parseDouble(ht.get("temperature"));
               double tol = Double.parseDouble(ht.get("tolerance"));
               //Since it is initialized...weight = dryweight
               TankData td = new GenericTankData(cap, //Capacity
                                                 den, //Density
                                                 dw,  //Dry Weight
                                                 rate,//Empty Rate
                                                 null,//Error
                                                 fuel,
                                                 false,//isError
                                                 num,  //Tank Number
                                                 stage,//Stage
                                                 temp,
                                                 tol,
                                                 dw    //Init weight
                                                 );
               this._tankData = td;
            }
         }
         catch(NumberFormatException nfe){
            this._tankData = null;
         }
      }
   }

   //
   //
   //
   private void setUpTankData
   (
      double capacity,
      double emptyRate,
      double temp,
      double weight
   ){
      TankData td = null;
      double den  = this._tankData.density();
      double dw   = this._tankData.dryWeight();
      String fuel = this._tankData.fuel();
      int    num  = this._tankData.number();
      int    stage= this._tankData.stage();
      double tol  = this._tankData.tolerance();
      
      td = new GenericTankData(capacity,  //Measure Capacity
                               den,       //Density
                               dw,        //Dry Weight
                               emptyRate, //Empty Rate
                               null,      //error (TBD)
                               fuel,      //fuel type
                               false,     //no errors (yet)
                               num,       //tank number
                               stage,     //Current Stage
                               temp,      //measured temperature
                               tol,       //tolerance
                               weight     //measured weight
                               );
      synchronized(this._obj){
         this._measuredTankData = td;
      }
   }

   //
   //
   //
   private void setUpThread(){
      String name = new String("Tank: "+this._stageNumber+", ");
      name += this._tankNumber;
      this._rt0 = new Thread(this, name);
      this._rt0.start();
   }

   //
   //
   //
   private void tankData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setTankData(read.readTankDataInfo());
      }
   }

   //Fuel Temp MUST be the same regargless of State!!!
   //
   //
   private String temperatureError(){
      String tempError = null;
      /*
      double ul = this._temperature*(2 - this._tolerance);
      double ll = this._temperature*this._tolerance;
      double m  = this._measuredTemperature;
      //Error out based on tradtional limit ranges...
      if(m < ll || m > ul){
         this._isError = true;
         String s = new String("\nTank Tempearture Error:  ");
         if(this._error == null){
            this._error = new String(s);
         }
         else{
            this._error += s;
         }
         this._error += "\nRequired Temp:  "+this._temperature;
         this._error += "\nMeasured Temp:  "+m;
      }
      */
      return tempError;
   }

   //
   //
   //
   private  String weightError(){
      String weightError = null;
      return weightError;
   }
   ///////////////////////Tank Interface Methods//////////////////////
   //
   //
   //
   public TankData monitor(){
      synchronized(this._obj){
         return this._measuredTankData;
      }
   }

   //
   //
   //
   public void initialize(String file)throws IOException{
      if(this._stageNumber > 0 && this._tankNumber > 0){
         this.tankData(file);
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

   //////////////////////////Runnble Interface////////////////////////
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
               //Something different to do...
               this.monitorTank();
               if(this._state.state() == INIT){
                  //For later determiniation
                  Thread.sleep(5000);
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
