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

public class LaunchSystemOne implements ErrorListener, LaunchSystem,
Publisher, SystemListener{
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
   private ClockSubscriber     clockSubscriber;
   private Subscriber          subscriber;
   private CountdownTimer      countdownTimer;
   private Rocket              rocket;
   private LaunchingMechanism  launchingMechanism;
   //private Payload             payload;
   private Thread              rt0;        //Probably not needed
   private boolean             start;      //related to threading
   private boolean             kill;       //related to threading
   private String              feederFile; //Init the Feeders

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

      clockSubscriber              = null;
      countdownTimer               = null;
      feederFile                   = null;
      kill                         = false;
      launchingMechanism           = false;
      rocket                       = null;
      rt0                          = null;
      start                        = false;
      stateSubstate                = null;
      subscriber                   = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public LaunchSystemOne(Subscriber sub){
      this(sub,null,null);
   }

   //
   //
   //
   public LaunchSystenOne(Subscriber sub, ClockSubscriber csub){
      this(sub,csub,null);
   }

   //
   //
   //
   public LaunchSystemOne
   (
      Subscriber sub, 
      ClockSubscriber csub, 
      String file
   ){
      //this.subscriber(sub);
      //this.clockSubscriber(csub);
      //this.feederFile(file);
   }


   ///////////////ErrorListener Interface Implementation//////////////
   //
   //
   //
   public void errorOccured(ErrorEvent e){}

   ///////////////LaunchSystem Interface Implementation///////////////
   //
   //
   //
   public void abort(){
      System.out.println("System Aborting!!!!");
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
   public void initialize(String file){}

   //
   //
   //
   public void initialize(String file, boolean initFeeder){}

   //
   //
   //
   public void preLaunchTime(int hours, int mins, int secs){}
}
//////////////////////////////////////////////////////////////////////
