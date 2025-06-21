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
import rosas.lou.runnables.*;

public class GenericLaunchingMechanismData
implements LaunchingMechanismData{
   private String                     _error;
   private int                        _holds;
   private boolean                    _isError;
   private double                     _measuredWeight;
   private int                        _model;
   private LaunchStateSubstate        _state;
   private String                     _time;
   private double                     _tolerance;
   private List<MechanismSupportData> _supportData;

   {
      _error            = null;
      _holds            = -1;
      _isError          = false;
      _measuredWeight   = Double.NaN;
      _model            = -1;
      _tolerance        = Double.NaN;
      _state            = null;
      _time             = null;
      _supportData      = null;
   }
   
   /////////////////////////Contructor////////////////////////////////
   //
   //
   //
   public GenericLaunchingMechanismData
   (
      String                       er,
      int                       holds,
      boolean                   iserr,
      double                       mw,
      int                         mdl,
      LaunchStateSubstate       state,
      double                      tol,
      List<MechanismSupportData> data
   ){
      this._error          =    er;
      this._holds          = holds;
      this._isError        = iserr;
      this._measuredWeight =    mw;
      this._model          =   mdl;
      //Time Calculated by Instance...
      //this._time           =  time;
      this._state          = state;
      this._tolerance      =   tol;
      this._supportData    =  data;
   }

   /////////LaunchingMechanismData Interface Implementation///////////
   //
   //
   //
   public String error(){
      return this._error;
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
   public boolean isError(){
      return this._isError;
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
   public int model(){
      return this._model;
   }

   //
   //
   //
   public List<MechanismSupportData> supportData(){
      return this._supportData;
   }

   //
   //
   //
   public LaunchStateSubstate state(){
      return this._state;
   }

   //
   //
   //
   public String time(){
      return this._time;
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
      List<MechanismSupportData> data = this.supportData();
      String string = this.model() + "\n" + this.holds() + "\n";
      string       += this.isError()+":"+this.error() + "\n";
      string       += this.measuredWeight() + "\n";
      string       += this.state() + "\n";
      string       += this.tolerance() + "\n";
      for(int i = 0; i < data.size(); ++i){
         string += data.get(i) + "\n";
      }
      return string;
   }
}
//////////////////////////////////////////////////////////////////////
