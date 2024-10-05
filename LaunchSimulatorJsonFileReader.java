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
import java.io.*;
import rosas.lou.runnables.*;

public class LaunchSimulatorJsonFileReader{
   private FileReader     _fr;
   private BufferedReader _br;
   private String         _pathAndFile;

   {
      _fr          = null;
      _br          = null;
      _pathAndFile = null;
   };

   ///////////////////////////Contructor//////////////////////////////
   //
   //
   //
   public LaunchSimulatorJsonFileReader(String file){
      this.setPathAndFile(file);
   }

   ////////////////////////Public Methods/////////////////////////////
   //
   //
   //
   //
   public Hashtable<String,String>
   readLaunchingMechanismInfo()throws IOException{
      try{
         Hashtable<String,String> data = null;
         data = new Hashtable<String,String>();

         String jsonData = this.grabJSONFileData();
         return this.parseLaunchingMechanismData(jsonData);
      }
      catch(IOException ioe){
         this.closeFile();
         throw ioe;
      }
   }

   //
   //
   //
   public Hashtable<String,String> readRocketInfo()throws IOException{
      try{
         boolean found = false;
         Hashtable<String,String> data = null;
         data = new Hashtable<String,String>();

         String jsonData = this.grabJSONFileData();
         return this.parseRocketData(jsonData);
      }
      catch(IOException ioe){
         this.closeFile();
         throw ioe;
      }
   }

   ///////////////////////Private Methods/////////////////////////////
   //
   //
   //
   private void closeFile(){
      try{
         this._br.close();
         this._fr.close();
      }
      catch(NullPointerException npe){}
      catch(IOException ioe){}
   }

   //
   //
   //
   private String grabJSONFileData() throws IOException{
      try{
         String data = new String();
         String line = new String();
         
         this.openFile();
         while((line = this._br.readLine()) != null){
            data = data.concat(line);
         }
         this.closeFile();

         return data;
      }
      catch(IOException ioe){
         this.closeFile();
         throw ioe;
      }
   }

   //
   //
   //
   private void openFile() throws IOException{
      try{
         this._fr = new FileReader(this._pathAndFile);
         this._br = new BufferedReader(this._fr);
      }
      catch(IOException ioe){
         this.closeFile();
         throw ioe;
      }
   }

   //
   //
   //
   private Hashtable<String,String>
   parseLaunchingMechanismData(String data){
      boolean found               = false;
      String[] array              = data.split(",");
      String[] saves              = new String[array.length];
      int savesCount              = 0;
      Hashtable<String,String> ht = null;

      ht = new Hashtable<String,String>();

      for(int i = 0; i < array.length; ++i){
         array[i] = array[i].strip();
      }
      for(int i = 0; i < array.length; ++i){
          String value = array[i];
          if(value.contains("launching_mechanism")){
             found = true;
          }
          if(found){
             String values[] = value.split(":");
          }
      }
   }

   //
   //
   //
   private Hashtable<String,String> parseRocketData(String data){
      boolean found               = false;
      boolean ignoreStage         = false;
      String[] array              = data.split(",");
      String[] saves              = new String[array.length];
      int savesCount              = 0;
      Hashtable<String,String> ht = null;
      ht = new Hashtable<String,String>();

      for(int i = 0; i < array.length; ++i){
         array[i] = array[i].strip();
      }
      for(int i = 0; i < array.length; ++i){
         String value = array[i];
         if(value.contains("rocket")){
            found = true;
         }
         if(found){
            String[] values = value.split(":");
            for(int j = 0; j < values.length; ++j){
               values[j] = values[j].strip();
            }
            for(int k = 0; k < values.length; ++k){
               if(values[k].contains("stage") && 
                  !values[k].contains("stages")){
                  //System.out.println(values[k]);
                  ignoreStage = true;
               }
               if(!ignoreStage){
                  //this is the data we want...
                  if(!values[k].contains("rocket")){
                     String[] temp = values[k].split("\"");
                     for(int l = 0; l < temp.length; ++l){
                        temp[l] = temp[l].strip();
                        if(temp[l].length() > 0){
                           char c = temp[l].charAt(0);
                           if(Character.isLetter(c) ||
                              Character.isDigit(c)){
                              saves[savesCount] = temp[l];
                              ++savesCount;
                           }
                        }
                     }
                  }
               }
               if(values[k].indexOf(']')!=values[k].lastIndexOf(']')){
                  if(ignoreStage){
                     ignoreStage = false;
                  }
               }
            }
            //this is the ending point for the rocket data
            if(value.lastIndexOf('}')==value.length()-1){
               found = false;
            }
         }
      }
      for(int i = 0; i < savesCount; i+=2){
         try{
            ht.put(saves[i],saves[i+1]);
         }
         catch(ArrayIndexOutOfBoundsException e){
            ht.put(saves[i],null);
         }
      }
      return ht;
   }

   //
   //
   //
   private void setPathAndFile(String file){
      this._pathAndFile = file;
   }
}
//////////////////////////////////////////////////////////////////////
