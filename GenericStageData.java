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

public class GenericStageData implements StageData{
   private long             _model;
   private int              _number;
   private int              _engines;
   private double           _weight;
   private List<EngineData> _engineData;
   private FuelSystemData   _fuelSystemData;

   {
      _model          = -1;
      _number         = -1;
      _engines        = -1;
      _weight         = Double.NaN;
      _engineData     = null;
      _fuelSystemData = null;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   GenericStageData
   (
      long             model,
      int              number,
      int              engines,
      double           weight,
      List<EngineData> engineData,
      FuelSystemData   fuelSystemData
   ){
      this.model(model);
      this.stageNumber(number);
      this.engines(engines);
      this.weight(weight);
      this.engineData(engineData);
      this.fuelSystemData(fuelSystemData);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void engines(int number){
      if(number > 0){
         this._engines = number;
      }
   }

   //
   //
   //
   private void engineData(List<EngineData> data){
      if(data != null){
         this._engineData = data;
      }
   }

   //
   //
   //
   private void fuelSystemData(FuelSystemData data){
      if(data != null){
         this._fuelSystemData = data;
      }
   }

   //
   //
   //
   private void model(long number){
      this._model = number;
   }

   //
   //
   //
   private void stageNumber(int stagenumber){
      if(stagenumber > 0){
         this._number = stagenumber;
      }
   }

   //
   //
   //
   private void weight(double w){
      if(w > 0.){
         this._weight = w;
      }
   }

   
   //////////////////Stage Data Interface Implementation//////////////
   //
   //
   //
   public List<EngineData> engineData(){ return this._engineData; }

   //
   //
   //
   public FuelSystemData fuelSystemData(){
      return this._fuelSystemData;
   }

   //
   //
   //
   public long model(){ return this._model; }

   //
   //
   //
   public int numberOfEngines(){ return this._engines; }

   //
   //
   //
   public int stageNumber(){ return this._number; }

   //Weight is in Newtons!!
   //
   //
   public double weight(){ return this._weight; }

   //
   //
   //
   public String toString(){
      String data = new String("\nGenericStageData: \n");
      data += "Stage:   " + this.stageNumber() + "\n";
      data += "Model:   " + String.format("0x%X\n",this.model());
      data += "Engines: " + this.numberOfEngines() + "\n";
      data += "Weight:  " + this.weight() + "\n";
      Iterator<EngineData> it = this.engineData().iterator();
      while(it.hasNext()){
         data += it.next().toString() + "\n";
      }
      data += this.fuelSystemData().toString() + "\n";
      return data;
   }
}
//////////////////////////////////////////////////////////////////////
