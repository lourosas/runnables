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
import rosas.lou.runnables.*;

public class RocketInitializable implements Initializable{
   private RocketData _rocketData = null;

   ////////////////////////////Contructors////////////////////////////
   //
   //
   //
   public RocketInitializable(){}

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void initializeRocket(String file)throws IOException{
      //Test Print (for now)
      System.out.print("initializeRocket(): "+file);
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      Hashtable<String,String> ht = read.readRocketInfo();
      String model     = this.getModel(ht);
      int numStages    = this.getNumberOfStages(ht);
      double empWeight = this.getEmptyWeight(ht);
      double lddWeight = this.getLoadedWeight(ht);
      double tolerance = this.getTolerance(ht);
      this._rocketData = new GenericRocketData(
                                    model,
                                    1,         //Current Stage
                                    numStages, //Number of Stages
                                    empWeight, //Empty Weight
                                    lddWeight, //Loaded Weight
                                    Double.NaN,//Calculated Weight
                                    false,     //Is Error
                                    null,      //Error String
                                    null,      //Payload Data
                                    null,      //Stage(s) Data
                                    tolerance);//Tolerance
   }

   //
   //
   //
   private boolean isPathFile(String file)throws IOException{
      boolean isPath = false;
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         if(read.readPathInfo().get("parameter") == null){
            throw new NullPointerException("Not a Path File");
         }
         isPath = true;
      }
      catch(IOException ioe){
         isPath = false;
         throw ioe
      }
      catch(NullPointerException npe){
         isPath = false;
      }
      return isPath;
   }

   ///////////////Initializable Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      //Test Print (for now)
      System.out.println("RocketInitializable:  "+file);
      String rFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         rFile = read.readPathInfo().get("rocket");
      }
      this.initizlizeRocket(rFile);
   }

   //
   //
   //
   public Object initialized(){
      this._rocketData;
   }
}
//////////////////////////////////////////////////////////////////////
