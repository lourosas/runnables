//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;
import rosas.lou.runnables.*;

public class Maker implements Runnable{
   private enum State{READY,BREWING};
   private List<Subscriber> _subscribers;
   private Reservoir        _reservoir;
   private Thread           _t;
   private boolean          _power;
   private State            _state;
   //Temporary for the moment
   private Object           _o;

   {
      _subscribers = null;
      _reservoir   = null;
      _t           = null;
      _power       = true;
      _state       = State.READY;
      _o           = null;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public Maker(){
      this._reservoir = new Reservoir();
      //Two lines below may be temporary
      this._o         = new Object();
      Carafe.instance().setObject(this._o);
      this._t = new Thread(this);
      //since power is on, go ahead and start the thread...
      //this is the POWERON (super)state
      this._t.start();
   }
   
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
         Carafe.instance().addSubscriber(subscriber);
         this.notifyOfState();
         this.notifyReservoirStartupStatus();

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
         this.setBrewing();
      }
      else{
         this.notifyError(new AlreadyBrewingException());
      } 
   }
   
   //
   //
   //
   public void fillReservoir(double amount){
      try{
         this._reservoir.fill(amount);
      }
      catch(OverflowException oe){
         this.notifyError(oe.getMessage());
      }
      finally{
         //Notify how full the Reservoir is...
         this.notifyReservoirStatus();
      }
   }

   //
   //
   //
   //
   public void power(boolean toPowerUp){
      //Will want to set up a mutex
      this._power = toPowerUp;
      this._t.interrupt();
   }

   //
   //
   //
   public CarafeInterface pullCarafe(){
      //Do it like this!!
      //public void pullCarafe(){}
      //Perhaps, it is time to implement this in an
      //Publish-Subscribe type Design Pattern...
      Carafe carafe                   = null;
      CarafeInterface carafeInterface = null;
      try{
         Carafe.instance().pull();
         carafeInterface = Carafe.instance();
         this.notifyCarafeStatus();
         
      }
      catch(NotHomeException nhe){
         //Print this for the temporary...
         nhe.printStackTrace();
         //Handle the error...TBD...
         this.notifyError(nhe);
      }
      finally{
         //Test Print
         return carafeInterface;
      }
   }

   //
   //
   //
   public void removeSubscriber(Subscriber subscriber){}

   //
   //
   //
   public void returnCarafe(){
      //Will want to set up mutex
      Carafe.instance().putback();
      this.notifyCarafeStatus();
   }

   ////////////////////////Private Methods////////////////////////////
   //
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
         this.notifyError(abe.getMessage());
      }
      catch(ClassCastException cce){}
      try{
         EmptyReservoirException ece =
                                   (EmptyReservoirException)exception;
         this.notifyError(ece.getMessage());
         /*
         String message = ece.getMessage();
         if(message.toUpperCase().contains("FALSE")){
            this.notifyReservoirStatus();
         }
         */
      }
      catch(ClassCastException cce){}
      try{
         NotHomeException nhe = (NotHomeException)exception;
         this.notifyCarafeStatus();
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
   //
   //
   private void notifyCarafeStatus(){
      String state = null;
      String component = new String("Carafe State");
      if(Carafe.instance().isHome()){
         state = new String("Home");
      }
      else if(Carafe.instance().isPulled()){
         state = new String("Pulled");
      }
      else if(Carafe.instance().isPouring()){
         state = new String("Pouring");
      }
      this.notify(state,component);
      Double quantity = Double.valueOf(Carafe.instance().quantity());
      this.notify(quantity, new String("Carafe Quantity"));
   }

   //
   //Get the current state of the Carafe and notify
   //
   private void notifyCarafeStartupStatus(){
      Double capacity = Double.valueOf(Carafe.instance().capacity());
      this.notify(capacity, new String("Carafe Capacity"));
      this.notifyCarafeStatus();
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
   private void notifyReservoirStatus(){
      if(this._reservoir.isEmpty()){
         String empty = new String("Error Empty");
         String state = new String("Reservoir State");
         this.notify(empty, state);
      }
      Double quantity = Double.valueOf(this._reservoir.quantity());
      this.notify(quantity, new String("Reservoir Quantity"));
   }

   //
   //
   //
   private void notifyReservoirStartupStatus(){
      Double capacity = Double.valueOf(this._reservoir.capacity());
      this.notify(capacity,new String("Reservoir Capacity"));
      this.notifyReservoirStatus();
   }

   //
   //
   //
   private void setBrewing(){
      this._state = State.BREWING;
      //Notify Observers of State Change
      this.notifyOfState();
   }

   //
   //
   //
   private void setReady(){
      this._state = State.READY;
      //Notfiy Observers of State Change
      this.notifyOfState();
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
      while(this._power){//get rid of and make an accessor...
         try{
            //This is definitely a redundant check...
            if(isBrewing()){
               try{
                  amount = this._reservoir.empty(reservoirSleepTime);
                  while(true){
                     Thread.sleep(reservoirSleepTime);
                     try{
                        Carafe.instance().fill(amount);
                     }
                     catch(NotHomeException nhe){
                        synchronized(this._o){
                           //Wait until the Carafe is returned
                           this._o.wait();
                        }
                        Carafe.instance().fill(amount);
                     }
                     finally{
                        //System.out.println(
                        //             Carafe.instance().quantity());
                        this.notifyReservoirStatus();
                        amount = 
                            this._reservoir.empty(reservoirSleepTime);
                     }
                  }
               }
               catch(EmptyReservoirException ece){
                  //May want to set ready at some other point
                  this.notifyReservoirStatus();
               }
               finally{
                  //Once Done, set back to the READY state
                  //BREWING-->READY
                  this.setReady();
                  System.out.println(
                             "Carafe: "+Carafe.instance().quantity());
               }
            }
            Thread.sleep(sleepTime);
         }
         catch(InterruptedException ie){
            Carafe.instance().takeOutOfUse();
            System.out.println("Thread Interrupted, "+this._power);
         }
      }
   }
}

//////////////////////////////////////////////////////////////////////
