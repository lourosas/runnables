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
      this.prelaunch(
                  LaunchSimulatorStateSubstate.PreLaunchSubstate.SET);
   }

   //
   //
   //
   private void prelaunch
   (
      LaunchSimulatorStateSubstate.PreLaunchSubstate substate
   ){
      boolean update = false;
      LaunchSimulatorStateSubstate.State state = null;
      state = LaunchSimulatorStateSubstate.State.PRELAUNCH;
      LaunchSimulatorStateSubstate.PreLaunchSubstate sub = substate;
 
      //SET should ONLY Be SET ONCE!! When stateSubstate is null
      //To Avoid updates, CANNOT set twice!!!
      if(sub == LaunchSimulatorStateSubstate.PreLaunchSubstate.SET){
         if(this.stateSubstate == null){
            this.stateSubstate = new LaunchSimulatorStateSubstate(
                                                             state,
                                                             sub,
                                                             null,
                                                             null);
            update = true;
         }
      }
      else if(sub ==
             LaunchSimulatorStateSubstate.PreLaunchSubstate.CONTINUE){
         LaunchSimulatorStateSubstate.PreLaunchSubstate css = null;
         //Need to check the current Substate
         css = this.stateSubstate.prelaunchSubstate();
         if(css==LaunchSimulatorStateSubstate.PreLaunchSubstate.SET){
            this.stateSubstate = new LaunchSimulatorStateSubstate(
                                                             state,
                                                             sub,
                                                             null,
                                                             null);
            update = true;
         }
      }
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
         this.error(error, null);
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
      LaunchSimulatorStateSubstate.PreLaunchSubstate sub = null;
      sub = LaunchSimulatorStateSubstate.PreLaunchSubstate.CONTINUE;
      this.start = true;
      this.countdownTimer.start();
      this.prelaunch(sub);
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
                  //md = this.launchingMechanism.monitorPrelaunch();
                  if(++printCounter == 100){
                     //So far, just test prints
                     System.out.println("\n"+this.stateSubstate);
                     /*System.out.println(md.size());
                     for(int i = 0; i < md.size(); ++i){
                        System.out.println(md.get(i));
                     }
                     */
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
   }

   //
   //
   //
   public void remove(Subscriber s){}
}
//////////////////////////////////////////////////////////////////////
