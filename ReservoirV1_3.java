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

public class ReservoirV1_3 extends Reservoir{
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
   public ReservoirV1_3(){
   }

   ////////////////////////Public Methods/////////////////////////////
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
         if(this.quantity() > this.capacity()){
            this.quantity(this.capacity());
            String exc = "Reservoir Overflow Exception Quantity: ";
            exc += this.quantity();
            throw new OverflowException(exc);
         }
      }
   }
}

//////////////////////////////////////////////////////////////////////
