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
   private int _stage;
   private int _tank;

   {
      _stage = -1;
      _tank  = -1;
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

   ///////////////////Pump Interface Implementation///////////////////
   //
   //
   //
   public void initialization(String file)throws IOExcpetion{
      if((this._stage > 0) && (this._tank > 0)){
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
