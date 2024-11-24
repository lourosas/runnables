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

public class GenericEngine implements Engine, Runnable{
   private String  _error;
   private double  _exhaustFlow;
   private boolean _isError;
   private boolean _isIgnited;
   private int     _model;
   private int     _number;
   private int     _stage;
   private double  _temperature;
   private double  _tolerance;

   {
      _error         = null;
      _exhaustFlow   = Double.NaN;
      _isError       = false;
      _isIgnited     = false;
      _model         = -1;
      _number        = -1;
      _stage         = -1;
      _temperature   = Double.NaN;
      _tolerance     = Double.NaN;
   };

   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public GenericEngine(int number, int stage){
      if(number > 0){
         this._number = number;
      }
      if(stage > 0){
         this._stage = stage;
      }
   }

   ////////////////////////////Private Methods////////////////////////
   //
   //
   //
   private void engineData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorInifileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setEngineData(read.readEngineDataInfo());
      }
   }

   //
   //
   //
   private void setEngineData
   (
      List<Hashtable<String,String>> data
   ){
   
   }

   ////////////////////Engine Interface Implementation////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      if(this._number > -1 && this._stage > -1){
         this.engineData(file);
      }
   }

   //
   //
   //
   public EngineData monitorPrelaunch(){
      return null;
   }

   //
   //
   //
   public EngineData monitorIgnition(){
      return null;
   }

   //
   //
   //
   public EngineData monitorLaunch(){
      return null;
   }

   //////////////////Runnable Interface Implementation////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
