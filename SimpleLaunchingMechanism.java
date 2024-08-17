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

public class SimpleLaunchingMechanism implements LaunchingMechanism{
   //This should change based on file input
   private int                    _numberOfSupports;
   private List<MechanismSupport> _supports;

   {
      _numberOfSupports = -1;//again, should change based in input
      _supports         = null;
   };

   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   public SimpleLaunchingMechanism(){
      this(4);
   }

   //
   //
   //
   public SimpleLaunchingMechanism(int supports){
      this._numberOfSupports = supports;
      this.setUpSupports();
   }

   //////////////////////////Public Methods///////////////////////////
   /////////////////////////Private Methods///////////////////////////
   /**/
   private void setUpSupports(){
      this._supports = new LinkedList<MechanismSupport>();
      for(int i = 0; i < this._numberOfSupports; ++i){
         this._supports.add(new SimpleMechanismSupport(i));
      }
   }

   /*
    *Will need to request weight measurements from Mechanism Supports
    * */
   private double totalWeight(){
      double totalWeight = 0.;
      return totalWeight;
   }

   /////////////LaunchingMechanism Interface Implementation///////////
   //
   //
   //
   public List<LaunchingMechanismData> monitorPrelaunch(){
      List<LaunchingMechanismData> data = null;
      try{
         data = new LinkedList<LaunchingMechanismData>();
         //Get the Prelaunch Data from all the supports...
         for(int i = 0; i < this._supports.size(); ++i){
            MechanismSupport s = this._supports.get(i);
            data.add(s.monitorPrelaunch());
         }
         
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         data = null;
      }
      finally{
         return data;
      }
   }

   //
   //
   //
   public List<LaunchingMechanismData> monitorIgnition(){
      return null;
   }

   //
   //
   //
   public List<LaunchingMechanismData> monitorLaunch(){
      return null;
   }

   //
   //
   //
   public void release(){}
}
//////////////////////////////////////////////////////////////////////

