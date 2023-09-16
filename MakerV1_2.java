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
implements Runnable, Subscriber{
   private static MakerV1_2 _instance;

   private enum   State{READY, BREWING};
   private enum   PowerState{OFF, ON};

   private List<Subscriber> _subscribers;

   private Reservoir  _reservoir;
   private Thread     _t;
   private PowerState _powerState;
   private State      _state;
   private Object     _o;

   {
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
         this.notifyOfPower();
         this.notifyOfState();
         this._reservoir.addSubscriber(this);
         Carafe.instance().addSubscriber(this);
      }
   }

   ///////////////////////////Private Methods/////////////////////////
   //
   //
   //
   //
   private void notifyOfPower(){}

   //
   //
   //
   //
   private void notifyOfState(){}

   /////////////////////////Interface Methods/////////////////////////
   /////////////////////////////Runnable//////////////////////////////
   //
   //
   //
   public void run(){}
   ////////////////////////////Subscriber/////////////////////////////
   //
   //
   //
   public void update(Object o){}

   //
   //
   //
   //
   public void update(Object o, String s){}

   //
   //
   //
   //
   public void error(RuntimeException re){}

   //
   //
   //
   //
   public void error(RuntimeException re, Object o){}

   //
   //
   //
   //
   public void error(String error){}   
}
//////////////////////////////////////////////////////////////////////
