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

import java.util.*;
import java.io.*;
import rosas.lou.runnables.*;

public class GenericPayload implements Payload, Runnable{
   private static boolean TOPRINT = true;

   private LaunchStateSubstate.State INIT                      = null;
   private LaunchStateSubstate.State PRELAUNCH                 = null;
   private LaunchStateSubstate.State IGNITION                  = null;
   private LaunchStateSubstate.State LAUNCH                    = null;
   private LaunchStateSubstate.PreLaunchSubstate SET           = null;
   private LaunchStateSubstate.PreLaunchSubstate CONT          = null;
   private LaunchStateSubstate.PreLaunchSubstate FUEL          = null;
   private LaunchStateSubstate.PreLaunchSubstate HOLD          = null;
   private LaunchStateSubstate.IgnitionSubstate  IGN           = null;
   private LaunchStateSubstate.IgnitionSubstate  BUP           = null;
   private LaunchStateSubstate.AscentSubstate    STG           = null;
   private LaunchStateSubstate.AscentSubstate    IGNE          = null;

   private boolean  _kill;

   private DataFeeder            _feeder;
   private List<ErrorListener>   _errorListeners;
   private List<SysetemListener> _systemListeners;
   private LaunchStateSubstate   _state;
   private Object                _obj;
   private Thread                _rt0;
   private PayloadData           _payloadData;
   private PayloadData           _measuredPayloadData;

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

      _kill                = false;

      _feeder              = null;
      _errorListeners      = null;
      _systemListeners     = null;
      _state               = null;
      _obj                 = null;
      _rt0                 = null;
      _payloadData         = null;
      _measuredPayloadData = null; 
   };
   
   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericPayload(){
      this._obj = new Object();
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setUpThread(){
      String name = new String("Generic Payload");
      this._rt0 = new Thread(this,name);
      this._rt0.start();
   }

   /////////////////////////Payload Interface/////////////////////////
   //
   //
   //
   public PayloadData monitor(){
      synchronized(this._obj){
         return this._measuredPayloadData;
      }
   }

   //
   //
   //
   public void initialize(String file)throws IOException{}

   //
   //
   //
   public void addDataFeeder(DataFeeder feeder){}

   //
   //
   //
   public void addErrorListener(ErrorListener listener){}

   //
   //
   //
   public void addSystemListener(SystemListener listener){}

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate state){}

   /////////////////////////Runnable Interface////////////////////////
   //
   //
   //
   public void run(){}

}
//////////////////////////////////////////////////////////////////////
