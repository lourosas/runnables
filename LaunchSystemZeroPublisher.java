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

public class LaunchSystemZeroPublisher extends LaunchSystemPublisher{
   private RocketData           _rocketData;
   //private LaunchPlatformData   _platformData;

   {
      _rocketData      = null;
      //_platformData    = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public LaunchSystemZeroPublisher(){}

   //
   //
   //
   public LaunchSystemZeroPublisher
   (
      RocketData         rocketData //, 
      //LaunchPlatformData launchPlatformData
   ){
      this._rocketData   = rocketData;
      //this._platformData = launchPlatformData;
   }
   //////////////////////////Private Methods//////////////////////////

   /////////////////Publisher Interface Implementation////////////////
   //
   //
   //
   public void publish(){
      if(this._rocketData != null 
         /*|| this._platformData != nll*/
         || this.exception != null){
         try{
            Object obj = null;
            Iterator<Subscriber> it = this.subscribers.iterator();
            if(this._rocketData != null){
               obj = this._rocketData;
            }
            /*
            else if(this._platformData != null){
               obj = this._platformData;
            }
            */
            else if(this.exception != null){
               obj = this.exception;
            }
            while(it.hasNext() && obj != null){
               it.next().update(obj);
            }
         }
         catch(NullPointerException npe){}
      }
   }

   //
   //
   //
   public void publish(Object data){
      try{
         this._rocketData = (RocketData)data;
      }
      catch(ClassCastException cce){
         this._rocketData = null;
      }
      /*
      try{
         this._platformData = (PlatformData)data;
      }
      catch(ClassCastException cce){
         this._platformData = null;
      }
      */
      try{
         this.exception = (Exception)data;
      }
      catch(ClassCastException cce){
         this.exception = null;
      }
      this.publish();
   }
}
//////////////////////////////////////////////////////////////////////
