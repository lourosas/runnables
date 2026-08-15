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
   private void initializeLaunchPlatform(String file)
   throws IOException{
      //Test print (for now)
      System.out.println("Initialize Launch Platform: "+file);
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      Hashtable<String,String> ht = read.readLaunchingMechanismInfo();
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
   public void initializeData(String key, Object data){}

   //
   //
   //
   public Object initialized(){
      return this._launchPlatformData;
   }
}
//////////////////////////////////////////////////////////////////////
