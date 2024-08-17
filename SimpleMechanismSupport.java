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
   private int _id = -1;

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public SimpleMechanismSupport(int id){
      this._id = id;
   }

   /////////////////////////Public Methods////////////////////////////
   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private ForceVector direction(){
      return null;
   }

   //
   //
   //
   private boolean isError(){
      return true;
   }

   //
   //
   //
   private String findError(){
      return new String("Error");
   }

   //
   //
   //
   private double force(){
      double force = 0.;
      return force;
   }

   /////////////////MechanismSupport Implementation///////////////////
   //
   //
   //
   public LaunchingMechanismData monitorPrelaunch(){
      ForceVector vec = null; //Need to create concrete classes
      boolean error   = this.isError();
      double  force   = this.force();
      String  reason  = this.findError();

      return new SimpleLaunchingMechanismData(vec,
                                              force,
                                              this.id(),
                                              error,
                                              reason);
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
   public int id(){
      return this._id;
   }

   //
   //
   //
   public String toString(){
      String s = this.getClass().getName() + ": " + this.id();
      return s;
   }
}
//////////////////////////////////////////////////////////////////////
