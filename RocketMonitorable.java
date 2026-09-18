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

public class RocketMonitorable implements Monitorable{
   private RocketData    _rocketData;

   {
      _rocketData = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public RocketMonitorable(){}

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void addError(String error){
      boolean isE = this._rocketData.isError();

      if(error.toUpperCase().contains("ERROR")){
         isE = true;
         String temp = error.toUpperCase();
         temp = temp.split("ERROR")[1];
         System.out.println(temp);
         String[] arr = temp.split(" ");
         boolean done = false;
         for(int i = 0; i < arr.length && !done; ++i){
            if(arr[i].length() > 0){
               if(arr[i].charAt(0)>='A'  && arr[i].charAt(0)<='z'){
                  System.out.println(arr[i].length());
                  System.out.println(arr[i]);
                  done = true;
               }
            }
         }
      }
      //Test Prints...
      System.out.println(error);

   }

   ////////////////Monitorable Interface Implementation///////////////
   //
   //
   //
   public void addData(Object data){
      try{
         this._rocketData = (RocketData)data;
      }
      catch(ClassCastException cce){
         //This print out is probably going to stay
         cce.printStackTrace();
         this._rocketData = null;
      }
   }

   //
   //
   //
   public void addData(String type, Object data){
      RocketData rd = null;
      if(type.toUpperCase().contains("ERROR")){
         this.addError(type);
      }
      try{
         rd = (RocketData)data;
         System.out.println(rd.emptyWeight());
         System.out.println(rd.calculatedWeight());
         System.out.println(rd.loadedWeight());
         System.out.println(rd.tolerance());
      }
      catch(ClassCastException   cce){}
      catch(NullPointerException npe){}
   }

   //
   //
   //
   public Object monitor(){
      return this._rocketData;
   }
}
//////////////////////////////////////////////////////////////////////
