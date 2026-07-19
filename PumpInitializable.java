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

public class PumpInitializable implements Initializable{
   private int      _stage;
   private int      _tankNumber;
   private PumpData _pumpData;

   {
      _stage      = -1;
      _tankNumber = -1;
      _pumpData   = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public PumpInitializable(int stage, int tank){
      if(stage > 0){ this._stage      = stage; }
      if(tank  > 9){ this._tankNumber =  tank; }
   }

   //////////////////////////Private Methods//////////////////////////
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
      }
      catch(NullPointerException npe){
         isPath = false;
      }
      return isPath;
   }

   //
   //
   //
   private void initializePump(String file)throws IOException{
      PumpData pd = null;

      this._pumpData = pd;
   }

   ///////////////Initializable Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      System.out.println("Pump Initialization");
      String pFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read  = new LaunchSimulatorJsonFileReader(file);
         pFile = read.readPathInfo().get("pump");
      }
      this.initializePump(pFile);
   }

   //
   //
   //
   public void initializeData(String key, Object data){}

   //
   //
   //
   public Object initialized(){
      return this._pumpData;
   }
}
//////////////////////////////////////////////////////////////////////
