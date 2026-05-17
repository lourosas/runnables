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

public class RocketObservable implements Observable{
   private List<Observer>  _observers;
   private RocketData      _rockedData;

   {
      _observers    = null;
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
   public void addObserver(Observer observer){
      try{
         this._observers.add(observer);
      }
      catch(NullPointerException npe){
         this._observers = new LinkedList<Observer>();
         this._observers.add(observer);
      }
   }

   //
   //
   //
   public void notify(){
      Iterator<Observer> it = this._observers.iterator();
      while(it.hasNext()){
         it.next().update(this._rocketData);
      }
   }

   //
   //
   //
   public void notify(Object data){
      try{
         this._rocketData = (RocketData)data;
      }
      catch(ClassCastException cce){
         this._rocketData = null;
      }
   }

   //
   //
   //
   public void notify(Object data){}

   //
   //
   //
   public void removeObserver(Observer observer){
      try{
         this._observers.remove(observer);
      }
      catch(ClassCastException cce){
         cce.printStackTrace();
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      catch(UnsuppoertedOperationException uoe){
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
