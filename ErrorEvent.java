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
import java.text.*;
import java.time.*;
import java.time.format.*;
public class ErrorEvent extends EventObject{
   private String event;
   private Object state;
   private String time;
   {
      event = null;
      state = null;
      time  = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public ErrorEvent(Object source, Object state, String event){
      super(source);
      this.event = event;
      this.state = state;
      //this.time  = time; Set the time as part of the creation
      this.setTime();
   }


   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public String getEvent(){
      return this.event;
   }

   //
   //
   //
   public Object getState(){
      return this.state;
   }

   //
   //
   //
   public String getTime(){
      return this.time;
   }

   //
   //
   //
   public String toString(){
      String returnString = new String(this.getSource().toString());
      returnString += "\n"+this.getEvent();
      returnString += "\n"+this.getState();
      returnString += "\n "+this.getTime();
      return returnString;
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setTime(){
      LocalDateTime now = LocalDateTime.now();
      DateTimeFormatter dtf = null;
      dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.SSS");
      this.time = now.format(dtf);
   }
}
//////////////////////////////////////////////////////////////////////
