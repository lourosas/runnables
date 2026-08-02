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

   //
   //
   //
   private void setUpThread(){
      String name = new String("Fuel System "+this.stage);
      name += (", "+this.engines);
      this._rt0 = new Thread(this, name);
      this._rt0.start();
   }

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
               */
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
