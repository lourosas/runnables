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

public class RocketInitializable implements Initializable{
   private RocketData      _rocketData;
   private List<StageData> _list;
   
   {
      _rocketData = null;
      _list       = null;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public RocketInitializable(){}

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private double getEmptyWeight(Hashtable<String,String> ht){
      double emptyWeight = Double.NaN;
      try{
         emptyWeight = Double.parseDouble(ht.get("empty_weight"));
      }
      catch(NumberFormatException nfe){
         emptyWeight = Double.NaN;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         emptyWeight = Double.NaN;
      }
      return emptyWeight;
   }

   //
   //
   //
   private double getLoadedWeight(Hashtable<String,String> ht){
      double loadedWeight = Double.NaN;
      try{
         loadedWeight = Double.parseDouble(ht.get("loaded_weight"));
      }
      catch(NumberFormatException nfe){
         loadedWeight = Double.NaN;
      }
      catch(NullPointerException  npe){
         npe.printStackTrace();
         loadedWeight = Double.NaN;
      }
      return loadedWeight;
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
   private int getNumberOfStages(Hashtable<String,String> ht){
      int stages = -1;
      try{
         stages = Integer.parseInt(ht.get("stages"));
      }
      catch(NumberFormatException nfe){
         stages = -1;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         stages = -1;
      }
      return stages;
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
      catch(NullPointerException  npe){
         npe.printStackTrace();
         tolerance = Double.NaN;
      }
      return tolerance;
   }

   //
   //
   //
   private void initializePayloadData(Object data){
      PayloadData pd = null;
      try{
         pd = (PayloadData)data;
      }
      catch(ClassCastException cce){}
      double  calcWeight   = this._rocketData.calculatedWeight();
      int     currStage    = this._rocketData.currentStage();
      String  error        = this._rocketData.error();
      double  emptyWeight  = this._rocketData.emptyWeight();
      boolean isError      = this._rocketData.isError();
      double  loadedWeight = this._rocketData.loadedWeight();
      String  model        = this._rocketData.model();
      int     stages       = this._rocketData.numberOfStages();
      List<StageData> sd   = this._rocketData.stages();
      double  tolerance    = this._rocketData.tolerance();
      RocketData rd = new GenericRocketData(model,
                                            currStage,
                                            stages,
                                            emptyWeight,
                                            loadedWeight,
                                            calcWeight,
                                            isError,
                                            error,
                                            pd,
                                            sd,
                                            tolerance);
      this._rocketData = rd;
   }

   //
   //
   //
   private void initializeRocket(String file)throws IOException{
      //Test Print (for now)
      System.out.println("initializeRocket(): "+file);
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      Hashtable<String,String> ht = read.readRocketInfo();
      String model     = this.getModel(ht);
      int numStages    = this.getNumberOfStages(ht);
      double empWeight = this.getEmptyWeight(ht);
      double lddWeight = this.getLoadedWeight(ht);
      double tolerance = this.getTolerance(ht);
      this._rocketData = new GenericRocketData(
                                    model,
                                    1,         //Current Stage
                                    numStages, //Number of Stages
                                    empWeight, //Empty Weight
                                    lddWeight, //Loaded Weight
                                    Double.NaN,//Calculated Weight
                                    false,     //Is Error
                                    null,      //Error String
                                    null,      //Payload Data
                                    null,      //Stage(s) Data
                                    tolerance);//Tolerance
   }

   //
   //
   //
   private void initializeStageData(Object data){
      StageData sd = null;
      try{
         sd = (StageData)data;
      }
      catch(ClassCastException   cce){}
      try{
         this._list.add(sd);
      }
      catch(NullPointerException npe){
         this._list = new LinkedList<StageData>();
         this._list.add(sd);
      }
      double  calcWeight   = this._rocketData.calculatedWeight();
      int     currStage    = this._rocketData.currentStage();
      String  error        = this._rocketData.error();
      double  emptyWeight  = this._rocketData.emptyWeight();
      boolean isError      = this._rocketData.isError();
      double  loadedWeight = this._rocketData.loadedWeight();
      String  model        = this._rocketData.model();
      int     stages       = this._rocketData.numberOfStages();
      PayloadData pd       = this._rocketData.payloadData();
      double  tolerance    = this._rocketData.tolerance();
      RocketData rd = new GenericRocketData(model,
                                            currStage,
                                            stages,
                                            emptyWeight,
                                            loadedWeight,
                                            calcWeight,
                                            isError,
                                            error,
                                            pd,
                                            this._list,
                                            tolerance);
      this._rocketData = rd;
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
         throw ioe;
      }
      catch(NullPointerException npe){
         isPath = false;
      }
      return isPath;
   }

   ///////////////Initializable Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      //Test Print (for now)
      System.out.println("RocketInitializable:  "+file);
      String rFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         rFile = read.readPathInfo().get("rocket");
      }
      this.initializeRocket(rFile);
   }

   //
   //
   //
   public void initializeData(String key, Object data){
      if(key.toUpperCase().contains("STAGE")){
         this.initializeStageData(data);
      }
      else if(key.toUpperCase().contains("PAYLOAD")){
         this.initializePayloadData(data);
      }
      else if(key.toUpperCase().contains("CAPSULE")){
         this.initializePayloadData(data);
      }
   }

   //
   //
   //
   public Object initialized(){
      return this._rocketData;
   }
}
//////////////////////////////////////////////////////////////////////
