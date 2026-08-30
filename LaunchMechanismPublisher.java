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

public class LaunchMechanismPublisher implements Publisher{
   private List<Subscriber>    _subscribers;
   private LaunchMechanismData _launchMechanismData;
   private Exception           _exception;

   {
      _subscribers         = null;
      _launchMechanismData = null;
      _exception           = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public LaunchMechanismPublisher(){}

   //
   //
   //
   public LaunchMechanismPublisher(LaunchMechanismData data){
      this._launchMechanismData = data;
   }

   //
   //
   //
   public LaunchMechanismPublisher(Exception exception){
      this._exception = exception;
   }

   //////////////////////////Private Methods//////////////////////////

   /////////////////Publisher Interface Implementation////////////////
   //
   //
   //
   public void addSubscriber(Subscriber sub){
      try{
         this._subscribers.add(sub);
      }
      catch(NullPointerException npe){
         this._subscribers = new LinkedList<Subscriber>();
         this._subscribers.add(sub);
      }
   }

   //
   //
   //
   public void publish(){
      if(this._launchMechanismData!=null || this._exception!=null){
         try{
            Object obj = null;
            Iterator<Subscriber> it = this._subscribers.iterator();
            if(this._launchMechanismData != null){
               obj = this._launchMechanismData;
            }
            else if(this._exception != null){
               obj = this._exception;
            }
            while(it.hasNext() && obj != null){
               it.next().update(obj);
            }
            this._launchMechanismData = null;
            this._exception           = null;
         }
         catch(NullPointerException npe){}
      }
   }

   //
   //
   //
   public void publish(Object data){
      try{
         this._launchMechanismData = (LaunchMechanismData)data;
      }
      catch(ClassCastException cce){
         this._launchMechanismData = null;
      }
      try{
         this._exception = (Exception)data;
      }
      catch(ClassCastException cce){
         this._exception = null;
      }
      this.publish();
   }

   //
   //
   //
   public void removeSubscriber(Subscriber sub){
      try{
         this._subscribers.remove(sub);
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
      return this._launchMechanismData;
   }
}
//////////////////////////////////////////////////////////////////////
