//////////////////////////////////////////////////////////////////////
/*
Copyright 2026 Lou Rosas

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
import java.io.*;
import rosas.lou.runnables.*;

public class GenericEngine extends Engine implements Runnable{
   private boolean _kill;
   private Thread  _rt0;

   {
      _kill = false;
      _rt0  = null;

      engine = -1;
      obj    = null;
      stage  = -1;
   };

   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public GenericEngine(int number, int stage){
      if(number > 0){
         this.engine = number;  //Set up the Engine Number
      }
      if(stage > 0){
         this.stage = stage;    //Set up the Stage
      }
      this.obj = new Object();
      this.setUpThread();
   }

   ////////////////////////////Private Methods////////////////////////
   //
   //
   //
   private void setUpThread(){
      int eng = this.engine;
      int stg = this.stage;
      String name = new String("Engine: "+stg+", "+eng);
      this._rt0 = new Thread(this, name);
      this._rt0.start();
   }

   //////////////////////////Engine Override//////////////////////////

   /////////////////////////Protected Methods/////////////////////////

   //////////////////Runnable Interface Implementation////////////////
   //
   //
   //
   public void run(){
      try{
         int     count   = 0;
         boolean check   = false;
         int     compare = -1;
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this.getStateSubstate() != null){
               if(this.getStateSubstate().state() == INIT){
                  //Initialization State:  Check every 5 seconds
                  compare = 5000;
               }
               if((compare > 0) && (count++%compare == 0)){
                  //For Initialize, check every 5 seconds...
                  check = true;
                  count = 1; //Reset the Counter
               }
            }
            if(check){
               this.monitorExhaustFlowRate();
               this.monitorFuelFlowRate();
               this.monitorTemperature();
               this.checkErrors();
               this.alertSubscribers();
               check = false;
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
      catch(NullPointerException npe){
         npe.printStackTrace();
         System.exit(0);
      }
   }
}
//////////////////////////////////////////////////////////////////////
