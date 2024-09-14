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
   public enum PreLaunchSubstate{SET,CONTINUE,HOLD};
   public enum IgnitionSubstate{IGNITION,BUILDUP,RELEASED};
   public enum LaunchSubstate{ASCENT,STAGING,IGNITEENGINES};

   private State             _state             = null;
   private PreLaunchSubstate _preLaunchSubstate = null;
   private IgnitionSubstate  _ignitionSubstate  = null;
   private LaunchSubstate    _launchSubstate    = null;

   ///////////////////////////Construtors/////////////////////////////
   /**/
   public LaunchSimulatorStateSubstate(){}

   /**/
   public LaunchSimulatorStateSubstate
   (
      State             state,
      PreLaunchSubstate prelaunch,
      IgnitionSubstate  ignition,
      LaunchSubstate    launch
   ){
      this.setState(state);
      this.setPrelaunchSubstate(prelaunch);
      this.setIgnitionSubstate(ignition);
      this.setLaunchSubstate(launch);
   }
   ////////////////////////Public Methods/////////////////////////////
   /**/
   public State state(){
      return this._state;
   }

   /**/
   public PreLaunchSubstate prelaunchSubstate(){
      return this._preLaunchSubstate;
   }

   /**/
   public IgnitionSubstate ignitionSubstate(){
      return this._ignitionSubstate;
   }

   /**/
   public LaunchSubstate launchSubstate(){
      return this._launchSubstate;
   }

   /**/
   public String toString(){
      String s = new String();

      s += "State:\t\t"+this._state+"\n";
      s += "Pre Launch:\t"+this._preLaunchSubstate+"\n";
      s += "Ignition:\t"+this._ignitionSubstate+"\n";
      s += "Launch:\t\t"+this._launchSubstate+"\n";
      return s;
   }
   ///////////////////////Private Methods/////////////////////////////
   /**/
   private void setIgnitionSubstate(IgnitionSubstate ignition){
      this._ignitionSubstate = ignition;
   }

   /**/
   private void setLaunchSubstate(LaunchSubstate launch){
      this._launchSubstate = launch;
   }

   /**/
   private void setPrelaunchSubstate(PreLaunchSubstate prelaunch){
      this._preLaunchSubstate = prelaunch;
   }
   
   /**/
   private void setState(State state){
      this._state = state;
   }
}

//////////////////////////////////////////////////////////////////////
