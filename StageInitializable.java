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

public class StageInitializable implements Initializable{
   private List<EngineData> _list;
   private int              _stage;
   private StageData        _stageData;

   {
      _list     = null;
      _stage    = -1;
      _stageData = null;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public StageInitializable(int stage){
      if(stage > 0){
         this._stage = stage;
      }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private double getDryMass(Hashtable<String,String> ht){
      double drymass = Double.NaN;
      try{
        drymass = Double.parseDouble(ht.get("emptymass"));
      }
      catch(NumberFormatException npe){
         drymass = Double.NaN;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         drymass = Double.NaN;
      }
      return drymass;
   }

   //
   //
   //
   private double getDryWeight(Hashtable<String,String> ht){
      double dryweight = Double.NaN;
      try{
         dryweight = Double.parseDouble(ht.get("dryweight"));
      }
      catch(NumberFormatException nfe){
         dryweight = Double.NaN;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         dryweight = Double.NaN;
      }
      return dryweight;
   }

   //
   //
   //
   private double getLoadedMass(Hashtable<String,String> ht){
      double loadedMass = Double.NaN;
      try{
         loadedMass = Double.parseDouble(ht.get("loadedmass"));
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
      catch(NumberFormatException nfe){
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
   private int getNumberOfEngines(Hashtable<String,String> ht){
      int engines = -1;
      try{
         engines = Integer.parseInt(ht.get("engines"));
      }
      catch(NumberFormatException nfe){
         engines = -1;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         engines = -1;
      }
      return engines;
   }

   //
   //
   //
   private Hashtable<String,String> getStageData
   (
      List<Hashtable<String,String>> lst
   ){
      Hashtable<String,String> ht = null;
      Iterator<Hashtable<String,String>> it = lst.iterator();
      try{
         while(it.hasNext()){
            Hashtable<String,String> temp = it.next();
            String sstg = temp.get("number");
            int    stg  = Integer.parseInt(sstg);
            if(stg == this._stage){
               ht = temp;
            }
         }
      }
      catch(NullPointerException npe){
         ht = null;
      }
      catch(NumberFormatException nfe){
         ht = null;
      }
      return ht;
      
   }

   //
   //
   //
   private void initializeEngine(Object data){
      //Needs to change to check for Stage and Number!!!
      EngineData ed = null;
      try{
         ed = (EngineData)data;
      }
      catch(ClassCastException cce){
         ed = null;
      }
      //Make sure the EngineData added is for the correct stage
      //And NOT already added (must be a different Engine)
      if((ed != null) && (this._stage == ed.stage())){
         try{
            boolean inList = false;
            Iterator<EngineData> it = this._list.iterator();
            while(it.hasNext()){
               EngineData ed2 = it.next();
               if(ed.engine() == ed2.engine()){
                  inList = true;
               }
            }
            if(!inList){
               this._list.add(ed);
            }
         }
         catch(NullPointerException npe){
            this._list = new LinkedList<EngineData>();
            this._list.add(ed);
         }
      }
      double   dw    = this._stageData.dryWeight();
      double   dm    = this._stageData.dryMass();
      String   err   = this._stageData.error();
      long     mdl   = this._stageData.model();
      boolean  isE   = this._stageData.isError();
      int      sn    = this._stage;
      int      egs   = this._stageData.numberOfEngines();
      double   lm    = this._stageData.loadedMass();
      double   mw    = this._stageData.maxWeight();
      double   tol   = this._stageData.tolerance();
      double   wgt   = this._stageData.weight();

      FuelSystemData fsd = this._stageData.fuelSystemData();
      StageData sd = new GenericStageData(dw,
                                          dm,
                                          err,
                                          mdl,
                                          isE,
                                          sn,
                                          egs,
                                          lm,
                                          mw,
                                          tol,
                                          wgt,
                                          this._list,
                                          fsd);
      this._stageData = sd;
   }

   //
   //
   //
   private void initializeFuelSystem(Object data){
      FuelSystemData fsd = null;
      try{
         fsd = (FuelSystemData)data;
      }
      catch(ClassCastException cce){
         fsd = null;
      }
      double           dw = this._stageData.dryWeight();
      double           dm = this._stageData.dryMass();
      String          err = this._stageData.error();
      long            mdl = this._stageData.model();
      boolean         isE = this._stageData.isError();
      int              sn = this._stage;
      int             egs = this._stageData.numberOfEngines();
      double           lm = this._stageData.loadedMass();
      double           mw = this._stageData.maxWeight();
      double          tol = this._stageData.tolerance();
      double          wgt = this._stageData.weight();
      List<EngineData> ed = this._stageData.engineData();
      StageData sd = new GenericStageData(dw,
                                          dm,
                                          err,
                                          mdl,
                                          isE,
                                          sn,
                                          egs,
                                          lm,
                                          mw,
                                          tol,
                                          wgt,
                                          ed,
                                          fsd);
      this._stageData = sd;
   }

   //
   //
   //
   private void initializeStage(String file)throws IOException{
      StageData sd = null;
      //Test Print (for now)
      System.out.println("initializeStage");
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      List<Hashtable<String,String>> lst = read.readStageInfo();
      Hashtable<String,String> ht = this.getStageData(lst);
      int      eng = this.getNumberOfEngines(ht);
      double   dw  = this.getDryWeight(ht);
      double   dm  = this.getDryMass(ht); 
      int      stg = this._stage;
      double   lm  = this.getLoadedMass(ht);
      double   mw  = this.getMaxWeight(ht);
      long     mdl = this.getModel(ht);
      double   tol = this.getTolerance(ht);
      double   wgt = Double.NaN;
      sd = new GenericStageData(dw,      //dry weight
                                dm,      //dry mass
                                null,    //error
                                mdl,     //model
                                false,   //isError
                                stg,     //Stage Number
                                eng,     //number of engines
                                lm,      //loaded mass
                                mw,      //max weight
                                tol,     //Tolerance
                                wgt,     //Meas. Weight
                                null,    //Engine Data
                                null);   //Fuel System Data
      this._stageData = sd;
   }

   //
   //
   //
   private boolean isPathFile(String file) throws IOException{
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
      System.out.println("StageInitializable");
      String sFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         sFile = read.readPathInfo().get("stage");
      }
      this.initializeStage(sFile);
   }

   //
   //
   //
   public void initializeData(String key, Object data){
      if(key.toUpperCase().contains("ENGINE DATA")){
         this.initializeEngine(data);
      }
      else if(key.toUpperCase().contains("FUEL DATA")){
         this.initializeFuelSystem(data);
      }
   }

   //
   //
   //
   public Object initialized(){
      return this._stageData;
   }
}
//////////////////////////////////////////////////////////////////////
