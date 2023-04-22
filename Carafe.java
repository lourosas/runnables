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

   private enum State{HOME,PULLED};

   private final double CAPACITY = 32.;//Set for 32 at the moment

   private double _quantity;
   private State  _state;
   private Object _o;

   {
      _quantity = 0.;
      _state    = State.HOME;
      _instance = null;
      //This may be temporary
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
   public void fill(double amount) throws NotHomeException{
      /* Need to redo this completely!!!*/
      //System.out.println("Carafe: "+Thread.currentThread().getId());
      if(this._state == State.HOME){
         this._quantity += amount;
         if(this._quantity >= this.CAPACITY){
            if(this._quantity > this.CAPACITY){
               System.out.println("Carafe Overfilling!");
            }
            this._quantity = this.CAPACITY;
         }
      }
      else{
         throw new NotHomeException();
      }
   }

   //
   //This may be temporary
   //
   public void setObject(Object o){ this._o = o; }

   //
   //
   //
   public void pour(Mug mug){}

   //
   //
   //
   public void pull() throws NotHomeException{
      //Carafe carafe = null;
      if(this._state == State.HOME){
         this._state = State.PULLED;
      }
      else{
         throw new NotHomeException();
      }
   }

   //
   //
   //
   public void putback(){
      if(this._state != State.HOME){
         this._state = State.HOME;
         //This may be temporary...
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

   ////////////////////////Private Methods////////////////////////////
   ////////////////////////Constructors///////////////////////////////
   //
   //
   //
   private Carafe(){}

   ///////////////////////Interface Methods///////////////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
