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

public class SimpleMechanismSupport implements MechanismSupport{
   private int          _id;
   private double       _weight;
   private double       _angle; //In Radians
   private String       _error;
   private boolean      _isError;
   private ForceVector  _vector;

   {
      _id       = -1;
      _weight   = Double.NaN;
      _angle    = Double.NaN;
      _error    = null;
      _isError  = true;
      _vector   = null;
   };

   ///////////////////////////Constructors////////////////////////////
   //PROBABLY NO LONGER NEEDED!  EVERYTHING WILL BE SET BY THE
   //ini FILE!!!
   //
   public SimpleMechanismSupport(int id, double weight){
      this._id            = id;
      this._weight = weight;
   }

   /////////////////////////Public Methods////////////////////////////
   //
   //
   //
   public String toString(){
      String s = this.getClass().getName() + ": " + this.id();
      return s;
   }
   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private String error(){
      this._error = null;
      if(this.isError()){
         this._error = new String("There is an Error (TBD)");
      }
      return this._error;
   }

   //This is where you "measure" the force
   //
   //
   private double force(){
      //For the time being, just return the _supportWeight
      //this will need to change to add "Reality" to the Simulation
      return this._weight;
   }

   //"Measure" the force vector
   //
   //
   private ForceVector direction(){
      //Again, very simple!  Just construct a SimpleForceVector and 
      //put the vector at -z and x=y=0 (all force is straight down)
      //THIS WILL CHANGE!!!
      double force = (-1)*this._weight;
      //for the time being, but the force straight down! (Change:TBD)
      SimpleForceVector s = new SimpleForceVector(0,0,force);
      return s;
   }

   //
   //
   //
   private boolean isError(){
      return this._isError;
   }

   /////////////////MechanismSupport Implementation///////////////////
   //
   //
   //
   public int id(){
      return this._id;
   }

   //
   //
   //
   public void initialize(String file){}

   //
   //
   //
   public MechanimsSupportData monitorPrelaunch(){
      /*
      double force   = this.force();
      ForceVector v  = this.direction();

      //determine error and get error string if error
      boolean isError = this.isError();
      String  error   = this.error();

      LaunchingMechanismData lmd = null;
      int id                     = this._id;
      lmd=new SimpleLaunchingMechanismData(v,force,id,isError,error);
      return lmd;
      */
      return null;
   }

   //
   //
   //
   public MechanismSupportData monitorIgnition(){
      return null;
   }

   //
   //
   //
   public MechanismSupportData monitorLaunch(){
      return null;
   }
}
//////////////////////////////////////////////////////////////////////
