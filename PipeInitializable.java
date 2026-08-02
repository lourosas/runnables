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

public class PipeInitializable implements Initializable{
   private int      _stage;
   private int      _tankNumber;
   private int      _number; //current number of the pipe connected
   private PipeData _pipeData; 

   {
      _stage       = -1;
      _tankNumber  = -1;
      _number      = -1;
      _pipeData    = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //Stage, tank number, pipe number
   //
   //
   public PipeInitializable(int stage, int tank, int num){
      if(stage > 0){ this._stage      = stage; }
      if(tank  > 0){ this._tankNumber = tank;  }
      if(num   > 0){ this._number     = num;   }
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private String getFuelType(Hashtable<String,String> ht){
      String type = null;
      try{
         type = ht.get("type");
      }
      catch(NullPointerException npe){
         type = null;
      }
      return type;
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
   private Hashtable<String,String> getPipeHashtable
   (
      List<Hashtable<String,String>> lst
   ){
      Hashtable<String,String> ht = null;
      Iterator<Hashtable<String,String>> it = lst.iterator();
      try{
         while(it.hasNext()){
            boolean found = false;
            while(!found && it.hasNext()){
               Hashtable<String,String> temp = it.next();
               int stg = this.getHashtableStage(temp);
               int num = this.getHashtableTankNumber(temp);
               if(stg == this._stage && num == this._tankNumber){
                  ht    = temp;
                  found = true;
               }
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
   private void initializePipe(String file)throws IOException{
      PipeData pd = null;
      LaunchSimulatorJsonFileReader read = null;
      read = new LaunchSimulatorJsonFileReader(file);
      List<Hashtable<String,String>> lst = read.readPumpDataInfo();
      Hashtable<String,String> ht = this.getPipeHashtable(lst);
      String err  = null;
      boolean isE = false;
      int     num = this._number; //current number of Pipe...
      double  rte = this.getRate(ht);
      int     stg = this._stage;
      int     tkn = this._tankNumber;
      double  tmp = this.getTemperature(ht);
      double  tol = this.getTolerance(ht);
      String  typ = this.getFuelType(ht);
      pd = new GenericPipeData(err,isE,num,rte,stg,tkn,tmp,tol,typ);
      this._pipeData = pd;
   }

   ///////////////Initializable Interface Implementation//////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      System.out.println("Pipe Initializable");
      String pFile = file;
      if(this.isPathFile(file)){
         LaunchSimulatorJsonFileReader read = null;
         read  = new LaunchSimulatorJsonFileReader(file);
         pFile = read.readPathInfo().get("pipe");
      }
      this.initializePipe(pFile);
   }

   //
   //
   //
   public void initializeData(String key, Object data){}

   //
   //
   //
   public Object initialized(){
      return this._pipeData;
   }
}
//////////////////////////////////////////////////////////////////////
