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

public class PayloadInitializable implements Initializable{
   private PayloadData _payloadData;

   {
      _payloadData = null;
   };

   ////////////////////////////Contstructors//////////////////////////
   //
   //
   //
   public PayloadInitializable(){}
   
   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private int getCrew(Hashtable<String,String> ht){
      int crew = -1;
      try{
         crew = Integer.parseInt(ht.get("crew"));
      }
      catch(NumberFormatException nfe){
         crew = -1;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         crew = -1;
      }
      return crew;
   }

   //
   //
   //
   private double getDryWeight(Hashtable<String,String> ht){
      double dryWeight = Double.NaN;
      try{
         dryWeight = Double.parseDouble(ht.get("dryweight"));
      }
      catch(NumberFormatException nfe){
         dryWeight = Double.NaN;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         dryWeight = Double.NaN;
      }
      return dryWeight;
   }

   //
   //
   //
   private double getEmptyMass(Hashtable<String,String> ht){
      double emptyMass = Double.NaN;
      try{
         emptyMass = Double.parseDouble(ht.get("empty_mass"));
      }
      catch(NumberFormatException nfe){
         emptyMass = Double.NaN;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         emptyMass = Double.NaN;
      }
      return emptyMass;
   }

   //
   //
   //
   private boolean getIsOccupied(Hashtable<String,String> ht){
      boolean isOccupied = false;
      try{
         isOccupied = Boolean.parseBoolean(ht.get("occupied"));
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         isOccupied = false;
      }
      return isOccupied;
   }

   //
   //
   //
   private double getLoadedMass(Hashtable<String,String> ht){
      double loadedMass = Double.NaN;
      try{
         loadedMass = Double.parseDouble(ht.get("loaded_mass"));
      }
      catch(NumberFormatException nfe){
         loadedMass = Double.NaN; 
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         loadedMass = Double.NaN;
      }
      return loadedMass;
   }

   //
   //
   //
   private double getMaxWeight(Hashtable<String,String> ht){
      double maxWeight = Double.NaN;
      try{
         maxWeight = Double.parseDouble(ht.get("maxweight"));
      }
      catch(NumberFormatException npe){
         maxWeight = Double.NaN;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         maxWeight = Double.NaN;
      }
      return maxWeight;
   }

   //
   //
   //
   private String getModel(Hashtable<String,String> ht){
      String model = null;
      try{
         model = ht.get("model");
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         model = null;
      }
      return model;
   }

   //
   //
   //
   private void initializePayload(String file)throws IOException{
      //Test Print for now
      System.out.println("initializePayload(...) "+file);
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      Hashtable<String,String> ht = read.readPayloadInfo();
      int        crew      = this.getCrew(ht);
      double       cw      = Double.NaN;
      double       dw      = this.getDryWeight(ht);
      double       em      = this.getEmptyMass(ht);
      String      err      = null;
      boolean     isE      = false;
      boolean     isO      = this.getIsOccupied(ht);
      double       lm      = this.getLoadedMass(ht);
      double       mw      = this.getMaxWeight(ht);
      String      mdl      = this.getModel(ht);
      double      o2p      = this.getO2Percent(ht);
      double      tem      = this.getTemperature(ht);
      double      tol      = this.getTolerance(ht);
      String     type      = this.getType(ht);
   }

   //
   //
   //
   private boolean isPathFile(String file)throws IOException{
      boolean isPath = false;
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         if(read.readPathInfo().get("paramenter") == null){
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
      //Test Print
      String pFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         pFile= read.readPathInfo().get("payload");
      }
      this.initializePayload(pFile);
   }

   //
   //
   //
   public void initializeData(String key, Object data){}

   //
   //
   //
   public Object initialized(){
      return this._payloadData;
   }
}
//////////////////////////////////////////////////////////////////////
