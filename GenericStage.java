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
   private int _stageNumber;
   private int _modelNumber;
   private int _totalEngines;

   {
      _stageNumber =  -1;
      _modelNumber =  -1;
      _totalEngines = -1;
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
   //
   //
   //
   private void setStageData
   (
      List<Hashtable<String,String>> data
   ){
      //will need to figure out which Stage it is...pretty simple
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
      }
   }

   //
   //
   //
   public StageData monitorPrelaunch(){ return null; }

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
