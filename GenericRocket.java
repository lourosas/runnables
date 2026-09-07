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
import java.io.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class GenericRocket extends Rocket implements  Runnable{
   private static boolean TOPRINT = true;

   private boolean             _kill;
   private Object              _obj;
   private Thread              _rt0;
   private boolean             _start;

   {
      _kill             = false;
      _obj              = null;
      _rt0              = null;
      _start            = false;
      payload           = null;//Sone sort of payload!!
      stages            = null;//At least 1 stage!
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public GenericRocket(){
      this._obj = new Object();
      this.setUpThread();
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void alertSubscribers(){
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
   private void checkErrors(){
      System.out.println("*****************************************");
      System.out.println("Rocket:  Check Errors");
      System.out.println("*****************************************");
   }

   //
   //
   //
   private void initializePayload(String file)throws IOException{
      try{
         this.payload = new GenericPayload();
         this.payload.initializeComponent(file);
         PayloadData pd = null;
         pd = (PayloadData)this.payload.initializationStatus();
         this.initializable.initializeData("Payload Data", pd);
      }
      catch(ClassCastException cce){
         throw new IOException("Payload Class Cast Exception");
      }
   }

   //
   //
   //
   private void initializeStages(String file)throws IOException{
      try{
         RocketData rd = (RocketData)this.initializable.initialized();
         for(int i = 0; i < rd.numberOfStages(); ++i){
            Stage stage = new GenericStage(i+1);
            stage.initializeComponent(file);
            StageData sd = (StageData)stage.initializationStatus();
            try{
               //Might need to cast
               this.stages.add(stage);
            }
            catch(NullPointerException npe){
               this.stages = new LinkedList<Stage>();
               this.stages.add(stage);
            }
            this.initializable.initializeData("Stage Data", sd);
         }
      }
      catch(ClassCastException cce){
         throw new IOException("Stage Class Cast Exception");
      }
   }

   //
   //
   //
   private void monitorPayload(){
      System.out.println("*****************************************");
      System.out.println("Rocket:  Monitor Payload");
      System.out.println("*****************************************");
   }

   //
   //
   //
   private void monitorRocket(){
      System.out.println("*****************************************");
      System.out.println("Rocket:  Monitor Rocket");
      System.out.println("*****************************************");
   }

   //
   //
   //
   private void monitorStages(){
      System.out.println("*****************************************");
      System.out.println("Rocket:  Monitor Stages");
      System.out.println("*****************************************");
   }

   //
   //
   //
   private void setUpThread(){
      String name = new String("Generic Rocket");
      this._rt0 = new Thread(this, name);
      this._rt0.start();
   }

   /////////////////////////Rocket Override///////////////////////////
   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      super.initializeComponent(file);
      this.initializeStages(file);
      this.initializePayload(file);
      //The Super Class call
      this.setMonitorable();
      this.alertSubscribers();
   }
   ///////////////Runnable Interface Implementation///////////////////
   //
   //
   //
   public void run(){
      try{
         int     count = 0;
         boolean check = false;
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this.getStateSubstate() != null){
               if(this.getStateSubstate().state() == INIT){
                  //In the Initialization State, check every
                  //10 Seconds
                  if(count++%10000 == 0){
                     check = true;
                     count = 1; //Reset the Counter
                  }
               }
            }
            if(check){
              System.out.println("\nGR 1\n+++++++++++++++++++++++");
              System.out.print("Rocket: ");
              System.out.println(Thread.currentThread().getName());
              System.out.print("Rocket: ");
              System.out.println(Thread.currentThread().getId());
              this.monitorRocket();
              this.monitorStages();
              this.monitorPayload();
              this.checkErrors();
              this.alertSubscribers();
              System.out.println("+++++++++++++++++++++++\nGR 2\n");
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
