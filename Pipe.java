//////////////////////////////////////////////////////////////////////
/*
Copyright 2024 Lou Rosas

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

public abstract class Pipe extends SystemComponent{
   protected int stage;
   protected int tankNumber;
   //From a Tank--the number of pipes = number of Engines in Stage
   protected int number;  //The Pipe number to the engine

   /////////////////SystemComponents Methods Overrides////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this.publisher.addSubscriber(subscriber);
      }
      catch(NullPointerException npe){
         this.setPublisher(new PipePublisher());
         this.publisher.addSubscriber(subscriber);
      }
   }

   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      System.out.println("Pipe");
      if(this.initializable == null){
         int stg    = this.stage;
         int tkn    = this.tankNumber;
         int num    = this.number;
         this.setInitializable(new PipeInitializable(stg,tkn,num));
      }
      synchronized(this.obj){
         this.initializable.initialize(file);
      }
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
      //More to continue post here...
   }

   //
   //
   //
   public void setSimulation(boolean isSim){
      super.setSimulation(isSim);
      //More to continue post here...
   }

   /////////////////////////Protected Methods/////////////////////////
   //
   //
   //
   protected void alertSubscribers(){
      //Since this is a primitive type System Component, everything is
      //handled at the Abstract (Pipe) Level...
      try{
         PipeData pipeData = null;
         if(this.getStateSubstate() != null){
            //Once the State is set, the Monitorable Thread is running
            pipeData = (PipeData)this.monitorable.monitor();
         }
         else{
            pipeData = (PipeData)this.initializable.initialized();
         }
         //Notity the Subscribers
         this.publisher.publish(pipeData);
      }
      catch(NullPointerException npe){}
      catch(ClassCastException cce){
         cce.printStackTrace();
         System.exit(1);
      }
      System.out.println("+++++++++++++++++++++++++++++++++++++++++");
      System.out.println("Pipe:  Alert Subscribers");
      System.out.println("+++++++++++++++++++++++++++++++++++++++++");
   }

   //
   //
   //
   protected void checkErrors(){
      //What needs checking?
      //Flow Rate
      //Temperature
      System.out.println("+++++++++++++++++++++++++++++++++++++++++");
      System.out.println("Pipe:  Check Errors");
      System.out.println("+++++++++++++++++++++++++++++++++++++++++");
   }

   //
   //
   //
   protected void monitorPipe(){
      if(this.isSimulation){
         System.out.println("++++++++++++++++++++++++++++++++++++++");
         System.out.println("Pipe:  Monitor Pipe");
         synchronized(this.obj){
            try{
               //The Monitor Instance runs off its own Thread...so, it
               //will have to grab its own lock to ensure no race
               //condition...
               PipeData data = null;
               data = (PipeData)this.dataFeeder.monitor();
               this.monitorable.addData(data);
            }
            catch(ClassCastException cce){
               cce.printStackTrace();
               System.exit(1);
            }
         }
         System.out.println("++++++++++++++++++++++++++++++++++++++");
      }
      else{}
   }

   /////////////////SystemComponents Protected Methods////////////////
   //
   //
   //
   protected void setMonitorable(){
      this.setMonitorable(new PipeMonitorable());
   }
}
//////////////////////////////////////////////////////////////////////
