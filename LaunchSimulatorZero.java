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

   {
      clockSubscriber    = null;
      subscriber         = null;
      countdownTimer     = null;
      state              = State.PRELAUNCH;
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
   public LaunchSimulatorZero(){}

   /**/
   public LaunchSimulatorZero(Subscriber sub){}

   /**/
   public LaunchSimulatorZero(Subscriber sub, ClockSubscriber csub){
      this.addSubscriber(sub);
      this.addClockSubscriber(csub);
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
      this.preLaunchSubstate = PreLaunch.CONTINUE;
   }

   //
   //
   //
   private void prelaunch(PreLaunch substate){
      this.state             = State.PRELAUNCH;
      this.preLaunchSubstate = substate;
   }

   //
   //
   //
   private void preaunchSubstate(PreLaunch substate){
      this.preLaunchSubstate = substate;
   }

   //
   //
   //
   private void setPrelaunchTime(int hours,int mins,int secs){
      System.out.printf("%03d:%02d:%02d\n", hours,mins,secs);
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
      this.setPrelaunchTime(hours,mins,secs);
   }

   //
   //
   //
   public void startCountdown(){
      this.start = true;
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
               Thread.sleep(1000);
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
