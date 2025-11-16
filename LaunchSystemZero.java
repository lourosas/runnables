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
import java.io.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class LaunchSystemZero
implements ErrorListener,LaunchSystem,Publisher,SystemListener{
   private enum Sim{NO, YES}

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
   private Sim                 simState; //Set once, do NOT change!

   private ClockSubscriber     clockSubscriber;
   private Subscriber          subscriber;
   private CountdownTimer      countdownTimer;
   private Rocket              rocket;
   private LaunchingMechanism  launchingMechanism;
   //private Payload             payload;
   private Thread              rt0;   //Probably not needed
   private boolean             start; //related to threading
   private boolean             kill;  //related to threading
   private DataFeeder          feeder;

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
      //Unfortunately, need to initialize the State to Sim.NO
      simState            = Sim.NO;
      subscriber          = null;
      countdownTimer      = null;
      launchingMechanism  = null;
      rocket              = null;
      rt0                 = null;
      start               = false;
      kill                = false;
      feeder              = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public LaunchSystemZero(DataFeeder feeder){
      this(feeder, null, null);
   }

   //
   //
   //
   public LaunchSystemZero(DataFeeder feeder, Subscriber sub){
      this(feeder, sub, null);
   }

   //
   //
   //
   public LaunchSystemZero
   (
      DataFeeder feeder,
      Subscriber sub,
      ClockSubscriber csub
   ){
      this.addFeeders(feeder);
      this.add(sub);
      this.addClockSubscriber(csub);
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void addClockSubscriber(ClockSubscriber cs){
      if(cs != null){
         this.clockSubscriber = cs;
      }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void addFeeders(DataFeeder feeder){
      //This is no longer needed...
      //simState set by the input of this.simulation(...) method
      /*
      if(feeder != null){
         this.simState = Sim.YES;
         //Set all the Simualation Feeder data
         this.feeder    = feeder;
      }
      else{
         this.simState = Sim.NO;
         this.feeder    = null;
      }
      */
   }

   //
   //
   //
   private void checkErrors(MissionSystemEvent event){
      try{
         boolean isError            = false;
         String  errorString        = new String("");
         LaunchingMechanismData lmd = null;
         //Do this for EVERY component sendoing out
         //MissionSystemEvent:  LaunchingMechanism,Rocket,payload
         lmd = (LaunchingMechanismData)event.eventState();
         List<MechanismSupportData> list = lmd.supportData();
         isError |= lmd.isError();
         if(isError){
            errorString += "Launching Mechanism Error"; 
         }
         Iterator<MechanismSupportData> it = list.iterator();
         while(it.hasNext()){
            boolean error = false;
            try{
               MechanismSupportData data = it.next();
               error |= data.isError();
               if(error){
                  errorString += "\nMechanism Support Error: ";
                  errorString += data.id();
               }
               isError |= error;
            }
            catch(NullPointerException npe){}
         }
         if(isError){
            this.errorOccurred(new ErrorEvent(event,lmd,errorString));
         }
      }
      catch(ClassCastException cce){}
   }

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
         if(this.simState == Sim.YES){
            //Add the Data feeder to the Launching Mechanism...
            this.launchingMechanism.addDataFeeder(this.feeder);
         }
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
   private void initializeRocket(String file) throws IOException{
      try{
         this.rocket = new GenericRocket();
         this.rocket.initialize(file);
         this.rocket.addErrorListener(this);
         this.rocket.addSystemListener(this);
         if(this.simState == Sim.YES){
            //Add the Data Feeder to the Launching Mechanism...
            this.rocket.addDataFeeder(this.feeder);
         }
      }
      catch(IOException ioe){
         throw ioe;
      }
   }
   
   
   /////////////ErrorListener Interface Implementation////////////////
   //
   //
   //
   public void errorOccurred(ErrorEvent e){
      String event = e.getEvent();
      this.error(event,e);
   }

   //////////////LaunchSystem Interface Implementation////////////////
   //Completely ABORT!!!!!!
   //
   //
   public void abort(){
      System.out.println("SYSTEM ABORTING!!!");
      System.exit(0);
   }

   //
   //
   //
   public void abortCountdown(){}

   //
   //
   //
   public void holdCountdown(){}

   //
   //
   //
   public void ignite(){}

   //
   //
   //
   public void initialize(String file){
      //Do some test prints, first...
      System.out.println(file);
      System.out.println(this.simState);
      //This will need changing...
      //Transition to INITIALIZE, regardless...alert the Subscribers
      try{
         LaunchStateSubstate s = null;
         s = new LaunchStateSubstate(INIT,null,null,null);
         this.stateSubstate = s;
         //Comment out what is not needed at the moment while I figure
         //this shit out...
         //this.initializeRocket(file);
         this.initializeLaunchingMechanism(file);
         //this.initializePayload(file);
         //This will need to change--communicate the state to all
         //the Components of the System...
         //if(this.simState == Sim.YES){
         //   this.feeder.setStateSubstate(s);
         //}
         //Need to alert the Subscribers of the Init State--if for
         //no other reason than to disable the simuulation check box
      }
      catch(IOException ioe){
         //TODO need to handle exception!
      }
   }

   //
   //
   //
   public void preLaunchTime(int hours, int mins, int secs){}

   //
   //
   //
   public void resumeCountdown(){}

   //
   //
   //
   public void simulation(boolean isSim){
      this.simState = isSim ? Sim.YES : Sim.NO;
   }

   //
   //
   //
   public void startCountdown(){}

   ////////////////Publisher Interface Implementation/////////////////
   //
   //
   //
   public void add(Subscriber s){
      if(s != null){
         this.subscriber = s;
      }
   }

   //
   //
   //
   public void error(String s, Object o){
      RuntimeException re = new RuntimeException(s);
      this.subscriber.error(re, o);
   }

   //
   //
   //
   public void notify(String s, Object o){
      this.subscriber.update(o,s);
   }

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
   public void update(MissionSystemEvent event){
      this.notify("Mission System Event", event);
      this.checkErrors(event);
   }
}
//////////////////////////////////////////////////////////////////////
