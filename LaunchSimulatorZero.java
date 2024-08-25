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
import rosas.lou.runnables.*;
import rosas.lou.clock.*;
/*
@Model
*/
public class LaunchSimulatorZero
implements Runnable,Publisher,LaunchSimulator{
   private LaunchSimulatorStateSubstate stateSubstate;

   private ClockSubscriber    clockSubscriber;
   private Subscriber         subscriber;
   private CountdownTimer     countdownTimer;
   private Rocket             rocket;
   private LaunchingMechanism launchingMechanism;
   private Thread             rt0;
   private boolean            start;
   private boolean            kill;
   private int                prelaunchHours;
   private int                prelaunchMins;
   private int                prelaunchSecs;

   {
      stateSubstate      = null;
      clockSubscriber    = null;
      subscriber         = null;
      countdownTimer     = null;
      rocket             = null;
      launchingMechanism = null;
      rt0                = null;
      start              = false;
      kill               = false;
      prelaunchHours     = -1;
      prelaunchMins      = -1;
      prelaunchSecs      = -1;
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
      this.addSubscriber(sub);
      this.addClockSubscriber(csub);
      //Go ahead and start the Thread
      this.setUpThread();
   }

   ///////////////////////Public Methods//////////////////////////////
   /**/
   public void addClockSubscriber(ClockSubscriber cs){
      this.clockSubscriber = cs;
   }

   /**/
   public void addSubscriber(Subscriber s){
      this.subscriber = s;
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
      LaunchSimulatorStateSubstate.PreLaunchSubstate plSubstate =
              LaunchSimulatorStateSubstate.PreLaunchSubstate.SET;
      this.prelaunch(plSubstate);
   }

   //
   //
   //
   private void prelaunch
   (
      LaunchSimulatorStateSubstate.PreLaunchSubstate substate
   ){
      boolean update = false;
      LaunchSimulatorStateSubstate.State state =
                         LaunchSimulatorStateSubstate.State.PRELAUNCH;
      //SET should ONLY Be SET ONCE!! When stateSubstate is null
      //To Avoid updates, CANNOT set twice!!!
      if(substate ==
                  LaunchSimulatorStateSubstate.PreLaunchSubstate.SET){
         if(this.stateSubstate == null){
            this.stateSubstate = new LaunchSimulatorStateSubstate(
                                                             state,
                                                             substate,
                                                             null,
                                                             null);
            update = true;
         }
      }
      else{
         this.stateSubstate = new LaunchSimulatorStateSubstate(state,
                                                  substate,null,null);
         update = true;
      }
      if(update){
         String message = new String("State:  "+state);
         message +=
               " Prelaunch:  "+this.stateSubstate.prelaunchSubstate();
         this.subscriber.update(this.stateSubstate,message);
      }
   }

   //
   //
   //
   private void setPrelaunchTime(int hours,int mins,int secs){
      //this will change now!!!
      //Basically, combine setUpCountdownTimer() and
      //setTheCountdownTime() MINUS the starting of the Clock
      //startCountdown() WILL HANDLE THAT!!!
      System.out.printf("%03d:%02d:%02d\n", hours,mins,secs);
      LClock clock        = new LClock();
      this.countdownTimer = new CountdownTimer(clock);
      //Get the Clock Thread going but DO NOT START the Clock!...
      Thread t = new Thread(clock, "clock");
      t.start();
      this.countdownTimer.addSubscriber(this.clockSubscriber);
      this.countdownTimer.inputTime(hours,mins,secs);
      this.prelaunch();
      //Going to change...
      //this.countdownTimer.start();
      //this.countdownTimer.stop();
      this.countdownTimer.broadcastTime();
   }


   //
   //THIS WILL NEED TO CHAANGE...SEPARATE OUT THE START!!!!
   //
   private void setTheCountdownTime(){
      int hours = this.prelaunchHours;
      int mins  = this.prelaunchMins;
      int secs  = this.prelaunchSecs;
      this.countdownTimer.inputTime(hours,mins,secs);
      this.countdownTimer.start();
   }
   
   //
   //
   //
   private void setUpCountdownTimer(){
      LClock clock        = new LClock();
      this.countdownTimer = new CountdownTimer(clock);
      //Get the Clock Thread going...
      Thread t = new Thread(clock, "clock");
      t.start();
      this.countdownTimer.addSubscriber(this.clockSubscriber);
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

   ////////////LaunchSimulator Interface Implementation////////////////
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
   //So as to add different LaunchingMechanisms to the 
   //Simulator
   //
   public void launchingMechanism(LaunchingMechanism lm){
      this.launchingMechanism = lm;
   }

   //
   //
   //
   public void preLaunchTime(int hours,int mins,int secs){
      if(hours < 1){
         String error = new String("Time Entry!  Please Enter a\n");
         error += "Countdown Time greater than\n";
         error += "59 minutes 59 seconds!";
         this.subscriber.error(error);
      }
      else{
         this.setPrelaunchTime(hours,mins,secs);
      }
   }

   //
   //
   //
   public void resumeCountdown(){}

   //
   //
   //
   public void startCountdown(){
      //Way too fucking complex!! Need to change!!!
      this.setUpCountdownTimer();
      this.setTheCountdownTime();
      this.start = true;
      this.prelaunch(
             LaunchSimulatorStateSubstate.PreLaunchSubstate.CONTINUE);
   }

   ////////////////Runnable Interface Implementation//////////////////
   //
   //
   //
   public void run(){
      int printCounter                = 0;
      List<LaunchingMechanismData> md = null;
      try{
         while(true){
            if(this.kill){
               this.rocket.abort();
               throw new InterruptedException();
            }
            Thread.sleep(100);
            if(this.start){
               //Thread.sleep(50);
               if(this.stateSubstate.state() ==
                        LaunchSimulatorStateSubstate.State.PRELAUNCH){
                  md = this.launchingMechanism.monitorPrelaunch();
                  if(++printCounter == 100){
                     //So far, just test prints
                     System.out.println("\n"+this.stateSubstate);
                     System.out.println(md.size());
                     for(int i = 0; i < md.size(); ++i){
                        System.out.println(md.get(i));
                     }
                     printCounter = 0;
                  }
               }
            }
         }
      }
      catch(InterruptedException ie){}
      //Do NOT want this!  And, print this!!
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //////////////////////Publisher Interface//////////////////////////
   //
   //
   //
   public void add(Subscriber s){}

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
   public void remove(Subscriber s){}
}
//////////////////////////////////////////////////////////////////////
