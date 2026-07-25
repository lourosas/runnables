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

public class FuelSystemInitializable implements Initializable{
   //private List<TankData>    _tankData;
   private int               _engines;
   private int               _stage;
   private FuelSystemData    _fuelSystemData;

   {
      //_tankData       = null;
      _engines        = -1;
      _fuelSystemData = null;
      _stage          = -1;
   };

   ////////////////////////////Constructors//////////////////////////
   //
   //
   //
   public FuelSystemInitializable(int stage, int engines){
      if(stage > 0){
         this._stage = stage;
      }
      if(engines > 0){
         this._engines = engines;
      }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void initializePipeData(Object data){}

   //
   //
   //
   private void initializePumpData(Object data){

      List<PumpData> pdl = null;
      PumpData       pd  = null;
      try{
         pd = (PumpData)data;
      }
      catch(ClassCastException cce){
         pd = null;
      }
      if((pd != null) && (this._stage == pd.stage())){
         pdl = this._fuelSystemData.pumps();
         try{
            pdl.add(pd);
         }
         catch(NullPointerException npe){
            pdl = new LinkedList<PumpData>();
            pdl.add(pd);
         }
      }
      int eng            = this._fuelSystemData.engines();
      int stg            = this._fuelSystemData.stage();
      List<TankData> tdl = this._fuelSystemData.tanks();
      List<PipeData> pid = this._fuelSystemData.pipes();
      FuelSystemData fsd = null;
      fsd = new GenericFuelSystemData(eng,stg,pid,pdl,tdl);
   }

   //
   //
   //
   private void initializeTankData(Object data){
      List<TankData> tdl = null;
      TankData td        = null;
      try{
         td = (TankData)data;
      }
      catch(ClassCastException cce){
         td = null;
      }
      if((td != null) && (this._stage == td.stage())){
         tdl = this._fuelSystemData.tanks();
         try{
            tdl.add(td);
         }
         catch(NullPointerException npe){
            tdl = new LinkedList<TankData>();
            tdl.add(td);
         }
      }
      int eng            = this._fuelSystemData.engines();
      int stg            = this._fuelSystemData.stage();
      List<PipeData> pid = this._fuelSystemData.pipes();
      List<PumpData> pud = this._fuelSystemData.pumps();
      FuelSystemData fsd = null;
      //fsd=new GenericFuelSystemData(eng,stg,pid,pud,this._tankData);
      fsd = new GenericFuelSystemData(eng,stg,pid,pud,tdl);
      this._fuelSystemData = fsd;
   }

   //////////////Initializable Interface Implementation///////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      System.out.println("FuelSystem Initializable");
      //No file to read...so just initialize the instance...
      FuelSystemData fsd = new GenericFuelSystemData(this._engines,
                                                     this._stage,
                                                     null,
                                                     null,
                                                     null);
      this._fuelSystemData = fsd;
   }

   //Only need to do this since FuelSystemData is based one
   //the three below...
   //
   public void initializeData(String key, Object data){
      if(key.toUpperCase().contains("PIPE DATA")){
         this.initializePipeData(data);
      }
      else if(key.toUpperCase().contains("PUMP DATA")){
         System.out.println("++++++++++FS Pump Data+++++++++");
         System.out.println(data);
         System.out.println("++++++++++FS Pump Data+++++++++");
         this.initializePumpData(data);
      }
      else if(key.toUpperCase().contains("TANK DATA")){
         this.initializeTankData(data);
      }
   }

   //
   //
   //
   public Object initialized(){
      return this._fuelSystemData;
   }
}
//////////////////////////////////////////////////////////////////////
