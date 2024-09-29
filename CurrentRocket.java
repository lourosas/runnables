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
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class CurrentRocket implements Rocket{

   private static final int NUMBEROFSTAGES = 1;

   private int     _currentStage;
   private double  _loadedWeight;  //Measure in Newtons!!!
   private double  _emptyWeight;   //Measure in Netwons!!!

   {
      _currentStage   = 1;
      _emptyWeight   = 1e6;
      _loadedWeight  = 3e6;
   }
   //////////////////////Contructors/////////////////////////////////
   //
   //
   //
   public CurrentRocket(){}

   //////////////Rocket Interface Implementation//////////////////////
   //
   //
   //
   public double emptyWeight(){
      return this._emptyWeight;
   }
   
   //Bring in the initialization data (JSON, INI, etc...)
   //
   //
   public void initialize(String file){}

   //
   //
   //
   public double loadedWeight(){
      return this._loadedWeight;
   }

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
   public int stages(){
      return this.NUMBEROFSTAGES;
   }

   ///////////////////Runnable Interface Implementation//////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
