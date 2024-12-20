/////////////////////////////////////////////////////////////////////
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

public class GenericCapsule implements Payload, Runnable{
   private static final int PRELAUNCH = -1;
   private static final int IGNITION  =  0;
   private static final int LAUNCH    =  1;

   String _model;
   String _type;
   int    _crew;
   double _dryweight;

   {
      _model      = null;
      _type       = null;
      _crew       = -1;
      _dryweight  = Double.NaN;
   };

   ////////////////////////////Constructor///////////////////////////
   //
   //
   //
   public GenericCapsule(){}

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void capsuleData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         this.setCapsuleData(read.readPayloadInfo());
      }
   }

   //
   //
   //
   private void setCapsuleData(Hashtable<String,String> data){
      try{
         this._model = data.get("model");
      }
      catch(NullPointerException npe){}
      try{
         this._type = data.get("type");
      }
      catch(NullPointerException npe){}
      try{
         int i = Integer.parseInt(data.get("crew"));
         if(i > 0){
            this._crew = i;
         }
      }
      catch(NumberFormatException nfe){}
      catch(NullPointerException npe){}
      try{
         double d = Double.parseDouble(data.get("dryweight"));
         if(d > 0.){
            this._dryweight = d;
         }
      }
      catch(NumberFormatException nfe){}
      catch(NullPointerException npe){}
   }

   //////////////////Payload Interface Implementation/////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      this.capsuleData(file);
   }

   //
   //
   //
   public PayloadData monitorPrelaunch(){ return null; }

   //
   //
   //
   public PayloadData monitorIgnition(){ return null; }

   //
   //
   //
   public PayloadData monitorLaunch(){ return null; }

   //
   //
   //
   public PayloadData monitorPostLaunch(){ return null; }

   /////////////////Runnable Interface Implementation/////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
