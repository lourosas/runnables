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
import java.io.*;
import rosas.lou.runnables.*;

public class GenericFuelSystem implements FuelSystem{
   
   private int        _stageNumber;
   private int        _engines;
   private Tank       _fuel;
   private Tank       _oxidizer;
   private List<Pipe> _pipes;
   private Pump       _oxidizerPump;
   private Pump       _fuelPump;

   {
      _engines       = -1;
      _stageNumber   = -1;
      _fuel          = null;
      _oxidizer      = null;
      _pipes         = null;
      _oxidizerPump  = null;
      _fuelPump      = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericFuelSystem(int stage, int engines){
      //At least one pipe per each tank feeding the engines...
      if(stage > 0){
         this._stageNumber = stage;
      }
      if(engines > 0){
         //Needed to determine the number of pipes...
         this._engines = engines;
      }
   }

   ////////////////////Private Methods////////////////////////////////
   //
   //
   //
   private void setUpPipes(String file)throws IOException{
      //Two Pipes Per Engine, per Stage, per Tank
      for(int i = 0; i < this._engines; ++i){
         //Tank
         for(int j = 0; j < 2; ++j){
            System.out.println("Stage Number: "+this._stageNumber);
            //Pipe                   tank,stage,engine
            Pipe p = new GenericPipe(j+1,this._stageNumber,i+1);
            p.initialize(file);
            try{
               this._pipes.add(p);
            }
            catch(NullPointerException npe){
               this._pipes = new LinkedList<Pipe>();
               this._pipes.add(p);
            }
         }
      }
   }

   //
   //
   //
   private void setUpPumps(String file)throws IOException{
      System.out.println("Pumps: " + file);
   }

   //
   //
   //
   private void setUpTanks(String file)throws IOException{
      //Allow for the possibility the data is different based on the
      //tank...
      this._fuel     = new GenericTank(this._stageNumber, 1);
      this._oxidizer = new GenericTank(this._stageNumber, 2);
      this._fuel.initialize(file);
      this._oxidizer.initialize(file);
   }
   
   /////////////////Fuel System Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      System.out.println("Fuel System:  "+file);
      this.setUpTanks(file);
      this.setUpPumps(file);
      this.setUpPipes(file);
   }

   //
   //
   //
   public FuelSystemData monitorPrelaunch(){ return null; }

   //
   //
   //
   public FuelSystemData monitorIgnition(){ return null; }

   //
   //
   //
   public FuelSystemData monitorLaunch(){ return null; }
}
//////////////////////////////////////////////////////////////////////
