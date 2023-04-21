//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;
import rosas.lou.runnables.*;

public class Maker implements Runnable{
   private enum State{READY,BREWING};
   private Reservoir _reservoir;
   private Carafe    _carafe;
   private Thread    _t;
   private boolean   _power;
   private State     _state;

   {
      _reservoir = null;
      _carafe    = null;
      _t         = null;
      _power     = true;
      _state     = State.READY;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public Maker(){
      this._reservoir = new Reservoir();
      //This is going to want to change, as well!!!
      this._carafe    = Carafe.instance();
      //REMOVE THE TEST PRINTS!!!
      //System.out.println(Thread.currentThread().getId());
      this._t = new Thread(this);
      //since power is on, go ahead and start the thread...
      //this is the POWERON (super)state
      this._t.start();
   }
   
   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void brew(double amount)throws AlreadyBrewingException{
      //Check the current state--only brew if not currently brewing...
      //subject to change...
      if(!this.isBrewing()){
         try{
            this._reservoir.fill(amount);
         }
         catch(OverflowException oe){
            //oe.printStackTrace();
            System.out.println(oe.getMessage());
         }
         finally{
            this.brew();
         }
      }
      else{
         throw new AlreadyBrewingException();
      }
   }

   //
   //
   //
   public Carafe getCarafe(){
      //This is WRONG!!!  This is going to need to change!
      //will need to set-up a Mutex region!
      this._carafe.pull();
      return this._carafe;
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
   public void returnCarafe(){
      //Will want to set up mutex
      this._carafe.putback();
   }

   ////////////////////////Privatte Methods///////////////////////////
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
                     this._carafe.fill(amount);
                     System.out.println(this._carafe);
                     //System.out.println(Thread.currentThread().getId());
                     System.out.println("R: "+this._reservoir.quantity());
                     System.out.println("C: "+this._carafe.quantity());
                     amount=this._reservoir.empty(reservoirSleepTime);
                  }
                  catch(NotHomeException nhe){
                     System.out.println(nhe.getMessage());
                     System.out.println("R: "+this._reservoir.quantity());
                     System.out.println("C: "+this._carafe.quantity());
                  }
               }
               //Once Done, set back to the READY state
               //BREWING-->READY
               this.setReady();
               System.out.println("Carafe: "+this._carafe.quantity());
            }
            Thread.sleep(sleepTime);
         }
         catch(InterruptedException ie){
            System.out.println("Thread Interrupted, "+this._power);
         }
      }
   }
}

//////////////////////////////////////////////////////////////////////
