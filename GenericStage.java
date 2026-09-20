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

public class GenericStage extends Stage implements Runnable{
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State INIT      = null; 
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null;

   private boolean               _kill;
   private Thread                _rt0;
   private boolean               _start;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _kill      = false;
      _rt0       = null;
      _start     = false;
      engines    = null;
      fuelSystem = null;
      obj        = null;
      stage      = -1;
   };

   /////////////////////////////Constructor///////////////////////////
   //
   //
   //
   public GenericStage(int number){
      if(number > 0){
         this.stage = number;
      }
      this.obj = new Object();
      this.setUpThread();
   }

   ///////////////////////////Stage Override//////////////////////////
   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      super.initializeComponent(file);
      this.initializeEngines(file);
      this.initializeFuelSystem(file);
      //Super Class Call
      this.setMonitorable();
      this.alertSubscribers();
   }


   //////////////////////////Protected Methods////////////////////////
   //
   //
   //
   protected void initializeEngines(String file)throws IOException{
      try{
         StageData sd = (StageData)this.initializable.initialized();
         System.out.println("%%%%%%%%%%%%%%Generic Stage%%%%%%%%%");
         System.out.println(sd.numberOfEngines());
         System.out.println(this.stage);
         for(int i = 0; i < sd.numberOfEngines(); ++i){
            Engine engine = new GenericEngine(i+1,this.stage);
            //Since the Stage can have multiple engines with mutiple
            //different models...will need to keep track for the model
            //number and the total number for the given engines
            //Model A has 2, Model B has three total for the Stage=5
            //So, NEED TO MAKE SURE when figuring that out!!
            engine.initializeComponent(file);
            EngineData ed = (EngineData)engine.initializationStatus();
            System.out.println("EngineData\n"+ed);
            try{
               //put the engines here!!!
               this.engines.add(engine);
            }
            catch(NullPointerException npe){
               this.engines = new LinkedList<Engine>();
               this.engines.add(engine);
            }
            this.initializable.initializeData("Engine Data", ed);
         }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         throw new IOException("Engine Class Cast Exception");
      }
   }

   //
   //
   //
   protected void initializeFuelSystem(String file)throws IOException{
      try{   
         System.out.println("$$$$$$$$$Generic Fuel System$$$$$$$$$");
         StageData sd  = (StageData)this.initializable.initialized();
         int engines   = sd.numberOfEngines();
         FuelSystem fs = new GenericFuelSystem(this.stage,engines);
         fs.initializeComponent(file);
         FuelSystemData fsd=(FuelSystemData)fs.initializationStatus();
         System.out.println("FuelSystemData\n"+fsd);
         this.initializable.initializeData("Fuel System Data",fsd);
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         throw new IOException("Fuel System Cast Exception");
      }
   }

   ///////////////////////////Private Methods/////////////////////////
   //
   //
   //
   private void setUpThread(){
      String name = new String("Generic Stage "+ this.stage);
      this._rt0   = new Thread(this, name);
      this._rt0.start();
   }
   ////////////////Runnable Interface Implementation//////////////////
   //
   //
   //
   public void run(){
      try{
         int     count   = 0;
         boolean check   = false;
         int     compare = -1;
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this.getStateSubstate() != null){
               if(this.getStateSubstate().state() == INIT){
                  //In the Initialization State, check every 2 seconds
                  compare = 2000;
               }
               if((compare > 0) && (count++%compare == 0)){
                  check = true;
                  count = 1;  //Reset the Counter
               }
            }
            if(check){
               this.monitorEngines();
               this.monitorFuelSystem();
               this.checkErrors();
               this.alertSubscribers();
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
