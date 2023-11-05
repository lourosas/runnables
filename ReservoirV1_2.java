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
import rosas.lou.runnables.*;

public class ReservoirV1_2 extends Reservoir{
   private ContainerState _reservoirState;
   private Subscriber     _subscriber;

   {
      _reservoirState = null;
      _subscriber     = null;
   };
   /*
   public static final double EMPTY = 0.25;

   private enum State{STARTUP,EMPTY,FILLED,WASFILLED};
   
   private final double CAPACITY = 32.;

   private double _emptyRate;
   private double _quantity;
   private State  _state;

   {
      _quantity  = 0.;
      _emptyRate = 1.; //Oz/sec (whatever)
      _state     = State.STARTUP;
   };
   */
   ////////////////////////Constructors///////////////////////////////
   //
   //
   //
   public ReservoirV1_2(){
      this.state(ContainerStateMask.ALL);
   }

   ////////////////////////Public Methods/////////////////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      this._subscriber = subscriber;
   }

   //
   //
   //
   public double empty(int elapsedMillis)
   throws EmptyReservoirException{
      final double ZERO          = 0.;
      final double SECSPERMILLIS = 0.001;      
      double amount = 0.;
      if(this.quantity() > this.emptyCheck()){
         amount = elapsedMillis*SECSPERMILLIS*this.emptyRate();
         if((this.quantity() - amount <= ZERO)){
            amount = this.quantity();
         }
         this.quantity(this.quantity() - amount);
         this.state(ContainerStateMask.ALL);
      }
      else{
         String mpty = new String("Empty Reservoir Exception ");
         if(this.isFilled()){
            this.setWasFilled();
            mpty += "WASFILLED State";
         }
         else{
            this.setEmpty();
            mpty += "EMPTY State";
         }
         this.state(ContainerStateMask.ALL);
         throw new EmptyReservoirException(mpty);
      }
      return amount;
   }

   //
   //
   //
   public void fill(double amount) throws OverflowException{
      if(amount > EMPTY){
         this.quantity(this.quantity() + amount);
         this.setFilled();
         this.state(ContainerStateMask.ALL);//Best to do this!!
         if(this.quantity() > this.capacity()){
            this.quantity(this.capacity());
            this.state(ContainerStateMask.ALL);//Best to do this!!
            String exc = "Overflow Exception: Reservoir";
            throw new OverflowException(exc);
         }
      }
   }
   
   //
   //
   //
   public ContainerState state(){
      return this._reservoirState;
   }

   ///////////////////////Private Methods/////////////////////////////
   //
   //
   //
   private void state(int mask){
      //Tell the Model what has changed...
      String state    = null;
      double capacity = Double.NaN;
      double quantity = Double.NaN;
      if(mask == ContainerStateMask.NONE){}
      if((mask & ContainerStateMask.STATE) != 0){
         if(this.isStartup()){
            state = new String("STARTUP");
         }
         else if(this.isEmpty()){
            state = new String("EMPTY");
         }
         else if(this.isFilled()){
            state = new String("FILLED");
         }
         else if(this.wasFilled()){
            state = new String("WASFILLED");
         }
      }
      if((mask & ContainerStateMask.CAPACITY) != 0){
         capacity = this.capacity();
      }
      if((mask & ContainerStateMask.QUANTITY) != 0){
         quantity = this.quantity();
      }
      this._reservoirState =
                          new ReservoirState(state,capacity,quantity);
   }
}

//////////////////////////////////////////////////////////////////////
