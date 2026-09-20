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
      Iterator<Engine> it = this.engines.iterator();
      try{
         while(it.hasNext()){
            EngineData data = (EngineData)it.next().monitor();
            this.monitorable.addData("Engine Data",data);
         }
      }
      catch(ClassCastException cce){}
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
