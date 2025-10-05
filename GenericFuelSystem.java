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

public class GenericFuelSystem implements FuelSystem, Runnable{
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State INIT      = null; 
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null; 

   private int                 _engines;
   private boolean             _kill;
   private int                 _stageNumber;
   private boolean             _start;

   private List<ErrorListener> _errorListeners;
   private DataFeeder          _feeder;
   private Object              _obj;
   private Tank                _fuel;
   private Tank                _oxidizer;
   private List<Pipe>          _pipes;
   private Pump                _fuelPump;
   private Pump                _oxidizerPump;
   private Thread              _rt0;
   private FuelSystemData      _fuelSystemData;
   private LaunchStateSubstate _state;
   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _engines        = -1;
      _kill           = false;
      _stageNumber    = -1;
      _start          = false;
      _errorListeners = null;
      _feeder         = null;
      _fuel           = null;
      _obj            = null;
      _oxidizer       = null;
      _pipes          = null;
      _oxidizerPump   = null;
      _fuelPump       = null;
      _rt0            = null;
      _fuelSystemData = null;
      _state          = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericFuelSystem(int stage, int engines){
      //At least one pipe per each tank feeding the engines...
      if(stage > 0){
         this._stageNumber = stage;
      }
      if(engines > 0){
         //Needed to determine the number of pipes...
         this._engines = engines;
      }
      this._obj = new Object();
      this.setUpThread();
   }

   ////////////////////Private Methods////////////////////////////////
   //
   //
   //
   private List<PipeData> monitorPipes(){
      List<PipeData> pd = null;
      try{
         Iterator<Pipe> it = this._pipes.iterator();
         while(it.hasNext()){
            synchronized(this._obj){
               pd.add(it.next().monitor());
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

   //
   //
   //
   private void setUpThread(){
      String name = new String("Fuel System "+this._stageNumber);
      name += (", "+this._engines);
      this._rt0 = new Thread(this, name);
      this._rt0.start();
   }
   
   /////////////////Fuel System Interface Implementation//////////////
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
   public void initialize(String file)throws IOException{
      System.out.println("Fuel System:  "+file);
      this.setUpTanks(file);
      this.setUpPumps(file);
      this.setUpPipes(file);
      this._state = new LaunchStateSubstate(INIT,null,null,null);
      this._start = true;
   }

   //
   //
   //
   public FuelSystemData monitor(){
      FuelSystemData fsd = null;
      return fsd;
   }

   //
   //
   //
   public FuelSystemData monitorPrelaunch(){
      /*
      FuelSystemData fsd = null;
      List<TankData> td  = new LinkedList<TankData>();
      List<PumpData> pd  = new LinkedList<PumpData>();
      List<PipeData> pi  = new LinkedList<PipeData>();

      td.add(this._fuel.monitorPrelaunch());
      td.add(this._oxidizer.monitorPrelaunch());
      pd.add(this._fuelPump.monitorPrelaunch());
      pd.add(this._oxidizerPump.monitorPrelaunch());

      Iterator<Pipe> it = this._pipes.iterator();
      while(it.hasNext()){
         pi.add(it.next().monitorPrelaunch());
      }
      fsd = new GenericFuelSystemData(pi,pd,td);
      return fsd;
      */
      return null;
   }

   //
   //
   //
   public FuelSystemData monitorIgnition(){ return null; }

   //
   //
   //
   public FuelSystemData monitorLaunch(){ return null; }

   //////////////////////Runnable Implementation//////////////////////
   //
   //
   //
   public void run(){
      try{
         int count = 0;
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this._start){
               //Monitor at a constant pace regardless of
               //State/Substate
               List<TankData> tankData = this.monitorTanks();
               List<PumpData> pumpData = this.monitorPumps();
               List<PipeData> pipeData = this.monitorPipes();
               //if(isError()){ this.alertErrorListeners(); }
               Thread.sleep(10);
               if(count%100 == 0){
                  System.out.println("******FuelSystem TankData*****");
                  System.out.println(tankData);
                  System.out.println("Count: "+count);
                  System.out.println("******FuelSystem TankData*****");
                  System.out.println("******FuelSystem PumpData*****");
                  System.out.println("Count: "+count);
                  System.out.println(pumpData);
                  System.out.println("******FuelSystem PumpData*****");
                  System.out.println("******FuelSystem PipeData*****");
                  System.out.println("Count: "+count);
                  System.out.println(pipeData);
                  System.out.println("******FuelSystem PipeData*****");

               }
               ++count;
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
