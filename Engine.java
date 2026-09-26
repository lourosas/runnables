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
import rosas.lou.runnables.*;
import java.io.IOException;

public abstract class Engine extends SystemComponent{
   protected int engine; //Engine Number
   protected int stage;  //Stage

   /////////////////SystemComponent Methods Overrides/////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this.publisher.addSubscriber(subscriber);
      }
      catch(NullPointerException npe){
         this.setPublisher(new EnginePublisher());
         this.publisher.addSubscriber(subscriber);
      }
   }

   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      System.out.println("Engine");
      if(this.initializable == null){
         int en = this.engine;
         int st = this.stage;
         //Stage and Engine Number is needed
         this.setInitializable(new EngineInitializable(en, st));
      }
      synchronized(this.obj){
         this.initializable.initialize(file);
      }
      this.setMonitorable();
      this.alertSubscribers();
   }

   //
   //
   //
   public void setInitializable(Initializable init){
      super.setInitializable(init);
      if(this.isSimulation){
         //Grab or create the Data Feeder
         //Add the Initializable to the Data Feeder
      }
   }

   //
   //
   //
   public void setSimulation(boolean isSim){
      super.setSimulation(isSim);
      if(this.isSimulation && this.initializable != null){
         //Grab or create the Data Feeder
         //Add Initializable to the Data Feeder
         //Set the substate to the Feeder
      }
   }

   /////////////////////////Protected Methods/////////////////////////
   //
   //
   //
   protected void alertSubscribers(){
      //Everything can be handled at the Engine (Abstract) Level
      //In Initializaton, any way...
      try{
         EngineData engineData = null;
         if(this.getStateSubstate() != null){
            //Once the State is set, the Monitor Thread is running
            engineData = (EngineData)this.monitorable.monitor();
         }
         else{
            engineData = (EngineData)this.initializable.initialized();
         }
         //Notify the Subscribers
         this.publisher.publish(engineData);
      }
      catch(NullPointerException npe){}
      catch(ClassCastException cce){}   
      System.out.println("*****************************************");
      System.out.println("Engine:  Alert Subscribers");
      System.out.println("*****************************************");
   }


   //
   //
   //
   protected void checkErrors(){
      if(this.getStateSubstate() != null){
         if(this.getStateSubstate().state() == INIT){
            this.checkInitializedStateErrors();
         }
         System.out.println("**************************************");
         System.out.println("Engine:  Check Errors");
         System.out.println("**************************************");
      }
   }

   //
   //
   //
   protected void checkInitializedStateErrors(){
       System.out.println("***************************************");
       System.out.println("Engine: Check Initialized State Errors");
       System.out.println("***************************************");
   }

   //
   //
   //
   protected void monitorExhaustFlowRate(){
      //Simulation or "Striaght measure"
      if(this.isSimulation){
         System.out.println("**************************************");
         System.out.println("Engine:  Monitor Exhaust Flow Rate");
         System.out.println("**************************************");
      }
      else{}
   }

   //
   //
   //
   protected void monitorFuelFlowRate(){
      //Simulation or "Straight Measure"
      if(this.isSimulation){
         System.out.println("**************************************");
         System.out.println("Engine:  Monitor Fuel Flow Rate");
         System.out.println("**************************************");
      }
      else{}
   }

   //
   //
   //
   protected void monitorTemperature(){
      //Simulation or Straight Measure
      if(this.isSimulation){
         System.out.println("**************************************");
         System.out.println("Engine:  Monitor Temperature");
         System.out.println("**************************************");
      }
      else{}
   }

   //
   //
   //
   protected void setMonitorable(){
      this.setMonitorable(new EngineMonitorable());
   }
}
//////////////////////////////////////////////////////////////////////
