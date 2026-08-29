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

public class LaunchMechanismInitializable implements Initializable{
   private LaunchMechanismData _launchMechanismData;
   private int                 _holdNumber;

   {
      _holdNumber          = -1;
      _launchMechanismData = null;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public LaunchMechanismInitializable(int holdNumber){
      if(holdNumber > -1){
         this._holdNumber = holdNumber;
      }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private double getAngle(Hashtable<String,String> ht){
      double angle = Double.NaN;
      try{
         angle = Double.parseDouble(ht.get("angle_of_holds"));
      }
      catch(NumberFormatException nfe){ angle = Double.NaN; }
      catch(NullPointerException npe){
         npe.printStackTrace();
         angle = Double.NaN;
      }
      return angle;
   }

   //
   //
   //
   private double getHoldsTolerance(Hashtable<String,String> ht){
      double tolerance = Double.NaN;
      try{
         tolerance = Double.parseDouble(ht.get("holds_tolerance"));
      }
      catch(NumberFormatException nfe){ tolerance = Double.NaN; }
      catch(NullPointerException npe){
         npe.printStackTrace();
         tolerance = Double.NaN;
      }
      return tolerance;
   }

   //
   //
   //
   private void initializeLaunchMechanism(String file)
   throws IOException{
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      Hashtable<String,String> ht = read.readLaunchingMechanismInfo();
      double  agl   = this.getAngle(ht);
      String  err   = null;
      boolean isE   = false;
      double  mWgt  = Double.NaN;
      int     num   = this._holdNumber;
      double  ten   = Double.NaN;
      double  tol   = this.getHoldsTolerance(ht);
      LaunchMechanismData lmd = null;
      lmd = new GenericLaunchMechanismData(agl, //Hold Angle
                                           err, //Error String
                                           isE, //Current Error
                                           mWgt,//Measured Weight
                                           num, //Hold Number
                                           ten, //Tension
                                           tol);//Tolerance
      this._launchMechanismData = lmd;
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
      //Test Print for now
      System.out.println("LaunchMechanismInitializable");
      String lFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         lFile = read.readPathInfo().get("launching_mechanism");
      }
      this.initializeLaunchMechanism(lFile);
   }

   //
   //
   //
   public void initializeData(String key, Object data){}

   //
   //
   //
   public Object initialized(){
      return this._launchMechanismData;
   }
}
//////////////////////////////////////////////////////////////////////
