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
import java.lang.*;
import rosas.lou.runnables.*;

public abstract class FuelSystemData{
   private int            _engines;
   private String         _error;
   private boolean        _isError;
   private int            _stage;
   private List<PipeData> _pipes;
   private List<PumpData> _pumps;
   private List<TankData> _tanks;

   {
      _engines     = -1;
      _error       = null;
      _isError     = false;
      _pipes       = null;
      _pumps       = null;
      _stage       = -1;
      _tanks       = null;
   };

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public int engines(){ return this._engines; }

   //
   //
   //
   public String error(){ return this._error; }
   
   //
   //
   //
   public boolean isError(){ return this._isError; }

   //
   //
   //
   public int stage(){ return this._stage; }

   //
   //
   //
   public List<PipeData> pipes(){ return this._pipes; }
   
   //
   //
   //
   public List<PumpData> pumps(){ return this._pumps; }
   
   //
   //
   //
   public List<TankData> tanks(){ return this._tanks; }

   public String toString(){
      String data = new String("\nFuel System Data: ");
      data += "\n------------------------------------";
      data += "\nStage:         "+this.stage();
      data += "\nEngines:       "+this.engines();
      data += "\nIs Error:      "+this.isError();
      data += "\nError:         "+this.error();
      data += "\nTanks:\n";
      try{
         Iterator<TankData> it = this.tanks().iterator();
         while(it.hasNext()){ data += it.next().toString(); }
      }
      catch(NullPointerException npe){ data += npe.getMessage(); }
      data += "\nPumps:\n";
      try{
         Iterator<PumpData> pd = this.pumps().iterator();
         while(pd.hasNext()){ data += pd.next().toString(); }
      }
      catch(NullPointerException npe){ data += npe.getMessage(); }
      data += "\nPipes:\n";
      try{
         Iterator<PipeData> pid = this.pipes.iterator();
         while(pid.hasNext()){ data += pip.next().toString(); }
      }
      catch(NullPointerException npe){ data += npe.getMessage(); }
      return data;
   }
   
   /////////////////////////Protected Methods/////////////////////////
   //
   //
   //
   protected void engines(int engines){
      if(engines > 0){
         this._engines = engines;
      }
   }

   //
   //
   //
   protected void error(String error){ this._error = error; }

   //
   //
   //
   protected void isError(boolean isError){ this._isError = error; } 

   //
   //
   //
   protected void stage(int stage){ this._stage = stage; }

   //
   //
   //
   protected void pipes(List<PipeData> pipes){
      this._pipes = pipes;
   }

   //
   //
   //
   protected void pumps(List<PumpData> pumps){
      this._pumps = pumps;
   }

   //
   //
   //
   protected void tanks(List<TankData> tanks){
      this._tanks = tanks;
   }
}
//////////////////////////////////////////////////////////////////////
