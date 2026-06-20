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

public class EngineInitializable implements Initializable{
   private int        _engine;
   private int        _stage;
   private EngineData _engineData;

   {
      _engine     = -1;
      _stage      = -1;
      _engineData = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public EngineInitializable(int engine, int stage){
      if(engine > -1){ this._engine = engine; }
      if(stage  > -1){ this._stage  = stage;  }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private Hashtable<String,String> getEngineData
   (
      List<Hashtable<String,String>> lst
   ){
      Hashtable<String,String> ht = null;
      Iterator<Hashtable<String,String>> it = lst.iterator();
      try{
         int engine    = this._engine;
         boolean found = false;
         while(it.hasNext() && !found){
            Hashtable<String,String> temp = it.next();
            int stage  = -1;
            int total  = -1;
            try{
               String sstg = temp.get("stage");
               stage = Integer.parseInt(sstg);
            }
            catch(NumberFormatException nfe){
               stage = -1;
            }
            try{
               String stot = temp.get("total");
               total = Integer.parseInt(stot);
            }
            catch(NumberFormatException nfe){
               total = -1;
            }
            if(stage == this._stage && engine <= total){
               ht = temp;
               found = true;
            }
            else if(engine > total){
               engine -= total;
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
   private void initializeEngine(String file)throws IOException{
      EngineData ed = null;
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      List<Hashtable<String,String>> lst = read.readEngineDataInfo();
      Iterator<Hashtable<String,String>> it = lst.iterator();
      //MUCH BETTER WAY TO DO IT THAN THIS!!!
      /*
      while(it.hasNext()){
         Hashtable<String,String> ht = it.next();
      }
      */
      Hashtable<String,String> ht = this.getEngineData(lst);
      System.out.println("+++++++++++++++++++++++++++++++++++++");
      System.out.println("InitializeEngine()");
      System.out.println(ht);
      System.out.println("+++++++++++++++++++++++++++++++++++++");
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
      return  isPath;
   }

   //////////////Initializeable Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      System.out.println("Engine Initializable");
      String eFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         eFile = read.readPathInfo().get("engine");
      }
      this.initializeEngine(eFile);
   
   }

   //
   //
   //
   public void initializeData(String key, Object data){}

   //
   //
   //
   public Object initialized(){
      return this._engineData;
   }
}
//////////////////////////////////////////////////////////////////////
