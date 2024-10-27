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
implements Runnable,Publisher,LaunchSimulator{
   private LaunchSimulatorStateSubstate.State PREL             = null;
   private LaunchSimulatorStateSubstate.State INIT             = null;
   private LaunchSimulatorStateSubstate.State LAUN             = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate SET  = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate CONT = null;
   private LaunchSimulatorStateSubstate.PreLaunchSubstate HOLD = null;
   private LaunchSimulatorStateSubstate.IgnitionSubstate  IGN  = null;
   private LaunchSimulatorStateSubstate.IgnitionSubstate  BUP  = null;
   private LaunchSimulatorStateSubstate.IgnitionSubstate  REL  = null;
   private LaunchSimulatorStateSubstate.LaunchSubstate    ASC  = null;
   private LaunchSimulatorStateSubstate.LaunchSubstate    STAG = null;
   private LaunchSimulatorStateSubstate.LaunchSubstate    IGNE = null;

   private LaunchSimulatorStateSubstate stateSubstate;

   private ClockSubscriber    clockSubscriber;
   private Subscriber         subscriber;
   private CountdownTimer     countdownTimer;
   private Rocket             rocket;
   private LaunchingMechanism launchingMechanism;
   private Thread             rt0;
   private boolean            start;
   private boolean            kill;

   {
      PREL = LaunchSimulatorStateSubstate.State.PRELAUNCH;
      INIT = LaunchSimulatorStateSubstate.State.INITIATELAUNCH;
      LAUN = LaunchSimulatorStateSubstate.State.LAUNCH;
      SET  = LaunchSimulatorStateSubstate.PreLaunchSubstate.SET;
      CONT = LaunchSimulatorStateSubstate.PreLaunchSubstate.CONTINUE;
      HOLD = LaunchSimulatorStateSubstate.PreLaunchSubstate.HOLD;
      IGN  = LaunchSimulatorStateSubstate.IgnitionSubstate.IGNITION;
      BUP  = LaunchSimulatorStateSubstate.IgnitionSubstate.BUILDUP;
      REL  = LaunchSimulatorStateSubstate.IgnitionSubstate.RELEASED;
      ASC  = LaunchSimulatorStateSubstate.LaunchSubstate.ASCENT;
      STAG = LaunchSimulatorStateSubstate.LaunchSubstate.STAGING;
      IGNE =LaunchSimulatorStateSubstate.LaunchSubstate.IGNITEENGINES;

      stateSubstate      = null;
      clockSubscriber    = null;
      subscriber         = null;
      countdownTimer     = null;
      rocket             = null;
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
   private void setPrelaunchTime(int hours,int mins,int secs){
      //Set the SET State once AND ONLY once!!!...
      if(this.stateSubstate == null){
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
   //
   private LaunchSimulatorStateSubstate.PreLaunchSubstate
   prelaunchSubstate(){
      return this.stateSubstate.prelaunchSubstate();
   }
   //
   //
   //
   private LaunchSimulatorStateSubstate.State state(){
      return this.stateSubstate.state();
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
         this.rocket             = new GenericRocket();
         this.launchingMechanism = new GenericLaunchingMechanism();
         
         this.rocket.initialize(file);
         this.launchingMechanism.initialize(file);
         //Can acutally get the Monitor Prelaunch 
         //this.rocket.monitorPrelaunch();
         LaunchingMechanismData lm = null;
         lm = this.launchingMechanism.monitorPrelaunch();
         //System.out.println(lm);
         this.notify("Initialize",lm);
      }
      catch(IOException ioe){
         //for the time being, do this...something else later...
         this.error(ioe.getMessage(), null);
      }
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
      try{
         while(true){
            if(this.kill){
               //this.rocket.abort();-->TBD!!!
               throw new InterruptedException();
            }
            Thread.sleep(100);
            if(this.start){
               //Thread.sleep(50);
               if(this.state() == PREL){
                  md = this.launchingMechanism.monitorPrelaunch();
                  if(++printCounter == 100){
                     //So far, just test prints
                     System.out.println("\n"+this.stateSubstate);
                     printCounter = 0;
                     //Test this at the moment...
                     this.notify(null,md);
                  }
               }
            }
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
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //
   //
   //
   public void remove(Subscriber s){}
}
//////////////////////////////////////////////////////////////////////
