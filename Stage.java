//////////////////////////////////////////////////////////////////////
/*
Copyright 2026 Lou Rosas

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
import rosas.lou.runnables.*;
import java.io.IOException;

public abstract class Stage extends SystemComponent{
   protected List<Engine> engines;
   protected FuelSystem   fuelSystem;
   protected int          stage;

   //////////////////////////Public Methods///////////////////////////

   ////////////////SystemComponent Methods Overrides//////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      synchronized(this.obj){
         try{
            this.publisher.addSubscriber(subscriber);
         }
         catch(NullPointerException npe){
            this.setPublisher(new StagePublisher());
            this.publisher.addSubscriber(subscriber);
         }
      }
   }

   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      System.out.println("Stage");
      if(this.initializable == null){
         this.setInitializable(new StageInitializable(this.stage));
      }
      synchronized(this.obj){
         this.initializable.initialize(file);
      }
   }

   //
   //
   //
   public void setInitializable(Initializable init){
      super.setInitializable(init);
      if(this.isSimulation){
         if(this.dataFeeder == null){
            int stg = this.stage;
            this.dataFeeder = new StageDataFeeder(stg);
         }
         this.dataFeeder.addInitializable(this.initializable);
      }
   }

   //
   //
   //
   public void setSimulation(boolean isSim){
      super.setSimulation(isSim);
      try{
         Iterator<Engine> it = this.engines.iterator();
         while(it.hasNext()){
            it.next().setSimulation(isSim);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      try{
         this.fuelSystem.setSimulation(isSim);
      }
      catch(NullPointerException npe){}
      if(this.isSimulation && this.initializable != null){
         if(this.dataFeeder == null){
            int stg = this.stage;
            this.dataFeeder = new StageDataFeeder(stg);
         }
         this.dataFeeder.addInitializable(this.initializable);
         this.dataFeeder.setStateSubstate(this.stateSubstate);
      }
   }

   /////////////////////////Protected Methods/////////////////////////
   //
   //
   //
   protected void alertSubscribers(){
      try{
         StageData stageData = null;
         if(this.getStateSubstate() != null){
            //Once the State is set, the Monitor Thread is running
            stageData = (StageData)this.monitorable.monitor();
         }
         else{
            stageData = (StageData)this.initializable.initialized();
         }
         this.publisher.publish(stageData);
      }
      catch(NullPointerException npe){}
      catch(ClassCastException cce){}
      System.out.println("*****************************************");
      System.out.println("Stage:  Alert Subscribers");
      System.out.println("*****************************************");
   }

   //
   //
   //
   protected void checkErrors(){
      System.out.println("*****************************************");
      System.out.println("Stage:  Check Errors");
      System.out.println("*****************************************");
   }

   //
   //
   //
   protected void initializeEngines(String file)throws IOException{}

   //
   //
   //
   protected void initializeFuelSystem(String file)throws IOException{
   }

   //
   //
   //
   protected void monitorEngines(){
      System.out.println("*****************************************");
      System.out.println("Stage:  Monitor Engines");
      //First, clear out the Engine Data in the Stage...
      this.monitorable.addData("Engine Data", null);
      Iterator<Engine> it = this.engines.iterator();
      try{
         //Then, add new monitored data to the Monitorable...
         while(it.hasNext()){
            synchronized(this.obj){
               EngineData data = (EngineData)it.next().monitor();
               this.monitorable.addData("Engine Data",data);
            }
         }
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      catch(NullPointerException npe){}
      System.out.println("*****************************************");
   }

   //
   //
   //
   protected void monitorFuelSystem(){
      System.out.println("*****************************************");
      System.out.println("Stage:  Monitor Fuel System");
      System.out.println("*****************************************");
   }

   //The Stage Monitoring only needs to happen in Initialization and
   //only needs to happen once.  This is due in actual simulation,
   //there is nothing of the Stage to monitor outside of the
   //Compnents--whch are monitored independantly.
   protected void monitorStage(){
      boolean toMonitor = false;
      if(this.stateSubstate.state() == INIT){
         if(this.monitorable.monitor() == null){
            toMonitor = true;
         }
      }
      if(toMonitor){
         //Have one Simulation Mode and one for "Straight Measure"
         if(this.isSimulation){
            System.out.println("************************************");
            System.out.println("*******Stage:  Monitor Stage********");
            if(this.monitorable.monitor() == null){
               try{
                  synchronized(this.obj){
                     StageData d=(StageData)this.dataFeeder.monitor();
                     this.monitorable.addData(d);
                  }
               }
               catch(ClassCastException cce){
                  cce.printStackTrace();
                  System.exit(1);
               }
            }
            System.out.println("************************************");
         }
         else{}
      }
   }

   //
   //
   //
   protected void setMonitorable(){
      this.setMonitorable(new StageMonitorable());
   }


   /////////////////StateMutable Interface Overrides//////////////////
   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate ss){
      super.setStateSubstate(ss);
      try{
         Iterator<Engine> it = this.engines.iterator();
         while(it.hasNext()){
            it.next().setStateSubstate(ss);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      try{
         this.fuelSystem.setStateSubstate(ss);
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }
}
//////////////////////////////////////////////////////////////////////
