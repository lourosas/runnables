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
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class CurrentRocket implements Rocket{

   private static final int NUMBEROFSTAGES = 1;

   private int currentStage;
   private boolean start;
   private boolean abort;

   {
      currentStage = 1;
      start        = false;
      abort        = false;
   }
   //////////////////////Contructors/////////////////////////////////
   //
   //
   //
   public CurrentRocket(){}

   //////////////Rocket Interface Implementation//////////////////////
   //
   //
   //
   public void abort(){
      this.abort = true;
   }

   //
   //
   //
   public int currentStage(){
      return this.currentStage;
   }

   //
   //
   //
   public int stages(){
      return NUMBEROFSTAGES;
   }

   /////////////////////Runnable Interface////////////////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            if(this.abort){
               System.out.println("Rocket Aborting");
               throw new InterruptedException();
            }
            Thread.sleep(250);
            System.out.println("Rocket Running");
         }
      }
      catch(InterruptedException ie){}
   }
}

//////////////////////////////////////////////////////////////////////
