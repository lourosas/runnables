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
      //This is going to want to change, as well!!!
      Carafe carafe = Carafe.instance();
      //Two lines below may be temporary
      this._o         = new Object();
      carafe.setObject(this._o);
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
         //Notify the Subscribers of all the State information
         this.notify(new String("State: " + this._state));

      }
   }
   
   //
   //
   //
   public void brew(double amount)/*throws AlreadyBrewingException*/{
      //Check the current state--only brew if not currently brewing...
      //subject to change...
      if(!this.isBrewing()){
         try{
            this._reservoir.fill(amount);
         }
         catch(OverflowException oe){
            this.notifyError(oe.getMessage());
         }
         finally{
            this.brew();
         }
      }
      else{
         this.notifyError(new AlreadyBrewingException());
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
         
      }
      catch(NotHomeException nhe){
         //Print this for the temporary...
         nhe.printStackTrace();
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
   }

   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private void brew(){
      //1.  Set the State to State.BREWING
      //READY-->BREWING
      this.setBrewing();
   }

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
      Iterator<Subscriber> it = this._subscribers.iterator();
      while(it.hasNext()){
         Subscriber s = it.next();
         s.error(exception);
      }
   }

   //
   //
   //
   private void notifyError(RuntimeException exception, Object o){}

   //
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
   private void setBrewing(){
      this._state = State.BREWING;
   }

   //
   //
   //
   private void setReady(){
      this._state = State.READY;
   }

   //////////////////////Interface Methods////////////////////////////
   /////////////////////Runnable Interface ///////////////////////////
   //
   //
   //
   public void run(){
      int sleepTime          = 100;
      int reservoirSleepTime = 1000;
      while(this._power){
         try{
            //This is definitely a redundant check...
            if(this._state == State.BREWING){  
               double amount=this._reservoir.empty(reservoirSleepTime);
               while(amount > 0){
                  Thread.sleep(reservoirSleepTime);
                  try{
                     try{
                        Carafe.instance().fill(amount);
                     }
                     catch(OverflowException oe){
                        System.out.println(oe.getMessage());
                     }
                     System.out.println(Carafe.instance().quantity());
                     amount=this._reservoir.empty(reservoirSleepTime);
                  }
                  catch(NotHomeException nhe){
                     System.out.println(nhe.getMessage());
                     synchronized(this._o){
                        //Wait until the Carafe gets returned...
                        this._o.wait();
                     }
                  }
               }
               //Once Done, set back to the READY state
               //BREWING-->READY
               this.setReady();
               System.out.println("Carafe: "+Carafe.instance().quantity());
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
