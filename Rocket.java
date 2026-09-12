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

   //
   //
   //
   protected void initializePayload(String file)throws IOException{}

   //
   //
   //
   protected void initializeStage(String file)throws IOException{}

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
   protected void monitorStages(){
      System.out.println("*****************************************");
      System.out.println("Rocket:  Monitor Stages");
      System.out.println("*****************************************");
   }

   //
   //
   //
   protected void monitorRocket(){
      System.out.println("*****************************************");
      System.out.println("Rocket:  Monitor Rocket");
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
   }
}
//////////////////////////////////////////////////////////////////////
