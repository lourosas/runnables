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
   public Hashtable<String,Object> readRocketInfo()throws IOException{
      try{
         Hashtable<String,Object> data = null;
         data = new Hashtable<String,Object>();

         String jsonData = this.grabJSONFileData();
         System.out.println(jsonData);
         String [] sdata = jsonData.split(",");
         for(int i = 0; i < sdata.length; ++i){
            sdata[i] = sdata[i].trim();
            System.out.println(sdata[i]);
         }
         return null;
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
   private void setPathAndFile(String file){
      this._pathAndFile = file;
   }
}
//////////////////////////////////////////////////////////////////////
