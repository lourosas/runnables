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
   private void alertErrorListeners(){}

   //
   //
   //
   private void alertSubscribers(){}

   //
   //
   //
   private void checkErrors(){}

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
   private boolean getInitializedOccupd(Hashtable<String,String> ht){
      return Boolean.parseBoolean(ht.get("occupied"));
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
   private void monitorPayload(){}

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
   public void addDataFeeder(DataFeeder feeder){}

   //
   //
   //
   public void addErrorListener(ErrorListener listener){}

   //
   //
   //
   public void addSystemListener(SystemListener listener){}

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate state){}

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
                     counter = 1;  //reset the counter
                  }
               }
            }
            if(check){
               this.monitorPayload();
               this.checkErrors();
               this.alertSubscribers();
               check = false;
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
