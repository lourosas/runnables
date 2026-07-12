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

public class TankInitializable implements Initializable{
   private int      _stage;
   private int      _number;
   private TankData _tankData;

   {
      _stage    = -1;
      _number   = -1;
      _tankData = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TankInitializable(int stage, int number){
      if(stage > 0){  this._stage  = stage;  }
      if(number > 0){ this._number = number; }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private double getCapacity(Hashtable<String,String> ht){
      double capacity = Double.NaN;
      try{
         capacity = Double.parseDouble(ht.get("capacity"));
      }
      catch(NumberFormatException nfe){
         capacity = Double.NaN;
      }
      catch(NullPointerException npe){
        npe.printStackTrace();
        capacity = Double.NaN;
      }
      return capacity;
   }

   //
   //
   //
   private double getDensity(Hashtable<String,String> ht){
      double density = Double.NaN;
      try{
         density = Double.parseDouble(ht.get("density"));
      }
      catch(NumberFormatException nfe){
         density = Double.NaN;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         density = Double.NaN;
      }
      return density;
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
      catch(NullPointerException  npe){
         npe.printStackTrace();
         dryWeight = Double.NaN;
      }
      return dryWeight;
   }

   //
   //
   //
   private double getEmptyRate(Hashtable<String,String> ht){
      double emptyRate = Double.NaN;
      try{
         emptyRate = Double.parseDouble(ht.get("rate"));
      }
      catch(NumberFormatException nfe){
         emptyRate = Double.NaN;
      }
      catch(NullPointerException  npe){
         npe.printStackTrace();
         emptyRate = Double.NaN;
      }
      return emptyRate;
   }

   //
   //
   //
   private String getFuelType(Hashtable<String,String> ht){
      String fuelType = null;
      try{
         fuelType = ht.get("fuel");
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         fuelType = null;
      }
      return fuelType;
   }

   //
   //
   //
   private long getModelNumber(Hashtable<String,String> ht){
      long model = -1;
      try{
         model = Long.parseLong(ht.get("model"),16);
      }
      catch(NumberFormatException nfe){
         model = -1;
      }
      catch(NullPointerException  npe){
         npe.printStackTrace();
         model = -1;
      }
      return model;
   }

   //
   //
   //
   private int getHashtableNumber(Hashtable<String,String> ht){
      int number = -1;
      try{
         number = Integer.parseInt(ht.get("number"));
      }
      catch(NumberFormatException nfe){
         number = -1;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         number = -1;
      }
      return number;
   }

   //
   //
   //
   private int getHashtableStage(Hashtable<String,String> ht){
      int stage = -1;
      try{
         stage = Integer.parseInt(ht.get("stage"));
      }
      catch(NumberFormatException nfe){
         stage = -1;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         stage = -1;
      }
      return stage;
   }

   //
   //
   //
   private Hashtable<String,String> getTankHashtable
   (
      List<Hashtable<String,String>> lst
   ){
      Hashtable<String,String> ht = null;
      Iterator<Hashtable<String,String>> it = lst.iterator();
      try{
         boolean found = false;
         while(it.hasNext() && !found){
            Hashtable<String,String> temp = it.next();
            int stage  = this.getHashtableStage(temp);
            int number = this.getHashtableNumber(temp);
            if(stage == this._stage && number == this._number){
               ht = temp;
               found = true;
            }
         }
      }
      catch(NullPointerException npe){
         ht = null;
      }

      return ht;
   }

   //
   //
   //
   private double getTemperature(Hashtable<String,String> ht){
      double temperature = Double.NaN;
      try{
         temperature = Double.parseDouble(ht.get("temperature"));
      }
      catch(NumberFormatException nfe){
         temperature = Double.NaN;
      }
      catch(NullPointerException  npe){
         npe.printStackTrace();
         temperature = Double.NaN;
      }
      return temperature;
   }

   private double getTolerance(Hashtable<String,String> ht){
      double tolerance = Double.NaN;
      try{
         tolerance = Double.parseDouble(ht.get("tolerance"));
      }
      catch(NumberFormatException nfe){
         tolerance = Double.NaN;
      }
      catch(NullPointerException  npe){
         npe.printStackTrace();
         tolerance = Double.NaN;
      }
      return tolerance;
   }

   //
   //
   //
   private void initializeTank(String file)throws IOException{
      TankData td = null;
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      List<Hashtable<String,String>> lst = read.readTankDataInfo();
      Hashtable<String,String> ht = this.getTankHashtable(lst);
      double cap = this.getCapacity(ht);
      double den = this.getDensity(ht);
      double dw  = this.getDryWeight(ht);
      double er  = this.getEmptyRate(ht);
      String fu  = this.getFuelType(ht);
      double mlr = Double.NaN;  //Mass Loss Rate
      long   mod = this.getModelNumber(ht);
      int    num = this._number;
      int    stg = this._stage;
      double temp= this.getTemperature(ht);
      double tol = this.getTolerance(ht);
      double wgt = Double.NaN; //Measured Weight
      td = new GenericTankData(cap,  //Capacity
                               den,  //Density
                               dw,   //Dry Weight
                               er,   //Empty Rate
                               null, //Error String
                               fu,   //Fuel Type
                               false,//Is Error
                               mlr,  //Mass Loss Rate
                               mod,  //Model
                               num,  //Number
                               stg,  //Stage
                               temp, //Temperature
                               tol,  //Tolerance
                               wgt); //Weight
      this._tankData = td;
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
      System.out.println("Tank Initializable");
      String tFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         tFile = read.readPathInfo().get("tank");
      }
      this.initializeTank(tFile);
   }

   //
   //
   //
   public void initializeData(String key, Object data){}

   //
   //
   //
   public Object initialized(){
      return this._tankData;
   }
}
//////////////////////////////////////////////////////////////////////
