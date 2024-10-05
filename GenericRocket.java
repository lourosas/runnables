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
import rosas.lou.clock.*;

public class GenericRocket implements Rocket, Runnable{
   private int     _stages;
   private int     _currentStage;
   private double  _emptyWeight;
   private double  _loadedWeight;
   private Stage[] _theStages;
   private String  _model;

   {
      _stages       = -1;
      _currentStage = -1;
      _emptyWeight  = Double.NaN;
      _loadedWeight = Double.NaN;
      _model        = null;
      _theStages    = null;
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public GenericRocket(){}

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void rocketData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setRocketData(read.readRocketInfo());
      }
   }

   //Set up the data....
   //
   //
   private void setRocketData(Hashtable<String,String> data){
      //the JSON data is all lower case...
      if(data.containsKey("model")){
         this._model = data.get("model");
      }
      if(data.containsKey("stages")){
         try{
            this._stages = Integer.parseInt(data.get("stages"));
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
      if(data.containsKey("empty_weight")){
         try{
            double v = Double.parseDouble(data.get("empty_weight"));
            this._emptyWeight = v;
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
      if(data.containsKey("loaded_weight")){
         try{
            double v = Double.parseDouble(data.get("loaded_weight"));
            this._loadedWeight = v;
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
   }

   //////////////////Rocket Interface Implementation//////////////////
   //
   //
   //
   /*
   public double emptyWeight(){
      return this._emptyWeight;
   }
   */

   //
   //
   //
   public void initialize(String file)throws IOException{
      this.rocketData(file);
      //this.stageData(file);
   }

   //
   //
   //
   /*
   public double loadedWeight(){
      return this._loadedWeight;
   }
   */

   //
   //
   //
   /*
   public String model(){
      return this._model;
   }
   */

   //
   //
   //
   public RocketData monitorPrelaunch(){
      return null;
   }

   //
   //
   //
   public RocketData monitorIgnition(){
      return null;
   }

   //
   //
   //
   public RocketData monitorLaunch(){
      return null;
   }

   //
   //
   //
   /*
   public int stages(){
      return this._stages;
   }
   */

   ///////////////Runnable Interface Implementation///////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
