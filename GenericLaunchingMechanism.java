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

public class GenericLaunchingMechanism implements LaunchingMechanism,
Runnable{
   private int                    _holds;
   private int                    _model;
   private List<MechanismSupport> _supports; //Keep them in a list
   //This weight is to be calculated
   private double                 _supportedWeight;
   //Weight read in from the init file
   private double                 _inputWeight;
   private double                 _tollerance;

   {
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public GenericLaunchingMechanism(){}

   /////////////////////////Private Methods///////////////////////////
   //Sets up/saves the mechanism data for the System
   //
   //
   public void mechanismData(String file)throws IOException{
      System.out.println(file);
   }

   //Sets up/saves the data related to all the individual supports
   //The caveat is all supports are the same...this will probably
   //change
   public void supportsData(String file)throws IOException{}

   /////////Launching Mechanism Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      this.mechanismData(file);
      this.supportsData(file);
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
