//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;

public class Reservoir implements ReservoirInterface{
   private enum State{STARTUP,EMPTY,FILLED, WASFILLED};
   
   //set it for 32 oz for the moment
   private final double CAPACITY = 32.;

   private double  _emptyRate; //Oz/Sec--pretty Self-Explanatory
   private double  _quantity;
   private State   _state;

   {
      _quantity  = 0.;
      _emptyRate = 1.; //Oz/Sec
      _state     = State.STARTUP;
   };

   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   public Reservoir(){}

   //////////////////////////Public Methods///////////////////////////
   /*
   */
   public double capacity(){
      return this.CAPACITY;
   }

   /*
   */
   public boolean isEmpty(){
      return (this._state == State.EMPTY);
   }

   /*
   */
   public boolean isFilled(){
      return (this._state == State.FILLED);
   }

   /*
   */
   public boolean isStartup(){
      return (this._state == State.STARTUP);
   }

   /*
   */
   public boolean wasFilled(){
      return (this._state == State.WASFILLED);
   }

   //
   //Fill up to a certain amount
   //
   public void fill(double amount) throws OverflowException{
      final double EMPTY = 0.25;
      //Test Prints
      if(amount > EMPTY){
         System.out.println("Reservoir.fill():  "+amount);
         this.quantity(this.quantity() + amount);
         //Indicate the State as FILLED...
         this.setFilled();
         if(this.quantity() > this.CAPACITY){
            this.quantity(this.CAPACITY);
            throw new OverflowException("Reservoir Overflowing!!");
         }
      }
   }

   //
   //Empty the Tank at the specified rate
   //Send in the elapsed time running
   //amount = emptyRate*elaspedTime
   //Enter the elapsed time in milli-seconds
   //
   public double empty(int elapsedTime)throws EmptyReservoirException{
      final double SECSPERMILLIS = .001;
      final double EMPTY         = 0.25;
      if(this.quantity() <= EMPTY){
         if(this.isFilled()){
            this.setWasFilled();
         }
         else{
            this.setEmpty();
         }
         //Alert the User if the Reservoir was intially filled
         throw new EmptyReservoirException();
      }
      //1.  Get the amount to empty
      double amount = this.emptyRate() * (elapsedTime*SECSPERMILLIS);
      //2.  Check against remaining amount left
      //3.  If the amount left in the reservoir < the amount to
      //    extract, set the amount to extract to what is left in the
      //    reservoir (cannot remove more than what is left)
      if(this.quantity() < amount){
         amount = this.quantity();
      }
      //4.  Subtract the amount from the quantity
      this.quantity(this.quantity() - amount);
      //5.  Return the amount "pummped" (emptied) from the reservoir
      return amount;
   }

   ////////////////////////Private Methods////////////////////////////
   /*
   */
   private double emptyRate(){
      return this._emptyRate;
   }

   /*
   */
   private void quantity(double quant){
      this._quantity = quant;
   }

   /*
   */
   private void setEmpty(){
      this._state = State.EMPTY;
   }

   /*
   */
   private void setFilled(){
      this._state = State.FILLED;
   }

   /*
   */
   private void setWasFilled(){
      this._state = State.WASFILLED;
   }
   ///////////////////////Interface Methods///////////////////////////
   /*
   */
   public double quantity(){
      return this._quantity;
   }

}
//////////////////////////////////////////////////////////////////////
