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
public class ErrorObject{
   private int     errorValue;
   private boolean isError = false;

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public ErrorObject(){}

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public int get(){
      synchronized(this){
         System.out.println("get():  lock obtained");
         while(!this.isError){
            try{
               wait(); //Wait for an error
            }
            catch(InterruptedException ie){}
         }
         this.isError = false;
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
   public void set(int error){
      synchronized(this){
         System.out.println("set():  lock obtained");
         while(this.isError){
            try{
               wait(); //Wait if there is already an error
            }
            catch(InterruptedException ie){}
         }
         this.errorValue = error;
         this.isError = true;
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
