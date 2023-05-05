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
   private Thread    _t;
   private boolean   _power;
   private State     _state;
   //Temporary for the moment
   private Object    _o;

   {
      _reservoir = null;
      _t         = null;
      _power     = true;
      _state     = State.READY;
      _o         = null;
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
         return carafeInterface;
      }
   }

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
