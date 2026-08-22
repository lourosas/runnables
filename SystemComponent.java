//////////////////////////////////////////////////////////////////////
/*
Copyright 2026 Lou Rosas

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

public abstract class SystemComponent implements StateMutable{
   protected LaunchStateSubstate.State INIT                    = null;
   protected LaunchStateSubstate.State PRELAUNCH               = null;
   protected LaunchStateSubstate.State IGNITION                = null;
   protected LaunchStateSubstate.State LAUNCH                  = null;
   protected LaunchStateSubstate.PreLaunchSubstate SET         = null;
   protected LaunchStateSubstate.PreLaunchSubstate CONT        = null;
   protected LaunchStateSubstate.PreLaunchSubstate FUEL        = null;
   protected LaunchStateSubstate.PreLaunchSubstate HOLD        = null;
   protected LaunchStateSubstate.IgnitionSubstate  IGN         = null;
   protected LaunchStateSubstate.IgnitionSubstate  BUP         = null;
   protected LaunchStateSubstate.AscentSubstate    STG         = null;
   protected LaunchStateSubstate.AscentSubstate    IGNE        = null;

   protected Publisher           publisher     = null;
   protected Initializable       initializable = null;
   protected LaunchStateSubstate stateSubstate = null;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;
      SET       = LaunchStateSubstate.PreLaunchSubstate.SET;
      CONT      = LaunchStateSubstate.PreLaunchSubstate.CONTINUE;
      FUEL      = LaunchStateSubstate.PreLaunchSubstate.FUELING;
      HOLD      = LaunchStateSubstate.PreLaunchSubstate.HOLD;
      IGN       = LaunchStateSubstate.IgnitionSubstate.IGNITION;
      BUP       = LaunchStateSubstate.IgnitionSubstate.BUILDUP;
      STG       = LaunchStateSubstate.AscentSubstate.STAGING;
      IGNE      = LaunchStateSubstate.AscentSubstate.IGNITEENGINES;
   };

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){}

   //
   //
   //
   public void initializeComponent(String file)throws IOException{}

   //
   //
   //
   public Object initializationStatus(){
      return this.initializable.initialized();
   }
   
   //
   //
   //
   public void setInitializable(Initializable init){
      this.initializable = init;
   }

   //
   //
   //
   public void setPublisher(Publisher publisher){
      this.publisher = publisher;
   }

   ///////////////////StateMutable Interface Methods//////////////////
   //
   //
   //
   public LaunchStateSubstate getStateSubstate(){
      return this.stateSubstate;
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate ss){
      this.stateSubstate = ss;
   } 
}
//////////////////////////////////////////////////////////////////////
