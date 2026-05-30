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

public class StageInitializable implements Initializable{
   private int       _stage;
   private StageData _stageData;

   {
      _stage    = -1;
      _stageData = null;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public StageInitializable(int stage){
      this._stage = stage;
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private Hashtable<String,String> getStageData
   (
      List<Hashtable<String,String>> lst
   ){
      Hashtable<String,String> ht = null;
      Iterator<Hashtable<String,String>> it = lst.iterator();
      try{
         while(it.hasNext()){
            Hashtable<String,String> temp = it.next();
            String sstg = temp.get("number");
            int    stg  = Integer.parseInt(sstg);
            if(stg == this._stage){
               ht = temp;
            }
         }
      }
      catch(NullPointerException npe){
         ht = null;
      }
      catch(NumberFormatException nfe){
         ht = null;
      }
      return ht;
      
   }
   //
   //
   //
   private void initializeStage(String file)throws IOException{
      //Test Print (for now)
      System.out.println("initializeStage");
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      List<Hashtable<String,String>> lst = read.readStageInfo();
      Hashtable<String,String> ht = this.getStageData(lst);
   }

   //
   //
   //
   private boolean isPathFile(String file) throws IOException{
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
      }
      catch(NullPointerException npe){
         isPath = false;
      }
      return isPath;
   }

   //////////////Initializable Interface Implementation///////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      System.out.println("StageInitializable");
      String sFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         sFile = read.readPathInfo().get("stage");
      }
      this.initializeStage(sFile);
   }

   //
   //
   //
   public void initializeData(String key, Object data){}

   //
   //
   //
   public Object initialized(){
      return this._stageData;
   }
}
//////////////////////////////////////////////////////////////////////
