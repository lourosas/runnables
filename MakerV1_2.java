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

public class MakerV1_2 implements Runnable/*, Subscriber*/{
   private static MakerV1_2 _instance;

   private enum   State{READY, BREWING};
   private enum   PowerState{OFF, ON};

   private List<Subscriber> _subscribers;

   private ReservoirV1_2  _reservoir;
   private Thread         _t;
   private MakerState     _makerState;
   private PowerState     _powerState;
   private State          _state;
   private Object         _o;

   {
      _subscribers= null;
      _instance   = null;
      _reservoir  = null;
      _t          = null;
      _makerState = null;
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
         this._subscribers = new LinkedList<Subscriber>();
         this._subscribers.add(subscriber);
      }
      finally{
         this.notifySubscribers();
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
   static public MakerV1_2 instance(){
      if(_instance == null){
         _instance = new MakerV1_2();
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
   public MakerState getState(){
      return this._makerState;
   }

   //
   //
   //
   public void pourCoffeeIntoMug(Mug mug){}

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

   //
   //
   //
   public void pullCarafe(){
      try{
         CarafeV1_2.instance().pull();
      }
      catch(NotHomeException nhe){
         this.notifySubscribersOfException(nhe);
      }
      finally{
         this.setState();
         this.notifySubscribers();
      }
   }

   //
   //
   //
   public void returnCarafe(){
      CarafeV1_2.instance().putback();
      this.setState();
      this.notifySubscribers();
   }

   ///////////////////////////Private Methods/////////////////////////
   //////////////////////////////Constructor//////////////////////////
   //
   //
   //
   private MakerV1_2(){
      this._reservoir = new ReservoirV1_2();
      this._o         = new Object();
      CarafeV1_2.instance().setObject(this._o);
      //Will need to indicate how to set the default power
      //setting at some point...
      this.ready(false);
      this.off(false);
      this._t = new Thread(this);
      this._t.start();
   }

   //Might consider using the Maker State, instead...
   //
   //
   private boolean isReady(){
      String state = null;
      try{
         state = this._makerState.state();
      }
      catch(NullPointerException npe){
         this.setState();
         state = this._makerState.state();
      }
      return(state.toUpperCase().equals("READY"));
      /*
      return(this._state == State.READY);
      */
   }


   //
   //
   //
   private boolean isBrewing(){
      String state = null;
      try{
         state = this._makerState.state();
      }
      catch(NullPointerException npe){
         this.setState();
         state = this._makerState.state();
      }
      return(state.toUpperCase().equals("BREWING"));
      //return(this._state == State.BREWING);
   }

   //
   //
   //
   //
   private boolean isPowerOff(){
      String power = null;
      try{
         power = this._makerState.power();
      }
      catch(NullPointerException npe){
         this.setState();
         power = this._makerState.power();
      }
      return(power.toUpperCase().equals("OFF"));
      //return(this._powerState == PowerState.OFF);
   }

   //
   //
   //
   private boolean isPowerOn(){
      String power = null;
      try{
         power = this._makerState.power();
      }
      catch(NullPointerException npe){
         this.setState();
         power = this._makerState.power();
      }
      return(power.toUpperCase().equals("ON"));
      //return(this._powerState == PowerState.ON);
   }

   //
   //
   //
   //
   private void notifySubscribers(){
      //CarafeState
      ContainerState cState = CarafeV1_2.instance().state();
      //ReservoirState
      ContainerState rState = this._reservoir.state();
      TotalState totalState =
                      new TotalState(this._makerState,cState,rState);
      //This will inlcude Notify of Power and of state, as well as
      //request the states of the components--the Carafe and the
      //reservoir...
      try{
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            (it.next()).update(totalState);
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
   private void brewing(boolean toNotify){
      this._state = State.BREWING;
      //Indicate the State Changed
      this.setState();
      if(toNotify){
         this.notifySubscribers();
      }
   }

   //
   //
   //
   private void off(boolean toNotify){
      this._powerState = PowerState.OFF;
      //Indicate the Power Changed...
      this.setState();
      if(toNotify){
         this.notifySubscribers();
      }
   }

   //
   //
   //
   private void on(boolean toNotify){
      this._powerState = PowerState.ON;
      //Indicate the Power Changed...
      this.setState();
      if(toNotify){
         this.notifySubscribers();
      }
   }

   //
   //
   //
   private void ready(boolean toNotify){
      this._state = State.READY;
      //Indicate tHe State changed
      this.setState();
      if(toNotify){
         this.notifySubscribers();
      }
   }

   //
   //
   //
   private void setState(){
      String state      = null;
      String powerState = null;
      powerState = new String("" + this._powerState);
      state = new String("" + this._state);
      //Just set up the MakerState...
      this._makerState = new MakerState(powerState, state);
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
                     try{
                        //Put this here, for now...
                        Thread.sleep(reservoirSleepTime);
                        CarafeV1_2.instance().fill(amount);
                        //Thread.sleep(reservoirSleepTime);
                     }
                     catch(NotHomeException nhe){
                        synchronized(this._o){
                           this._o.wait();
                        }
                        CarafeV1_2.instance().fill(amount);
                     }
                     catch(OverflowException oe){
                        //Alert the Subscribers of the Exception
                        this.notifySubscribersOfException(oe);
                     }
                     finally{
                        int rst = reservoirSleepTime;
                        this.setState();
                        //Notify the suscribers...
                        this.notifySubscribers();
                        amount = this._reservoir.empty(rst);
                     }
                  }
               }
               catch(EmptyReservoirException ere){
                  String state = this._reservoir.state().state();
                  //If the Carafe is in the Empty state...notify the
                  //the Subscribers the Reservoir is Empty and needs
                  //filling...
                  if(state.toUpperCase().equals("EMPTY")){
                     this.notifySubscribersOfException(ere);
                  }
                  //Notify the Suscbribers...
                  //TBD--grab the current state, et. al.
                  //Transition out of Brewing state
                  this.ready(true);//set the state as well
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
