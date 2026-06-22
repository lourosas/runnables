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
import java.io.*;
import rosas.lou.runnables.*;

public class FuelSystemInitializable implments Initializable{
   private int             _stage;
   private FuelSystemData  _fuelSystemData;

   {
      _fuelSystemData = null;
      _stage          = -1;
   };

   ////////////////////////////Constructors//////////////////////////
   //
   //
   //
   public FuelSystemInitializable(int stage){
      if(stage > 0){
         this._stage = stage;
      }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void initializeFuelSystem(String file)throws IOException{
      String piFile = file;
      String pmFile = file;
      String taFile = file;
      if(this.isPathFile(file)){}
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
         throw ioe;
      }
      catch(NullPointerException npe){
         isPath = false;
      }
      return isPath;
   }

   //////////////Initializable Interface Implementation///////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      System.out.println("FuelSystemInitializable");
      this.initializeFuelSystem(file);
   }

   //
   //
   //
   public void initializeData(String key, Object data){
      if(key.toUpperCase().contains("PIPE DATA")){}
      else if(key.toUpperCase().contains("PUMP DATA")){}
      else if(key.toUpperCase().contains("TANK DATA")){}
   }

   //
   //
   //
   public Object initialized(){
      return this._fuelSystemData;
   }
}
//////////////////////////////////////////////////////////////////////
