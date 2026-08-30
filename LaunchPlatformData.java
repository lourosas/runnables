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

import java.util.*;
import java.lang.*;
import rosas.lou.runnables.*;

public abstract class LaunchPlatformData{
   private String                    _error;
   private boolean                   _isError;
   private int                       _holds;
   private double                    _measuredWeight;
   private String                    _model;
   private double                    _tolerance;
   private List<LaunchMechanismData> _mechanisms;
   
   {
      _error          = null;
      _isError        = false;
      _holds          = -1;
      _measuredWeight = Double.NaN;
      _model          = null;
      _tolerance      = Double.NaN;
      _mechanisms     = null;
   };

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public String error(){
      return this._error;
   }

   //
   //
   //
   public boolean isError(){
      return this._isError;
   }

   //
   //
   //
   public int holds(){
      return this._holds;
   }

   //
   //
   //
   public double measuredWeight(){
      return this._measuredWeight;
   }

   //
   //
   //
   public List<LaunchMechanismData> mechanisms(){
      return this._mechanisms;
   }

   //
   //
   //
   public String model(){
      return this._model;
   }

   //
   //
   //
   public double tolerance(){
      return this._tolerance;
   }

   //
   //
   //
   public String toString(){
      String data = new String("\nLaunch Platform Data");
      data += "\n--------------------------------------";
      data += "\nModel:            "+this.model();
      data += "\nHolds:            "+this.holds();
      data += "\nMeasured Weight:  "+this.measuredWeight();
      data += "\nTolerance:        "+this.tolerance();
      data += "\nIs Error:         "+this.isError();
      data += "\nError:            "+this.error();
      try{
         Iterator<LaunchMechanismData> it=this._mechanisms.iterator();
         while(it.hasNext()){ data += "\n"+it.next().toString(); }
      }
      catch(NullPointerException npe){data += "\n"+npe.getMessage();}
      return data;
   }

   /////////////////////////Protected Methods/////////////////////////
   //
   //
   //
   protected void error(String err){
         this._error = err;
   }

   //
   //
   //
   protected void errors(){
      try{
         Iterator<LaunchMechanismData> it=this._mechanisms.iterator();
         while(it.hasNext()){
            LaunchMechanismData lmd = (LaunchMechanismData)it.next();
            if(lmd.isError()){
               if(!this.isError()){
                  this.isError(true);
               }
               if(this.error() == null){
                  this.error(lmd.error());
               }
               else{ 
                  this.error(this.error()+" "+lmd.error());
               }
            }
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   protected void isError(boolean isE){
      this._isError = isE;
   }

   //
   //
   //
   protected void holds(int hlds){
      if(hlds > 0){
         this._holds = hlds;
      }
   }

   //
   //
   //
   protected void measuredWeight(double measWgt){
      if(measWgt > 0.){
         this._measuredWeight = measWgt;
      }
   }

   //
   //
   //
   protected void mechanisms(List<LaunchMechanismData> mechs){
         this._mechanisms = mechs;
   }

   //
   //
   //
   protected void model(String mdl){
         this._model = mdl;
   }

   //
   //
   //
   protected void tolerance(double tol){
      if(tol > 0.){
         this._tolerance = tol;
      }
   }
}

//////////////////////////////////////////////////////////////////////
