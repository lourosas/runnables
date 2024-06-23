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
public class LaunchSimulator implements Runnable{
   private enum State{PRELAUNCH, INTIATELAUNCH, LAUNCH};
   private enum PreLaunch{CONTINUE, HOLD};
   private enum InitiateLaunch{IGNITEENGINES,BUILDUP,ROCKETRELEASED};
   private enum Launch{ASCENT,STAGING,IGNITEENGINES};

   private State          state;
   private PreLaunch      preLaunchSubstate;
   private InitiateLaunch intitiateLaunchSubstate;
   private Launch         launchSubstate;
   private Rocket         rocket;
   private Thread         rt0;
   private boolean        start;
   private boolean        kill;

   {
      state  = State.PRELAUNCH;
      rt0    = null;
      start  = false;
      kill   = false;
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
   public void endSimulator(){
      this.start = false;
   }

   //
   //
   //
   public void killSimulation(){
      this.kill = true;
   }

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
   public void startPrelaunch(){}

   //
   //
   //
   public void preLaunchTime(int hours, int mis, int secs){}

   //
   //
   //
   public void startSimulator(){
      this.start = true;
   }


   //////////////////////Private Methods//////////////////////////////
   //
   //
   //
   private void setPrelaunch(){
      this.state             = State.PRELAUNCH;
      this.preLaunchSubstate = PreLaunch.CONTINUE;
   }

   //
   //
   //
   private void setPrelaunch(PreLaunch substate){
      this.state             = State.PRELAUNCH;
      this.preLaunchSubstate = substate;
   }

   //
   //
   //
   private void setPrelaunchSubstate(PreLaunch substate){
      this.preLaunchSubstate = substate;
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
}
//////////////////////////////////////////////////////////////////////
