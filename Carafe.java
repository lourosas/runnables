//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import rosas.lou.runnables.Mug;

public class Carafe implements Runnable{
   private static Lock lock = new ReentrantLock();
   private static Carafe _instance;

   private enum State{HOME,PULLED};

   private final double CAPACITY = 32.;//Set for 32 at the moment

   private double _quantity;
   private State  _state;

   {
      _quantity = 0.;
      _state    = State.HOME;
      _instance = null;
   };


   ////////////////////////////Public Methods/////////////////////////
   //
   //
   //
   static public Carafe instance(){
      if(_instance == null){
         _instance = new Carafe();
      }
      //This may have to change again!!!
      //I definitely need to think about this
      if(_instance._state == State.HOME){
         return _instance;
      }
      else{
         return null;
      }
   }

   //
   //
   //
   public void fill(double amount) throws NotHomeException{
      /* Need to redo this completely!!!
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
      */
   }

   //
   //
   //
   public void pour(Mug mug){}

   //
   //
   //
   public void pull(){
      this._state = State.PULLED;
   }

   //
   //
   //
   public void putback(){
      this._state = State.HOME;
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
