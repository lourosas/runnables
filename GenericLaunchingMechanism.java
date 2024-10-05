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

public class GenericLaunchingMechanism implements LaunchingMechanism,
Runnable{
   private int                    _holds;
   private int                    _model;
   private List<MechanismSupport> _supports; //Keep them in a list
   private double                 _supportedWeight;//Might not need

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public GenericLaunchingMechanism(){}

   /////////////////////////Private Methods///////////////////////////

   /////////Launching Mechanism Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file){
   }

   //
   //
   //
   public List<LaunchingMechanismData> monitorPrelaunch(){
      return null;
   }

   //
   //
   //
   public List<LaunchingMechanismData> monitorIgnition(){
      return null;
   }

   //
   //
   //
   public List<LaunchingMechanismData> monitorLaunch(){
      return null;
   }

   //
   //
   //
   public void release(){}

   //Probably not needed...might be able to remove...
   //
   //
   public double supportedWeight(){
      return this._supportedWeight;
   }

   ////////////////Runnable Interface Implementation//////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
