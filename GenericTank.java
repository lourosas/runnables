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
   private double calculateWeight(double cap){
      double weight = 0.;
      double g      = 9.81;  //Acceleration of Gravity...
      try{
         TankData td      = this.myTankData();
         double dryWeight = td.dryWeight();
         double den       = td.density();
         double mass      = cap * den;
         weight           = mass * g * dryWeight;
      }
      catch(NullPointerException npe){
         //Default value for now...stop gap...until hardare can
         //be queried
         weight = this._tankData.dryWeight();
      }
      finally{
         return weight;
      }
   }

   //
   //
   //
   private String capacityError(double capacity){
      double edge      = Double.NaN;
      double ul        = Double.NaN;
      double ll        = Double.NaN;
      double tolerance = Double.NaN;
      String error     = null;
      try{
         //I should this make this a fucking method!!!
         TankData td = this.myTankData();
         tolerance   = td.tolerance();
         if(!Double.isNaN(capacity) && !Double.isNaN(tolerance)){
            //Will need to separate this out by state!!
            if(this._state.state() == INIT){
               //Durring the INIT, the TANK SHOULD BE EMPTY!!
               ll = tolerance - 1.;
               ul = 1 - tolerance;
            }
            else if(this._state.state() == PRELAUNCH){}
            if(capacity < ll || capacity > ul){
               error = new String("Measured Capacity: "+capacity);
               if(capacity < ll){
                  error += " too low";
               }
               else if (capacity > ul){
                  error += " too high";
               }
            }
         }
      }
      catch(NullPointerException npe){
         error     = new String(npe.getMessage());
         error    += ":  Capacity Error Unkdown";
      }
      finally{
         return error;
      }
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
   private String flowError(double rate){
      double edge      = Double.NaN;
      double ul        = Double.NaN;
      double ll        = Double.NaN;
      double tolerance = Double.NaN;
      String error     = null;
      try{
         TankData td = this.myTankData();
         tolerance   = td.tolerance();
         if(!Double.isNaN(rate) && !Double.isNaN(tolerance)){
            //Separate out by state Tank instance determines this...
            if(this._state.state() == INIT){
               //Durring INIT, rate should be 0!!!
               ll = tolerance - 1.;
               ul = 1 - tolerance;
            }
            else if(this._state.state() == PRELAUNCH){}
            if(rate < ll || rate > ul){
               error = new String("Measured Flow Rate: "+rate);
               if(rate < ll){
                  error += " too low";
               }
               else if(rate > ul){
                  error += " too high";
               }
            }
         }
      }
      catch(NullPointerException npe){
         error  = new String(npe.getMessage());
         error += ":  Rate Error Unknown";
      }
      finally{
         return error;
      }
   }

   //
   //
   //
   private void isError
   (
      double capacity,
      double emptyRate,
      double temperature,
      double weight
   ){
      boolean isError = false;
      String  error   = new String();
      //Determine the Error For all the Measured data...
      String capError = this.capacityError(capacity);
      if(capError != null){
         error   = " " + capError;
         isError = true;
      }
      String flowError = this.flowError(emptyRate);
      if(flowError != null){
         error  += " "+flowError;
         isError = true;
      }
      String tempError = this.temperatureError(temperature);
      if(tempError != null){
         error  += " " + tempError;
         isError = true;
      }
      String weightError = this.weightError(weight);
      if(weightError != null){
         error += " " + weightError;
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
         TankData td = this.myTankData();
         capacity    = td.capacity();
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
         TankData td = this.myTankData();
         emptyRate   = td.emptyRate();
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
         TankData td = this.myTankData();
         temperature = td.temperature();
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
      double weight = this.calculateWeight(cap);
      //Determine the Error based on setting the data...
      this.setUpTankData(cap,er,temp,weight);
      this.isError(cap, er, temp, weight);
   }

   //The only way to do this to keep the code from exploding and 
   //repeating the same code in several methods...!!!
   //
   private TankData myTankData() throws NullPointerException{
      TankData tankData = null;
      try{
         RocketData            rd = this._feeder.rocketData();
         List<StageData>     list = rd.stages();
         Iterator<StageData>   it = list.iterator();
         boolean found            = false;
         while(!found && it.hasNext()){
            StageData sd = it.next();
            if(sd.stageNumber() == this._stageNumber){
               FuelSystemData  fsd      = sd.fuelSystemData();
               List<TankData>  tdList   = fsd.tankData();
               Iterator<TankData> t_it  = tdList.iterator();
               while(t_it.hasNext() && !found){
                  TankData td = t_it.next();
                  if(td.number() == this._tankNumber){
                     tankData = td;
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
         tankData = null;
         throw npe;
      }
      finally{
         return tankData;
      }
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
   private String temperatureError(double temp){
      double ll        = Double.NaN;
      double ul        = Double.NaN;
      double tolerance = Double.NaN;
      String error     = null;
      try{
         TankData td = this.myTankData();
         tolerance = td.tolerance();
         if(!Double.isNaN(temp) && !Double.isNaN(tolerance)){
            //Separate out by State
            if(this._state.state() == INIT){
               //Since there is nothing in the tank...ul, ll could be
               //anything within reason--no need to worry about tol
               ul = 373.15; //Boiling pt of water in K
               ll = 273.15; //Freezing pt of water in K
            }
            else if(this._state.state() == PRELAUNCH){}
            if(temp < ll || temp > ul){
               error = new String("Measured Temperature:  "+temp);
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
         error += ": Temperature Error Unknown";
      }
      finally{
         return error;
      }
   }

   //
   //
   //
   private  String weightError(double weight){
      double ll        = Double.NaN;
      double ul        = Double.NaN;
      double tolerance = Double.NaN;
      double refWeight = Double.NaN; //Referenced
      String error     = null;
      try{
         TankData td = this.myTankData();
         tolerance   = td.tolerance();
         refWeight   = td.weight(); //Get weight from Feeder
         boolean inputGood = !Double.isNaN(weight);
         inputGood &= !Double.isNaN(tolerance);
         inputGood &= !Double.isNaN(refWeight);
         if(inputGood){
            //Compare Calculated Weight to the referenced weight
            ll = refWeight*tolerance;
            ul = refWeight*(2-tolerance);
            if(weight < ll || weight > ul){
               error = new String("Measured Weight: "+weight);
               if(weight < ll){
                  error += "too low expected:  ";
               }
               else if(weight > ul){
                  error += "too high expected:  ";
               }
               //This is the current weight the tank should be
               error += refWeight;
            }
         }
      }
      catch(NullPointerException npe){}
      finally{
         return error;
      }
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
