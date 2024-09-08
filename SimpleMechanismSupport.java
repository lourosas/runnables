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
   //
   //
   //
   public String toString(){
      String s = this.getClass().getName() + ": " + this.id();
      return s;
   }
   ////////////////////////Private Methods////////////////////////////
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
