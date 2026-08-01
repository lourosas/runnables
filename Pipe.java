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

import java.util.*;
import java.io.*;
import rosas.lou.runnables.*;

public abstract class Pipe extends SystemComponent{
   protected int stage;
   protected int tankNumber;
   //From a Tank--the number of pipes = number of Engines in Stage
   protected int number;  //The Pipe number to the engine

   /////////////////SystemComponents Methods Overrides////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this.publisher.addSubscriber(subscriber);
      }
      catch(NullPointerException npe){
         this.setPublisher(new PipePublisher());
         this.publisher.addSubscriber(subscriber);
      }
   }

   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      System.out.println("Pipe");
      if(this.initializable == null){
         int stg    = this.stage;
         int tkn    = this.tankNumber;
         int num    = this.number;
         this.setInitializable(new PipeInitializable(stg,tkn,num));
      }
      this.initializable.initialize(file);
      //Similar to the Tank, the initialization should be able to be
      //handled at the Pump (abstract) level...
      try{
         PipeData pipeData = null;
         pipeData = (PipeData)this.initializable.initialized();
         //Notity the Subscribers
         this.publisher.publish(pipeData);
      }
      catch(NullPointerException npe){}
      catch(ClassCastException cce){}
   }
}
//////////////////////////////////////////////////////////////////////
