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
   private static final int PRELAUNCH = -1; //All 0xF's...
   private static final int IGINTION  =  0;
   private static final int LAUNCH    =  1;

   private String                 _error;
   private List<ErrorListener>    _errorListeners;
   private String                 _errorTime;
   private int                    _holds;
   private boolean                _kill; //TBD to use correctly
   private int                    _state;
   private boolean                _isError;
   private int                    _model;
   private LaunchingMechanismData _launchingMechanismData;
   private List<MechanismSupport> _supports; //Keep them in a list
   //This weight is to be calculated
   private double                 _measuredWeight;
   //Weight read in from the init file
   private double                 _inputWeight;
   private boolean                _start;
   private double                 _tolerance;
   private Thread                 _rt0;

   {
      _error                 = null;
      _errorListeners        = null;
      _errorTime             = null;
      _holds                 = -1;
      _kill                  = false;
      _isError               = false;
      _launchingMechanismData = null;
      _model                 = -1;
      _supports              = null;
      _start                 = true;
      _state                 = PRELAUNCH;
      _measuredWeight        = Double.NaN;
      _inputWeight           = Double.NaN;
      _tolerance             = Double.NaN;
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
   private void isError(){
      double  edge      = Double.NaN;
      double  ul        = Double.NaN;
      double  ll        = Double.NaN;
      double  lim       = 1.- this._tolerance;
      boolean inputGood = (this._inputWeight    != Double.NaN);
      boolean measGood  = (this._measuredWeight != Double.NaN);
      this._isError     = false;

      //TODO What happens if a Measurement is not good? Indicate an
      //error or not?
      if(inputGood && measGood){
         //Account for the State to determine errors...
         if(this._state == PRELAUNCH){
            edge = this._inputWeight * lim;
            if(this._inputWeight < 0.){
               edge = this._inputWeight * -lim;
            }
            ll = this._inputWeight - edge;
            ul = this._inputWeight + edge;
            if(this._measuredWeight<ll || this._measuredWeight>ul){
               this._isError = true;
            }
            if(this._isError){
               this.setError("Measured Weight",LocalDateTime.now());
            }
         }
      }
   }

   //Measure the weight of the rocket based in terms of this
   //Mechanism...will evolve...
   //
   private void measureWeight(){
      //Make this more complex, based on release...for now, just get
      //something working...
      //_inputWeight IS the Rocket Weight!
      this._measuredWeight = this._inputWeight;
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
      if(data.containsKey("total_tollerance")){
         try{
            String sToll    = data.get("total_tollerance");
            this._tolerance = Double.parseDouble(sToll);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
   }


   //Should only get the loaded weight of the Rocket...get the empty
   //wieght in addition...
   //Gets the Rocket Info needed for Support Responsibilities
   private void rocketData(Hashtable<String, String> data){
      if(data.containsKey("loaded_weight")){
         try{
            String loadedWeight = data.get("loaded_weight");
            this._inputWeight   = Double.parseDouble(loadedWeight);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
      //@TODO figure out where to save...
      if(data.containsKey("empty_weight")){}
   }

   //
   //
   //
   private void setError(String errorType, LocalDateTime dt){
      this._error     = new String();
      this._errorTime = new String();      
      DateTimeFormatter dtf = null;
      dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
      this._errorTime = dt.format(dtf);
      if(errorType.toUpperCase().contains("MEASURED")){
         this._error += "\n";
         this._error += "Launching Mechanism Measured Weight";
         this._error += " Error";
         this._error += "\nMeasured: " + this._measuredWeight;
         this._error += "\nExpected: " + this._inputWeight;
      }
      //Alert the ErrorListeners
      ErrorEvent e = new ErrorEvent(this,this._error,this._errorTime);
      try{
         Iterator<ErrorListener> it = this._errorListeners.iterator();
         while(it.hasNext()){
            //Alert all the Listeners
            it.next().errorOccurred(e);
         }
      }
      catch(NullPointerException npe){
         //Shold NEVER get here!!!
         npe.printStackTrace();
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
      //Throw it up to the ErrorListeners...
      //As well as handle it in this Object as needed...
   }

   /////////Launching Mechanism Interface Implementation//////////////
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
   public void initialize(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){}
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = null;
         ht = read.readRocketInfo();
         this.rocketData(ht);
         ht = read.readLaunchingMechanismInfo();
         this.mechanismData(ht);
      }
      for(int i = 0; i < this._holds; ++i){
         MechanismSupport support = null;
         support = new GenericMechanismSupport(i);
         support.initialize(file);
         support.addErrorListener(this);
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
   public LaunchingMechanismData monitorInitialization(){
      return this.monitorPrelaunch();
   }

   //
   //
   //
   public LaunchingMechanismData monitorPrelaunch(){
      this._state = PRELAUNCH;
      return this._launchingMechanismData;
   }

   //
   //
   //
   public LaunchingMechanismData monitorIgnition(){
      return null;
   }

   //
   //
   //
   public LaunchingMechanismData monitorLaunch(){
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
      string += "\n" + this._measuredWeight + ", "+this._inputWeight;
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
               //Debug Initial prints
               Thread.sleep(1000);
               System.out.println(Thread.currentThread().getName());
               System.out.println(Thread.currentThread().getId());
               //Will need to check the the Supports at this
               //point...
               /*
               List<MechanismSupportData> md = null;
               md = new LinkedList<MechanismSupportData>();
               for(int i = 0; i < this._supports.size(); ++i){
                  MechanimsSupport sup = this._supports.get(i);
                  if(this._state == PRELAUNCH){
                     Thread.sleep(100);
                     md.add(sup.monitorPrelaunch());
                  }
                  else if(this._state == IGNITION){
                     Thread.sleep(10);
                  }
                  else if(this._state == LAUNCH){
                     Thread.sleep(300);
                  }
               }
               this.measureWeight();
               this.isError();
               LaunchingMechanismData data = null;
               data = new GenericLaunchingMechanismData(
                                                  this._error,
                                                  this._holds,
                                                  this._isError,
                                                  this._measuredWeight,
                                                  this._model,
                                                  this._tolerance,
                                                  md);
               this._launchingMechanismData = data;
               */
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
