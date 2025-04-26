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
import rosas.lou.runnables.*;

public class LaunchStateSubstate{
   public enum State{INITIALIZE,PRELAUNCH,IGNITION,LAUNCH,ASCENT};
   public enum PreLaunchSubstate{SET,CONTINUE,FUELING,HOLD};
   public enum IgnitionSubstate{INGNITION,BUILDUP};
   public enum AscentSubstate{STAGING,IGNITEENGINES};

   private State                _state             = null;
   private AscentSubstate       _ascentSubstate    = null;
   private IngntionSubstate     _ignitionSubstate  = null;
   private PreLaunchSubstate    _preLaunchSubstate = null;

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public LaunchStateSubstate(){}

   //
   //
   //
   public LaunchStateSubstate
   (
      State              state,
      PreLaunchSubstate  prelaunch,
      IgnitionSubstate   ignition,
      AscentSubstate     ascent
   ){
      this.state(state);
      this.prelaunchSubstate(prelaunch);
      this.ignitionSubstate(ignition);
      this.ascentSubstate(ascent);
   
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public State state(){
      return this._state;
   }

   //
   //
   //
   public AscentSubstate ascentSubstate(){
      return this._ascentSubstate;
   }

   //
   //
   //
   public IngitionSubstate ignitionSubstate(){
      return this._ignitionSubstate;
   }

   //
   //
   //
   public PreLaunchSubstate prelaunchSubstate(){
      return this._preLaunchSubstate;
   }

   //
   //
   //
   public String toString(){
      String s = new String();

      s += "State:\t\t" + this._state + "\n";
      s += "Pre Launch:\t" + this._preLaunchSubstate + "\n";
      s += "Ignition:\t" + this._ignitionSubstate + "\n";
      s += "Ascent:\t\t" + this._ascentSubstate + "\n";

      return s;
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void ascentsSubstate(AscentSubstate ascent){
      this._ascentSubstate = ascent;
   }

   //
   //
   //
   private void ignitionSubstate(IgnitionSubstate ignition){
      this._ignitionSubstate = ignition;
   }

   //
   //
   //
   private void preLaunchSubstate(PreLaunchSubstate preLaunch){
      this._preLaunchSubstate = preLaunch;
   }

   //
   //
   //
   private void setState(State state){
      this._state = state;
   }
}
//////////////////////////////////////////////////////////////////////
