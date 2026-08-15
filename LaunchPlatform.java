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
import rosas.lou.clock.*;
public abstract class LaunchPlatform extends SystemComponent{
   protected List<LaunchMechanism> mechanisms;
   protected Rocket                rocket;
   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void addRocket(Rocket rckt){
      if(rckt != null){
         this.rocket = rckt;
         try{
            Iterator<LaunchMechanism> it = mechanisms.iterator();
            while(it.hasNext()){
               it.next().addRocket(rckt);
            }
         }
         catch(NullPointerException npe){}
      }
   }

   //////////////////SystemComponent Methods Overrides////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this.publisher.addSubscriber(subscriber);
      }
      catch(NullPointerException npe){
         this.setPublisher(new LaunchPlatformPublisher());
         this.publisher.addSubscriber(subscriber);
      }
   }

   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      System.out.println("LaunchPlatform");
      try{
         this.initializable.initialize(file);
      }
      catch(NullPointerException npe){
         this.setInitializable(new LaunchPlatformInitializable());
         this.initializable.initialize(file);
      }
   }

   //////////////////StateMutable Interface Overrides/////////////////
   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate ss){
      super.setStateSubstate(ss);
      try{
         Iterator<LaunchMechanism> it = this.mechanisms.iterator();
         while(it.hasNext()){
            it.next().setStateSubstate(ss);
         }
      }
      catch(NullPointerException npe){}
      //Do not need to worry about the Rocket--already set
   }
}
//////////////////////////////////////////////////////////////////////
