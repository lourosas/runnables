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
   private State     _state;

   {
      _reservoir = null;
      _carafe    = null;
      _t         = null;
      _state     = State.READY;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public Maker(){
      this._reservoir = new Reservoir();
      this._carafe    = Carafe.instance();
      //REMOVE THE TEST PRINTS!!!
      //System.out.println(Thread.currentThread().getId());
      this._t = new Thread(this);
      //this._t.start();
   }
   
   ///////////////////////////Public Methods//////////////////////////
   //
   //Brew an entire pot of coffee
   //
   /*
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
   */

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
         catch(OverflowException oe){}
         finally{
            this.brew();
         }
      }
      else{
         throw new AlreadyBrewingException();
      }
   }

   //
   //Brew something other than a full pot
   //
   /*
   public void brewCoffee(double amount){
      //0.  Check the current state--only brew if not currently
      //    brewing--subject to change, possibly...
      if(this._state != State.BREWING){
      
      }
   }
   */

   //
   //
   //
   public Carafe getCarafe(){
      return this._carafe;
   }

   //
   //
   //
   public void returnCarafe(){}

   ////////////////////////Privatte Methods///////////////////////////
   //
   //
   //
   private void brew(){
      //1.  Set the State to State.BREWING
      //READY-->BREWING
      this.setBrewing();
      //2.  Go ahead and start the thread meant for brewing...
      //This means I need to update/create new sequence diagram for
      //this use case...
      //The thread starting here may be TEMPORARY until I figure out
      //if I like it...
      this._t.start();
   }

   //
   //
   //
   //
   private boolean isBrewing()(
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
   private void setReady(){}

   //////////////////////Interface Methods////////////////////////////
   //
   //
   //
   public void run(){
      //For the time being, sleep for .5 seconds!
      int sleepTime = 1000;
      try{
         //This is definitely a redundant check...
         if(this._state == State.BREWING){  
            double amount = this._reservoir.empty(sleepTime);
            while(amount > 0){
               Thread.sleep(sleepTime);
               this._carafe.fill(amount);
               System.out.println("Carafe: "+this._carafe.quantity());
               amount = this._reservoir.empty(sleepTime);
            }
            //Once Done, set back to the READY state
            //BREWING-->READY
            this.setReady();
            System.out.println("Carafe: "+this._carafe.quantity());
         }
      }
      catch(InterruptedException ie){}
   }
}

//////////////////////////////////////////////////////////////////////
