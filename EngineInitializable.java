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

public class EngineInitializable implements Initializable{
   private int        _engine;
   private int        _stage;
   private EngineData _engineData;

   {
      _engine     = -1;
      _stage      = -1;
      _engineData = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public EngineInitializable(int engine, int stage){
      if(engine > -1){ this._engine = engine; }
      if(stage  > -1){ this._stage  = stage;  }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private Hashtable<String,String> getEngineHashtable
   (
      List<Hashtable<String,String>> lst
   ){
      Hashtable<String,String> ht = null;
      Iterator<Hashtable<String,String>> it = lst.iterator();
      try{
         int engine    = this._engine;
         boolean found = false;
         while(it.hasNext() && !found){
            Hashtable<String,String> temp = it.next();
            int stage  = -1;
            int total  = -1;
            stage = this.getStage(temp);
            //Create a method to get this data--replace!!!
            total = this.getTotalEngines(temp);
            if(stage == this._stage && engine <= total){
               ht = temp;
               found = true;
            }
            else if(engine > total){
               engine -= total;
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
   private double getExhaustRate(Hashtable<String,String> ht){
      double exhaustRate = Double.NaN;
      try{
         exhaustRate = Double.parseDouble(ht.get("exhaust_flow"));
      }
      catch(NumberFormatException nfe){
         exhaustRate = Double.NaN;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         exhaustRate = Double.NaN;
      }
      return exhaustRate;
   }

   //
   //
   //
   private double getFuelFlowRate(Hashtable<String,String> ht){
      double fuelFlowRate = Double.NaN;
      try{
         fuelFlowRate = Double.parseDouble(ht.get("fuel_flow"));
      }
      catch(NumberFormatException nfe){
         fuelFlowRate = Double.NaN;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         fuelFlowRate = Double.NaN;
      }
      return fuelFlowRate;
   }

   //
   //
   //
   private long getModel(Hashtable<String,String> ht){
      long model = -1;
      try{
         model = Long.parseLong(ht.get("model"),16);
      }
      catch(NumberFormatException nfe){
         model = -1;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         model = -1;
      }
      return model;
   }

   //
   //
   //
   private int getStage(Hashtable<String,String> ht){
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
   private double getTemperature(Hashtable<String,String> ht){
      double temperature = Double.NaN;
      try{
         temperature = Double.parseDouble(ht.get("temperature"));
      }
      catch(NumberFormatException npe){
         temperature = Double.NaN;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         temperature = Double.NaN;
      }
      return temperature;
   }

   //
   //
   //
   private double getTolerance(Hashtable<String,String> ht){
      double tolerance = Double.NaN;
      try{
         tolerance = Double.parseDouble(ht.get("tolerance"));
      }
      catch(NumberFormatException nfe){
         tolerance = Double.NaN;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         tolerance = Double.NaN;
      }
      return tolerance;
   }

   //
   //
   //
   private int getTotalEngines(Hashtable<String,String> ht){
      int totalEngines = -1;
      try{
         totalEngines = Integer.parseInt(ht.get("total"));
      }
      catch(NumberFormatException nfe){
         totalEngines = -1;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         totalEngines = -1;
      }
      return totalEngines;
   }

   //
   //
   //
   private void initializeEngine(String file)throws IOException{
      EngineData ed = null;
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      List<Hashtable<String,String>> lst = read.readEngineDataInfo();
      Iterator<Hashtable<String,String>> it = lst.iterator();
      Hashtable<String,String> ht = this.getEngineHashtable(lst);
      int    eng   = this._engine;
      double exh   = this.getExhaustRate(ht);
      double ffl   = this.getFuelFlowRate(ht);
      long   mdl   = this.getModel(ht);
      int    stg   = this._stage;
      double tmp   = this.getTemperature(ht);
      double tol   = this.getTolerance(ht);
      int    tot   = this.getTotalEngines(ht);
      ed = new GenericEngineData(eng,   //Engine Number
                                 null,  //Error
                                 exh,   //Exhaust Rate
                                 false, //Is Error
                                 false, //Is Ignited
                                 ffl,   //Fuel Flow
                                 mdl,   //Model
                                 stg,   //Stage
                                 tmp,   //Temperature
                                 tol,   //Tolerance
                                 tot);  //Total Engines
      this._engineData = ed;
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
      return  isPath;
   }

   //////////////Initializeable Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      System.out.println("Engine Initializable");
      String eFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         eFile = read.readPathInfo().get("engine");
      }
      this.initializeEngine(eFile);
   
   }

   //
   //
   //
   public void initializeData(String key, Object data){}

   //
   //
   //
   public Object initialized(){
      return this._engineData;
   }
}
//////////////////////////////////////////////////////////////////////
