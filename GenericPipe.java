//////////////////////////////////////////////////////////////////////
/*
Copyright 2024 Lou Rosas

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
import java.io.IOException;
import rosas.lou.runnables.*;

public class GenericPipe extends Pipe implements Runnable{
   private boolean  _kill;
   private Thread   _rt0;

   {
      _kill      = false;
      _rt0       = null;

      obj        = null;
      stage      = -1;
      tankNumber = -1;
      number     = -1;
   };

   ///////////////////////////Constructor/////////////////////////////
   //Tank Number
   //Stage Number
   //Pipe Number
   public GenericPipe(int tank, int stage, int number){
      if(tank > 0){
         this.tankNumber = tank;
      }
      if(stage > 0){
         this.stage = stage;
      }
      if(number > 0){
         //Essentially, this is the Rocket Engine the Pipe Feeds...
         this.number = number;
      }
      this.obj = new Object();
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setUpThread(){
      String name = new String("Pipe: "+this.stage+", ");
      name += this.tankNumber+", "+this.number;
      this._rt0 = new Thread(this, name);
      this._rt0.start();
   }

   ///////////////////Runnable Interface Implementation///////////////
   //
   //
   //
   public void run(){
      int counter   = 0;
      boolean check = false;
      try{
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this.getStateSubstate() != null){
               if(this.getStateSubstate().state() == INIT){
                  if(counter++%1500 == 0){
                     check = true;
                     counter = 1;//reset the counter
                  }
               }
            }
            if(check){
               /*
               this.monitorPipe();
               this.checkErrors();
               this.alertSubscribers();
               */
               check = false;
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
      catch(NullPointerException npe){
         //Should NEVER GET HERE!!!
         npe.printStackTrace();
         System.exit(0);
      }
   }
}
//////////////////////////////////////////////////////////////////////
