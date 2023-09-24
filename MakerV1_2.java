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

public class MakerV1_2 extends Maker,
implements Runnable/*, Subscriber*/{
   private static MakerV1_2 _instance;

   private enum   State{READY, BREWING};
   private enum   PowerState{OFF, ON};

   private List<Subscriber> _subscribers;

   private ReservoirV1_2  _reservoir;
   private Thread         _t;
   private PowerState     _powerState;
   private State          _state;
   private Object         _o;

   {
      _subscribers= null;
      _instance   = null;
      _reservoir  = null;
      _t          = null;
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
         this._subscribers = new LikedList<Subscriber>();
         this._subscriber.add(subscriber);
      }
      finally{
         this.notifyOfState();
      }
   }

   ///////////////////////////Private Methods/////////////////////////
   //
   //
   //
   //
   private void notifyOfState(){
      //This will inlcude Notify of Power and of state, as well as
      //request the states of the components--the Carafe and the
      //reservoir...
   }

   /////////////////////////Interface Methods/////////////////////////
   /////////////////////////////Runnable//////////////////////////////
   //
   //
   //
   public void run(){
      int sleepTime          = 100;
      int reservoirSleepTime = 1000;
      double amount          = -1.;
      try{
         while(true){
            if(isPowerOn() && isBrewing()){
               try{
                  amount = this._reservoir.empty(reservoirSleepTime);
                  while(true){//Something else to go here...
                     try{}
                     catch(NotHomeException nhe){}
                     catch(OverflowException oe){}
                     finally{}
                  }
               }
               catch(EmptyReservoirException ere){
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
