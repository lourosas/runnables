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

public abstract class LaunchMechanism extends SystemComponent{
   protected Rocket                rocket;

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void addRocket(Rocket rckt){
      if(rckt != null){
         this.rocket = rckt;
      }
   }

   //////////////////SystemComponent Methods Overrides////////////////
   //
   //
   //
   public void addSubcriber(Subscriber subscriber){
     try{
        this.publisher.addSubscriber(subscriber);
     }
     catch(NullPointerException npe){
        this.setPublisher(new LaunchMechanismPublisher());
     }
   }

   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      System.out.println("LaunchMechanism");
      try{
         this.initializable.initialize(file);
      }
      catch(NullPointerException npe){
         this.setInitializable(new LaunchMechanismInitializable());
         this.initializable.initialize(file);
      }
   }
}
//////////////////////////////////////////////////////////////////////
