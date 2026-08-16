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
public class GenericLaunchPlatform extends LaunchPlatform 
implements Runnable{
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

      _kill      = false;
      _obj       = null;
      _rt0       = null;
      _start     = false;
      mechanisms = null;
      rocket     = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public GenericLaunchPlatform(){
      this._obj = new Object();
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void initializeMechanisms(String file){
      System.out.println("Platfom Initialization "+file);
   }

   //
   //
   //
   private void setUpThread(){
      String name = new String("Generic Launch Platform");
      this._rt0   = new Thread(this, name);
   }

   //////////////////////Launch Platform Overrides////////////////////
   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      super.initializeComponent(file);
      System.out.println("Launch Platform: "+file);
      this.initializeMechanisms(file);
      /* Stub this first
      try{
         LaunchPlatformData lpData = null;
         lpData = (LaunchPatformData)this.intializable.initialized();
         //Notify the Observers
         this.publisher.publish(lpData);
      }
      catch(NullPointerException npe){
         //Test Print
         npe.printStackTrace();
      }
      catch(ClassCastException   cce){
         //Test Print
         cce.printStackTrace();
      }
      */
   }

   /////////////////Runnable Interface Implementation/////////////////
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
                  //12 seconds
                  if(count++%12000 == 0){
                     check = true;
                     count = 1; //Reset the counter
                  }
               }
               if(check){
                  check = false;
               }
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException  ie){}
      catch(NullPointerException npe){
         npe.printStackTrace();
         System.exit(0);
      }
   }
}
//////////////////////////////////////////////////////////////////////
