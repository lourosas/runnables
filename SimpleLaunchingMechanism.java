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

public class SimpleLaunchingMechanism implements LaunchingMechanism,
Runnable{
   private LaunchSimulatorStateSubstate.State PREL;
   private LaunchSimulatorStateSubstate.State INIT;
   private LaunchSimulatorStateSubstate.State LAUN;

   private LaunchSimulatorStateSubstate _stateSubstate;
   //This should change based on file input
   private int                    _numberOfSupports;
   private List<MechanismSupport> _supports;
   private double                 _rocketWeight;
   private double                 _supportedWeight;
   private Thread                 _t0;
   private boolean                _kill;
   private Object                 _o;
   private boolean                _isDataRequested;

   {
      PREL = LaunchSimulatorStateSubstate.State.PRELAUNCH;
      INIT = LaunchSimulatorStateSubstate.State.INITIATELAUNCH;
      LAUN = LaunchSimulatorStateSubstate.State.LAUNCH;

      _isDataRequested  = false;
      _stateSubstate    = null;
      _kill             = false;
      _numberOfSupports = -1;//again, should change based in input
      _rocketWeight     = -1;
      _supports         = null;
      _supportedWeight  = -1;
      _t0               = null;
      _o                = null;
   };

   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   public SimpleLaunchingMechanism(double rocketWeight){
      this(4, rocketWeight);
   }

   //
   //
   //
   public SimpleLaunchingMechanism(int supports, double rocketWeight){
      this._numberOfSupports = supports;
      this._rocketWeight     = rocketWeight;
      this._o                = new Object(); //To Synchronize
      this.setUpSupports();
      this.setUpThread();
   }

   //////////////////////////Public Methods///////////////////////////
   /////////////////////////Private Methods///////////////////////////
   /**/
   private void setUpThread(){
      this._t0 = new Thread(this, "Launching Mechanism");
      this._t0.start();
   }

   /*
    *Will need to request weight measurements from Mechanism Supports
    Will calculate the current total weight of the System is
    supporting (should be DAMN CLOSE to the actual weight)...
    * */
   private void totalWeight(){
      //For the time being do this...to be calculated later...
      this._supportedWeight = this._rocketWeight;
   }

   /**/
   private void setUpSupports(){
      int supports = this._numberOfSupports;
      this._supports = new LinkedList<MechanismSupport>();
      for(int i = 0; i < supports; ++i){
         double weight = this._rocketWeight/supports;
         this._supports.add(new SimpleMechanismSupport(i,weight));
      }
   }

   /////////////LaunchingMechanism Interface Implementation///////////
   //
   //
   //this will need to change!!!
   public List<LaunchingMechanismData> monitorPrelaunch(){
      List<LaunchingMechanismData> data = null;
      try{
         data = new LinkedList<LaunchingMechanismData>();
         for(int i = 0; i < this._supports.size(); ++i){
            MechanismSupport support = this._supports.get(i);
            data.add(support.monitorPrelaunch());
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         data = null;
      }
      finally{
         return data;
      }
   }

   //
   //
   //
   public List<LaunchingMechanismData> monitorIgnition(){
      return null;
   }

   //
   //
   //
   public List<LaunchingMechanismData> monitorLaunch(){
      return null;
   }

   //
   //
   //
   public void release(){}

   //
   //Basically, the total weight of the rocket...(or, damn near close)
   //At least "as measured"...
   public double supportedWeight(){
      //return -1.;
      //temporary, for now...
      return this._rocketWeight;
   }

   //////////////////Runnable Interface Implementation////////////////
   //
   //
   //
   public void run(){
      //Tells when to get the data--hence can change
      //final int GETDATA      = 5000;
      final int SLEEP        = 5000;
      //final int SLEEP        = 50;

      int getDataCounter     = 0;
      int printCounter       = 0;
      try{
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            //Going to need to Synchronize the return data with an
            //object so as to prevent a race-condition--create a
            //mutex to ensure only one Thread at a time has access to
            //the data
            //Keep this for future incarnations...
            Thread.sleep(SLEEP);//This will change to like 10^-3 secs
         }
      }
      catch(InterruptedException ie){}
   }

}
//////////////////////////////////////////////////////////////////////

