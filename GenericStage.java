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

public class GenericStage implements Stage, Runnable{
   private List<Engine>  _engines;
   private String        _error;
   private FuelSystem    _fuelSystem;
   private int           _stageNumber;
   private long          _modelNumber;
   private int           _totalEngines;
   private boolean       _isError;
   private double        _weight;

   {
      _engines      = null;
      _error        = null;
      _fuelSystem   = null;
      _stageNumber  = -1;
      _modelNumber  = -1;
      _totalEngines = -1;
      _isError      = false;
      _weight       = Double.NaN;
   };

   /////////////////////////////Constructor///////////////////////////
   //
   //
   //
   public GenericStage(int number){
      if(number > 0){
         this._stageNumber = number;
      }
   }

   ///////////////////////////Private Methods/////////////////////////
   //Calculated the weight of the entire stage...
   //
   //
   private void calculateWeight(){}

   //
   //
   //
   private void engineData(String file)throws IOException{
      for(int i = 0; i < this._totalEngines; ++i){
         Engine engine = new GenericEngine(i+1,this._stageNumber);
         engine.initialize(file);
         try{
            this._engines.add(engine);
         }
         catch(NullPointerException npe){
            this._engines = new LinkedList<Engine>();
            this._engines.add(engine);
         }
      }
   }

   //
   //
   //
   private void fuelSystem(String file)throws IOException{
      int stage = this._stageNumber;
      int engs  = this._totalEngines;
      this._fuelSystem = new GenericFuelSystem(stage,engs);
      this._fuelSystem.initialize(file);
   }

   //
   //
   //
   private void setStageData
   (
      List<Hashtable<String,String>> data
   ){
      System.out.println(data);
      //will need to figure out which Stage it is...pretty simple
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String,String> ht = data.get(i);
         try{
            String num = ht.get("number");
            if(Integer.parseInt(num) == this._stageNumber){
               this._totalEngines=Integer.parseInt(ht.get("engines"));
               int v = Integer.parseUnsignedInt(ht.get("model"),16);
               this._modelNumber = Integer.toUnsignedLong(v);
            }
         }
         catch(NumberFormatException npe){}
      }
   }

   //
   //
   //
   private void stageData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setStageData(read.readStageInfo());
      }
   }

   ///////////////////Stage Interface Implementation//////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      if(this._stageNumber > -1){
         this.stageData(file);
         this.engineData(file);
         this.fuelSystem(file);
      }
   }

   //
   //
   //
   public StageData monitorPrelaunch(){
      //Part of the StageData
      List<EngineData> engineData = new LinkedList<EngineData>();
      for(int i = 0; i < this._engines.size(); ++i){
      }
      return null;
   }

   //
   //
   //
   public StageData monitorIgnition(){ return null; }

   //
   //
   //
   public StageData monitorLaunch(){ return null; }

   ////////////////Runnable Interface Implementation//////////////////
   //
   //
   //
   public void run(){}
}

//////////////////////////////////////////////////////////////////////
