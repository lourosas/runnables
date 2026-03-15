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

public class GenericPayload implements Payload, Runnable{
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State INIT                      = null;
   private LaunchStateSubstate.State PRELAUNCH                 = null;
   private LaunchStateSubstate.State IGNITION                  = null;
   private LaunchStateSubstate.State LAUNCH                    = null;
   private LaunchStateSubstate.PreLaunchSubstate SET           = null;
   private LaunchStateSubstate.PreLaunchSubstate CONT          = null;
   private LaunchStateSubstate.PreLaunchSubstate FUEL          = null;
   private LaunchStateSubstate.PreLaunchSubstate HOLD          = null;
   private LaunchStateSubstate.IgnitionSubstate  IGN           = null;
   private LaunchStateSubstate.IgnitionSubstate  BUP           = null;
   private LaunchStateSubstate.AscentSubstate    STG           = null;
   private LaunchStateSubstate.AscentSubstate    IGNE          = null;

   private boolean  _kill;

   private DataFeeder            _feeder;
   private List<ErrorListener>   _errorListeners;
   private List<SystemListener>  _systemListeners;
   private LaunchStateSubstate   _state;
   private Object                _obj;
   private Thread                _rt0;
   private PayloadData           _payloadData;
   private PayloadData           _measuredPayloadData;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;
      SET       = LaunchStateSubstate.PreLaunchSubstate.SET;
      CONT      = LaunchStateSubstate.PreLaunchSubstate.CONTINUE;
      FUEL      = LaunchStateSubstate.PreLaunchSubstate.FUELING;
      HOLD      = LaunchStateSubstate.PreLaunchSubstate.HOLD;
      IGN       = LaunchStateSubstate.IgnitionSubstate.IGNITION;
      BUP       = LaunchStateSubstate.IgnitionSubstate.BUILDUP;
      STG       = LaunchStateSubstate.AscentSubstate.STAGING;
      IGNE      = LaunchStateSubstate.AscentSubstate.IGNITEENGINES;

      _kill                = false;

      _feeder              = null;
      _errorListeners      = null;
      _systemListeners     = null;
      _state               = null;
      _obj                 = null;
      _rt0                 = null;
      _payloadData         = null;
      _measuredPayloadData = null; 
   };
   
   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericPayload(){
      this._obj = new Object();
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void alertErrorListeners(){
      String error   = null;
      PayloadData pd = null;
      synchronized(this._obj){
         error = this._measuredPayloadData.error();
         pd    = this._measuredPayloadData;
      }
      try{
         Iterator<ErrorListener> it = this._errorListeners.iterator();
         while(it.hasNext()){
            it.next().errorOccurred(new ErrorEvent(this,pd,error));
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void alertSubscribers(){
      PayloadData pd         = null;
      LaunchStateSubstate ss = this._state;
   
      String event = ss.state() + ", " + ss.ascentSubstate();
      event += ", " + ss.ignitionSustate() + ", ";
      event += ss.prelaunchSubstate();
      synchronized(this._obj){
         pd = this._measuredPayloadData;
      }
      try{
         Iterator<SystemLister> it = null;
         it = this._systemListeners.iterator();
         while(it.hasNext()){
            MissionSystemEvent mse = null;
            mse = new MissionSystemEvent(this,pd,event,ss);
            it.next().update(mse);
         }
      }
      catch(NullPointerException npe){}
   }

   //Check the Current Weight, O2 Percent, Temperature...
   //
   //
   private void checkErrors(){
      String err      = new String();
      boolean isError = false;
      PayloadData pd     = null;
      synchronized(this._obj){
         pd = this._payloadData;
      }
      //Grab the immutables first
      int crew   = pd.crew();        double  dw  = pd.dryWeight();
      double em  = pd.emptyMass();   boolean isO = pd.isOccupied();
      double lm  = pd.loadedMass();  double  mw  = pd.maxWeight();
      String mod = pd.model();       double  tol = pd.tolerance();
      String type= pd.type();

      if(this.checkWeight()){
         err    += "Weight Error\n";
         isError = true;
      }
      if(this.checkO2Percent()){
         err    += "O2 Percent Error\n";
         isError = true;
      }
      if(this.checkTemperature()){
         err    += "Temperature Error\n";
         isError = true;
      }
      if(isError){
         synchronized(this._obj){
            pd = this._measuredPayloadData;
         }
         double cw    = pd.currentWeight();
         double o2Per = pd.o2Percent();
         double temp  = pd.tempreature();
         td = new GenericPayloadData(crew,     //Crew
                                     cw,       //Current Weight
                                     dw,       //Dry Weight
                                     em,       //Empty Mass
                                     err,      //Error
                                     isError,
                                     isO,      //Is Occupied
                                     lm,       //loaded mass
                                     mw,       //Max Weight
                                     mod,      //Model
                                     o2Per,    //O2 Perc
                                     temp,     //Temperature
                                     tol,      //Tolerance
                                     type);    //Type
         synchronized(this._obj){
            this._measuredPayloadData = pd;
         }
         this.alertErrorListeners();
      }
   }

   //
   //
   //
   private boolean checkO2Percent(){
      double  percent   = Double.NaN;
      double  min       = Double.NaN;
      boolean isOcc     = false;
      synchronized(this._obj){
         min     = this._payloadData.o2Percent();
         isOcc   = this._measuredPaylaodData.isOccupied();
         percent = this._measuredPayloadData.o2Percent();
      }
      //Honestly, do not need the State, just if occupied
      if(this._state.state() == INIT){
         if(!isOcc){
            min = 0.;  //If not occupied, O2 percent not significant
         }
      }
      return(percent < min);
   }

   //
   //
   //
   private boolean checkTemperature(){
      double   lim    = 5.;
      double   temp   = Double.NaN;
      double   min    = Double.NaN;
      double   max    = Double.NaN;
      boolean  isOcc  = false;
      synchronized(this._obj){
         min  = this._payloadData.temperature - lim;
         max  = this._payloadData.tempreature + lim;
         temp = this._measuredPayloadData.temperature(); 
      }
      if(this._state.state() == INIT){
         if(!isOcc){
            //If not occupied, reasonable cargo temp is between the
            //freezing and boiling points of H2O
            min = 273.15;
            max = 373.15;
         }
      }
      return ((temp < min) || (temp > max));
   }

   //
   //
   //
   private boolean checkWeight(){
      boolean isError   = false;
      double  weight    = Double.NaN;
      double  maxWeight = Double.NaN;
      double  minWeight = Double.NaN;
      boolean isOcc     = false;
      double  min       = Double.NaN;
      double  max       = Double.NaN;
      double  tolerance = Double.NaN;
      synchronized(this._obj){
         weight    = this._measuredPayloadData.currentWeight();
         isOcc     = this._meausredPayloadData.isOccupied();
         minWeight = this._payloadData.dryWeight();
         maxWeight = this._payloadData.maxWeight();
         tolerance = this._payloadData.tolerance();
      }
      //Technically, should be the same, regardless
      if(this._state.state() == INIT){
         if(isOcc){
            min = maxWeight * tolerance;
            max = maxWeight * (2 - tolerance);
         }
         else{
            min = minWeight * tolerance;
            max = minWeight * (2 - tolerance);
         }
      }
      return((wieght < min) || (weight > max));
      //return isError;
   }

   //
   //
   //
   private int getInitializedCrewData(Hashtable<String,String> ht){
      int crew = -1;
      try{
         crew = Integer.parseInt(ht.get("crew"));
      }
      catch(NumberFormatException nfe){
         crew = -1;
      }
      return crew;
   }

   //
   //
   //
   private double getInitializedDryWeight(Hashtable<String,String> ht){
      double dryWeight = Double.NaN;
      try{
         dryWeight = Double.parseDouble(ht.get("dryweight"));
      }
      catch(NumberFormatException nfe){
         dryWeight = Double.NaN;
      }
      return dryWeight;
   }

   //
   //
   //
   private double getInitializedEmptMass(Hashtable<String,String> ht){
      double emptyMass = Double.NaN;
      try{
         emptyMass = Double.parseDouble(ht.get("empty_mass"));
      }
      catch(NumberFormatException npe){
         emptyMass = Double.NaN;
      }
      return emptyMass;
   }

   //
   //
   //
   private double getInitializedLoadMass(Hashtable<String,String> ht){
      double loadedMass = Double.NaN;
      try{
         loadedMass = Double.parseDouble(ht.get("loaded_mass"));
      }
      catch(NumberFormatException npe){
         loadedMass = Double.NaN;
      }
      return loadedMass;
   }

   //
   //
   //
   private double getInitializedMaxWght(Hashtable<String,String> ht){
      double maxWeight = Double.NaN;
      try{
         maxWeight = Double.parseDouble(ht.get("maxweight"));
      }
      catch(NumberFormatException nfe){
         maxWeight = Double.NaN;
      }
      return maxWeight;
   }

   //
   //
   //
   private double getInitializedO2Perc(Hashtable<String,String> ht){
      double o2Percent = Double.NaN;
      try{
         o2Percent = Double.parseDouble(ht.get("o2percent"));
      }
      catch(NumberFormatException nfe){
         o2Percent = Double.NaN;
      }
      return o2Percent;
   }

   //
   //
   //
   private boolean getInitializedOccupd(Hashtable<String,String> ht){
      return Boolean.parseBoolean(ht.get("occupied"));
   }

   //
   //
   //
   private double getInitializedTemp(Hashtable<String,String> ht){
      double temperature = Double.NaN;
      try{
         temperature = Double.parseDouble(ht.get("temperature"));
      }
      catch(NumberFormatException nfe){
         temperature = Double.NaN;
      }
      return temperature;
   }

   //
   //
   //
   private double getInitializedTol(Hashtable<String,String> ht){
      double tolerance = Double.NaN;
      try{
         tolerance = Double.parseDouble(ht.get("tolerance"));
      }
      catch(NumberFormatException nfe){
         tolerance = Double.NaN;
      }
      return tolerance;
   }

   //
   //
   //
   private void initializePayloadDataJSON(String file)
   throws IOException{
      //Test Print
      System.out.println("Payload: "+file);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = read.readPayloadInfo();
         int crw     = this.getInitializedCrewData(ht);
         double  dw  = this.getInitializedDryWeight(ht);
         double  em  = this.getInitializedEmptMass(ht);
         boolean isO = this.getInitializedOccupd(ht);
         double  lm  = this.getInitializedLoadMass(ht);
         double  mw  = this.getInitializedMaxWght(ht);
         String  mod = ht.get("model");
         double  o2p = this.getInitializedO2Perc(ht);
         double  temp= this.getInitializedTemp(ht);
         double  tol = this.getInitializedTol(ht);
         String  type= ht.get("type");
         this._payloadData = new GenericPayloadData(
                                           crw,        //Crew
                                           Double.NaN,//Current Weight
                                           dw,         //Dry Weight
                                           em,         //Empty Mass
                                           null,       //Error
                                           false,      //is Error
                                           isO,        //Is Occupied
                                           lm,         //Loaded Mass
                                           mw,         //Max Weight
                                           mod,        //Model
                                           o2p,        //O2 Percent
                                           temp,       //Temperature
                                           tol,        //Tolerance
                                           type);      //Type
      }
      catch(IOException ioe){
         this._payloadData = null;
         throw ioe;
      }
   }

   //
   //
   //
   private boolean isPathFile(String file)throws IOException{
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
         throw ioe;
      }
      catch(NullPointerException npe){
         isPath = false;
      }
      return isPath;
   }

   //
   //
   //
   private void measure(){
      try{
         if(this._feeder != null){
            RocketData rd  = (RocketData)this._feeder.monitor();
            PayloadData pd = rd.payloadData();
            synchronized(th8is._obj){
               this._measuredPayloadData = pd;
            }
         }
         else{
            throw new NullPointerException("No DataFeeder");
         }
      }
      catch(ClassCastException cce){
         try{
            PayloadData pd = (PayloadData)this._feeder.monitor();
            synchronized(this._obj){
               this._measuredPayloadData = pd;
            }
         }
         catch(ClassCastException e){
            throw new NullPointerException("No PayloadDataFeeder");
         }
      }
      catch(NullPointerException npe){
         //This will need to change to be updated...and to remove
         //the Print Stack Trace-->this is here only for debug
         //purposes...
         npe.printStackTrace();
         synchronized(this._obj){
            this._measuredPayloadData = this._payloadData;
         }
      }
   }

   //
   //
   //
   private void monitorPayload(){
      this.measure();
      this.checkErrors();
   }

   //
   //
   //
   private void payloadData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){}
      else if(file.toUpperCase().contains("JSON")){
         this.initializePayloadDataJSON(file);
      }
   }

   //
   //
   //
   private void setUpPayloadData(){
      String err = null;
   }

   //
   //
   //
   private void setUpThread(){
      String name = new String("Generic Payload");
      this._rt0 = new Thread(this,name);
      this._rt0.start();
   }

   /////////////////////////Payload Interface/////////////////////////
   //
   //
   //
   public PayloadData monitor(){
      synchronized(this._obj){
         return this._measuredPayloadData;
      }
   }

   //
   //
   //
   public void initialize(String file)throws IOException{
      String pldFile = file;
      if(this.isPathFile(pldFile)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(pldFile);
         pldFile = read.readPathInfo().get("payload");
      }
      this.payloadData(pldFile);
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
      if(listener != null){
         try{
            this._errorListeners.add(listener);
         }
         catch(NullPointerException npe){
            this._errorListeners = new LinkedList<ErrorListener>();
            this._errorListeners.add(listener);
         }
      }
   }

   //
   //
   //
   public void addSystemListener(SystemListener listener){
      if(listener != null){
         try{
            this._systemListeners.add(listener);
         }
         catch(NullPointerException npe){
            this._systemListeners = new LinkedList<SystemListener>();
            this._systemListeners.add(listener);
         }
      }
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate state){
      this._state = stateSubstate;
   }

   /////////////////////////Runnable Interface////////////////////////
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
                  //In Initialization, if the payload is a capsule,
                  //it should not be occupied...if not a capsule, it
                  //is just payload, so check every 10 seconds...
                  if(counter++%10000 == 0){
                     check = true;
                  }
               }
            }
            if(check){
               this.monitorPayload();
               this.alertSubscribers();
               check = false;
               counter = 1;  //reset the counter
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
