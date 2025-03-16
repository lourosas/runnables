//////////////////////////////////////////////////////////////////////
/*
Copyright 2025 Lou Rosas

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
import rosas.lou.clock.*;

//Trying to fucking understand wait()/notify()...
public class ErrorObject2{
   private int     errorValue;
   private boolean transfer = true; //see what the fuck happens here

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public ErrorObject2(){}

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public int receive(){
      synchronized(this){
         System.out.println("receive():  Lock Obtained");
         while(this.transfer){
            try{
               wait(); //Wait for a send
            }
            catch(InterruptedException ie){}
         }
         this.transfer = true;
         try{
            notify();
         }
         catch(IllegalMonitorStateException e){
            e.printStackTrace();
         }
         return this.errorValue;
      }
   }

   //
   //
   //
   public void send(int error){
      synchronized(this){
         System.out.println("send():  Lock Obtained");
         while(!this.transfer){
            try{
               wait();
            }
            catch(InterruptedException ie){}
         }
         this.errorValue = error;
         this.transfer   = false;
         try{
            notify();
         }
         catch(IllegalMonitorStateException e){
            e.printStackTrace();
         }
      }
   }
}
//////////////////////////////////////////////////////////////////////
