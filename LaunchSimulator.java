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
public class LaunchSimulator implements Runnable,Publisher{
   private enum State{PRELAUNCH, INTIATELAUNCH, LAUNCH};
   private enum PreLaunch{CONTINUE, HOLD};
   private enum Ignition{IGNITION,BUILDUP,RELEASED};
   private enum Launch{ASCENT,STAGING,IGNITEENGINES};

   private CountdownTimer    countdownTimer;
   private State             state;
   private PreLaunch         preLaunchSubstate;
   private InitiateLaunch    intitiateLaunchSubstate;
   private Launch            launchSubstate;
   private Rocket            rocket;
   private LaunchingMechnism launchingMechanism;
   private Thread            rt0;
   private boolean           start;
   private boolean           kill;

   {
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
   public LaunchSimulator(){}

   ///////////////////////Public Methods//////////////////////////////
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
   public void preLaunchTime(int hours, int mis, int secs){}

   //
   //
   //
   public void resumeCountdown(){}

   //
   //
   //
   public void setLaunchingMechanism(LaunchingMechanism lm){}
   //
   //
   //
   public void setRocket(Rocket currentRocket){
      this.rocket = currentRocket;
      this.rt0 = new Thread(this.rocket);
      this.rt0.start();
   }


   //
   //
   //
   public void startCountdown(){
      this.start = true;
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
   private void setPrelaunchTime(int hours, int mins, int secs){}

   //
   //
   //
   private State state(){}

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
