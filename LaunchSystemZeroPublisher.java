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
      RocketData         rocketData, 
      LaunchPlatformData launchPlatformData
   ){
      this._rocketData     = rocketData;
      this._platformData   = launchPlatformData;
   }

   /////////////////Publisher Interface Implementation////////////////
   //
   //
   //
   public void publish(){
      try{
          Iterator<Subscribers> it = this._subscribers.iterator();
          while(it.hasNext()){
             Subscriber s = it.next();
             s.update(this._rocketData);
             //s.update(this._platformData);
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
      }
      catch(ClassCastException cce){}
      /*
      try{
         this._platformData = (LaunchPlatformData)data;
      }
      catch(ClassCastException cce){}
      */
   }
}
//////////////////////////////////////////////////////////////////////
