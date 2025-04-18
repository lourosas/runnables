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
import java.text.*;
import java.time.*;
import java.time.format.*;
public class LaunchMechanismSimulator{
   private double emptyWeight;
   private double inputWeight;
   private double currentWeight; //Calculated weight
   private Random random;

   {
      emptyWeight   = Double.NaN;
      inputWeight   = Double.NaN;
      currentWeight = Double.NaN; //Might not need
      random        = null;
   };
   
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public LaunchMechanismSimulator(){
      this.random = new Random();
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void initialize(String file){
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String, String> ht = null;
         ht = read.readRocketInfo();
         this.grabNeededData(ht);
      }
      catch(IOException ioe){
         ioe.printStackTrace();
      }
   }

   //
   //
   //
   public double weight(){
      int     weight     = -1;
      double  lim        = .01;
      double  edge       = this.inputWeight * lim;
      double  lowerLimit = this.inputWeight - edge;
      double  upperLimit = this.inputWeight + edge;
      boolean found      = false;
      while(!found){
         weight = random.nextInt(2000000);
         if(weight >= lowerLimit && weight <= upperLimit){
            found = true;
         }
      }
      return (double)weight;
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void grabNeededData(Hashtable<String,String> ht){
      String ew = ht.get("empty_weight");
      String iw = ht.get("loaded_weight");
      try{
         this.emptyWeight = Double.parseDouble(ew);
      }
      catch(NumberFormatException nfe){
         this.emptyWeight = Double.NaN;
      }
      try{
         this.inputWeight = Double.parseDouble(iw);
      }
      catch(NumberFormatException nfe){
         this.inputWeight = Double.NaN;
      }
   }
}
//////////////////////////////////////////////////////////////////////
