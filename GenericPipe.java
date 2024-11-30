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

public class GenericPipe implements Pipe{
   private int    _tank;  //Tank Number (1,2)
   private int    _stage; //Stage Number (1...total stages)
   //Pipe Number for the Tank (1,2)--corresponds to the engine...
   private int    _number; 
   private double _rate;
   private double _temperature;
   private double _tolerance;

   {
      _tank       = -1;
      _stage      = -1;
      _number     = -1;
   };

   ///////////////////////////Constructor/////////////////////////////
   //Tank Number
   //Stage Number
   //Pipe Number
   public GenericPipe(int tank, int stage, int number){
      if(tank > 0){
         this._tank = tank;
      }
      if(stage > 0){
         this._stage = stage;
      }
      if(number > 0){
         this._number = number;
      }
   }
   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void pipeData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setPipeData(read.readPipeDataInfo());
      }
   }

   //
   //
   //
   private void setPipeData(List<Hashtable<String,String>> data ){
      System.out.println("Pipes: "+data);
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String,String> ht = data.get(i);
         try{
            int tk     = this._tank;
            int st     = this._stage;
            int num    = this._number;
            int tank   = Integer.parseInt(ht.get("tanknumber"));
            int stage  = Integer.parseInt(ht.get("stage"));
            if((tk == tank) && (st == stage)){
               System.out.println("Engine:  "+this._number);
               System.out.println(ht);
               this._rate = Double.parseDouble(ht.get("rate"));
               Double d = Double.parseDouble(ht.get("temperature"));
               this._temperature = d;
               d = Double.parseDouble(ht.get("tolerance"));
               this._tolerance = d;
            }
         }
         catch(NumberFormatException nfe){}
      }
   }

   ////////////////////Pipe Interface Implementation//////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      int tank  = this._tank;
      int stage = this._stage;
      int num   = this._number;
      if((tank > 0) && (stage > 0) && (num > 0)){
         this.pipeData(file);
      }
   }

   //
   //
   //
   public PipeData monitorPrelaunch(){ return null; }

   //
   //
   //
   public PipeData monitorIgnition(){ return null; }

   //
   //
   //
   public PipeData monitorLaunch(){ return null; }
}
//////////////////////////////////////////////////////////////////////
