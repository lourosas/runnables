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

   private LaunchStateSubstate.State INIT                      = null;
   private LaunchStateSubstate.State PRELAUNCH                 = null;
   private LaunchStateSubstate.State IGNITION                  = null;
   private LaunchStateSubstate.State LAUNCH                    = null;
   private LaunchStateSubstate.PreLaunchSubstate SET           = null;
   private LaunchStateSubstate.PreLaunchSubstate CONT          = null;
   private LaunchStateSubstate.PreLaunchSubstate FUEL          = null;
   private LaunchStateSubstate.PreLaunchSubstate HOLD          = null;
   private LaunchStateSubstate.IgnitionSubstate  IGN           = null;
   private LaunchStateSubstate.IgnitionSubstate  BUP           = null;
   private LaunchStateSubstate.AscentSubstate    STG           = null;
   private LaunchStateSubstate.AscentSubstate    IGNE          = null;

   private boolean             _kill;
   private Object              _obj;
   private Thread              _rt0;
   private boolean             _start;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;
      SET       = LaunchStateSubstate.PreLaunchSubstate.SET;
      CONT      = LaunchStateSubstate.PreLaunchSubstate.CONTINUE;
      FUEL      = LaunchStateSubstate.PreLaunchSubstate.FUELING;
      HOLD      = LaunchStateSubstate.PreLaunchSubstate.HOLD;
      IGN       = LaunchStateSubstate.IgnitionSubstate.IGNITION;
      BUP       = LaunchStateSubstate.IgnitionSubstate.BUILDUP;
      STG       = LaunchStateSubstate.AscentSubstate.STAGING;
      IGNE      = LaunchStateSubstate.AscentSubstate.IGNITEENGINES;

      _kill             = false;
      _obj              = null;
      payload           = null;//Sone sort of payload!!
      _rt0              = null;
      stages            = null;//At least 1 stage!
      _start            = false;
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
      try{
         RocketData rocketData = null;
         rocketData = (RocketData)this.initializable.initialized();
         //Notify the Observers
         this.publisher.publish(rocketData);
      }
      catch(NullPointerException npe){
         //npe.printStackTrace();
      }
      catch(ClassCastException cce){
         //cce.printStackTrace();
      }
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
                  //In the Initialization Stage, check every
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
              //Eventually perform all of this...
              /*
              this.monitorRocket();
              this.checkErrors();
              this.alertSubscribers();
              */
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
