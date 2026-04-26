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

public class GenericRocket extends Rocket implements  Runnable{
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

   //Accumulation of all the Weight of the Stages!!!
   //Get rid of Calculated Weight
   //private int                 _currentStage;
   //private List<ErrorListener> _errorListeners;
   //private List<SystemListener>_systemListeners;
   //private DataFeeder          _feeder;
   private boolean             _kill;
   private Object              _obj;
   private Payload             _payload;
   private Thread              _rt0;
   private List<Stage>         _stages;
   private boolean             _start;
   //private LaunchStateSubstate _state;
   private RocketData          _rocketData;
   private RocketData          _measRocketData;

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

      //_currentStage     = -1;
      //_errorListeners   = null;
      //_feeder           = null;
      _kill             = false;
      _obj              = null;
      _payload          = null;
      _rt0              = null;
      _stages           = null;
      _start            = false;
      //_state            = null;
      //_systemListeners  = null;
      _rocketData       = null;
      _measRocketData   = null;
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public GenericRocket(){
      this._obj = new Object();
      this.setUpThread();
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   /*
   private void checkErrors(){
      String err        = new String();
      boolean isError   = false;
      if(this.checkMeasurementWeightError()){
         isError = true;
         err+= "Measured Weight Error\n";
      }
      if(this.checkStageErrors()){
         isError = true;
         err += "Stage Errors\n";
      }
      if(this.checkPayloadErrors()){
         isError = true;
         err += "Payload Errors\n";
      }
      if(isError){
         RocketData rd = null;
         rd = this.setUpRocketData(Double.NaN,null,null,err,isError);
         synchronized(this._obj){
            this._measRocketData = rd;
         }
         this.alertErrorListeners();
      }
   }
   */

   //
   //
   //
   /*
   private boolean checkMeasurementWeightError(){
      double min    = Double.NaN;
      double max    = Double.NaN;
      double weight = Double.NaN;
      synchronized(this._obj){
         weight = this._measRocketData.calculatedWeight();
      }
      double dryWeight = this._rocketData.emptyWeight();
      double tolerance = this._rocketData.tolerance();
      if(this._state.state() == INIT){
         min = dryWeight * tolerance;
         max = dryWeight * (2 - tolerance);
      }
      return ((weight < min) || (weight > max));
   }
   */

   //
   //
   //
   /*
   private boolean checkPayloadErrors(){
      boolean isError = false;
      PayloadData data = null;
      synchronized(this._obj){
         data = this._measRocketData.payloadData();
      }
      try{
         isError = data.isError();
      }
      catch(NullPointerException npe){
         isError = false;
      }
      return isError;
   }
   */

   //
   //
   //
   /*
   private boolean checkStageErrors(){
      boolean isError      = false;
      List<StageData> list = null;
      synchronized(this._obj){
         list = this._measRocketData.stages();
      }
      try{
         Iterator<StageData> it = list.iterator();
         while(it.hasNext()){
            isError = it.next().isError();
         }
      }
      catch(NullPointerException npe){
         isError = false;
      }
      return isError;
   }
   */

   //
   //
   //
   /*
   private double computeRocketWeight
   (
      RocketData      rd,
      PayloadData     pd,
      List<StageData> list
   ){
      double weight = 0.; //Of course, start with a 0 weight
      //Get the current weight of the payload and the weights of the
      //stages and add them together...do not try to get all cute...
      //weight += {get the capsule weight}--to be monitored!! TBD
      Iterator<StageData> it = list.iterator();
      while(it.hasNext()){
         StageData sd = it.next();
         weight += sd.weight();
      }
      //See if this will work...
      weight += pd.currentWeight();
      return weight;
   }
   */

   //
   //
   //
   /*
   private PayloadData monitorPayload(){
      return this._payload.monitor();
   }
   */
   //
   //
   //
   /*
   private void monitorRocket(){
      String error        = null;
      //Might not need...possibly delete...
      List<StageData> lst = this.monitorStage();
      PayloadData    pldd = this.monitorPayload();
      RocketData rd       = null;
      try{
         if(this._feeder != null){
            rd = (RocketData)this._feeder.monitor();
         }
         else{
            throw new NullPointerException("No Data Feeder");
         }
      }
      catch(ClassCastException cce){
         throw new NullPointerException("No Data Feeder");
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         rd = this._rocketData;
      }
      finally{
         try{
            double weight = this.computeRocketWeight(rd,pldd,lst);
            rd = this.setUpRocketData(weight,lst,pldd,error,false);
         }
         catch(NullPointerException npe){
            rd = this._rocketData;
         }
         finally{
            synchronized(this._obj){
               this._measRocketData = rd;
            }
         }
      }
   }
   */
   //
   //
   //
   /*
   private List<StageData> monitorStage(){
      List<StageData> list = null;
      try{
         list = new LinkedList<StageData>();
         synchronized(this._obj){
            Iterator<Stage> it = this._stages.iterator();
            while(it.hasNext()){
               list.add(it.next().monitor());
            }
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         list = null;
      }
      finally{
         return list;
      }
   }
   */

   //
   //
   //
   /*
   private RocketData setUpRocketData
   (
      double          weight,
      List<StageData> list,
      PayloadData     pldd,
      String          error,
      boolean         isError
   ){
      RocketData rd = null;
      synchronized(this._obj){
         rd = this._measRocketData;
      }
      try{
         int    cs  = rd.currentStage(); //This has to be most current
         double ew  = this._rocketData.emptyWeight();
         double lw  = this._rocketData.loadedWeight();
         String mdl = this._rocketData.model();
         int    ns  = this._rocketData.numberOfStages();
         double tol = this._rocketData.tolerance();
         if(Double.isNaN(weight) && list == null && pldd == null){
            if(isError || (error!=null && error.length() > 0)){
               weight = rd.calculatedWeight();
               pldd   = rd.payloadData();
               list   = rd.stages();
            }
         }
         rd = new GenericRocketData(mdl,    //model
                                    cs,     //Current Stage
                                    ns,     //Number of Stages
                                    ew,     //Empty Weight
                                    lw,     //Loaded Weight
                                    weight, //Calculated Weight
                                    isError,
                                    error,  //error
                                    pldd,   //Payload Data
                                    list,   //Stages Data
                                    tol);   //Tolerance
      }
      catch(NullPointerException npe){
         rd = this._rocketData;
      }
      finally{
         return rd;
      }
   }
   */

   //
   //
   //
   private void setUpThread(){
      this._rt0 = new Thread(this, "Generic Rocket");
      this._rt0.start();
   }

   //
   //
   //
   /*
   public RocketData monitor(){
      synchronized(this._obj){
         return this._measRocketData;
      }
   }
   */

   //
   //
   //
   /*
   public void initialize(String file)throws IOException{
      String rFile = file;
      String sFile = file;
      String pFile = file;
      this._currentStage = 1;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         rFile = read.readPathInfo().get("rocket");
         sFile = read.readPathInfo().get("stage");
         pFile = read.readPathInfo().get("payload");
      }
      //Real Simple for initialization...
      this.initializeRocket(rFile);
      this.initializeStage(sFile);
      this.initializePayload(pFile);
   }
   */

   //
   //
   //
   /*
   public void addDataFeeder(DataFeeder feeder){
      if(feeder != null){
         this._feeder = feeder;
         try{
            //Add the Components
            Iterator<Stage> it = this._stages.iterator();
            while(it.hasNext()){
               Stage stage = (Stage)it.next();
               stage.addDataFeeder(this._feeder);
            }
            this._payload.addDataFeeder(feeder);
         }
         catch(NullPointerException npe){
            //Should never get here!!!
            npe.printStackTrace();
         }
      }
   }
   */

   //
   //
   //
   /*
   public void addErrorListener(ErrorListener listener){
      try{
         this._errorListeners.add(listener);
      }
      catch(NullPointerException npe){
         this._errorListeners = new LinkedList<ErrorListener>();
         this._errorListeners.add(listener);
      }
   }
   */
   //
   //
   //
   /*
   public void addSystemListener(SystemListener listener){
      try{
         this._systemListeners.add(listener);
      }
      catch(NullPointerException npe){
         this._systemListeners = new LinkedList<SystemListener>();
         this._systemListeners.add(listener);
      }
   }
   */

   //
   //
   //
   /*
   public int currentStage(){
      int cs = -1;
      try{
         this._measRocketData.currentStage();
      }
      catch(NullPointerException npe){
         cs = this._currentStage;     
      }
      return cs;
   }
   */

   //
   //
   //
   /*
   public void setStateSubstate(LaunchStateSubstate state){
      this._state = state;
      try{
         Iterator<Stage> it = this._stages.iterator();
         while(it.hasNext()){
            it.next().setStateSubstate(this._state);
         }
         this._payload.setStateSubstate(this._state);
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }
   */

   //
   //
   //
   /*
   public int totalStages(){
      int ts = -1;
      try{
         this._rocketData.numberOfStages();
      }
      catch(NullPointerException npe){}
      return ts;
   }
   */
   ///////////////Runnable Interface Implementation///////////////////
   //
   //
   //
   public void run(){
      try{
         int     count = 0;
         boolean check = false;
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this._state != null){
               if(this._state.state() == INIT){
                  //In the Initialization Stage, check every
                  //10 Seconds
                  if(count++%10000 == 0){
                     check = true;
                     count = 1; //Reset the Counter
                  }
               }
            }
            if(check){
              System.out.println("\nGR 1\n+++++++++++++++++++++++");
              System.out.print("Rocket: ");
              System.out.println(Thread.currentThread().getName());
              System.out.print("Rocket: ");
              System.out.println(Thread.currentThread().getId());
              //Eventually perform all of this...
              /*
              this.monitorRocket();
              this.checkErrors();
              this.alertSubscribers();
              */
              System.out.println("+++++++++++++++++++++++\nGR 2\n");
              check = false;
            }
            Thread.sleep(1);
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
