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
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import rosas.lou.runnables.Mug;

//
//Implements the Singleton
//

public class CarafeV1_2 implements Runnable{
   private static final double EMPTY = 0.05;
   private static Lock  lock         = new ReentrantLock();

   private static CarafeV1_2 _instance;

   private enum State{HOME,PULLED,POURING};

   private final double CAPACITY = 32.;//Initial Setting

   private ContainerState _carafeState;
   private double         _quantity;
   private double         _emptyRate;
   private State          _state;
   private Mug            _mug;
   private Thread         _t;
   private Object         _o;

   {
      _quantity    = 0.;
      _emptyRate   = 0.25; //Volume Units/sec
      _state       = State.HOME; //Initialize it
      _mug         = null;  //Carafe fills a Mug
      _t           = null;
      _o           = null;
      _instance    = null;
      _carafeState = null;
   };

   ////////////////////////Public Methods/////////////////////////////
   //
   //
   //
   public double capacity(){
      return this.CAPACITY;
   }

   //
   //
   //
   public void fill(double amount)throws NotHomeException,
   OverflowException{
      if(this.isHome()){
         int mask = ContainerStateMask.ALL;
         double quant = amount + this.quantity();
         this.quantity(quant);
         this.state(mask);
         if(quant > this.capacity()){
            this.quantity(this.capacity());
            //May not need this...
            this.state(mask);
            throw new OverflowException("Overflow Exception: Carafe");
         }
      }
      else{
         throw new NotHomeException("Not Home Exception: Carafe");
      }
   }

   //
   //
   //
   static public CarafeV1_2 instance(){
      if(_instance == null){
         _instance = new CarafeV1_2();
      }
      return _instance;
   }

   //
   //
   //
   public boolean isHome(){
      return(this._state == State.HOME);
   }

   //
   //
   //
   public boolean isPouring(){
      return(this._state == State.POURING);
   }

   //
   //
   //
   public boolean isPulled(){
      return(this._state == State.PULLED);
   }

   //
   //
   //
   public void pull()throws NotHomeException{
      int mask = ContainerStateMask.ALL;
      if(!this.isHome()){
         throw new NotHomeException("Not Home Exception: Carafe");
      }
      this.setPulled();
      this.state(mask);
   }

   //
   //
   //
   public void putback(){
      //Only return when instance is in the PULLED State, not the
      //HOME nor POURING States
      if(this.isPulled()){
         //Just keep it like this...
         int mask = ContainerStateMask.ALL;
         this.setHome();
         this.state(mask);
         synchronized(this._o){
            this._o.notify();
         }
      }
   }

   //
   //
   //
   public double quantity(){
      return this._quantity;
   }

   //
   //
   //
   public void setObject(Object o){
      this._o = o;
   }

   //
   //
   //
   public ContainerState state(){
      return this._carafeState;
   }

   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private CarafeV1_2(){
      //Set up the State
      this.state(ContainerStateMask.ALL);
      this._t = new Thread(this);
      this._t.start();
      this.state(ContainerStateMask.ALL);
   }

   //
   //
   //
   private void quantity(double amount){
      this._quantity = amount;
   }

   //
   //
   //
   private void setHome(){
      this._state = State.HOME;
   }

   //
   //
   //
   private void setPouring(){
      this._state = State.POURING;
   }

   //
   //
   //
   private void setPulled(){
      this._state = State.PULLED;
   }

   //
   //
   //
   private void state(int mask){
      //Tell the Model what changed...
      String state     = null;
      double capacity  = Double.NaN;
      double quantity  = Double.NaN;
      if(mask == ContainerStateMask.NONE){}
      if((mask & ContainerStateMask.STATE)    != 0){
         state = new String("" + this._state);
         state = state.toUpperCase();
      }
      if((mask & ContainerStateMask.CAPACITY) != 0){
         capacity = this.capacity();
      }
      if((mask & ContainerStateMask.QUANTITY) != 0){
         quantity = this.quantity();
      }
      this._carafeState = new CarafeState(state,capacity,quantity);
   }

   /////////////////////Interface Implementations/////////////////////
   //
   //
   //
   public void run(){
      int sleepTime      =    50;
      int pourSleepTime  =   500;
      boolean toContinue = false;
      try{
         while(true){
            Thread.sleep(sleepTime);
         }
      }
      catch(InterruptedException ie){}
   }
}
