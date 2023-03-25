//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;
import rosas.lou.runnables.*;

public class Maker implements Runnable{
   private enum State{NOCOFFEE,BREWING,BREWED};
   private Reservoir _reservoir;
   private Carafe    _carafe;
   private Thread    _t;
   private State     _state;

   {
      _reservoir = null;
      //_carafe    = null;
      _t         = null;
      _state     = State.NOCOFFEE;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public Maker(){
      this._reservoir = new Reservoir();
      //this._carafe    = Carafe.instance();
      //REMOVE THE TEST PRINTS!!!
      //System.out.println(Thread.currentThread().getId());
      this._t = new Thread(this);
      //this._t.start();
   }
   
   ///////////////////////////Public Methods//////////////////////////
   //
   //Brew an entire pot of coffee
   //
   public void brewCoffee(){
      //0.  Check the current state--only brew if not currently
      //    brewing--subject to change, possibly...
      if(this._state != State.BREWING){
         //1.  Fill Reservoir completely
         this._reservoir.totalFill();
         //2.  Set the State to State.BREWING
         this._state = State.BREWING;
         //Start the Thread here???
         //This is going to be temporary until I decide where to start
         this._t.start();
      }
   }

   //
   //Brew something other than a full pot
   //
   public void brewCoffee(double amount){
      //0.  Check the current state--only brew if not currently
      //    brewing--subject to change, possibly...
      if(this._state != State.BREWING){
      
      }
   }

   //
   //
   //
   public Carafe getCarafe(){
      Carafe carafe = Carafe.instance();
      try{
      }
      catch(NullPointerException npe){}
      finally{
      }
   }

   //
   //
   //
   public void returnCarafe(){}

   //////////////////////Interface Methods////////////////////////////
   //
   //
   //
   public void run(){
      //For the time being, sleep for .5 seconds!
      int sleepTime = 1000;
      try{
         if(this._state == State.BREWING){  
            double amount = this._reservoir.empty(sleepTime);
            while(amount > 0){
               Thread.sleep(sleepTime);
               this._carafe.fill(amount);
               System.out.println("Carafe: "+this._carafe.quantity());
               amount = this._reservoir.empty(sleepTime);
            }
            this._state = State.BREWED;
            System.out.println("Carafe: "+this._carafe.quantity());
         }
      }
      catch(InterruptedException ie){}
   }
}

//////////////////////////////////////////////////////////////////////
