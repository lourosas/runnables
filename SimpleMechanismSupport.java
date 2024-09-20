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
   private int     _id            = -1;
   private double  _supportWeight = 0.;
   private String  _error         = null;
   private boolean _isError       = true;

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public SimpleMechanismSupport(int id, double weight){
      this._id            = id;
      this._supportWeight = weight;
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
      return this._error;
   }

   //This is where you "measure" the force
   //
   //
   private double force(){
      //For the time being, just return the _supportWeight
      //this will need to change to add "Reality" to the Simulation
      return this._supportWeight;
   }

   //"Measure" the force vector
   //
   //
   private ForceVector direction(){
      //Again, very simple!  Just construct a SimpleForceVector and 
      //put the vector at -z and x=y=0 (all force is straight down)
      //THIS WILL CHANGE!!!
      double force = (-1)*this._supportWeight;
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
   public LaunchingMechanismData monitorPrelaunch(){
      double force  = this.force();
      ForceVector v = this.direction();
      //determine error and get error string if error
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
}
//////////////////////////////////////////////////////////////////////
