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
   private static Lock lock = new ReentrantLock();
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
         this.notifyState();
         this.notifyCapacity();
         this.notifyQuantity();
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
   //
   public void setMug(Mug mug){
      this._mug = mug;
      this.notify(this._mug, "Mug");
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
   private void notify(Object object, String message){
      Iterator<Subscriber> it = this._subscribers.iterator();
      while(it.hasNext()){
         Subscriber s = it.next();
         s.update(object,message);
      }
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
      Iterator<Subscriber> it = this._subscribers.iterator();
      while(it.hasNext()){
         Subscriber s = it.next();
         s.error(error);
      }
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
   private void empty(Mug mug){
      //Test Prints
      System.out.println("Carafe.empty()");
      System.out.println(mug);
      //mug.fill(1.);
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
      int pourSleepTime = 1000;//sleep for a second while filling
      try{
         while(true){
            Thread.sleep(sleepTime);
         }
      }
      catch(InterruptedException ie){}
   }

   ///////////CarafeInterface Implementation//////////////////////////
   //
   //
   //
   public void pour(){
      if(this.isPulled()){
         this.setPour();
         this.notifyState();
      }
      else if(this.isHome()){}
      else if(this.isPouring()){
         //Notify of error
      }
   }

   //
   //
   //
   public void pour(Mug mug){
      this.setMug(mug);
      this.pour();
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
      this.setPulled();
   }
}
//////////////////////////////////////////////////////////////////////
