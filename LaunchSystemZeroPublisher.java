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
      try{
          Iterator<Subscribers> it = this._subscribers.iterator();
          while(it.hasNext()){
             Subscriber s = it.next();
             if(this._rocketData != null){
                s.update(this._rocketData);
             }
             //if(this._platformData != null){
                //s.update(this._platformData);
             //}
          }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   public void publish(Object data){
      RocketData rd          = null;
      //LaunchPlatformData lpd = null;
      Exception  ex          = null;
      try{
         rd = (RocketData)data;
      }
      catch(ClassCastException cce){
         rd = null;
      }
      /*
      try{
         lpd = (LaunchPlatformData)data;
      }
      catch(ClassCastException cce){
         lpd = null;
      }
      */
      try{
         ex = (Exception)data;
      }
      catch(ClassCastException cce){
         ex = null;
      }
      if(rd != null /*|| lpd != null*/ || ex != null){
         try{
            Iterator<Subscriber> it = this._subscribers.iterator();
            while(it.hasNext()){
               it.next().update(data);
            }
         }
         catch(NullPointerException npe){}
      }
   }
}
//////////////////////////////////////////////////////////////////////
