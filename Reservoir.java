//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;

public class Reservoir implements ReservoirInterface{
   //set it for 32 oz for the moment
   private final double CAPACITY = 32.;
   private double   _quantity;
   private double   _emptyRate; //Oz/Sec--pretty Self-Explanatory
   private boolean _initiallyFilled;

   {
      _quantity        = 0.;
      _emptyRate       = 1.; //Oz/Sec
      _initiallyFilled = false;
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

   //
   //Fill up to a certain amount
   //
   public void fill(double amount) throws OverflowException{
      final double EMPTY = 0.25;
      //Test Prints
      if(amount > EMPTY){
         System.out.println("Reservoir.fill():  "+amount);
         this.quantity(this.quantity() + amount);
         this.initiallyFilled(true);
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
         String s = new String("Empty Reservoir Initially Filled:");
         s += " " + this.initiallyFilled();
         //Once empty, it needs to be filled again
         this.initiallyFilled(false);
         //Alert the User if the Reservoir was intially filled
         throw new EmptyReservoirException(s);
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
   private boolean initiallyFilled(){
      return this._initiallyFilled;
   }

   /*
   */
   private void initiallyFilled(boolean filled){
      this._initiallyFilled = filled;
   }

   /*
   */
   private void quantity(double quant){
      this._quantity = quant;
   }
   ///////////////////////Interface Methods///////////////////////////
   /*
   */
   public double quantity(){
      return this._quantity;
   }

}
//////////////////////////////////////////////////////////////////////
