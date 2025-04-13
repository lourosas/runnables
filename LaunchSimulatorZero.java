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

import java.lang.*;
import java.util.*;
import java.io.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;
/*
@Model
*/
public class LaunchSimulatorZero
implements ErrorListener,Runnable,Publisher,LaunchSimulator{
   private LaunchSimulatorStateSubstate.State INIT             = null;
   private LaunchSimulatorStateSubstate.State PREL             = null;
   private LaunchSimulatorStateSubstate.State IGNI             = null;
   private LaunchSimulatorStateSubstate.State LAUN             = null;
   private LaunchSimulatorStateSubstate.State ASCE             = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate SET  = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate CONT = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate HOLD = null;
   private LaunchSimulatorStateSubstate.IgnitionSubstate  IGN  = null;
   private LaunchSimulatorStateSubstate.IgnitionSubstate  BUP  = null;
   private LaunchSimulatorStateSubstate.AscentSubstate    STG  = null;
   private LaunchSimulatorStateSubstate.AscentSubstate    IGNE = null;

   private LaunchSimulatorStateSubstate stateSubstate;

   private ClockSubscriber    clockSubscriber;
   private Subscriber         subscriber;
   private CountdownTimer     countdownTimer;
   private Rocket             rocket;
   private LaunchingMechanism launchingMechanism;
   private Payload            payload;
   private Thread             rt0;
   private boolean            start;
   private boolean            kill;

   {
      INIT = LaunchSimulatorStateSubstate.State.INITIALIZE;
      PREL = LaunchSimulatorStateSubstate.State.PRELAUNCH;
      IGNI = LaunchSimulatorStateSubstate.State.IGNITION;
      LAUN = LaunchSimulatorStateSubstate.State.LAUNCH;
      ASCE = LaunchSimulatorStateSubstate.State.ASCENT;
      SET  = LaunchSimulatorStateSubstate.PreLaunchSubstate.SET;
      CONT = LaunchSimulatorStateSubstate.PreLaunchSubstate.CONTINUE;
      HOLD = LaunchSimulatorStateSubstate.PreLaunchSubstate.HOLD;
      IGN  = LaunchSimulatorStateSubstate.IgnitionSubstate.IGNITION;
      BUP  = LaunchSimulatorStateSubstate.IgnitionSubstate.BUILDUP;
      STG  = LaunchSimulatorStateSubstate.AscentSubstate.STAGING;
      IGNE =LaunchSimulatorStateSubstate.AscentSubstate.IGNITEENGINES;

      stateSubstate      = null;
      clockSubscriber    = null;
      subscriber         = null;
      countdownTimer     = null;
      rocket             = null;
      payload            = null;
      launchingMechanism = null;
      rt0                = null;
      start              = false;
      kill               = false;
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public LaunchSimulatorZero(){
      //Go ahead and start the Thread
      this.setUpThread();
   }

   /**/
   public LaunchSimulatorZero(Subscriber sub){
      //Go ahead and start the Thread
      this.setUpThread();
   }

   /**/
   public LaunchSimulatorZero(Subscriber sub, ClockSubscriber csub){
      this.add(sub);
      this.addClockSubscriber(csub);
      //Go ahead and start the Thread
      this.setUpThread();
   }

   ///////////////////////Public Methods//////////////////////////////
   /**/
   public void addClockSubscriber(ClockSubscriber cs){
      this.clockSubscriber = cs;
   }

   //////////////////////Private Methods//////////////////////////////
   //
   //
   //
   private void displayPrelaunch(){}

   //
   //
   //
   private void ignition(){}

   //
   //
   //
   private void ignition
   (
      LaunchSimulatorStateSubstate.IgnitionSubstate ignition
   ){}

   //
   //
   //
   private void ignitionSubstate
   (
      LaunchSimulatorStateSubstate.IgnitionSubstate ignition
   ){}

   //
   //
   //
   private void initializeLaunchingMechanism(String file)
   throws IOException{
      if(this.stateSubstate == null){
         try{
            this.launchingMechanism = new GenericLaunchingMechanism();
            this.launchingMechanism.initialize(file);
            this.launchingMechanism.addErrorListener(this);
            LaunchingMechanismData lm = null;
            lm = this.launchingMechanism.monitorInitialization();
            this.notify("Initialize",lm);
         }
         catch(IOException ioe){
            this.error(ioe.getMessage(), null);
            throw ioe;
         }
      }
   }

   //
   //
   //
   private void initializePayload(String file)throws IOException{
      if(this.stateSubstate == null){
         try{
            this.payload = new GenericCapsule();
            this.payload.initialize(file);
            PayloadData pd = null;
            pd = this.payload.monitorPrelaunch();
            this.notify("Initialize",pd);
         }
         catch(IOException ioe){
            this.error(ioe.getMessage(),null);
            throw ioe;
         }
      }
   }

   //
   //
   //
   private void initializeRocket(String file)throws IOException{
      if(this.stateSubstate == null){
         try{
            this.rocket = new GenericRocket();
            this.rocket.initialize(file);
            RocketData rd = null;
            rd = this.rocket.monitorInitialization();
            this.notify("Initialize", rd);
         }
         catch(IOException ioe){
            this.error(ioe.getMessage(), null);
            throw ioe;
         }
      }
   }

   //
   //
   //
   private void prelaunch(){
      //First Attempt to use the Predefined constants!!!
      this.prelaunch(SET);
   }

   //
   //
   //
   private void prelaunch
   (
      LaunchSimulatorStateSubstate.PreLaunchSubstate substate
   ){
      boolean update                           = false;
      LaunchSimulatorStateSubstate.State state = PREL;
      //THIS IS GOING TO NEED TO FUCKING CHANGE!!!!
      //ONLY TRANSITION IF IN INITIALIZE STATE!!!
      this.stateSubstate = new LaunchSimulatorStateSubstate(
                                                          state,
                                                          substate,
                                                          null,
                                                          null);
      update = true;
      if(update){
         String m = new String("State:  "+state);
         m += " Prelaunch:  "+this.stateSubstate.prelaunchSubstate();
         this.notify(m, this.stateSubstate);
      }
   }

   //
   //
   //
   //
   private LaunchSimulatorStateSubstate.PreLaunchSubstate
   prelaunchSubstate(){
      return this.stateSubstate.prelaunchSubstate();
   }

   //
   //
   //
   private void setPrelaunchTime(int hours,int mins,int secs){
      //Set the SET State once AND ONLY once!!!...
      //Will need to FUCKING CHANGE!!!  Post Initialization!!!
      //ONLY Transition if in Initialization State!
      if(this.state() == INIT){
         System.out.printf("%03d:%02d:%02d\n", hours,mins,secs);
         LClock clock        = new LClock();
         this.countdownTimer = new CountdownTimer(clock);
         //Get the Clock Thread going but DO NOT START the Clock!...
         Thread t = new Thread(clock, "clock");
         t.start();
         this.countdownTimer.addSubscriber(this.clockSubscriber);
         this.countdownTimer.inputTime(hours,mins,secs);
         this.prelaunch();
      }
   }

   //
   //
   //
   private void setUpThread(){
      this.rt0 = new Thread(this, "Launch Simulator");
      this.rt0.start();
   }
   //
   //
   //
   private LaunchSimulatorStateSubstate.State state(){
      return this.stateSubstate.state();
   }

   ///////////////ErrorListener Interface Implementation//////////////
   //
   //
   //
   public void errorOccurred(ErrorEvent e){
      this.error(e.getEvent(), e);
   }

   ////////////LaunchSimulator Interface Implementation///////////////
   //
   //
   //
   public void abortCountdown(){
      if(this.state() == PREL && this.prelaunchSubstate() == HOLD){
         this.countdownTimer.reset();
         //Nullify the LaunchSimulationStateSubstate instance
         //Basically, clears it out so as to "Start Over"
         this.stateSubstate = null;
         //this.notify("Set: Prelaunch", this.stateSubstate);
         this.notify("Abort: Prelaunch", this.stateSubstate);
      }
   }

   //
   //
   //
   public void holdCountdown(){
      if(this.state() == PREL && this.prelaunchSubstate() == CONT){
         this.countdownTimer.stop();
         this.prelaunch(HOLD);
         //No need for Component Updates for the time-being...
         //This probably will change...
         this.start = false;
      }
   }

   //
   //
   //
   public void ignite(){}

   //
   //Based on the JSON/INI file, initialize everything...
   //
   public void initialize(String file){
      try{
         this.initializeRocket(file);
         this.initializeLaunchingMechanism(file);
         this.initializePayload(file);
         //now, need to set the State, Substate...
         LaunchSimulatorStateSubstate.State state = INIT;
         this.stateSubstate = new LaunchSimulatorStateSubstate(state,
                                                               null,
                                                               null,
                                                               null);
         String s = new String("State: "+state);
         s += " Prelaunch: " + null;
         this.notify(s, this.stateSubstate);
         this.start = true;
      }
      catch(IOException ioe){}
   }

   //
   //
   //
   public void preLaunchTime(int hours,int mins,int secs){
      if(hours < 1){
         String error = new String("Time Entry!  Please Enter a\n");
         error += "Countdown Time greater than\n";
         error += "59 minutes 59 seconds!";
         this.error(error, null);
      }
      else{
         this.setPrelaunchTime(hours,mins,secs);
      }
   }

   //
   //
   //
   public void resumeCountdown(){
      if(this.state() == PREL  && this.prelaunchSubstate() == HOLD){
         this.countdownTimer.start();
         this.prelaunch(CONT);
         this.start = true;
      }
   }

   //
   //
   //
   public void startCountdown(){
      if(this.state() == PREL && this.prelaunchSubstate() == SET){
         this.start = true;
         this.countdownTimer.start();
         //State = PRELAUNCH, Substate = PreLaunch.CONTINUE
         this.prelaunch(CONT);
      }
   }

   ////////////////Runnable Interface Implementation//////////////////
   //
   //
   //
   public void run(){
      int printCounter          = 0;
      LaunchingMechanismData md = null;
      RocketData rd             = null;
      PayloadData pd            = null;
      try{
         while(true){
            if(this.kill){
               //this.rocket.abort();-->TBD!!!
               throw new InterruptedException();
            }
            if(!this.start){
               Thread.sleep(1);
            }
            else{
               if(this.state() == INIT){
                  md = this.launchingMechanism.monitorInitialization();
                  //rd = this.rocket.monitorInitialization();
                  //pd = this.payload.monitorInitializatoin();
                  Thread.sleep(3000);
               }
               else if(this.state() == PREL){
                  md = this.launchingMechanism.monitorPrelaunch();
                  //rd = this.rocket.monitorPrelaunch();
                  //pd = this.payload.monitorPrelaunch();
                  if(this.prelaunchSubstate() == SET){
                     Thread.sleep(3000);
                  }
                  else if(this.prelaunchSubstate() == CONT){
                     Thread.sleep(1000);
                  }
                  else{
                     //In the Hold State, monitor at a faster rate
                     //Something may be wrong...
                     Thread.sleep(500);
                  }
               }
               else{
                  //Default Monitoring Rate
                  Thread.sleep(10000);
               }
            }
            //To be set up as part of development...
            //this.notify(null,md);
            //this.notify(null,rd);
            //this.notify(null,pd);
         }
      }
      catch(InterruptedException ie){}
      //Do NOT want this!  And, print this!!
      catch(NullPointerException npe){
         //If we get here, we are in trouble and need to kill the
         //application
         npe.printStackTrace();
         System.exit(0);
      }
   }

   //////////////////////Publisher Interface//////////////////////////
   //
   //
   //
   public void add(Subscriber s){
      this.subscriber = s;
   }

   //
   //
   //
   public void error(String s, Object o){
      if(o == null){
         this.subscriber.error(new RuntimeException(s));
      }
      else{
         //TODO:  finish for here
         this.subscriber.error(new RuntimeException(s), o);
      }
   }

   //
   //
   //
   public void notify(String s, Object o){
      try{
         LaunchSimulatorStateSubstate ss = null;
         ss = (LaunchSimulatorStateSubstate)o;
         
         System.out.println(ss);
         System.out.println(s);
         this.subscriber.update(ss, s);
         this.countdownTimer.broadcastTime();
      }
      catch(ClassCastException cce){}
      catch(NullPointerException npe){}
      try{
         LaunchingMechanismData lm = (LaunchingMechanismData)o;
         if(s != null){
            this.subscriber.update(lm, s);
         }
         else{
            this.subscriber.update(lm);
         }
      }
      catch(ClassCastException cce){}
      try{
         RocketData rd = (RocketData)o;
         if(s != null){
            this.subscriber.update(rd,s);
         }
         else{
            this.subscriber.update(rd);
         }
      }
      catch(ClassCastException cce){}
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      try{
         PayloadData pd = (PayloadData)o;
         if(s != null){
            this.subscriber.update(pd,s);
         }
         else{
            this.subscriber.update(pd);
         }
      }
      catch(ClassCastException cce){}
   }

   //
   //
   //
   public void remove(Subscriber s){}
}
//////////////////////////////////////////////////////////////////////
