//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import rosas.lou.runnables.Mug;

//
//Implements the Singleton Interface
//
//

public class Carafe implements Runnable{
   private static Lock lock = new ReentrantLock();
   private static Carafe _instance;

   private enum State{HOME,PULLED,POUR};

   private final double CAPACITY = 32.;//Set for 32 at the moment

   private double _quantity;
   private State  _state;
   private Object _o;

   {
      _quantity = 0.;
      _state    = State.HOME;
      _instance = null;
      _o        = null;
   };


   ////////////////////////////Public Methods/////////////////////////
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
   public void fill(double amount) throws NotHomeException,
   OverflowException{
      if(this.isHome()){
         this._quantity += amount;
         if(this._quantity > this.CAPACITY){
            this._quantity = this.CAPACITY;
            throw new OverflowException("Carafe Overfilling!");
         }
      }
      else{
         throw new NotHomeException();
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
   public void pour(Mug mug) throws NotPulledException{
      if(this.isPulled()){
         //Test Prints
         System.out.println("Carafe.pour()");
         this.empty(mug);
      }
      else{
         throw new NotPulledException();
      }
   }

   //
   //
   //
   public void pull() throws NotHomeException{
      if(this.isHome()){
         //this._state = State.PULLED;
         this.setPulled();
      }
      else{
         throw new NotHomeException();
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
         synchronized(this._o){
            this._o.notify();
         }
      }
   }

   //
   //This will need to be modified...for now, set it up for
   //Stubbing
   //
   public void stopPour(){}

   //
   //
   //
   public double quantity(){
      return this._quantity;
   }

   ////////////////////////Private Methods////////////////////////////
   ////////////////////////Constructors///////////////////////////////
   //
   //
   //
   private Carafe(){}

   //
   //
   //
   private boolean isHome(){
      return (this._state == State.HOME);
   }

   //
   //
   //
   //
   private boolean isPulled(){
      return(this._state == State.PULLED);
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
   private void empty(Mug mug){
      //Test Prints
      System.out.println("Carafe.empty()");
      System.out.println(mug);
      //mug.fill(1.);
   }

   ///////////////////////Interface Methods///////////////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
