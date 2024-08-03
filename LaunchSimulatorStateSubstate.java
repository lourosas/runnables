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

public class LaunchSimulatorStateSubstate{
   public enum State{PRELAUNCH,INITIATELAUNCH,LAUNCH};
   public enum PreLaunch{CONTINUE,HOLD};
   public enum Ignition{IGNITION,BUILDUP,RELEASED};
   public enum Launch{ASCENT,STAGING,IGNITEENGINES};

   private State     _state             = null;
   private PreLaunch _preLaunchSubstate = null;
   private Ingition  _ignitionSubstate  = null;
   private Launch    _launchSubstate    = null;

   ///////////////////////////Construtors/////////////////////////////
   /**/
   public LaunchSimulatorStateSubstate(){}

   /**/
   public LaunchSimulatorStateSubstate
   (
      State     state,
      PreLaunch prelaunch,
      Ignition  ignition,
      Launch    launch
   ){
      this.setState(state);
      this.setPrelaunchSubstate(prelaunch);
      this.setIgnitionSubstate(ignition);
      this.setLaunchSubstate(launch);
   }
   ////////////////////////Public Methods/////////////////////////////
   ///////////////////////Private Methods/////////////////////////////
   /**/
   private void setIgnitionSubstate(Ingnition ignition){
      this._ignitionSubstate = ignition;
   }

   /**/
   private void setLaunchSubstate(Launch launch){
      this._launchSubstate = launch;
   }

   /**/
   private void setPrelaunchSubstate(PreLaunch prelaunch){
      this._preLaunchSubstate = prelaunch;
   }
   
   /**/
   private void setState(State state){
      this._state = state;
   }
}

//////////////////////////////////////////////////////////////////////
