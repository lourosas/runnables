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
   private enum State{PRELAUNCH, INTIATELAUNCH, LAUNCH};
   private enum PreLaunch{CONTINUE, HOLD};
   private enum Ignition{IGNITION,BUILDUP,RELEASED};
   private enum Launch{ASCENT,STAGING,IGNITEENGINES};

   private ClockSubscriber    clockSubscriber;
   private Subscriber         subscriber;
   private CountdownTimer     countdownTimer;
   private State              state;
   private PreLaunch          preLaunchSubstate;
   private Ignition           ignitionSubstate;
   private Launch             launchSubstate;
   private Rocket             rocket;
   private LaunchingMechanism launchingMechanism;
   private Thread             rt0;
   private boolean            start;
   private boolean            kill;
   private int                prelaunchHours;
   private int                prelaunchMins;
   private int                prelaunchSecs;

   {
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
   private void ignition(Ignition ignition){}

   //
   //
   //
   private void ignitionSubstate(Ignition ignition){}

   //
   //
   //
   private void prelaunch(){
      this.state             = State.PRELAUNCH;
      this.prelaunchSubstate(PreLaunch.CONTINUE);
   }

   //
   //
   //
   private void prelaunch(PreLaunch substate){
      this.state = State.PRELAUNCH;
      this.prelaunchSubstate(substate);
   }

   //
   //
   //
   private void prelaunchSubstate(PreLaunch substate){
      if(substate==PreLaunch.CONTINUE || substate==PreLaunch.HOLD){
         this.preLaunchSubstate = substate;
      }
   }

   //
   //
   //
   private void setPrelaunch(PreLaunch substate){
      this.state = State.PRELAUNCH;
      this.prelaunchSubstate(substate);
      System.out.println(this.state);
      System.out.println(this.preLaunchSubstate);
   }

   //
   //
   //
   private void setPrelaunchTime(int hours,int mins,int secs){
      //this will change now!!!
      System.out.printf("%03d:%02d:%02d\n", hours,mins,secs);
      this.prelaunchHours = hours;
      this.prelaunchMins  = mins;
      this.prelaunchSecs  = secs;
      System.out.println("State:  " + this.state);
      System.out.println("Substate: "+this.preLaunchSubstate);
      //This will need to change and go somewheres else
      this.subscriber.update(null,"Ready:  Prelaunch");
   }

   //
   //
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
   private State state(){
      return this.state;
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
   //
   //
   public void launchingMechanism(LaunchingMechanism lm){}

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
      this.setUpCountdownTimer();
      this.setTheCountdownTime();
      this.start = true;
      this.setPrelaunch(PreLaunch.CONTINUE);
   }

   ////////////////Runnable Interface Implementation//////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            if(this.kill){
               this.rocket.abort();
               throw new InterruptedException();
            }
            Thread.sleep(100);
            if(this.start){
               System.out.println("Simulator Running");
               Thread.sleep(500);
            }
         }
      }
      catch(InterruptedException ie){}
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
