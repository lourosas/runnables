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

public class GenericMechanismSupport implements MechanismSupport{
   private double         _angle; //In Radians!!!
   private int            _id;
   //What to measure periodically
   private double         _measuredWeight;
   //Depends on the number of holds
   private double         _calculatedWeight;
   private String         _error;
   private boolean        _isError;
   private double         _tollerance;
   private ForceVector    _vector;

   {
      _angle            = Double.NaN;
      _id               = -1;
      _measuredWeight   = Double.NaN;
      _calculatedWeight = Double.NaN;
      _error            = null;
      _isError          = false;
      _tollerance       = Double.NaN;
      _vector           = null;
   };

   ////////////////////////Contructors////////////////////////////////
   //Pretty much all you will need...
   //
   //
   public GenericMechanismSupport(int id){
      this._id = id;
   }

   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private void data(Hashtable<String,String> rocket,
                Hashtable<String,String> mech){
      int holds = -1;
      if(mech.containsKey("number_of_holds")){
         try{
            String sHolds = mech.get("number_of_holds");
            holds = Integer.parseInt(sHolds);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }
      if(mech.containsKey("angle_of_holds")){
         try{
            String sAoHolds = mech.get("angle_of_holds");
            double degHolds = Double.parseDouble(sAoHolds);
            //Convert to Radians
            this._angle = ((Math.PI)/180.0 * degHolds);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }
      if(mech.containsKey("holds_tollerance")){
         try{
            String sToll = mech.get("holds_tollerance");
            this._tollerance = Double.parseDouble(sToll);
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }
      if(rocket.containsKey("loaded_weight")){
         try{
            String sWeight = rocket.get("loaded_weight");
            double weight = Double.parseDouble(sWeight);
            weight /= holds;
            weight /= Math.sin(this._angle);
            //This is the weight that is calculated based on
            //initialization data...
            this._calculatedWeight = weight;
         }
         catch(NumberFormatException nfe){}
         catch(NullPointerException  npe){}
      }
   }

   //Initialze the ForceVector
   //
   //
   private void initializeForceVector(){
      double x = Double.NaN;
      double y = Double.NaN;
      double z = Double.NaN;
      if(this.id() == 0){
         x = this._calculatedWeight*Math.cos(this._angle);
         y = 0.;
      }
      else if(this.id() == 1){
         x = 0.;
         y = this._calculatedWeight*Math.cos(this._angle);
      }
      else if(this.id() == 2){
         x = (-1.)*this._calculatedWeight*Math.cos(this._angle);
         y = 0;
      }
      else if(this.id() == 3){
         x = 0.;
         y = (-1.)*this._calculatedWeight*Math.cos(this._angle);
      }
      else{}
      z = (-1.)*this._calculatedWeight*Math.sin(this._angle);
      this._vector = new GenericForceVector(x,y,z);
      //System.out.println("\n"+this.toString());
   }
   //////////////MechanismSupport Interface Implementation////////////
   //
   //
   //
   public int id(){
      return this._id;
   }

   //
   //
   //
   public void initialize(String file){
      if(file.toUpperCase().contains("INI")){}
      else if(file.toUpperCase().contains("JSON")){
         try{
            LaunchSimulatorJsonFileReader read = null;
            read = new LaunchSimulatorJsonFileReader(file);
            Hashtable<String,String> rocketHt = null;
            rocketHt = read.readRocketInfo();
            Hashtable<String,String> mechHt = null;
            mechHt = read.readLaunchingMechanismInfo();
            this.data(rocketHt, mechHt);
            this.initializeForceVector();
         }
         catch(IOException ioe){}
      }
   }

   //
   //
   //
   public LaunchingMechanismData monitorPrelaunch(){
      return null;
   }

   //
   //
   //
   public LaunchingMechanismData monitorIgnition(){
      return null;
   }

   //
   //
   //
   public LaunchingMechanismData monitorLaunch(){
      return null;
   }

   //
   //
   //
   public String toString(){
      String string = this.getClass().getName()+" : "+this.id();
      string += "\n"+this._angle;
      string += "\n"+this._calculatedWeight+", "+this._measuredWeight;
      string += "\n"+this._isError+", "+this._error;
      string += "\n"+this._tollerance+"\n"+this._vector;
      return string;
   }
}
//////////////////////////////////////////////////////////////////////
