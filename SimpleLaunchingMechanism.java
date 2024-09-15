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
   private List<List<LaunchingMechanismData>> _data;
   private double                 _rocketWeight;
   private Thread                 _t0;
   private boolean                _kill;
   private Object                 _o;
   private boolean                _isDataRequested;

   {
      PREL = LaunchSimulatorStateSubstate.State.PRELAUNCH;
      INIT = LaunchSimulatorStateSubstate.State.INITIATELAUNCH;
      LAUN = LaunchSimulatorStateSubstate.State.LAUNCH;

      _data             = null;
      _isDataRequested  = false;
      _stateSubstate    = null;
      _kill             = false;
      _numberOfSupports = -1;//again, should change based in input
      _supports         = null;
      _rocketWeight     = -1;
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
   private void mechanismSupportData(){
      //Will need to be synchronized
      //Possible race condition...will need to attempt to alleviate
      List<LaunchingMechanismData> data = null;
      data = new LinkedList<LaunchingMechanismData>();
      for(int i = 0; i < this._supports.size(); ++i){
         MechanismSupport support = this._supports.get(i);
         if(this._stateSubstate.state() == PREL){
            data.add(support.monitorPrelaunch());
         }
         else if(this._stateSubstate.state() == INIT){}
         else if(this._stateSubstate.state() == LAUN){}
      }
      try{
         synchronized(this._o){
            this._data.add(data);
         }
      }
      catch(NullPointerException npe){
         this._data=new LinkedList<List<LaunchingMechanismData>>();
         synchronized(this._o){
            this._data.add(data);
         }
      }
   }
   
   /**/
   private void setUpThread(){
      this._t0 = new Thread(this, "Launching Mechanism");
      this._t0.start();
   }

   /*
    *Will need to request weight measurements from Mechanism Supports
    * */
   private double totalWeight(){
      double totalWeight = 0.;
      return totalWeight;
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
   public List<List<LaunchingMechanismData>> monitorPrelaunch(){
      //Going to need to Synchronize the return data with an
      //object so as to prevent a race-condition--create a mutex to
      //ensure only one Thread at a time has access to the data
      //this will now need to change...
      //Data requested and sent...
      try{
         if(this._stateSubstate.state() != PREL){
            //Set up the StateSubstate object...
            this.mechanismSupportData();
            this._stateSubstate=new LaunchSimulatorStateSubstate(
                                                 PREL,null,null,null);
            //I could just request the fucking data here out of synch,
            //but that would be too fucking easy!!!
            /*
            this._isOutOfSynch = true;
            try{
               synchronized(this._o){
                  this._o.wait();
               }
               System.out.println("Out of synch 1--easier way");
            }
            catch(InterruptedException ie){}
            */
            this._isDataRequested = false;
         }
         else{
            this._isDataRequested = true;
         }
      }
      catch(NullPointerException npe){
         this._stateSubstate = 
                new LaunchSimulatorStateSubstate(PREL,null,null,null);
         this.mechanismSupportData();
         //I could just request the fucking data here out of
         //synch, but that would be too fucking easy!!!
         /*
         this._isOutOfSynch = true;
         try{
            synchronized(this._o){
               this._o.wait();
            } 
            System.out.println("Out of synch 2--easier way");
         }
         catch(InterruptedException ie){}
         */
         this._isDataRequested = false;
      }
      finally{
         //this._isDataRequested = true;//May need to remove
         return this._data;
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
      //Monitor in its own separate Thread...
      int printCounter       = 0;
      try{
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this._stateSubstate != null){
               /*
               ++getDataCounter;
               if(this._isOutOfSynch){
                  this.mechanismSupportData();
                  System.out.println("Out of synch--easier way");
                  synchronized(this._o){
                     this._o.notify();
                  }
                  this._isOutOfSynch = false;
               }
               else if((getDataCounter % GETDATA) == 0){
                  if(this._isDataRequested){
                     //Clear out the data
                     //Set up the data structures
                     //Set to _isDataRequest to false
                     synchronized(this._o){
                        this._data.clear();
                     }
                     this._isDataRequested = false;
                  }
                  this.mechanismSupportData();
               }
               */
               //Go get the data
               if(this._isDataRequested){
                  //Clear out the data
                  //Set up the data structures
                  //Set to _isDataRequest to false
                  synchronized(this._o){
                     this._data.clear();
                  }
                  this._isDataRequested = false;
               }
               this.mechanismSupportData();
         //Going to need to Synchronize the return data with an
         //object so as to prevent a race-condition--create a mutex to
         //ensure only one Thread at a time has access to the data
            }
            Thread.sleep(SLEEP);//This will change to like 10^-3 secs
         }
      }
      catch(InterruptedException ie){}
   }

}
//////////////////////////////////////////////////////////////////////

