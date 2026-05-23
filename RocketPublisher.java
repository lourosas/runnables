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

public class RocketPublisher implements Publisher{
   private List<Subscriber>  _subscribers;
   private RocketData        _rocketData;

   {
      _subscribers  = null;
      _rocketData   = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public RocketObservable(){}

   //
   //
   //
   public RocketObservable(RocketData data){
      this._rocketData = data;
   }

   //////////////////////////Private Methods//////////////////////////

   ////////////////Observable Interface Implementation////////////////
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
   }

   //
   //
   //
   public void publish(){
      try{
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            it.next().update(this._rocketData);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   public void publish(Object data){
      try{
         this._rocketData = (RocketData)data;
         this.publish();
      }
      catch(ClassCastException cce){
         this._rocketData = null;
      }
   }

   //
   //
   //
   public void removeSubscriber(Subscriber subscriber){
      try{
         this._subscribers.remove(observer);
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      catch(UnsupportedOperationException uoe){
         uoe.printStackTrace();
      }
   }

   //
   //
   //
   public Object request(){
      return this._rocketData;
   }

}
//////////////////////////////////////////////////////////////////////
