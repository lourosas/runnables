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
      catch(NullPointerException npe){}
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
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      System.out.println("*****************************************");
      System.out.println("Stage:  Alert Subscribers");
      System.out.println("*****************************************");
   }

   //
   //
   //
   protected String checkEngineErrors(){
      String error = null;
      Iterator<Engine> it = this.engines.iterator();
      try{
         while(it.hasNext()){
            EngineData data = (EngineData)it.next().monitor();
            if(data.isError() && data.error() != null){
               if(error == null){
                  error = data.error();
               }
               else{
                  error += "\n" + data.error();
               }
            }
         }
      }
      catch(NullPointerException npe){
         error = null;
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      return error;
   }

   //
   //
   //
   protected void checkErrors(){
      String error    = null;
      String pubError = null;
      System.out.println("*****************************************");
      System.out.println("Stage:  Check Errors");
      error = this.checkEngineErrors();
      if(error != null){
         pubError = pubError != null ? pubError+"\n"+error : error;
      }
      error = this.checkFuelSystemErrors();
      if(error != null){
         pubError = pubError != null ? pubError+"\n"+error : error;
      }
      if(pubError != null){
         this.monitorable.addError(pubError, false);
         try{
            this.publisher.publish(new RuntimeException(pubError));
         }
         catch(NullPointerException npe){}
      }
      System.out.println("*****************************************");
   }

   //
   //
   //
   private String checkFuelSystemErrors(){
      String error = null;

      return error;
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
   protected List<EngineData> monitorEngines(){
      List<EngineData> data = new LinkedList<EngineData>();
      try{
         Iterator<Engine> it = this.engines.iterator();
         while(it.hasNext()){
            data.add((EngineData)(it.next().monitor()));
         }
      }
      catch(NullPointerException npe){}
      //Test Prints--remove
      System.out.println("Stage: monitorEngines: "+data);
      return data;
   }

   //
   //
   //
   protected List<FuelSystemData> monitorFuelSystem(){
      System.out.println("*****************************************");
      System.out.println("Stage:  Monitor Fuel System");
      System.out.println("*****************************************");
      return null;
   }

   //
   //
   //
   protected void monitorStage(){
      System.out.println("-----------------------------------------");
      System.out.println("\tStage:  Monitor Stage");
      if(this.isSimulation){
         try{
            synchronized(this.obj){
               StageData d = (StageData)this.dataFeeder.monitor();
               this.monitorable.addData(d);
            }
         }
         catch(ClassCastException cce){
            cce.printStackTrace();
            System.exit(1);
         }
      }
      else{}
      System.out.println("-----------------------------------------");
   }

   //
   //
   //
   protected void setEngineData(){
      System.out.println("-----------------------------------------");
      System.out.println("\tStage:  setEngineData()");
      this.monitorable.addData("Engines", this.monitorEngines());
      System.out.println("-----------------------------------------");
   }

   //
   //
   //
   protected void setFuelSystemData(){
      System.out.println("-----------------------------------------");
      System.out.println("\tStage:  setFuelSystemData()");
      List<FuelSystemData> fsd = this.monitorFuelSystem();
      this.monitorable.addData("Fuel System",fsd);
      System.out.println("-----------------------------------------");
   }

   /////////////////SystemComponent Protected Methods/////////////////
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
