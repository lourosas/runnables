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

public abstract class Pump extends SystemComponent{
   protected int stage;
   protected int tankNumber;

   //////////////////SystemComponents Methods Overrides///////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this.publisher.addSubscriber(subscriber);
      }
      catch(NullPointerException npe){
         this.setPublisher(new PumpPublisher());
         this.publisher.addSubscriber(subscriber);
      }
   }

   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      //THIS NEEDS TO CHANGE!!!!!
      System.out.println("Pump");
      if(this.initializable == null){
         int stg = this.stage;
         int tn  = this.tankNumber;
         this.setInitializable(new PumpInitializable(stg,tn));
      }
      this.initializable.initialize(file);
      //Similar to the Tank, the initialization should be able to be
      //handled at the Pump (abstract) level...
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
            int tn  = this.tankNumber;
            this.dataFeeder = new PumpDataFeeder(stg,tn);
         }
         this.dataFeeder.addInitializable(this.initializable);
      }
   }

   //
   //
   //
   public void setSimulation(boolean isSim){
      super.setSimulation(isSim);
      if(this.dataFeeder == null){
         int stg = this.stage;
         int  tn = this.tankNumber;
         this.dataFeeder = new PumpDataFeeder(stg, tn);
      }
      this.dataFeeder.addInitializable(this.initializable);
      this.dataFeeder.setStateSubstate(this.stateSubstate);
   }

   /////////////////////////Protected Methods/////////////////////////
   //
   //
   //
   protected void alertSubscribers(){
      //Since this is a primitive type System Component, everything is
      //handled at the abstract (Pump) level...
      try{
         PumpData pumpData = null;
         if(this.getStateSubstate() != null){
            //Once the State is set, the Monitorable Thread is running
            pumpData = (PumpData)this.monitorable.monitor();
         }
         else{
            pumpData = (PumpData)this.initializable.initialized();
         }
         //notify the Subscribers
         this.publisher.publish(pumpData);
      }
      catch(NullPointerException npe){}
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      System.out.println("/////////////////////////////////////////");
      System.out.println("\tPump:  Alert Subscribers");
      System.out.println("/////////////////////////////////////////");
   }

   //
   //
   //
   protected void checkErrors(){
      //What needs checking??
      //Pump Rate
      //Temperature (should indicate the temp of the fuel)
      System.out.println("/////////////////////////////////////////");
      System.out.println("\tPump:  Check Errors");
      if(this.getStateSubstate() != null){
         if(this.getStateSubstate().state() == INIT){
            this.checkInitializedStateErrors();
         }
      }
      System.out.println("/////////////////////////////////////////");
   }

   //
   //
   //
   protected void checkInitializedStateErrors(){
      double lowerLimit = 0.;
      double upperLimit = 0.001;
      String error      = null;
      String pubError   = null;
      System.out.println("/////////////////////////////////////////");
      System.out.println("Pump: Check Initialized State Errors");
      error = this.checkPumpRate(upperLimit, lowerLimit);
      if(error != null){
         pubError = (pubError != null) ? pubError+"\n"+error : error;
      }
      lowerLimit = 273.15;
      upperLimit = 373.15;
      error = this.checkTemperature(lowerLimit, upperLimit);
      if(error != null){
         pubError = (pubError != null) ? pubError+"\n"+error : error;
      }
      if(pubError != null){
         synchronized(this.obj){
            this.monitorable.addError(error, false);
            try{
               this.publisher.publish(new RuntimeException(pubError));
            }
            catch(NullPointerException npe){}
         }
      }
      System.out.println("/////////////////////////////////////////");
   }

   //
   //
   //
   protected String checkPumpRate(double ll, double ul){
      String error = null;
      PumpData  pd = null;
      try{
         synchronized(this.obj){
            pd = (PumpData)this.monitorable.monitor();
            double pumpRate = pd.rate();
            if(pumpRate < ll || pumpRate > ul){
               String pre = String.format("%.4f", pumpRate);
               error = new String("Pump Rate Error: " + pre);
            }
         }
      }
      catch(NullPointerException npe){}
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      return error;
   }

   //
   //
   //
   protected String checkTemperature(double ll, double ul){
      String error = null;
      PumpData  pd = null;
      try{
         synchronized(this.obj){
            pd = (PumpData)this.monitorable.monitor();
            double temp = pd.temperature();
            if(temp < ll || temp > ul){
               String tme = String.format("%.2f", temp);
               error = new String("Temperature Error: " + tme);
            }
         }
      }
      catch(NullPointerException npe){}
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      return error;
   }

   //
   //
   //
   protected void monitorPump(){
      if(this.isSimulation){
         System.out.println("//////////////////////////////////////");
         System.out.println("\tPump:  Monitor Pump");
         synchronized(this.obj){
            try{
               //The Monitor Instance runs off of its own Thread...so,
               //it will have to grab its own lock to ensure no race
               //condition...
               PumpData data = null;
               data = (PumpData)this.dataFeeder.monitor();
               this.monitorable.addData(data);
            }
            catch(ClassCastException cce){
               cce.printStackTrace();
               System.exit(1);
            }
         }
         System.out.println("//////////////////////////////////////");
      }
      else{}
   }

   /////////////////SystemComponents Protected Methods////////////////
   //
   //
   //
   protected void setMonitorable(){
      this.setMonitorable(new PumpMonitorable());
   }
}
//////////////////////////////////////////////////////////////////////
