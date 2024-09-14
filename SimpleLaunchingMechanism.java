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

public class SimpleLaunchingMechanism implements LaunchingMechanism,
Runnable{
   //This should change based on file input
   private int                    _numberOfSupports;
   private List<MechanismSupport> _supports;
   private double                 _rocketWeight;
   private Thread                 _t0;
   private boolean                _kill;

   {
      _kill             = false;
      _numberOfSupports = -1;//again, should change based in input
      _supports         = null;
      _rocketWeight     = -1;
      _t0               = null;
   };

   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   public SimpleLaunchingMechanism(double rocketWeight){
      this(4, rocketWeight);
   }

   //
   //
   //
   public SimpleLaunchingMechanism(int supports, double rocketWeight){
      this._numberOfSupports = supports;
      this._rocketWeight     = rocketWeight;
      this.setUpSupports();
      this.setUpThread();
   }

   //////////////////////////Public Methods///////////////////////////
   /////////////////////////Private Methods///////////////////////////
   /**/
   private void setUpThread(){
      this._t0 = new Thread(this, "Launching Mechanism");
      this._t0.start();
   }

   /*
    *Will need to request weight measurements from Mechanism Supports
    * */
   private double totalWeight(){
      double totalWeight = 0.;
      return totalWeight;
   }

   /**/
   private void setUpSupports(){
      int supports = this._numberOfSupports;
      this._supports = new LinkedList<MechanismSupport>();
      for(int i = 0; i < supports; ++i){
         double weight = this._rocketWeight/supports;
         this._supports.add(new SimpleMechanismSupport(i,weight));
      }
   }

   /////////////LaunchingMechanism Interface Implementation///////////
   //
   //
   //
   public List<LaunchingMechanismData> monitorPrelaunch(){
      List<LaunchingMechanismData> data = null;
      try{
         //this will now need to change...
         data = new LinkedList<LaunchingMechanismData>();
         //Get the Prelaunch Data from all the supports...
         for(int i = 0; i < this._supports.size(); ++i){
            MechanismSupport support = this._supports.get(i);
            //Do a test print for now...
            data.add(support.monitorPrelaunch());
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

   //////////////////Runnable Interface Implementation////////////////
   //
   //
   //
   public void run(){
      //Monitor in its own separate Thread...
      int printCounter       = 0;
      try{
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            System.out.println(Thread.currentThread());
            System.out.println(this._t0.getName());
            System.out.println(this._t0.getId());
            Thread.sleep(5000);//This will change to like 10^-3 secs
         }
      }
      catch(InterruptedException ie){}
   }

}
//////////////////////////////////////////////////////////////////////

