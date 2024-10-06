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
   private double                 _measuredWeight;
   //Weight read in from the init file
   private double                 _inputWeight;
   private double                 _tollerance;

   {
      _holds           = -1;
      _model           = -1;
      _supports        = null;
      _measuredWeight  = Double.NaN;
      _inputWeight     = Double.NaN;
      _tollerance      = Double.NaN;
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
   private void mechanismData(Hashtable<String,String> data){
   }


   //Should only get the loaded weight of the Rocket...get the empty
   //wieght in addition...
   //Gets the Rocket Info needed for Support Responsibilities
   private void rocketData(Hashtable<String, String> data){
      if(data.containsKey("loaded_weight")){
         try{
            String loadedWeight = data.get("loaded_weight");
            this._inputWeight   = Double.parseDouble(loadedWeight);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException npe){}
      }
      //@TODO figure out where to save...
      if(data.containsKey("empty_weight")){}
   }

   //Sets up/saves the data related to all the individual supports
   //The caveat is all supports are the same...this will probably
   //change
   public void supportsData(Hashtable<String,String> data){
   }

   /////////Launching Mechanism Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){}
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = null;
         ht = read.readRocketInfo();
         this.rocketData(ht);
         ht = read.readLaunchingMechanismInfo();
         this.mechanismData(ht);
      }
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
   //For the time being, return the Measured Weight...
   //
   public double supportedWeight(){
      return this._measuredWeight;
   }

   ////////////////Runnable Interface Implementation//////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
