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

public class TankInitializable implements Initializable{
   private int      _stage;
   pirvate int      _number;
   private TankData _tankData;

   {
      _stage    = -1;
      _number   = -1;
      _tankData = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TankInitializable(int stage, int number){
      if(stage > 0){  this._stage  = stage;  }
      if(number > 0){ this._number = number; }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private int getHashtableNumber(Hashtable<String,String> ht){
      int number = -1;
      try{
         number = Integer.parseInt(ht.get("number"));
      }
      catch(NumberFormatException nfe){
         number = -1;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         number = -1;
      }
      return number;
   }

   //
   //
   //
   private int getHashtableStage(Hashtable<String,String> ht){
      int stage = -1;
      try{
         stage = Integer.parseInt(ht.get("stage"));
      }
      catch(NumberFormatException nfe){
         stage = -1;
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         stage = -1;
      }
      return stage;
   }

   //
   //
   //
   private Hastable<String,String> getTankHashtable
   (
      List<Hashtable<String,String>> lst
   ){
      Hashtable<String,String> ht = null;
      Iterator<Hashtable<String,String>> it = lst.iterator();
      try{
         boolean found = false;
         while(it.hasNext() && !found){
            Hashtable<String,String> temp = it.next();
            int stage  = this.getHashtableStage(temp);
            int number = this.getHashtableNumber(temp);
            if(stage == this._stage && number == this._number){
               ht = temp;
               found = true;
            }
         }
      }
      catch(NullPointerException npe){
         ht = null;
      }

      return ht;
   }

   //
   //
   //
   private void initializeTank(String file)throws IOException{
      TankData td = null;
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      List<Hashtable<String,String>> lst = read.readTankDataInfo();
      Hashtable<String,String> ht = this.getTankHashtable(lst);
      System.out.println("++++++Tank Initializable+++++");
      System.out.println(ht);
      System.exit(0);
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
   public void initilalize(String file)throws IOException{
      System.out.println("Tank Initializable");
      String tFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         tFile = read.readPathInfo().get("tank");
      }
      this.initializeTank(tFile);
   }

   //
   //
   //
   public void initializeData(String key, Object data){}

   //
   //
   //
   public Object initialized(){
      return this._tankData;
   }
}
//////////////////////////////////////////////////////////////////////
