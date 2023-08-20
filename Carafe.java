//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import rosas.lou.runnables.CarafeInterface;
import rosas.lou.runnables.Mug;

//
//Implements the Singleton Interface
//Implements the CarafeInterface Interface
//

public class Carafe implements Runnable, CarafeInterface{
   private static final double EMPTY = 0.05;
   private static Lock lock          = new ReentrantLock();

   private static Carafe _instance;

   private enum State{HOME,PULLED,POURING};

   private final double CAPACITY = 32.;//Set for 32 at the moment

   private List<Subscriber> _subscribers;
   private double           _quantity;
   private double           _emptyRate;
   private State            _state;
   private Mug              _mug;
   private Thread           _t;
   private Object           _o;

   {
      _subscribers = null;
      _quantity    = 0.;
      _emptyRate   = 0.25; //Oz/sec-->put this low for now
      _state       = State.HOME;
      _mug         = null; //Carafe Fills a mug
      _t           = null;
      _instance    = null;
      _o           = null;
   };


   ////////////////////////////Public Methods/////////////////////////
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
         //notify the initialized values...
         //this.notifyState();
         //this.notifyCapacity();
         //this.notifyQuantity();
         this.notifySubscribersOfState();
      }
   }

   //
   //
   //
   public double capacity(){
      return this.CAPACITY;
   }

   //
   //
   //
   static public Carafe instance(){
      if(_instance == null){
         _instance = new Carafe();
      }
      return _instance;
   }

   //
   //
   //
   //
   public void notifySubscribersOfState(){
      this.notifyState();
      this.notifyCapacity();
      this.notifyQuantity();
   }

   //
   //
   //
   public void fill(double amount) throws NotHomeException{
      if(this.isHome()){
         this._quantity += amount;
         if(this._quantity > this.CAPACITY){
            this._quantity = this.CAPACITY;
            //throw new OverflowException("Carafe Overflowing!!");
            this.notifyError(new String("Carafe Overflowing!!"));
         }
         this.notifyQuantity();
      }
      else{
         throw new NotHomeException();
      }
   }

   //
   //
   //
   public boolean isHome(){
      return (this._state == State.HOME);
   }

   //
   //
   //
   public boolean isPouring(){
      return (this._state == State.POURING);
   }

   //
   //
   //
   public boolean isPulled(){
      return (this._state == State.PULLED);
   }

   //
   //
   //
   public void pull() throws NotHomeException{
      if(this.isHome()){
         this.setPulled();
         this.notifyState();
      }
      else{
         //Throw and notify Subscribers!!!
         NotHomeException nhe = new NotHomeException();
         this.notifyError(nhe);
         throw nhe;
      }
   }

   //
   //
   //
   public void putback(){
      //Only return when it is in the PULLED State, not the HOME,
      //nor POURING States
      if(isPulled()){
         this.setHome();
         this.notifyState();
         synchronized(this._o){
            this._o.notify();
         }
      }
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
   public void takeOutOfUse(){
      this._t.interrupt();
   }


   ////////////////////////Private Methods////////////////////////////
   ////////////////////////Constructors///////////////////////////////
   //
   //
   //
   private Carafe(){
      this._t = new Thread(this);
      this._t.start();
   }

   //
   //
   //
   private double empty(int pourTime){
      final double SECSPERMILLIS = 0.001;

      double amount = 0.;

      double quant  = this.quantity();
      if(quant <= EMPTY){
         amount = 0;
         this.notifyError("Carafe Empty");
         this.notifyQuantity();
      }
      else{
         amount = this.emptyRate()*(pourTime*SECSPERMILLIS);
         if(quant < amount){
            amount = quant;
         }
         this.quantity(quant - amount);
         this.notifyQuantity();
      }
      return amount;
   }

   private double emptyRate(){
      return this._emptyRate;
   }

   //
   //
   //
   private void notify(Object object, String message){
      try{
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            Subscriber s = it.next();
            s.update(object,message);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void notifyCapacity(){
      Double quantity = Double.valueOf(this.capacity());
      this.notify(quantity,new String("Carafe Capacity"));
   }

   //
   //
   //
   private void notifyError(RuntimeException re){
      this.notifyError("Carafe " + re.getMessage());
   }

   //
   //
   //
   private void notifyError(String error){
      try{
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            Subscriber s = it.next();
            s.error(error);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void notifyQuantity(){
      Double quantity = Double.valueOf(this.quantity());
      this.notify(quantity, new String("Carafe Quantity"));
   }

   //
   //
   //
   private void notifyState(){
      String state = "" + this._state;
      this.notify(state, new String("Carafe State"));
   }

   //
   //
   //
   private void quantity(double quant){
      this._quantity = quant;
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
   private void setPulled(){
      this._state = State.PULLED;
   }

   //
   //
   //
   private void setPour(){
      this._state = State.POURING;
   }
   ///////////////////////Interface Methods///////////////////////////
   ///////////////////////Runnable Implementation/////////////////////
   //
   //
   //
   public void run(){
      int sleepTime     = 50;
      //int pourSleepTime = 1000;//sleep for a second while filling
      int pourSleepTime = 500;
      boolean toContinue = false;
      try{
         while(true){
            toContinue = true;
            while(this.isPouring()){
               if(toContinue){
                  double amount = this.empty(pourSleepTime);
                  if(amount > EMPTY){
                     this._mug.fill(amount);
                     Thread.sleep(pourSleepTime);
                  }
                  else{
                     toContinue = false;
                  }
               }
               else{
                  //Not sure if this is actually needed, "safety add"
                  Thread.sleep(sleepTime);
               }
            }
            Thread.sleep(sleepTime);
         }
      }
      catch(InterruptedException ie){}
   }

   ///////////CarafeInterface Implementation//////////////////////////
   //
   //
   //
   public void pour(Mug mug){
      if(mug != null){
         if(this.isPulled()){
            this._mug = mug;
            this.setPour();
            this.notifyState();
         }
         else if(this.isHome()){
            //Notify of issues
            String error = new String("Carafe not pouring because ");
            error = error.concat("it is on the Coffee Maker (Home)");
            this.notifyError(new String(error));
            this._mug.noLongerNeeded();
            this._mug = null;
         }
         else if(this.isPouring()){
            String error = new String("Carafe already pouring");
            this.notifyError(error);
         }
      }
   }

   //
   //This may change to be an interface implentation...
   //
   public double quantity(){
      return this._quantity;
   }

   //
   //
   //
   public void stopPour(){
      if(this.isPouring()){
         //Go back to the Pulled State
         this.setPulled();
         //Notify Observers
         this.notifyState();
         this._mug.noLongerNeeded();
         //let me see if this works
         this._mug = null;
      }
   }
}
//////////////////////////////////////////////////////////////////////
