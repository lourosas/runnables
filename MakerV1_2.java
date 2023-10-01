//////////////////////////////////////////////////////////////////////
/*
Copyright 2023 Lou Rosas

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
import java.lang.*;
import rosas.lou.runnables.*;

public class MakerV1_2 implements Runnable/*, Subscriber*/{
   private static MakerV1_2 _instance;

   private enum   State{READY, BREWING};
   private enum   PowerState{OFF, ON};

   private List<Subscriber> _subscribers;

   private ReservoirV1_2  _reservoir;
   private Thread         _t;
   private MakerState     _makerState;
   private PowerState     _powerState;
   private State          _state;
   private Object         _o;

   {
      _subscribers= null;
      _instance   = null;
      _reservoir  = null;
      _t          = null;
      _makerState = null;
      _powerState = null;
      _state      = null;
      _o          = null;
   };

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this._subscribers.add(subscriber);
      }
      catch(NullPointerException npe){
         this._subscribers = new LinkedList<Subscriber>();
         this._subscribers.add(subscriber);
      }
      finally{
         this.notifyOfState();
      }
   }

   //
   //
   //
   //
   static public MakerV1_2 instance(){
      if(_instance == null){
         _instance = new MakerV1_2();
      }
      return _instance;
   }

   ///////////////////////////Private Methods/////////////////////////
   //////////////////////////////Constructor//////////////////////////
   //
   //
   //
   private MakerV1_2(){
      this._reservoir = new ReservoirV1_2();
      this._o         = new Object();
      Carafe.instance().setObject(this._o);
      //Will need to indicate how to set the default power
      //setting at some point...
      this._t = new Thread(this);
      this._t.start();
   }

   //
   //
   //
   //
   private boolean isBrewing(){
      //return(this._state == State.BREWING); Stub this out for now
      return true; //stub this for the time being...
   }

   //
   //
   //
   private boolean isPowerOn(){
      //return(this._power == Power.ON); Comment out for now
      return true; //stub this for now--change back to false
   }

   //
   //
   //
   //
   private void notifyOfState(){
      //This will inlcude Notify of Power and of state, as well as
      //request the states of the components--the Carafe and the
      //reservoir...
   }

   //
   //
   //
   private void setState(){
      
   }

   /////////////////////////Interface Methods/////////////////////////
   /////////////////////////////Runnable//////////////////////////////
   //
   //
   //
   public void run(){
      //int sleepTime          = 100;
      int sleepTime          = 1000;
      int reservoirSleepTime = 1000;
      double amount          = -1.;
      try{
         while(true){
            if(isPowerOn() && isBrewing()){
               try{
                  amount = this._reservoir.empty(reservoirSleepTime);
                  while(true){//Something else to go here...
                     try{
                        //Put this here, for now...
                        Thread.sleep(reservoirSleepTime);
                        CarafeV1_2.instance().fill(amount);
                        //Thread.sleep(reservoirSleepTime);
                     }
                     catch(NotHomeException nhe){
                        synchronized(this._o){
                           this._o.wait();
                        }
                        CarafeV1_2.instance().fill(amount);
                     }
                     catch(OverflowException oe){
                        //Alert of the Exception
                        try{
                           Iterator<Subscriber> it =
                                         this._subscribers.iterator();
                           while(it.hasNext()){
                              //Try this for the moment...
                              (it.next()).error(oe);
                           }
                        }
                        catch(NullPointerException npe){}
                     }
                     finally{
                        int rst = reservoirSleepTime;
                        this.setState();
                        //Notify the suscribers...
                        amount = this._reservoir.empty(rst);
                     }
                  }
               }
               catch(EmptyReservoirException ere){
                  ere.printStackTrace();
                  this.setState();
                  //this.setReady(true);
                  //Notify the Suscbribers...
                  //TBD--grab the current state, et. al.
                  //Transition out of Brewing state
               }
            }
            Thread.sleep(sleepTime);
         }
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
   }
}
//////////////////////////////////////////////////////////////////////
