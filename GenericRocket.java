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
   private Thread              _rt0;
   private boolean             _start;

   {
      _kill             = false;
      _rt0              = null;
      _start            = false;
      obj               = null;
      payload           = null;//Sone sort of payload!!
      stages            = null;//At least 1 stage!
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public GenericRocket(){
      this.obj = new Object();
      this.setUpThread();
   }

   ////////////////////////Protected Methods//////////////////////////
   //
   //
   //
   protected void checkErrors(){
      super.checkErrors();
      if(this.getStateSubstate() != null){
         if(this.getStateSubstate().state() == INIT){
            this.checkInitializedStateErrors();
         }
      }
   }

   //
   //
   //
   protected void checkInitializedStateErrors(){
      super.checkInitializedStateErrors();
   }

   //
   //
   //
   protected void initializePayload(String file)throws IOException{
      try{
         this.payload = new GenericPayload();
         this.payload.initializeComponent(file);
         PayloadData pd = null;
         pd = (PayloadData)this.payload.initializationStatus();
         synchronized(this.obj){
            this.initializable.initializeData("Payload Data", pd);
         }
      }
      catch(ClassCastException cce){
         throw new IOException("Payload Class Cast Exception");
      }
   }

   //
   //
   //
   protected void initializeStages(String file)throws IOException{
      try{
         RocketData rd = (RocketData)this.initializable.initialized();
         for(int i = 0; i < rd.numberOfStages(); ++i){
            Stage stage = new GenericStage(i+1);
            stage.initializeComponent(file);
            StageData sd = (StageData)stage.initializationStatus();
            synchronized(this.obj){
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
      }
      catch(ClassCastException cce){
         throw new IOException("Stage Class Cast Exception");
      }
   }

   /////////////////////////Private Methods///////////////////////////
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
