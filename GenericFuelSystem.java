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

public class GenericFuelSystem extends FuelSystem implements Runnable{
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State INIT      = null; 
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null; 

   private boolean              _kill;
   private Object               _obj;
   private Thread               _rt0;
   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      fuel             = null;
      oxidizer         = null;
      pipes            = null;
      pumps            = null;
      stage            = -1;
      engines          = -1;
      _kill            = false;
      _obj             = null;
      _rt0             = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericFuelSystem(int stg, int eng){
      //At least one pipe per each tank feeding the engines...
      if(stg > 0){
         this.stage = stg;
      }
      if(eng > 0){
         //Needed to determine the number of pipes...
         this.engines = eng;
      }
      this._obj = new Object();
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private int getTotalPipesInStage(String file)throws IOException{
      int pipes    = 0;
      String pFile = file;
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      if(read.readPathInfo().get("parameter") != null){
         pFile = read.readPathInfo().get("pipe");
      }
      read = new LaunchSimulatorJsonFileReader(pFile);
      List<Hashtable<String,String>> lst = read.readPipeDataInfo();
      Iterator<Hashtable<String,String>> it = lst.iterator();
      while(it.hasNext()){
         Hashtable<String,String> ht = it.next();
         try{
            int stg = Integer.parseInt(ht.get("stage"));
            if(stg == this.stage){
               ++pipes;
            }
         }
         catch(NumberFormatException nfe){
            pipes = 0;
         }
         catch(NullPointerException  npe){
            npe.printStackTrace();
            pipes = 0;
         }
      }
      return pipes;
   }

   //
   //
   //
   private int getTotalPumpsInStage(String file)throws IOException{
      int pumps    = 0;
      String pFile = file;
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      if(read.readPathInfo().get("parameter") != null){
         pFile = read.readPathInfo().get("pump");
      }
      else{
         pFile = file;
      }
      read = new LaunchSimulatorJsonFileReader(pFile);
      List<Hashtable<String,String>> lst = read.readPumpDataInfo();
      Iterator<Hashtable<String,String>> it = lst.iterator();
      while(it.hasNext()){
         Hashtable<String,String> ht = it.next();
         try{
            int stg = Integer.parseInt(ht.get("stage"));
            if(stg == this.stage){ ++pumps;} 
         }
         catch(NumberFormatException nfe){ pumps = 0; }
         catch(NullPointerException  npe){
            npe.printStackTrace();
            pumps = 0;
         }
      }
      return pumps;
   }

   //
   //
   //
   private void initializePipes(String file)throws IOException{
      try{
         int pipes  = this.getTotalPipesInStage(file);
         for(int i = 0; i < this.engines; ++i){
            for(int j = 0; j < pipes; ++j){
               //Pipe No.; stage No.; Engine No.
               Pipe p = new GenericPipe(j+1,this.stage,i+1);
               //This will need to pay attention and due dilligence!
               p.initializeComponent(file);
               PipeData pd = (PipeData)p.initializationStatus();
               this.initializable.initializeData("pipe data", pd);
               try{
                  this.pipes.add(p);
               }
               catch(NullPointerException npe){
                  this.pipes = new LinkedList<Pipe>();
                  this.pipes.add(p);
               }
            }
         }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         throw new IOException("Pipe Cast Exception");
      }
   }

   //
   //
   //
   private void initializePumps(String file)throws IOException{
      try{
         int pn = this.getTotalPumpsInStage(file);
         for(int i = 0; i < pn; ++i){
            Pump p = new GenericPump(this.stage, i+1);
            p.initializeComponent(file);
            PumpData pd = (PumpData)p.initializationStatus();
            this.initializable.initializeData("pump data", pd);
            try{
               this.pumps.add(p);
            }
            catch(NullPointerException npe){
               this.pumps = new LinkedList<Pump>();
               this.pumps.add(p);
            }
         }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         throw new IOException("Pump Cast Exception");
      }
   }

   //
   //
   //
   private void initializeTanks(String file)throws IOException{
      try{
         //First Tank
         this.fuel     = new GenericTank(this.stage,1);
         //Second Tank
         this.oxidizer = new GenericTank(this.stage,2);
         this.fuel.initializeComponent(file);
         this.oxidizer.initializeComponent(file);
         TankData td = (TankData)this.fuel.initializationStatus();
         this.initializable.initializeData("tank data",td);
         td = (TankData)this.oxidizer.initializationStatus();
         this.initializable.initializeData("tank data",td);
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         throw new IOException("Tank Cast Exception");
      }
   }

   /*
   //
   //
   //
   private void alertErrorListeners(){
      String error       = null;
      FuelSystemData fsd = null;
      synchronized(this._obj){
         fsd   = this._fuelSystemData;
         error = fsd.error();
      }
      try{
         Iterator<ErrorListener> it = this._errorListeners.iterator();
         while(it.hasNext()){
            it.next().errorOccurred(new ErrorEvent(this,fsd,error));
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void alertSubscribers(){
      FuelSystemData fsd     = null;
      LaunchStateSubstate ss = this._state;

      String event = ss.state()+", "+ss.ascentSubstate();
      event += ", "+ss.ignitionSubstate()+", ";
      event += ss.prelaunchSubstate();
      synchronized(this._obj){
         fsd = this._fuelSystemData;
      }
      try{
         Iterator<SystemListener> it = null;
         it = this._systemListeners.iterator();
         while(it.hasNext()){
            MissionSystemEvent mse = null;
            mse = new MissionSystemEvent(this,fsd,event,ss);
            it.next().update(mse);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void checkErrors(){
      synchronized(this._obj){
         if(this._fuelSystemData.isError()){
            this.alertErrorListeners();
         }
      }
   }

   //
   //
   //
   private List<PipeData> monitorPipes(){
      List<PipeData> pd = new LinkedList<PipeData>(); 
      Iterator<Pipe> it = this._pipes.iterator();
      try{
         while(it.hasNext()){
            synchronized(this._obj){
               pd.add((it.next()).monitor());
            }
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         pd = null;
      }
      finally{
         return pd;
      }
   }

   //
   //
   //
   private List<PumpData> monitorPumps(){
      List<PumpData> pd = null;
      try{
         pd = new LinkedList<PumpData>();
         synchronized(this._obj){
            pd.add(this._fuelPump.monitor());
            pd.add(this._oxidizerPump.monitor());
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         pd = null;
      }
      finally{
         return pd;
      }
   }

   //
   //
   //
   private List<TankData> monitorTanks(){
      List<TankData> td = null;
      try{
         td = new LinkedList<TankData>();
         synchronized(this._obj){
            td.add(this._fuel.monitor());
            td.add(this._oxidizer.monitor());
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         td = null;
      }
      finally{
         return td;
      }
   }

   //
   //
   //
   private void setFuelSystemData
   (
      List<PipeData> pipes,
      List<PumpData> pumps,
      List<TankData> tanks
   ){
      try{
         if(pipes == null){
            throw new NullPointerException("No Pipes");
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace(); //Temporary
         pipes = null;
      }
      try{
         if(pumps == null){
            throw new NullPointerException("No Pumps");
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace(); //Temporary
         pumps = null;
      }
      try{
         if(tanks == null){
            throw new NullPointerException("No Tanks");
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace(); //Temporary
         tanks = null;
      }
      FuelSystemData fsd = null;
      fsd = new GenericFuelSystemData(pipes,pumps,tanks);
      synchronized(this._obj){
         this._fuelSystemData = fsd;
      }
   }

   //
   //
   //
   private void setUpPipes(String file)throws IOException{
      //Number of Engines X 2(tanks)
      //Two Pipes Per Engine, per Stage, per Tank
      for(int i = 0; i < this._engines; ++i){
         //Tanks--For this Fuel System, the Tanks are assumed to be
         //two!
         for(int j = 0; j < 2; ++j){
            System.out.println("Stage Number: "+this._stageNumber);
            //Pipe,tank,stage,engine
            Pipe p = new GenericPipe(j+1,this._stageNumber,i+1);
            p.initialize(file);
            try{
               this._pipes.add(p);
            }
            catch(NullPointerException npe){
               this._pipes = new LinkedList<Pipe>();
               this._pipes.add(p);
            }
         }
      }
   }

   //
   //
   //
   private void setUpPumps(String file)throws IOException{
      System.out.println("Pumps: " + file);
      this._fuelPump     = new GenericPump(this._stageNumber, 1);
      this._oxidizerPump = new GenericPump(this._stageNumber, 2);
      this._fuelPump.initialize(file);
      this._oxidizerPump.initialize(file);
   }

   //
   //
   //
   private void setUpTanks(String file)throws IOException{
      //Allow for the possibility the data is different based on the
      //tank...
      this._fuel     = new GenericTank(this._stageNumber, 1);
      this._oxidizer = new GenericTank(this._stageNumber, 2);
      this._fuel.initialize(file);
      this._oxidizer.initialize(file);
   }
   */
   //
   //
   //
   private void setUpThread(){
      String name = new String("Fuel System "+this.stage);
      name += (", "+this.engines);
      this._rt0 = new Thread(this, name);
      this._rt0.start();
   }
   /*
   /////////////////Fuel System Interface Implementation//////////////
   //
   //
   //
   public FuelSystemData monitor(){
      synchronized(this._obj){
         return this._fuelSystemData;
      }
   }

   //
   //
   //
   public void initialize(String file)throws IOException{
      System.out.println("Fuel System:  "+file);
      this.setUpTanks(file);
      this.setUpPumps(file);
      this.setUpPipes(file);
   }

   //
   //
   //
   public void addDataFeeder(DataFeeder feeder){
      if(feeder != null){
         this._feeder = feeder;
         try{
            this._fuel.addDataFeeder(this._feeder);
            this._oxidizer.addDataFeeder(this._feeder);
            this._fuelPump.addDataFeeder(this._feeder);
            this._oxidizerPump.addDataFeeder(this._feeder);
            Iterator<Pipe> it = this._pipes.iterator();
            while(it.hasNext()){
               Pipe p = it.next();
               p.addDataFeeder(this._feeder);
            }
         }
         catch(NullPointerException npe){
            //Should NEVER GET here!!!
            npe.printStackTrace();
         }
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
   public void addSystemListener(SystemListener listener){
      try{
         if(listener != null){
            this._systemListeners.add(listener);
         }
      }
      catch(NullPointerException npe){
         this._systemListeners = new LinkedList<SystemListener>();
         this._systemListeners.add(listener);
      }
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      //Set up State Substate for the object and the components...
      this._state = stateSubstate;
      this._fuelPump.setStateSubstate(this._state);
      this._oxidizerPump.setStateSubstate(this._state);
      Iterator<Pipe> it = this._pipes.iterator();
      while(it.hasNext()){
         it.next().setStateSubstate(this._state);
      }
      this._fuel.setStateSubstate(this._state);
      this._oxidizer.setStateSubstate(this._state);
   }
   */

   /////////////////////FuelSystem Overrides//////////////////////////
   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      super.initializeComponent(file);
      this.initializeTanks(file);
      this.initializePipes(file);
      this.initializePumps(file);
      try{
         FuelSystemData fsd = null;
         fsd = (FuelSystemData)this.initializable.initialized();
         //Notify the Subscribers
         this.publisher.publish(fsd);
      }
      catch(NullPointerException npe){}
      catch(ClassCastException cce){}
   }

   //////////////////////Runnable Implementation//////////////////////
   //
   //
   //
   public void run(){
      try{
         int count     = 0;
         boolean check = false;
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this.getStateSubstate() != null){
               if(this.getStateSubstate().state() == INIT){
                  if(count++%100 == 0){
                     check = true;
                     count = 1; //Reset the counter
                  }
               }
            }
            if(check){
               /*
               List<TankData> tanks = this.monitorTanks();
               List<PumpData> pumps = this.monitorPumps();
               List<PipeData> pipes = this.monitorPipes();
               this.setFuelSystemData(pipes, pumps, tanks);
               this.checkErrors();
               this.alertSubscribers();
               check = false;
               */
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
