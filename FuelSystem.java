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

import java.util.*;
import rosas.lou.runnables.*;
import java.io.IOException;

public abstract class FuelSystem extends SystemComponent{
   protected int        engines;
   protected int        stage;
   protected Tank       fuel;
   protected Tank       oxidizer;
   protected List<Pipe> pipes;
   protected List<Pump> pumps;

   /////////////////SystemComponent Methods Overrides/////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this.publisher.addSubscriber(subscriber);
      }
      catch(NullPointerException npe){
         this.setPublisher(new FuelSystemPublisher());
         this.publisher.addSubscriber(subscriber);
      }
   }

   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      System.out.println("Fuel System");
      if(this.initializable == null){
         int st  = this.stage;
         int egs = this.engines;
         this.setInitializable(new FuelSystemInitializable(st,egs));
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
            int stage       = this.stage;
            int engines     = this.engines;
            this.dataFeeder = new FuelSystemDataFeeder(stage,engines);
         }
         this.dataFeeder.addInitializable(this.initializable);
      }
   }

   //
   //
   //
   public void setSimulation(boolean isSim){
      super.setSimulation(isSim);
      //The Stack Trace Prints are Temporary!!!
      try{
         this.fuel.setSimulation(isSim);
         this.oxidizer.setSimulation(isSim);
      }
      catch(NullPointerException npe){ npe.printStackTrace(); }
      try{
         Iterator<Pipe> it = this.pipes.iterator();
         while(it.hasNext()){
            it.next().setSimulation(isSim);
         }
      }
      catch(NullPointerException npe){ npe.printStackTrace(); }
      try{
         Iterator<Pump> it = this.pumps.iterator();
         while(it.hasNext()){
            it.next().setSimulation(isSim);
         }
      }
      catch(NullPointerException npe){ npe.printStackTrace(); }
      if(this.isSimulation && this.initializable != null){
         if(this.dataFeeder == null){
            int stage       = this.stage;
            int engines     = this.engines;
            this.dataFeeder = new FuelSystemDataFeeder(stage,engines);
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
         FuelSystemData fsd = null;
         if(this.getStateSubstate() != null){
            //Once the State is set, the Monitor Thread is running
            fsd = (FuelSystemData)this.monitorable.monitor();
         }
         else{
            fsd = (FuelSystemData)this.initializable.initialized();
         }
         this.publisher.publish(fsd);
      }
      catch(NullPointerException npe){}
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      System.out.println("*****************************************");
      System.out.println("Fuel System:  Alert Subscribers");
      System.out.println("*****************************************");
   }

   //
   //
   //
   protected int getTotalPipesInStage(String file)throws IOException{
      int pipes = 0;

      return pipes;
   }

   //
   //
   //
   protected int getTotalPumpsInStage(String file)throws IOException{
      int pumps = 0;

      return pumps;
   }

   //
   //
   //
   protected void initializePipes(String file)throws IOException{}

   //
   //
   //
   protected void initializePumps(String file)throws IOException{}

   //
   //
   //
   protected void initializeTanks(String file)throws IOException{}

   /////////////////SystemComponent Protected Methods/////////////////
   //
   //
   //
   protected void setMonitorable(){
      this.setMonitorable(new FuelSystemMonitorable());
   }

   //////////////////StateMutuable Interface Overrides////////////////
   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate ss){
      super.setStateSubstate(ss);
      try{
         this.fuel.setStateSubstate(ss);
         this.oxidizer.setStateSubstate(ss);
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      try{
         Iterator<Pipe> it = this.pipes.iterator();
         while(it.hasNext()){
            it.next().setStateSubstate(ss);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      try{
         Iterator<Pump> it = this.pumps.iterator();
         while(it.hasNext()){
            it.next().setStateSubstate(ss);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }
}
//////////////////////////////////////////////////////////////////////
