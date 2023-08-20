//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;
import rosas.lou.runnables.*;

public class Maker implements Runnable{
   private static Maker _instance;

   private enum State{READY,BREWING};
   private enum Power{OFF, ON};
   private List<Subscriber> _subscribers;
   private Reservoir        _reservoir;
   private Thread           _t;
   private State            _state;
   private Power            _power;
   //Temporary for the moment
   private Object           _o;

   {
      _instance    = null;
      _subscribers = null;
      _reservoir   = null;
      _t           = null;
      _state       = State.READY;
      _power       = Power.OFF;
      _o           = null;
   };
   
   ///////////////////////////Public Methods//////////////////////////
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
         this._reservoir.addSubscriber(subscriber);
         this.notifyOfState();
      }
   }

   //
   //
   //
   //
   public void brew(){
      final double EMPTY = 0.25;//Minisule amount
      //Check the current state--only brew if not currently brewing...
      //subject to change...
      if(!this.isBrewing()){
         //Set the State to State.BREWING
         this.setBrewing(true);
      }
      else{
         this.notifyError(new AlreadyBrewingException());
      } 
   }
   
   //
   //
   //
   //
   public void fillMug(Mug mug){
      Carafe.instance().pour(mug);
   }

   //
   //
   //
   public void fillReservoir(double amount){
      this._reservoir.fill(amount);
   }

   //
   //
   //
   static public Maker instance(){
      if(_instance == null){
         _instance = new Maker();
      }
      return _instance;
   }

   //
   //
   //
   //
   public void power(boolean powerOn){
      //Need to grab the Carafe State, in addition...
      String carafeState = new String("Carafe:  ");
      String powerState  = new String("Power: ");
      //Always set the System to ready...
      //Power on/off will determine what is next..
      this.setReady(false);
      //Need to get the Carafe and check on the state...
      if(Carafe.instance().isHome()){
         carafeState = carafeState.concat("Home");
      }
      else if(Carafe.instance().isPouring()){
         carafeState = carafeState.concat("Pouring");
      }
      else if(Carafe.instance().isPulled()){
         carafeState = carafeState.concat("Pulled");
      }
      if(powerOn){
         powerState = powerState.concat("On");
      }
      else{
         powerState = powerState.concat("Off");
      }
      this.notify(carafeState, powerState);
   }

   //
   //
   //
   public void removeSubscriber(Subscriber subscriber){}

   ////////////////////////Private Methods////////////////////////////
   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   private Maker(){
      this._reservoir = new Reservoir();
      //Two lines below may be temporary
      this._o         = new Object();
      Carafe.instance().setObject(this._o);
      this._t = new Thread(this);
      //since power is on, go ahead and start the thread...
      //this is the POWERON (super)state
      this._t.start();
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
   private void notify(Object o){
      Iterator<Subscriber> it = this._subscribers.iterator();
      while(it.hasNext()){
         Subscriber s = it.next();
         s.update(o);
      }
   }

   //
   //
   //
   private void notify(Object object, String string){
      Iterator<Subscriber> it = this._subscribers.iterator();
      while(it.hasNext()){
         Subscriber s = it.next();
         s.update(object, string);
      }
   }

   //
   //
   //
   private void notifyError(RuntimeException exception){
      //Re-Look at this--really do not need to do all of
      //this
      try{
         AlreadyBrewingException abe =
                                   (AlreadyBrewingException)exception;
         this.notifyError("Coffee Maker " + abe.getMessage());
      }
      catch(ClassCastException cce){}
   }

   //
   //
   //
   private void notifyError(RuntimeException exception, Object o){}

   //
   //
   //
   private void notifyError(String error){
      Iterator<Subscriber> it = this._subscribers.iterator();
      while(it.hasNext()){
         Subscriber s = it.next();
         s.error(error);
      }
   }

   //
   //Notify the Subscribers of all the State information
   //
   private void notifyOfState(){
      String state = new String("State: ") + this._state;
      this.notify(state);
   }

   //
   //
   //
   private void setBrewing(boolean toNotify){
      this._state = State.BREWING;
      //Notify Observers of State Change
      if(toNotify){
         this.notifyOfState();
      }
   }

   //
   //
   //
   private void setReady(boolean toNotify){
      this._state = State.READY;
      if(toNotify){
         //Notfiy Observers of State Change
         this.notifyOfState();
      }
   }

   //////////////////////Interface Methods////////////////////////////
   /////////////////////Runnable Interface ///////////////////////////
   //
   //
   //
   public void run(){
      final double EMPTY     = 0.25;
      int sleepTime          = 100;
      int reservoirSleepTime = 1000;
      double amount          = -1.;
      while(true){//get rid of and make an accessor...
         try{
            if(isBrewing()){
               amount = this._reservoir.empty(reservoirSleepTime);
               while(amount > Reservoir.EMPTY){
                  try{
                     Carafe.instance().fill(amount);
                     Thread.sleep(reservoirSleepTime);
                  }
                  catch(NotHomeException nhe){
                     synchronized(this._o){
                        //Wait until the Carafe is returned
                        this._o.wait();
                     }
                     Carafe.instance().fill(amount);
                  }
                  finally{
                     amount = 
                            this._reservoir.empty(reservoirSleepTime);
                  }
               }
               this.setReady(true);
            }
            Thread.sleep(sleepTime);
         }
         catch(InterruptedException ie){
            Carafe.instance().takeOutOfUse();
         }
      }
   }
}

//////////////////////////////////////////////////////////////////////
