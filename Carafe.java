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

   private enum State{HOME,PULLED};

   private final double CAPACITY = 32.;//Set for 32 at the moment

   private double _quantity;
   private State  _state;

   {
      _quantity = 0.;
      _state    = State.HOME;
   };

   ////////////////////////Constructors///////////////////////////////
   //
   //
   //
   public Carafe(){}

   ////////////////////////////Public Methods/////////////////////////
   //
   //
   //
   public void fill(double amount){
      //System.out.println("Carafe: "+Thread.currentThread().getId());
      this._quantity += amount;
      if(this._quantity >= this.CAPACITY){
         if(this._quantity > this.CAPACITY){
            System.out.println("Carafe Overfilling!");
         }
         this._quantity = this.CAPACITY;
      }
   }

   //
   //
   //
   public void pour(Mug mug){}

   //
   //
   //
   public void pull(){
      System.out.println("Carafe: "+this._state);
      this._state = State.PULLED;
      System.out.println("Carafe: "+this._state);
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

   ///////////////////////Interface Methods///////////////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
