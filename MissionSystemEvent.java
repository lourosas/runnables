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
import java.time.*;
import java.time.format.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class MissionSystemEvent extends EventObject{
   private String              event;
   private Object              eventState;
   private LaunchStateSubstate state;
   private String              time;

   {
      event      = null;
      eventState = null;
      state      = null;
      time       = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public MissionSystemEvent
   (
      Object source,
      Object eventState,//The data of the Event (not just the source)
      String event,
      LaunchStateSubstate state
   ){
      super(source);
      this.event      = event;
      this.eventState = eventState;
      this.state      = state;
      this.setTime();
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public String event(){
      return this.event;
   }

   //
   //
   //
   public Object eventState(){
      return this.eventState;
   }

   //
   //
   //
   public String time(){
      return this.time;
   }

   //
   //
   //
   public LaunchStateSubstate state(){
      return this.state;
   }

   //
   //
   //
   public String toString(){
      String returnString =  new String("Source:  ");
      returnString += this.getSource().toString();
      returnString += "\n" + this.event();
      returnString += "\n" + this.eventState();
      returnString += "\n" + this.time();
      returnString += "\n" + this.state.toString();
      return returnString;
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setTime(){
      LocalDateTime now  = LocalDateTime.now();
      DateTimeFormatter dtf = null;
      dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.SSS");
      this.time = now.format(dtf);
   }
}
//////////////////////////////////////////////////////////////////////
