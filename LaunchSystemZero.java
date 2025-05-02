//////////////////////////////////////////////////////////////////////
/*
Copyright 2025 Lou Rosas

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
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class LaunchSystemZero
implements ErrorListener,LaunchSystem,Publisher,SystemListener{
   private LaunchStateSubstate.State             INIT  = null;
   private LaunchStateSubstate.State             PREL  = null;
   private LaunchStateSubstate.State             IGNI  = null;
   private LaunchStateSubstate.State             LAUN  = null;
   private LaunchStateSubstate.State             ASCE  = null;
   private LaunchStateSubstate.PreLaunchSubstate SET   = null;
   private LaunchStateSubstate.PreLaunchSubstate CONT  = null;
   private LaunchStateSubstate.PreLaunchSubstate FUEL  = null;
   private LaunchStateSubstate.PreLaunchSubstate HOLD  = null;
   private LaunchStateSubstate.IgnitionSubstate  IGN   = null;
   private LaunchStateSubstate.IgnitionSubstate  BUP   = null;
   private LaunchStateSubstate.AscentSubstate    STG   = null;
   private LaunchStateSubstate.AscentSubstate    IGNE  = null;

   private LaunchStateSubstate stateSubstate;

   private ClockSubscriber     clockSubsciber;
   private Subscriber          subscriber;
   private CountdownTimer      countdownTimer;
   //private Rocket              rocket;
   private LaunchingMechanism  launchingMechanism;
   //private Payload             payload;
   private Thread              rt0;   //Probably not needed
   private boolean             start; //related to threading
   private boolean             kill;  //related to threading
   private DataFeeder          mechanismDataFeeder;
   private DataFeeder          rocketDataFeeder;

   {
      INIT = LaunchStateSubstate.State.INITIALIZE;
      PREL = LaunchStateSubstate.State.PRELAUNCH;
      IGNI = LaunchStateSubstate.State.IGNITION;
      LAUN = LaunchStateSubstate.State.LAUNCH;
      ASCE = LaunchStateSubstate.State.ASCENT;
      SET  = LaunchStateSubstate.PreLaunchSubstate.SET;
      CONT = LaunchStateSubstate.PreLaunchSubstate.CONTINUE;
      FUEL = LaunchStateSubstate.PreLaunchSubstate.FUELING;
      HOLD = LaunchStateSubstate.PreLaunchSubstate.HOLD;
      IGN  = LaunchStateSubstate.IgnitionSubstate.IGNITION;
      BUP  = LaunchStateSubstate.IgnitionSubstate.BUILDUP;
      STG  = LaunchStateSubstate.AscentSubstate.STAGING;
      IGNE = LaunchStateSubstate.AscentSubstate.IGNITEENGINES;

      stateSubstate       = null;
      subscriber          = null;
      countdownTimer      = null;
      launchingMechanism  = null;
      rt0                 = null;
      start               = false;
      kill                = false;
      mechanismDataFeeder = null;
      rocketDataFeeder    = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public LaunchSystemZero(){}

   //
   //
   //
   public LaunchSystemZero(Subscriber sub){
      this.addSubscriber(sub);
   }

   //
   //
   //
   public LaunchSystemZero(Subscriber sub, ClockSubscriber csub){
      this.addSubscriber(sub);
      this.addClockSubscriber(csub);
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void addClockSubscriber(ClockSubscriber cs){
      this.clockSubscriber = cs;
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void initializeLaunchingMechanism(String file)
   throws IOException{
      try{
         this.launchingMechanism = new GenericLaunchingMechanism();
         this.launchingMechanism.initialize(file);
         this.launchingMechanism.addErrorListener(this);
         this.launchingMechanism.addSystemListener(this);
         //LaunchingMechanismData lmd = null;
         //lm = this.launchingMechansim.monitorInitialization();
         //this.notify("Initilize",lmd);
      }
      catch(IOException ioe){
         //this.error(ioe.getMessage(),null);--need to add!
         throw ioe;
      }
   }

   //
   //
   //
   private void initializePayload(String file){}

   //
   //
   //
   private void initializeRocket(String file){}
   
   
   /////////////ErrorListener Interface Implementation////////////////
   //
   //
   //
   public void errorOccurred(ErrorEvent e){}

   //////////////LaunchSystem Interface Implementation////////////////
   //
   //
   //
   public void addDataFeeder(String type, DataFeeder feeder){
      try{
         if(type.toUpperCase().contains("ROCKET")){
            this.rocketDataFeeder = feeder;
         }
         else if(type.toUpperCase().contains("LAUNCHMECHANISM")){
            this.mechanismDataFeeder = feeder;
         }
         this.launchingMechanism.addDataFeeder(type,feeder);
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   public void initialize(String file){
      try{
         this.stateSubstate = new LaunchStateSubstate(INIT,
                                                      null,
                                                      null,
                                                      null);
         this.initilaizeRocket(file);
         this.initializeLaunchingMechanism(file);
         this.initializePayload(file);
         //Go ahead and set this!!!
         this.rocketDataFeeder = new RocketDataFeeder();
         this.rocketDataFeeder.initialize(file);
         this.launchingMechanism.setDataFeeder(this.rocketDataFeeder);
      }
      catch(IOException ioe){}
   }

   ////////////////Publisher Interface Implementation/////////////////
   //
   //
   //
   public void add(Subscriber s){
      this.subscriber = s;
   }

   //
   //
   //
   public void error(String s, Object o){}

   //
   //
   //
   public void notify(String s, Object o){}

   //
   //
   //
   public void remove(Subscriber s){
      if(this.subscriber == s){
         this.subscriber = null;
      }
   }

   //////////////SystemListener Interface Implementation//////////////
   //
   //
   //
   public void update(MissionSystemEvent event){}
}
//////////////////////////////////////////////////////////////////////
