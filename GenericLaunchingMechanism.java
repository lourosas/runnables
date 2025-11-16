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
import rosas.lou.clock.*;
import java.text.*;
import java.time.*;
import java.time.format.*;

public class GenericLaunchingMechanism implements LaunchingMechanism,
ErrorListener, Runnable{
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State  INIT      = null;
   private LaunchStateSubstate.State  PRELAUNCH = null;
   private LaunchStateSubstate.State  IGNITION  = null;
   private LaunchStateSubstate.State  LAUNCH    = null;

   private String                 _error;
   private List<ErrorListener>    _errorListeners;
   private List<SystemListener>   _systemListeners;
   private String                 _errorTime;
   private int                    _holds;
   private boolean                _kill; //TBD to use correctly
   private LaunchStateSubstate    _state;
   private boolean                _isError;
   private int                    _model;
   private LaunchingMechanismData _launchingMechanismData;
   private List<MechanismSupport> _supports; //Keep them in a list
   private List<MechanismSupportData> _supportsData;
   //Weight
   private double                 _emptyWeight;
   private double                 _loadedWeight;
   //This weight is to be calculated
   private double                 _measuredWeight;
   //Weight read in from the init file
   private boolean                _start;
   private double                 _tolerance;
   private DataFeeder             _feeder;
   private Thread                 _rt0;
   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _emptyWeight            = Double.NaN;
      _error                  = null;
      _errorListeners         = null;
      _systemListeners        = null;
      _errorTime              = null;
      _holds                  = -1;
      _kill                   = false;
      _isError                = false;
      _feeder                 = null;
      _loadedWeight           = Double.NaN;
      _launchingMechanismData = null;
      _model                  = -1;
      _supports               = null;
      _supportsData           = null;
      _start                  = false;
      _state                  = null;
      _measuredWeight         = Double.NaN;
      _tolerance              = Double.NaN;
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public GenericLaunchingMechanism(){
      this.setUpThread();
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void alertErrorListeners(){
      //Create an ErrorEvent
      LaunchingMechanismData lmd = this._launchingMechanismData;
      ErrorEvent e = new ErrorEvent(this, lmd, this._error);
      if(this.TOPRINT){
         this.printError(e);
      }
      /*
       * Worry about fucking software interrupts later!
      try{
         Iterator<ErrorListener> it = null;
         it = this._errorListeners.iterator();
         while(it.hasNext()){
            it.next().errorOccurred(e);
         }
      }
      catch(NullPointerException npe){
         //Should NEVER get here
         npe.printStackTrace();
      }
      */   
   }

   //Just pass along the already created ErrorEvent to the
   //ErrorListeners
   //
   private void alertErrorListeners(ErrorEvent e){
      try{
         Iterator<ErrorListener> it = null;
         it = this._errorListeners.iterator();
         while(it.hasNext()){
            it.next().errorOccurred(e);
         }
      }
      catch(NullPointerException npe){
         //Should NEVER GET HERE
         npe.printStackTrace();
      }
   }

   //
   //
   //
   private void alertSystemListeners(){
      //Create a SystemEvent
      MissionSystemEvent event = null;
      //Will NEED TO KEEP THE FUCKING ERROR mechanism in place!!
      //this._isError            = false;
      //this._error              = new String();
      String s = new String("Launching Mechanism Event");
      this.setUpLaunchingMechanismData();
      LaunchingMechanismData lmd = this._launchingMechanismData;
      //Going to go ahead and send in the entire object
      event = new MissionSystemEvent(this,lmd,s,this._state);
      try{
         Iterator<SystemListener> it = null;
         it = this._systemListeners.iterator();
         while(it.hasNext()){
            it.next().update(event);
         }
      }
      catch(NullPointerException npe){
         //Should NEVER get here
         npe.printStackTrace();
      }   
   }

   //
   //
   //
   private void isError(){
      double  edge      = Double.NaN;
      double  ul        = Double.NaN;
      double  ll        = Double.NaN;
      double  lim       = 1.- this._tolerance;
      boolean inputGood = !Double.isNaN(this._emptyWeight);
      inputGood        &= !Double.isNaN(this._loadedWeight);
      boolean measGood  = !Double.isNaN(this._measuredWeight);
      //Clear every time...
      this._isError     = false;
      this._error       = new String();

      //TODO What happens if a Measurement is not good? Indicate an
      //error or not?
      //For ease, might consider the _inputWeight based on measure...
      if(inputGood && measGood){
         //Account for the State to determine errors...
         if(this._state.state() == INIT){
            //Weight should be based sole-ly on the empty weight...
            ll = this._emptyWeight*this._tolerance;
            ul = this._emptyWeight*(2-this._tolerance);
            //Will need to test to make sure this works
            if(this._emptyWeight < 0){
               double temp = ul;
               ul = ll;
               ll = temp;
            }
         }
         else if(this._state.state() == PRELAUNCH){
            /*
             * For fueling, empty to loaded weight
            */
         }
         if(this._measuredWeight < ll || this._measuredWeight > ul){
            String error = new String("Measured Weight: ");
            this._isError = true;
            if(this._measuredWeight < ll){
               error += "too low";
            }
            else if(this._measuredWeight > ul){
               error += "too high";
            }
            this.setError(error);
         }
      }
   }

   //
   //
   //
   private void initializeINIFile(String file)throws IOException{}

   //0.  Get the Parameter File
   //1.  Open the Parameter File to read the Rocket and
   //    LaunchingMechanism data
   //2.  Save off
   private void initializeJSONFile(String file)throws IOException{
      //0.  Get the Parameter File
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      Hashtable<String,String> ht = null;
      ht = read.readPathInfo();
      System.out.println(ht);
      System.out.println(ht.get("parameter"));
      System.exit(0);
      /*
      ht = read.readRocketInfo();
      this.rocketData(ht);
      ht = read.readLaunchingMechanismInfo();
      this.mechanismData(ht);
      */
   }

   //Measure the weight of the rocket based on the Mechanism Supports
   //
   //
   private void measureWeight(){
      //As always fucking reset
      this._measuredWeight = 0.;
      this._supportsData = new LinkedList<MechanismSupportData>();
      Iterator<MechanismSupport> it = this._supports.iterator();
      while(it.hasNext()){
         try{
            MechanismSupportData msd = it.next().monitor();
            this._supportsData.add(msd);
            double currentForce = msd.measuredForce();
            this._measuredWeight += currentForce;
         }
         catch(NullPointerException npe){
            this._measuredWeight = Double.NaN;
         }  
      }
   }

   //Sets up/saves the mechanism data for the System
   //
   //
   private void mechanismData(Hashtable<String,String> data){
      if(data.containsKey("model")){
         try{
            String sModel = data.get("model");
            this._model   = Integer.parseInt(sModel);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
      if(data.containsKey("number_of_holds")){
         try{
            String sHolds = data.get("number_of_holds");
            this._holds   = Integer.parseInt(sHolds);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
      if(data.containsKey("total_tolerance")){
         try{
            String sToll    = data.get("total_tolerance");
            this._tolerance = Double.parseDouble(sToll);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
   }

   //
   //
   //
   private void printError(ErrorEvent e){
      String fileName = this.getClass().getName()+"_"+e.getDate();
      fileName       += "_error.txt";
      FileWriter  fw  = null;
      PrintWriter pw  = null;
      try{
         fw = new FileWriter(fileName, true);
         pw = new PrintWriter(fw,true);
         pw.println("\n"+e);
      }
      catch(IOException ioe){
         ioe.printStackTrace();
      }
      finally{
         pw.close();
      }
   }


   //Should only get the loaded weight of the Rocket...get the empty
   //wieght in addition...
   //Gets the Rocket Info needed for Support Responsibilities
   private void rocketData(Hashtable<String, String> data){
      if(data.containsKey("loaded_weight")){
         try{
            String loadedWeight = data.get("loaded_weight");
            this._loadedWeight  = Double.parseDouble(loadedWeight);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
      //@TODO figure out where to save...
      if(data.containsKey("empty_weight")){
         try{
            String emptyWeight = data.get("empty_weight");
            this._emptyWeight  = Double.parseDouble(emptyWeight);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }
   }

   //
   //
   //
   private void setError(String errorType){
      this._error     = new String();
      java.text.DecimalFormat df = null;
      df = new java.text.DecimalFormat("###,###,###.##");
      if(errorType.toUpperCase().contains("MEASURED")){
         this._error =  errorType;
         this._error += "\nState:    " + this._state.state();
         String formattedWeight = df.format(this._measuredWeight);
         this._error += "\nMeasured: " + formattedWeight;
         this._error += "\nEmpty:    " + this._emptyWeight;
         this._error += "\nLoaded:   " + this._loadedWeight;
      }
   }

   //
   //
   //
   private void setUpLaunchingMechanismData(){
      LaunchingMechanismData lmd     = null;
      List<MechanismSupportData> msd = null;
      msd = this._supportsData;
      Iterator<MechanismSupport> it = this._supports.iterator();
      lmd = new GenericLaunchingMechanismData(this._error,
                                              this._holds,
                                              this._isError,
                                              this._measuredWeight,
                                              this._model,
                                              this._state,
                                              this._tolerance,
                                              msd);
      this._launchingMechanismData = lmd;
   }

   //
   //
   //
   private void setUpMechanismSupports(String file){
      for(int i = 0; i < this._holds; ++i){
         MechanismSupport support = null;
         support = new GenericMechanismSupport(i);
         support.initialize(file);
         support.addErrorListener(this);
         //support.addSystemListener(this);
         try{
            this._supports.add(support);
         }
         catch(NullPointerException npe){
            this._supports = new LinkedList<MechanismSupport>();
            this._supports.add(support);
         }
      }
   }

   //
   //
   //
   private void setUpThread(){
      this._rt0 = new Thread(this, "Launching Mechanism");
      this._rt0.start();
   }

   ///////////////ErrorListener Interface Implementation//////////////
   //
   //
   //
   public void errorOccurred(ErrorEvent e){
      this.alertErrorListeners(e);
   }

   /////////Launching Mechanism Interface Implementation//////////////
   //
   //
   //
   public void addDataFeeder(DataFeeder feeder){
      try{
         if(feeder != null){
            this._feeder = feeder;
            for(int i = 0; i < this._supports.size(); ++i){
               MechanismSupport sup = null;
               sup = (MechanismSupport)this._supports.get(i);
               //Just make a fucking Generic Type Data Feeder for
               //Simulation!
               sup.addDataFeeder(feeder);
            }
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   public void addDataFeeder(String feederFile){
      //Will need to do a fucking lot for this!!!
   }

   //
   //
   //
   public void addErrorListener(ErrorListener e){
      try{
         this._errorListeners.add(e);
      }
      catch(NullPointerException npe){
         this._errorListeners = new LinkedList<ErrorListener>();
         this._errorListeners.add(e);
      }
   }

   //
   //
   //
   public void addSystemListener(SystemListener sl){
      try{
         this._systemListeners.add(sl);
      }
      catch(NullPointerException npe){
         this._systemListeners = new LinkedList<SystemListener>();
         this._systemListeners.add(sl);
      }
   }

   //
   //
   //
   public void initialize(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         this.initializeINIFile(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         this.initializeJSONFile(file);
      }
      this.setUpMechanismSupports(file);
      //The initialization phase assumes NO fuel loaded in the Rocket
      //ergo, the Rocket is at empty weght...this is PRIOR to actually
      //measuring the System...which is about three seconds...or
      //whenever the Threads start up--just so something is available
      this._measuredWeight = this._emptyWeight;
      this._state = new LaunchStateSubstate(INIT,null,null,null);
      //Once Initialized, can start the monitoring...
      this._start = true;
   }

   //
   //
   //
   public LaunchingMechanismData monitor(){
      return this._launchingMechanismData;
   }

   //
   //
   //
   public LaunchingMechanismData monitorInitialization(){
      //this._state = new LaunchStateSubstate(INIT,null,null,null);
      //this._start = true;
      return this._launchingMechanismData;
   }

   //
   //
   //
   public LaunchingMechanismData monitorPrelaunch(){
      this._state = new LaunchStateSubstate(PRELAUNCH,null,null,null);
      this._start = true;
      return this._launchingMechanismData;
   }

   //
   //
   //
   public LaunchingMechanismData monitorIgnition(){
      this._state = new LaunchStateSubstate(IGNITION,null,null,null);
      this._start = true;
      return null;
   }

   //
   //
   //
   public LaunchingMechanismData monitorLaunch(){
      this._state = new LaunchStateSubstate(LAUNCH,null,null,null);
      this._start = true;
      return null;
   }

   //
   //
   //
   public LaunchingMechanismData monitorPostlaunch(){
      //Once the Transition out of Launch State, no need to monitor
      //the Launching Mechanism
      this._start = false;
      return null;
   }

   //
   //
   //
   public void release(){}


   //Probably not needed...might be able to remove...
   //For the time being, return the Measured Weight...
   //
   public double supportedWeight(){
      return this._measuredWeight;
   }

   //
   //
   //
   public String toString(){
      String string = new String();
      string += "\n" + this._holds + ", "+this._model;
      string += "\n"+this._emptyWeight+", "+this._measuredWeight;
      string += "\n"+this._loadedWeight;
      for(int i = 0; i < this._holds; ++i){
         string += "\n" + this._supports.get(i).toString();
      }
      string += "\n" + this._tolerance;
      if(this._isError){
         string += "\nHas Error: "+this._isError;
         string += "\n"+this._error;
      }
      return string;
   }

   ////////////////Runnable Interface Implementation//////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            //TBD how to use this--if actually needed...
            if(this._kill){
               throw new InterruptedException();
            } 
            if(this._start){
               //It appears going to do the same god damned thing the
               //whole time...so just "change sleep time" and others
               //as needed...
               this.measureWeight();
               this.isError();
               if(this._isError){
                  //Not going to do this for the time being...
                  //figure out HOW to implement software interrupts!
                  this.alertErrorListeners();
               }
               //else{
                  this.alertSystemListeners();
               //}
               if(this._state.state() == INIT){
                  System.out.println("GLM 1\n----------------------");
                  System.out.println(Thread.currentThread().getName());
                  System.out.println(Thread.currentThread().getId());
                  System.out.println("----------------------\nGLM 2");
                  Thread.sleep(10000);//Sleep for 10 secs in INIT
               }
            }
            else{
               //Monitor for change every 10^-3 secs
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
