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
import java.io.IOException;

public abstract class Engine extends SystemComponent{
   protected int engine; //Engine Number
   protected int stage;  //Stage

   /////////////////SystemComponent Methods Overrides/////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this.publisher.addSubscriber(subscriber);
      }
      catch(NullPointerException npe){
         this.setPublisher(new EnginePublisher());
         this.publisher.addSubscriber(subscriber);
      }
   }

   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      System.out.println("Engine");
      if(this.initializable == null){
         int en = this.engine;
         int st = this.stage;
         //Stage and Engine Number is needed
         this.setInitializable(new EngineInitializable(en, st));
      }
      this.initializable.initialize(file);
      //Everything can be handled at the Engine (Abstract) Level
      //In Initializaton, any way...
      try{
         EngineData engineData = null;
         engineData = (EngineData)this.initializable.initialized();
         //Notify the Subscribers
         this.publisher.publish(engineData);
      }
      catch(NullPointerException npe){}
      catch(ClassCastException cce){}
   }
}
//////////////////////////////////////////////////////////////////////
