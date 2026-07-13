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

import java.util.*;
import java.io.*;
import rosas.lou.runnables.*;

public abstract class Pump extends SystemComponent{
   protected int stage;
   protected int tankNumber;

   //////////////////SystemComponents Methods Overrides///////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this.publisher.addSubscriber(subscriber);
      }
      catch(NullPointerException npe){
         this.setPublisher(new PumpPublisher());
         this.publisher.addSubscriber(subscriber);
      }
   }

   //
   //
   //
   public void initializeComponent(String file)throws IOException{
      System.out.println("Pump");
      if(this.initializable == null){
         int stg = this.stage;
         int tn  = this.tanknumber;
         this.setInitializable(new PumpInitializable(stg,tn));
      }
      this.initializable.initialize(file);
      //Similar to the Tank, the initialization should be able to be
      //handled at the Pump (abstract) level...
      try{
         PumpData pumpData = null;
         pumpData = (PumpData)this.initializable.initialized();
         //notify the Subscribers
         this.publisher.publish(pumpData);
      }
      catch(NullPointerException npe){}
      catch(ClassCastException cce){}
   }
}
//////////////////////////////////////////////////////////////////////
