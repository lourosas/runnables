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

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class Sudoku{
   private static final int SIZE = 9;
   private List<Subscriber> _subscribers;

   {
      _subscribers = null;
   };

   //////////////////////////Constructor//////////////////////////////
   //
   //
   //
   public Sudoku(){
   }


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
         this.notifySubscribers();
      }
   }

   //
   //
   //
   public void solve(){
   }

   /////////////////////////Private Methods///////////////////////////

   //
   //
   private void notifySubscribers(){
      System.out.println("Notify Subscribers");
      Iterator it = this._subscribers.iterator();
      while(it.hasNext()){
         Subscriber s = (Subscriber)it.next();
         s.update(this._block);
      }
   }
   //
   //This may be used at another time...
   //
   /*
   private void setUpBlock(){
      this._block[0].value(8, false);
      this._block[1].value(7, false);
      this._block[5].value(1, false);
      this._block[8].value(5, false);
      this._block[10].value(5,false);
      this._block[12].value(3,false);
      this._block[13].value(8,false);
      this._block[18].value(9,false);
      this._block[19].value(1,false);
      this._block[22].value(5,false);
      this._block[25].value(6,false);
      this._block[28].value(3,false);
      this._block[30].value(6,false);
      this._block[31].value(2,false);
      this._block[33].value(5,false);
      this._block[36].value(5,false);
      this._block[38].value(2,false);
      this._block[42].value(7,false);
      this._block[44].value(6,false);
      this._block[47].value(7,false);
      this._block[49].value(4,false);
      this._block[50].value(8,false);
      this._block[52].value(3,false);
      this._block[55].value(8,false);
      this._block[58].value(6,false);
      this._block[61].value(9,false);
      this._block[62].value(3,false);
      this._block[67].value(7,false);
      this._block[68].value(3,false);
      this._block[70].value(5,false);
      this._block[72].value(3,false);
      this._block[75].value(8,false);
      this._block[79].value(7,false);
      this._block[80].value(4,false);
   }
   */
}
//////////////////////////////////////////////////////////////////////
