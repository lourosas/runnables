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

public class MakerV1_3 implements Runnable{
   private static MakerV1_3 _instance;

   private enum   State{READY, BREWING};
   private enum   PowerState{OFF, ON};

   private List<Subscriber> _subscribers;

   private ReservoirV1_3  _reservoir;
   private Thread         _t;
   private State          _state;
   private PowerState     _powerState;
   private Object         _o;

   {
      _subscribers= null;
      _instance   = null;
      _reservoir  = null;
      _t          = null;
      _state      = null;
      _powerState = null;
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
         this.notifyState();
         this.notifyReservoirQuantity();
         this.notifyReservoirCapacity();
      }
   }

   //
   //
   //
   //
   public void brew(){
      if(!this.isBrewing()){
         this.brewing(true);
      }
      else{
         AlreadyBrewingException abe = new AlreadyBrewingException();
         this.notifySubscribersOfException(abe);
      }
   }

   //
   //
   //
   //
   static public MakerV1_3 instance(){
      if(_instance == null){
         _instance = new MakerV1_3();
      }
      return _instance;
   }

   //
   //
   //
   public void fillReservoir(double amount){
      try{
         try{
            this._reservoir.fill(amount);
         }
         catch(OverflowException oe){
            this.notifySubscribersOfException(oe);
            //this.notifySubscribers();-->send in the Carafe State,
            //as well...
         }
         finally{
            this.notifySubscribers();
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //
   //
   //
   public void power(boolean turnOn){
      if(turnOn && isPowerOff()){
         this.on(true);
      }
      else if(!turnOn && isPowerOn()){
         this.off(true);
      }
   }

   //////////////////////////////Constructor//////////////////////////
   //
   //
   //
   private MakerV1_3(){
      this._reservoir = new ReservoirV1_3();
      this._o         = new Object();
      CarafeV1_3.instance().setObject(this._o);
      //Will need to indicate how to set the default power
      //setting at some point...
      this.ready(false);
      this.off(false);
      this._t = new Thread(this);
      this._t.start();
   }

   ///////////////////////////Private Methods/////////////////////////
   //
   //
   //
   private void brewing(boolean toNotify){
      this._state = State.BREWING;
      if(toNotify){
         this.notifySubscribers();
      }
   }

   //
   //
   //
   private boolean isBrewing(){
      return(this._state == State.BREWING);
   }

   //
   //
   //
   private boolean isPowerOn(){
      return(this._powerState == PowerState.ON);
   }

   //
   //
   //
   //
   private boolean isPowerOff(){
      return(this._powerState == PowerState.OFF);
   }



   //Might consider using the Maker State, instead...
   //
   //
   private boolean isReady(){
      return(this._state == State.READY);
   }

   //
   //
   //
   private void notify(Object object){
      try{
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            it.next().update(object);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void notify(Object object, String message){
      try{
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            it.next().update(object, message);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void notifyReservoirCapacity(){
      Double cap = Double.valueOf(this._reservoir.capacity());
      this.notify(cap, new String(" Reservoir Capacity"));
   }

   //
   //
   //
   private void notifyReservoirQuantity(){
      Double quant = Double.valueOf(this._reservoir.quantity());
      this.notify(quant,new String(" Reservoir Quantity"));
   }

   //Send in the whole fucking thing...
   //
   //
   private void notifyState(){
      String state      = "Maker State "+this._state;
      String powerState = "Maker Power "+this._powerState;
      this.notify(state, powerState);
   }

   //
   //
   //SOMETHING ELSE WILL NEED TO GO HERE, INSTEAD...
   //
   private void notifySubscribers(){
      try{
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void notifySubscribersOfException(RuntimeException re){
      try{
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            (it.next()).error(re);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void off(boolean toNotify){
      this._powerState = PowerState.OFF;
      //Indicate the Power Changed...
      if(toNotify){
         //As Always, have to alert all three states...
         this.notifyState();
         this.notifyReservoirQuantity();
         this.notifyReservoirCapacity();
         CarafeV1_3.instance().systemPowerOff();
      }
   }

   //
   //
   //
   private void on(boolean toNotify){
      this._powerState = PowerState.ON;
      //Indicate the Power Changed...
      if(toNotify){
         //As always, have to alert all three states
         this.notifyState();
         this.notifyReservoirQuantity();
         this.notifyReservoirCapacity();
         CarafeV1_3.instance().systemPowerOn();
      }
   }

   //
   //
   //
   private void ready(boolean toNotify){
      this._state = State.READY;
      //Indicate tHe State changed
      if(toNotify){
         this.notifySubscribers();
      }
   }

   /////////////////////////Interface Methods/////////////////////////
   /////////////////////////////Runnable//////////////////////////////
   //
   //
   //
   public void run(){
      int sleepTime          = 100;
      int reservoirSleepTime = 1000;
      int carafeSleepTime    = 500;
      double amount          = Double.NaN;
      try{
         while(true){
            if(this.isPowerOne() && this.isBrewing()){
               try{
                  int rst = reservoirSleepTime;
                  amount = this._reservoir.empty(rst);
               }
               catch(EmptyReservoirException ere){
                  System.out.println(ere.getMessage());
                  this.ready(true);
               }
            }
            /*
            if(this.isPowerOn() && this.isBrewing()){
               try{
                  amount = this._reservoir.empty(reservoirSleepTime);
                  Thread.sleep(reservoirSleepTime);
                  CarafeV1_3.instance().fill(amount);
               }
               catch(NotHomeException nhe){}
               finally{
                  //this.notifyState();
                  //this.notiryReservoirQuantity();
                  this.notifySubscribers();
               }
            }
            else{
               Thread.sleep(sleepTime);
            }
            catch(EmptyReservoirException ere){
               System.out.println(ere.getMessage());
               this.ready(true);
            }
            */
         }
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
   }
}
//////////////////////////////////////////////////////////////////////
