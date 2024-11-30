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
import java.io.IOException;
import rosas.lou.runnables.*;

public class GenericPump implements Pump{
   private int    _stage;
   private int    _tank;
   private double _rate;
   private double _temperature;
   private double _tolerance;

   {
      _stage       = -1;
      _tank        = -1;
      _rate        = Double.NaN;
      _temperature = Double.NaN;
      _tolerance   = Double.NaN;
   };

   ////////////////////////////Constructor////////////////////////////
   //
   //
   //
   public GenericPump(int stage, int tank){
      if(stage > 0){
         this._stage = stage;
      }
      if(tank > 0){
         this._tank = tank;
      }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void pumpData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setPumpData(read.readPumpDataInfo());
      }
   }

   //
   //
   //
   private void setPumpData(List<Hashtable<String,String>> data){
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String,String> ht = data.get(i);
         try{
            int stage = Integer.parseInt(ht.get("stage"));
            int tank  = Integer.parseInt(ht.get("tanknumber"));
            if((this._tank == tank) && (this._stage == stage)){
               System.out.println("Pump: "+ht);
               this._rate = Double.parseDouble(ht.get("rate"));
               Double d   = Double.parseDouble(ht.get("temperature"));
               this._temperature = d;
               d = Double.parseDouble(ht.get("tolerance"));
               this._tolerance = d;
            }
         }
         catch(NumberFormatException nfe){}
      }
   }

   ///////////////////Pump Interface Implementation///////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      if((this._stage > 0) && (this._tank > 0)){
         this.pumpData(file);
      }
   }

   //
   //
   //
   public PumpData monitorPrelaunch(){ return null; }

   //
   //
   //
   public PumpData monitorIgnition(){ return null; }

   //
   //
   //
   public PumpData monitorLaunch(){ return null; }
}
//////////////////////////////////////////////////////////////////////
