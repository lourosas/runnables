//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;

public class Reservoir{
   //set it for 32 oz for the moment
   private final double CAPACITY = 32.;
   private double _quantity;
   private double _emptyRate; //Oz/Sec--pretty Self-Explanatory

   {
      _quantity  = 0.;
      _emptyRate = 1.; //Oz/Sec
   };

   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   public Reservoir(){}

   //////////////////////////Public Methods///////////////////////////
   //
   //Fill up to a certain amount
   //
   public void fill(double amount) throws OverflowException{
      //Test Prints
      System.out.println("Reservoir.fill():  "+amount);
      this._quantity += amount;
      if(this._quantity > this.CAPACITY){
         this._quantity = this.CAPACITY;
         throw new OverflowException("Reservoir Overflowing!!");
      }
   }

   //
   //Empty the Tank at the specified rate
   //Send in the elapsed time running
   //amount = emptyRate*elaspedTime
   //Enter the elapsed time in milli-seconds
   //
   public double empty(int elapsedTime){
      final double SECSPERMILLIS = .001;
      //1.  Get the amount to empty
      double amount = this._emptyRate * (elapsedTime*SECSPERMILLIS);
      //2.  Check against remaining amount left
      //3.  If the amount left in the reservoir < the amount to
      //    extract, set the amount to extract to what is left in the
      //    reservoir (cannot remove more than what is left)
      if(this._quantity < amount){
         amount = this._quantity;
      }
      //4.  Subtract the amount from the quantity
      this._quantity -= amount;
      //Test Prints
      System.out.println("Reservoir quantity: "+this._quantity);
      //5.  Return the amount "pummped" (emptied) from the reservoir
      return amount;
   }
}
//////////////////////////////////////////////////////////////////////
