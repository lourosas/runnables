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
import java.io.*;
import rosas.lou.runnables.*;

public abstract class Tank extends SystemComponent{
   protected int  stage;
   protected int  number;

   /////////////////SystemComponent Methods Overrides/////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this.publisher.addSubscriber(subscriber);
      }
      catch(NullPointerException npe){
         this.setPublisher(new TankPublisher());
         this.publisher.addSubscriber(subscriber);
      }
   }

   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      System.out.println("Initialize Tank");
      if(this.initializable == null){
         int stg = this.stage;
         int num = this.number;
         this.setInitializable(new TankInitializable(stg, num));
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
         if(this.dataFeeder == null){
            int stg = this.stage;
            int num = this.number;
            this.dataFeeder = new TankDataFeeder(stg,num);
         }
         this.dataFeeder.addInitializable(this.initializable);
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
         if(this.dataFeeder == null){
            int stg = this.stage;
            int num = this.number;
            this.dataFeeder = new TankDataFeeder(stg,num);
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
      //Since this is a primitive type to the System, everything is
      //handled at the Tank (Abstract) Level...
      try{
         TankData tankData = null;
         if(this.getStateSubstate() != null){
            //Once the State is set, the Monitoarble Thread is running
            tankData = (TankData)this.monitorable.monitor();
         }
         else{
            tankData = (TankData)this.initializable.initialized();
         }
         //Notify the Subscribers
         this.publisher.publish(tankData);
      }
      catch(NullPointerException npe){}
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      System.out.println("-----------------------------------------");
      System.out.println("Tank:  Alert Subscribers");
      System.out.println("-----------------------------------------");
   }

   //
   //
   //
   protected void checkErrors(){
      System.out.println("-----------------------------------------");
      System.out.println("Tank:  Check Errors");
      if(this.getStateSubstate() != null){
         if(this.getStateSubstate().state() == INIT){
            this.checkInitializedStateErrors();
         }
      }
      System.out.println("-----------------------------------------");
   }

   //
   //
   //
   private void checkInitializedStateErrors(){
      //What all needs checking?
      //Capacity
      //Temp
      //Empty Rate...
      //the Derived values of Mass Loss Rate and current weight will
      //reflect the Stability of the System...
      System.out.println("-----------------------------------------");
      System.out.println("Tank: Check Initialized State Errors");
      System.out.println("-----------------------------------------");
   }

   //
   //
   //
   protected void monitorTank(){
      if(this.isSimulation){
         System.out.println("--------------------------------------");
         System.out.println("Tank:  Monitor Tank");
         synchronized(this.obj){
            try{
               TankData data = null;
               data = (TankData)this.dataFeeder.monitor();
               this.monitorable.addData(data);
            }
            catch(ClassCastException cce){
               cce.printStackTrace();
               System.exit(1);
            }
         }
         System.out.println("--------------------------------------");
      }
      else{}
   }

   //
   //
   //
   protected void setMonitorable(){
      this.setMonitorable(new TankMonitorable());
   }
}
//////////////////////////////////////////////////////////////////////
