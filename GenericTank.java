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
import java.io.*;
import rosas.lou.runnables.*;

public class GenericTank implements Tank{
   private double  _capacity;
   private String  _fuel;
   private long    _model;
   private double  _emptyRate;
   private int     _stageNumber;
   private int     _tankNumber;
   private double  _temperature;
   private double  _tolerance;

   {
      _capacity     = Double.NaN;
      _fuel         = null;
      _model        = -1;
      _emptyRate    = Double.NaN;
      _stageNumber  = -1;
      _tankNumber   = -1;
      _temperature  = Double.NaN;
      _tolerance    = Double.NaN;
   };

   ///////////////////////////Constructor/////////////////////////////
   //
   //
   //
   public GenericTank(int stage, int number){
      if(stage > 0){
         this._stageNumber = stage;
      }
      if(number > 0){
         this._tankNumber  = number;
      }
   }

   //////////////////////////Tank Interface///////////////////////////
   //
   //
   //
   private void setTankData
   (
      List<Hashtable<String,String>> data
   ){
      System.out.println(data);
      //Get the Stage and Tank numbers for comparison
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String,String> ht = data.get(i);
         try{
            int stage = Integer.parseInt(ht.get("stage"));
            int num   = Integer.parseInt(ht.get("number"));
            if((stage==this._stageNumber) && (num==this._tankNumber)){
               System.out.println("Hashtable: "+ht);
            }
         }
         catch(NumberFormatException nfe){}
      }
   }

   //
   //
   //
   private void tankData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setTankData(read.readTankDataInfo());
      }
   }

   ///////////////////////Tank Interface Methods//////////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      if(this._stageNumber > 0 && this._tankNumber > 0){
         this.tankData(file);
      }
   }

   //
   //
   //
   public TankData monitorPrelaunch(){ return null; }

   //
   //
   //
   public TankData monitorIgnition(){ return null; }

   //
   //
   //
   public TankData monitorLaunch(){ return null; }
}
//////////////////////////////////////////////////////////////////////
