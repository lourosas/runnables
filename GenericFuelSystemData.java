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

public class GenericFuelSystemData implements FuelSystemData{
   private String         _error;
   private boolean        _isError;
   private int            _numPipes;
   private int            _numPumps;
   private int            _numTanks;
   private List<PipeData> _pipes;
   private List<PumpData> _pumps;
   private List<TankData> _tanks;

   {
      _error       = null;
      _isError     = false;
      _numPipes    = -1;
      _numPumps    = -1;
      _numTanks    = -1;
      _pipes       = null;
      _pumps       = null;
      _tanks       = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericFuelSystemData
   (
      List<PipeData> pipeData,
      List<PumpData> pumpData,
      List<TankData> tankData
   ){
      this._pipes = pipeData;
      this._pumps = pumpData;
      this._tanks = tankData;
      try{ this._numPipes = this._pipes.size(); }
      catch(NullPointerException npe){}
      try{ this._numPumps = this._pumps.size(); }
      catch(NullPointerException npe){}
      try{ this._numTanks = this._tanks.size(); }
      catch(NullPointerException npe){}
      this.errors();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void errors(){
      try{
         Iterator<PipeData> it = this._pipes.iterator();
         while(it.hasNext()){
            PipeData pd = (PipeData)it.next();
            if(pd.isError()){
               if(!this._isError){ this._isError = true;}
               if(this._error == null){
                  this._error = new String(pd.error());
               }
               else{ this._error += pd.error(); }
            }
         }
      }
      catch(NullPointerException npe){}
      try{
         Iterator<PumpData> it = this._pumps.iterator();
         while(it.hasNext()){
            PumpData pd = (PumpData)it.next();
            if(pd.isError()){
               if(!this._isError){ this._isError = true; }
               if(this._error == null){
                  this._error = new String(pd.error());
               }
               else{ this._error += pd.error(); }
            }
         }
      }
      catch(NullPointerException npe){}
      try{
         Iterator<TankData> it = this._tanks.iterator();
         while(it.hasNext()){
            TankData td = (TankData)it.next();
            if(td.isError()){
               if(!this._isError){ this._isError = true; }
               if(this._error == null){
                  this._error = new String(td.error());
               }
               else{ this._error += td.error(); }
            }
         }
      }
      catch(NullPointerException npe){}
   }

   //////////////FuelSystemData Interface Implementation//////////////
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
   public List<PipeData> pipeData(){ return this._pipes; }

   //
   //
   //
   public List<PumpData> pumpData(){ return this._pumps; }

   //
   //
   //
   public List<TankData> tankData(){ return this._tanks; }

   //
   //
   //
   public String toString(){
      String value = new String("\nFuel System:  ");
      Iterator<PipeData> it = this._pipes.iterator();
      while(it.hasNext()){
         value += it.next().toString();
      }
      Iterator<PumpData> ip = this._pumps.iterator();
      while(ip.hasNext()){
         value += ip.next().toString();
      }
      Iterator<TankData> is = this._tanks.iterator();
      while(is.hasNext()){
         value += is.next().toString();
      }
      //Add ERROR Data!!!!
      return value;
   }
}
//////////////////////////////////////////////////////////////////////
