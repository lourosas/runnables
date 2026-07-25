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

public class PumpInitializable implements Initializable{
   private int      _stage;
   private int      _tankNumber;
   private PumpData _pumpData;

   {
      _stage      = -1;
      _tankNumber = -1;
      _pumpData   = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public PumpInitializable(int stage, int tank){
      if(stage > 0){ this._stage      = stage; }
      if(tank  > 0){ this._tankNumber =  tank; }
   }

   //////////////////////////Private Methods//////////////////////////
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
      catch(NullPointerException  npe){
         npe.printStackTrace();
         stage = -1;
      }
      return stage;
   }

   //
   //
   //
   private int getHashtableTankNumber(Hashtable<String,String> ht){
      int tankNumber = -1;
      try{
         tankNumber = Integer.parseInt(ht.get("tanknumber"));
      }
      catch(NumberFormatException nfe){
         tankNumber = -1;
      }
      catch(NullPointerException  npe){
         npe.printStackTrace();
         tankNumber = -1;
      }
      return tankNumber;
   }

   //
   //
   //
   private Hashtable<String,String> getPumpHashtable
   (
      List<Hashtable<String,String>> lst 
   ){
      Hashtable<String,String> ht = null;
      Iterator<Hashtable<String,String>> it = lst.iterator();
      try{
         boolean found = false;
         while(it.hasNext() && !found){
            Hashtable<String,String> temp = it.next();
            int stg = this.getHashtableStage(temp);
            int num = this.getHashtableTankNumber(temp);
            if(stg == this._stage && num == this._tankNumber){
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
   private double getRate(Hashtable<String,String> ht){
      double rate = Double.NaN;
      try{
         rate = Double.parseDouble(ht.get("rate"));
      }
      catch(NumberFormatException nfe){
         rate = Double.NaN;
      }
      catch(NullPointerException  npe){
         npe.printStackTrace();
         rate = Double.NaN;
      }
      return rate;
   }

   //
   //
   //
   private double getTemperature(Hashtable<String,String> ht){
      double temp = Double.NaN;
      try{
         temp = Double.parseDouble(ht.get("temperature"));
      }
      catch(NumberFormatException nfe){
         temp = Double.NaN;
      }
      catch(NullPointerException  npe){
         npe.printStackTrace();
         temp = Double.NaN;
      }
      return temp;
   }

   //
   //
   //
   private double getTolerance(Hashtable<String,String> ht){
      double tol = Double.NaN;
      try{
         tol = Double.parseDouble(ht.get("tolerance"));
      }
      catch(NumberFormatException nfe){
         tol = Double.NaN;
      }
      catch(NullPointerException  npe){
         npe.printStackTrace();
         tol = Double.NaN;
      }
      return tol;
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

   //
   //
   //
   private void initializePump(String file)throws IOException{
      PumpData pd = null;
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      List<Hashtable<String,String>> lst = read.readPumpDataInfo();
      Hashtable<String,String> ht = this.getPumpHashtable(lst);
      String   err = null;
      boolean  isE = false;
      double   rte = this.getRate(ht);
      int      stg = this._stage;
      int      tkn = this._tankNumber;
      double   tmp = this.getTemperature(ht);
      double   tol = this.getTolerance(ht);
      pd = new GenericPumpData(err,isE,rte,stg,tkn,tmp,tol);
      this._pumpData = pd;
   }

   ///////////////Initializable Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      System.out.println("Pump Initializable");
      String pFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read  = new LaunchSimulatorJsonFileReader(file);
         pFile = read.readPathInfo().get("pump");
      }
      this.initializePump(pFile);
   }

   //
   //
   //
   public void initializeData(String key, Object data){}

   //
   //
   //
   public Object initialized(){
      return this._pumpData;
   }
}
//////////////////////////////////////////////////////////////////////
