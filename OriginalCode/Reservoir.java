//////////////////////////////////////////////////////////////////////
/*
Copyright 2023 Lou Rosas

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;

public class Reservoir implements ReservoirInterface{
   public static final double EMPTY = 0.25;

   private enum State{STARTUP,EMPTY,FILLED, WASFILLED};

   //set it for 32 oz for the moment
   private final double CAPACITY = 32.;
                            //Oz/sec or so--pretty self-explanatory
   private double           _emptyRate;
   private double           _quantity;
   private State            _state;
   private List<Subscriber> _subscribers;

   {
      _quantity    = 0.;
      _emptyRate   = 1.; //Oz/Sec
      _state       = State.STARTUP;
      _subscribers = null;
   };

   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   public Reservoir(){}

   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this._subscribers.add(subscriber);
      }
      catch(NullPointerException npe){
         this._subscribers = new LinkedList<Subscriber>();
         this._subscribers.add(subscriber);
      }
      finally{
         this.notifySubscribersOfState();
      }
   }

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
   public void fill(double amount){
      if(amount > EMPTY){
         this.quantity(this.quantity() + amount);
         //Indicate the State as FILLED...
         this.setFilled();
         if(this.quantity() > this.CAPACITY){
            this.quantity(this.CAPACITY);
            //throw new OverflowException("Reservoir Overflowing!!");
            this.notifyError(new String("Reservoir Overflowing"));
         }
         this.notifyQuantity();
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
      double amount = 0.;
      if(this.quantity() <= EMPTY){
         if(this.isFilled()){
            this.setWasFilled();
         }
         else{
            this.setEmpty();
            this.notifyError("Reservoir Empty!!");
         }
         //Alert the User if the Reservoir was intially filled
         amount = 0.;
      }
      else{
         //1.  Get the amount to empty
         amount = this.emptyRate() * (elapsedTime*SECSPERMILLIS);
         //2.  Check against remaining amount left
         //3.  If the amount left in the reservoir < the amount to
         //    extract, set the amount to extract to what is left in
         //    the reservoir (cannot remove more than what is left)
         if(this.quantity() < amount){
            amount = this.quantity();
         }
         //4.  Subtract the amount from the quantity
         this.quantity(this.quantity() - amount);
      }
      //5.Notify the Subscribers of the quantity
      this.notifyQuantity();
      //6.  Return the amount "pummped" (emptied) from the reservoir
      return amount;
   }

   //
   //
   //
   //
   public void systemPowerOff(){
      this.notifySubscribersOfState();
   }

   //
   //
   //
   //
   public void systemPowerOn(){
      this.notifySubscribersOfState();
   }

   //////////////////////Protected Methods////////////////////////////
   /*
   */
   protected double emptyCheck(){
      return EMPTY;
   }

   /*
   */
   protected double emptyRate(){
      return this._emptyRate;
   }

   /**/
   protected boolean isWasFilled(){
      return(this._state == State.WASFILLED);
   }

   /*
   */
   protected void quantity(double quant){
      this._quantity = quant;
   }

   /*
   */
   protected void setEmpty(){
      this._state = State.EMPTY;
   }

   /*
   */
   protected void setFilled(){
      this._state = State.FILLED;
   }

   /*
   */
   protected void setWasFilled(){
      this._state = State.WASFILLED;
   }
   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private void notify(Object object, String message){
      try{
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            Subscriber s = it.next();
            s.update(object, message);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void notifyCapacity(){
      Double cap = Double.valueOf(this.capacity());
      this.notify(cap,new String("Reservoir Capacity"));
   }

   //
   //
   //
   private void notifyError(RuntimeException re){
      this.notifyError("Reservoir " + re.getMessage());
   }

   //
   //
   //
   private void notifyError(String error){
      try{
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            Subscriber s = it.next();
            s.error(error);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void notifyQuantity(){
      Double quant = Double.valueOf(this.quantity());
      this.notify(quant,new String("Reservoir Quantity"));
   }

   //
   //
   //
   private void notifyState(){}

   /*
   */
   private void notifySubscribersOfState(){
      this.notifyState();
      this.notifyCapacity();
      this.notifyQuantity();
   }

   ///////////////////////Interface Methods///////////////////////////
   /*
   */
   public double quantity(){
      return this._quantity;
   }

}
//////////////////////////////////////////////////////////////////////
