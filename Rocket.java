/////////////////////////////////////////////////////////////////////
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
import java.io.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public abstract class Rocket extends SystemComponent{
   protected List<Stage> stages  = null;
   protected Payload     payload = null;
   //////////////////////////Public Methods///////////////////////////
   //
   //
   //

   ///////////////SystemComponent Methods Overrides///////////////////
   //
   //This now needs to be fixed
   //
   public void addSubscriber(Subscriber subscriber){
      synchronized(this.obj){
         try{
            this.publisher.addSubscriber(subscriber);
         }
         catch(NullPointerException npe){
            this.setPublisher(new RocketPublisher());
            this.publisher.addSubscriber(subscriber);
         }
      }
   }

   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      System.out.println("Rocket");
      if(this.initializable == null){
         this.setInitializable(new RocketInitializable());
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
         DataFeeder feeder = RocketDataFeeder.instance();
         feeder.addInitializable(this.initializable);
      }
   }

   //
   //
   //
   public void setSimulation(boolean isSim){
      super.setSimulation(isSim);
      try{
         Iterator<Stage> it = this.stages.iterator();
         while(it.hasNext()){
            it.next().setSimulation(isSim);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      try{
         this.payload.setSimulation(isSim);
      }
      catch(NullPointerException npe){}
      if(this.isSimulation && this.initializable != null){
         DataFeeder feeder = RocketDataFeeder.instance();
         feeder.addInitializable(this.initializable);
         feeder.setStateSubstate(this.stateSubstate);
      }
   }

   /////////////////////////Protected Methods/////////////////////////
   //
   //
   //
   protected void alertSubscribers(){
      try{
         RocketData rocketData = null;
         if(this.getStateSubstate() != null){
            //Once the state is set, the monitor thread is running, so
            //use the Monitorable instance 
            rocketData = (RocketData)this.monitorable.monitor();
         }
         else{
            rocketData = (RocketData)this.initializable.initialized();
         }
         //Notify the Observers
         this.publisher.publish(rocketData);
      }
      catch(NullPointerException npe){
         //npe.printStackTrace();
      }
      catch(ClassCastException cce){
         //cce.printStackTrace();
      }
      System.out.println("*****************************************");
      System.out.println("Rocket:  Alert Subscribers");
      System.out.println("*****************************************");
   }

   //
   //
   //
   protected void checkErrors(){
      System.out.println("*****************************************");
      System.out.println("Rocket:  Check Errors");
      System.out.println("*****************************************");
   }

   //For the Rocket, honestly, just want to check for the Calculated
   //Weight...
   //
   protected void checkInitializedStateErrors(){
      //In Initialization State, compare against Empty Weight
      RocketData rd  = null;
      synchronized(this.obj){
         try{
            rd         = (RocketData)this.monitorable.monitor();
            double cw  = rd.calculatedWeight();
            double ew  = rd.emptyWeight();
            double tol = rd.tolerance();
            double lrl = ew*tol; //Empty Weight * tolerance
            double upl = ew*(2-tol);     
            //If out of range, set error...
            if(cw < lrl || cw > upl){
               //Alert the Monitorable Object
               String error = new String("Initialized State: ");
               error += "Calculated Weight Out of Range";
               this.monitorable.addError(error);
               //Publish the Exception
               this.publisher.publish(new RuntimeException(error));
            }

         }
         catch(ClassCastException cce){
            rd = null;
         }
         catch(NullPointerException npe){}
      }
   }

   //
   //
   //
   protected void initializePayload(String file)throws IOException{}

   //
   //
   //
   protected void initializeStages(String file)throws IOException{}

   //
   //
   //
   protected void monitorPayload(){
      System.out.println("*****************************************");
      System.out.println("Rocket:  Monitor Payload");
      System.out.println("*****************************************");
   }

   //
   //
   //
   protected void monitorRocket(){
      //Have one for Simulation Mode and one for "Straight Measure"
      if(this.isSimulation){
         System.out.println("**************************************");
         System.out.println("Rocket:  Monitor Rocket");
         synchronized(this.obj){
            try{
               RocketData data = null;
               data=(RocketData)RocketDataFeeder.instance().monitor();
               this.monitorable.addData(data);
            }
            catch(ClassCastException cce){}
         }
         System.out.println("**************************************");  
      }
      else{}//For "Straight Measure"
   }
   //
   //
   //
   protected void monitorStages(){
      System.out.println("*****************************************");
      System.out.println("Rocket:  Monitor Stages");
      Iterator<Stage> it = this.stages.iterator();
      try{
         //TBD
         while(it.hasNext()){
            StageData data = (StageData)it.next().monitor();
            //Account for the Add...TBD
            this.monitorable.addData("Stage Data", data);
         }
      }
      catch(ClassCastException cce){}
      System.out.println("*****************************************");
   }

   //
   //
   //
   protected void setMonitorable(){
      this.setMonitorable(new RocketMonitorable());
   }

   /////////////////StateMutable Interface Overrides//////////////////
   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate ss){
      super.setStateSubstate(ss);
      try{
         Iterator<Stage> it = this.stages.iterator();
         while(it.hasNext()){
            it.next().setStateSubstate(ss);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      try{
         this.payload.setStateSubstate(ss);
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      if(this.isSimulation){
         RocketDataFeeder.instance().setStateSubstate(ss);
      }
   }
}
//////////////////////////////////////////////////////////////////////
