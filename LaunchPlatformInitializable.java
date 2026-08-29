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

public class LaunchPlatformInitializable implements Initializable{
   private LaunchPlatformData _launchPlatformData;
   //Should not need the LaunchMechanismData!!

   {
      _launchPlatformData = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public LaunchPlatformInitializable(){}

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private String getModel(Hashtable<String,String> ht){
      String model = null;
      try{
         model = ht.get("model");
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         model = null;
      }
      return model;
   }

   //
   //
   //
   private int getNumberOfHolds(Hashtable<String,String> ht){
      int number = -1;
      try{
         number = Integer.parseInt(ht.get("number_of_holds"));
      }
      catch(NumberFormatException nfe){ number = -1; }
      catch(NullPointerException  npe){
         npe.printStackTrace();
         number = -1;
      }
      return number;
   }

   //
   //
   //
   private double getPlatformTolerance(Hashtable<String,String> ht){
      double tolerance = Double.NaN;
      try{
         String tol = ht.get("total_tolerance");
         tolerance  = Double.parseDouble(tol);
      }
      catch(NumberFormatException nfe){ tolerance = Double.NaN; }
      catch(NullPointerException  npe){
         npe.printStackTrace();
         tolerance = Double.NaN;
      }
      return tolerance;
   }

   //
   //
   //
   private void initializeLaunchPlatform(String file)
   throws IOException{
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      Hashtable<String,String> ht = read.readLaunchingMechanismInfo();
      String   err   = null;
      boolean  isE   = false;
      int     hlds   = this.getNumberOfHolds(ht);
      double  mwgt   = Double.NaN;
      String   mod   = this.getModel(ht);
      double  ptol   = this.getPlatformTolerance(ht);
      LaunchPlatformData lpd = null;
      lpd = new GenericLaunchPlatformData(err,  //Error String
                                          isE,  //Is Error
                                          hlds, //Holdls
                                          mwgt, //Measured Weight
                                          mod,  //Model
                                          ptol, //Tolerance
                                          null);//No Mechanisms
      this._launchPlatformData = lpd;
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
      return  isPath;
   }

   //
   //
   //
   private void setErrorData(Object data){}

   //
   //
   //
   private void setMechanismData(Object data){
      //Test Prints, remove
      System.out.println("Launch Platform Mechanism Data");
      System.out.println(data);
   }

   ///////////////Initializable Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      //Test Print (for now)
      System.out.println("LaunchPlatformInitializable");
      String lFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         lFile = read.readPathInfo().get("launching_mechanism");
      }
      this.initializeLaunchPlatform(lFile);
   }

   //
   //
   //
   public void initializeData(String key, Object data){
      if(key.toUpperCase().contains("MECHANISM")){
         this.setMechanismData(data);
      }
      else if(key.toUpperCase().contains("ERROR")){
         this.setErrorData(data);
      }
   }

   //
   //
   //
   public Object initialized(){
      return this._launchPlatformData;
   }
}
//////////////////////////////////////////////////////////////////////
