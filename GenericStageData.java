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
   private List<EngineData> _engineData;
   private FuelSystemData   _fuelSystemData;

   {
      _model          = -1;
      _number         = -1;
      _engines        = -1;
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
      List<EngineData> engineData,
      FuelSystemData   fuelSystemData
   ){
      this.model(model);
      this.stageNumber(stageNumber);
      this.engines(engines);
      this.engineData(engineData);
      this.fuelSystemData(fuelSystemData);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void engines(int number){
      this._engines = number;
   }

   //
   //
   //
   private void engineData(List<EngineData> data){
      this._engineData = data;
   }

   //
   //
   //
   private void fuelSystemData(FuelSystemData data){
      this._fuelSystemData = data;
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
      this._number = stagenumber;
   }

   
   //////////////////Stage Data Interface Implementation//////////////
   //
   //
   //
   public List<EngineData> engineData(){ return null; }

   //
   //
   //
   public FuelSystemData fuelSystemData(){ return null; }

   //
   //
   //
   public String toString(){
      String data = new String("GenericStageData: \n");
      data += "Stage:   " + this._number + "\n";
      data += "Model:   " + String.format("0x%X\n",this.model);
      data += "Engines: " + this._engines + "\n";
      for(int i = 0; i < this._engineData.size(); ++i){
         data += this._engineData.get(i).toString() + "\n";
      }
      data += this._fuelSystemData.toString() + "\n";
      return data;
   }
}
//////////////////////////////////////////////////////////////////////
