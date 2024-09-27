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
   public String readRocketInfo() throws IOException{
      try{
         boolean found      = false;
         String  rocketInfo = new String();
         this.openFile();
         String line = new String();
         while((line = this._br.readLine()) != null){
            if(line.contains("rocket")){
               found = true;
               rocketInfo = rocketInfo.concat(line);
            }
            else if(found){
               rocketInfo = rocketInfo.concat(line);
               if(line.contains("}")){
                  found = false;
               }
            }
         }
         this.closeFile();
         return  rocketInfo;
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
