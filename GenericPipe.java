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
import java.io.IOException;
import rosas.lou.runnables.*;

public class GenericPipe extends Pipe implements Runnable{
   private LaunchStateSubstate.State INIT              = null;
   private LaunchStateSubstate.State PREL              = null;
   private LaunchStateSubstate.State IGNI              = null;
   private LaunchStateSubstate.State LAUN              = null;
   private LaunchStateSubstate.State ASCE              = null;
   private LaunchStateSubstate.PreLaunchSubstate SET   = null;
   private LaunchStateSubstate.PreLaunchSubstate CONT  = null;
   private LaunchStateSubstate.PreLaunchSubstate FUEL  = null;
   private LaunchStateSubstate.PreLaunchSubstate HOLD  = null;
   private LaunchStateSubstate.IgnitionSubstate  IGN   = null;
   private LaunchStateSubstate.IgnitionSubstate  BUP   = null;
   private LaunchStateSubstate.AscentSubstate    STG   = null;
   private LaunchStateSubstate.AscentSubstate    IGNE  = null;

   private boolean  _kill;
   private Object   _obj;
   private Thread   _rt0;

   {
      INIT = LaunchStateSubstate.State.INITIALIZE;
      PREL = LaunchStateSubstate.State.PRELAUNCH;
      IGNI = LaunchStateSubstate.State.IGNITION;
      LAUN = LaunchStateSubstate.State.LAUNCH;
      ASCE = LaunchStateSubstate.State.ASCENT;
      SET  = LaunchStateSubstate.PreLaunchSubstate.SET;
      CONT = LaunchStateSubstate.PreLaunchSubstate.CONTINUE;
      FUEL = LaunchStateSubstate.PreLaunchSubstate.FUELING;
      HOLD = LaunchStateSubstate.PreLaunchSubstate.HOLD;
      IGN  = LaunchStateSubstate.IgnitionSubstate.IGNITION;
      BUP  = LaunchStateSubstate.IgnitionSubstate.BUILDUP;
      STG  = LaunchStateSubstate.AscentSubstate.STAGING;
      IGNE = LaunchStateSubstate.AscentSubstate.IGNITEENGINES; 

      _kill      = false;
      _obj       = null;
      _rt0       = null;

      stage      = -1;
      tankNumber = -1;
      number     = -1;
   };

   ///////////////////////////Constructor/////////////////////////////
   //Tank Number
   //Stage Number
   //Pipe Number
   public GenericPipe(int tank, int stage, int number){
      if(tank > 0){
         this.tankNumber = tank;
      }
      if(stage > 0){
         this.stage = stage;
      }
      if(number > 0){
         //Essentially, this is the Rocket Engine the Pipe Feeds...
         this.number = number;
      }
      this._obj = new Object();
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   /*
   //
   //
   //
   private void alertErrorListeners(){
      String error = null;
      PipeData pd  = null;
      synchronized(this._obj){
         pd    = this._measuredPipeData;
         error = pd.error();
      }
      try{
         Iterator<ErrorListener> it = this._errorListeners.iterator();
         while(it.hasNext()){
            it.next().errorOccurred(new ErrorEvent(this, pd, error));
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void alertSubscribers(){
      PipeData            pd = null;
      LaunchStateSubstate ss = this._state;
      
      String event = ss.state()+", "+ss.ascentSubstate();
      event += ", "+ss.ignitionSubstate()+", ";
      event += ss.prelaunchSubstate();
      synchronized(this._obj){
         pd = this._measuredPipeData;
      }
      try{
         Iterator<SystemListener> it = null;
         it = this._systemListeners.iterator();
         while(it.hasNext()){
            MissionSystemEvent mse = null;
            mse = new MissionSystemEvent(this,pd,event,ss);
            it.next().update(mse);
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void checkErrors(){
      boolean  isError = false;
      PipeData pd      = null;
      synchronized(this._obj){
         pd = this._measuredPipeData;
      }
      String err = new String();
      double flw = pd.flow();      double temp = pd.temperature();
      double tol = pd.tolerance(); String type = pd.type();

      if(this.checkFlow()){
         err     += "\nFlow Rate Error";
         isError  = true;
      }
      if(this.checkTemperature()){
         err     += "\nTemperature Error";
         isError  = true;
      }
      if(isError){
         pd = new GenericPipeData(
                                  err,          //Error
                                  flw,          //Flow
                                  this._number, //Pipe Number
                                  isError,      //Is Error
                                  this._stage,  //Stage
                                  this._tank,   //Tank Number
                                  temp,         //Temperature
                                  tol,          //Tolerance
                                  type);        //Type
         synchronized(this._obj){
            this._measuredPipeData = pd;
         }
         this.alertErrorListeners();
      }
   }

   //
   //
   //
   private boolean checkFlow(){
      boolean isError    = false;
      double  flow       = Double.NaN;
      double  tolerance  = Double.NaN;
      synchronized(this._obj){
         flow      = this._measuredPipeData.flow();
         tolerance = this._measuredPipeData.tolerance();
      }
      double min = Double.NaN; double max = Double.NaN;
      //In the Initialization State, nothing should be flowing
      //outsid of Tolerance
      if(this._state.state() == INIT){
         max = 1. - tolerance;
         isError |= (flow > max);
      }
      return isError;
   }

   //
   //
   //
   private boolean checkTemperature(){
      boolean isError     = false;
      double  temperature = Double.NaN;
      double  tolerance   = Double.NaN;
      synchronized(this._obj){
         temperature = this._measuredPipeData.temperature();
         tolerance   = this._measuredPipeData.tolerance();
      }
      double min = Double.NaN;  double max = Double.NaN;
      //In the Initialization State, anything between the freezing and
      //Boiling points of wather is acceptable
      if(this._state.state() == INIT){
         min = 273.15; max = 373.15;
         isError |= (temperature < min || temperature > max);
      }
      return isError;
   }

   //
   //
   //
   private void initializePipeDataJSON(String file)throws IOException{
      double flo = Double.NaN; int num = this._number;
      int stg = -1; int tnk = -1; double tol = Double.NaN;
      double temp = Double.NaN; String type = null; String err = null;
      boolean isE = false;
      //Test Print
      System.out.println("Generic Pipe:  "+file);
      System.out.println(_stage+", "+_tank+", "+_number);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst = read.readPipeDataInfo();
         Iterator<Hashtable<String,String>> it = lst.iterator();
         while(it.hasNext()){
            Hashtable<String,String> ht = it.next();
            try{ stg = Integer.parseInt(ht.get("stage")); }
            catch(NumberFormatException nfe){ stg = -1; }
            try{ tnk = Integer.parseInt(ht.get("tanknumber")); }
            catch(NumberFormatException nfe){ tnk = -1; }
            System.out.println("in loop "+stg+", "+tnk+", "+num);
            if(this._stage == stg && this._tank == tnk){
               num = this._number;
               try{ flo = Double.parseDouble(ht.get("rate")); }
               catch(NumberFormatException nfe){ flo = Double.NaN; }
               try{temp= Double.parseDouble(ht.get("temperature")); }
               catch(NumberFormatException nfe){temp = Double.NaN; }
               try{ tol = Double.parseDouble(ht.get("tolerance")); }
               catch(NumberFormatException nfe){ tol = Double.NaN; }
               this._pipeData = new GenericPipeData(
                                             err,     //Error
                                             flo,     //flow
                                             num,     //Pipe No.
                                             isE,     //Is Error
                                             stg,     //Stage
                                             tnk,     //Tank Number
                                             temp,    //Temperature
                                             tol,     //Tolerance
                                             type);   //Type
               System.out.println("GenericPipeData"+this._pipeData);
            }
         }
      }
      catch(IOException ioe){
         this._pipeData = null;
         throw ioe;
      }
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
      catch(Exception e){
         e.printStackTrace();
         isPath = false;
      }
      finally{
         return isPath;
      }
   }

   //
   //
   //
   private void measure(){
      try{
         if(this._feeder != null){
            RocketData rd = (RocketData)this._feeder.monitor();
            StageData  sd = rd.stage(this._stage);
            FuelSystemData fsd = sd.fuelSystemData();
            List<PipeData> lst = fsd.pipeData();
            Iterator<PipeData> it = lst.iterator();
            int num = 0;
            while(it.hasNext()){
               ++num;
               PipeData pd = it.next();
               int sn  = pd.stage();
               int tnk = pd.tank();
               if(_stage == sn&&_tank == tnk&&_number == num){
                  this._measuredPipeData = pd;
               }
            }
         }
         else{
            throw new NullPointerException("No DataFeeder");
         }
      }
      catch(ClassCastException cce){
         try{
            synchronized(this._obj){
               PipeData pd = (PipeData)this._feeder.monitor();
               this._measuredPipeData = pd;
            }
         }
         catch(ClassCastException e){
            e.printStackTrace();
            throw new NullPointerException("No PumpDataFeeder");
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
         synchronized(this._obj){
            this._measuredPipeData = this._pipeData;
         }
      }
   }

   //
   //
   //
   private void monitorPipe(){
      this.measure();
   }

   //
   //
   //
   private void pipeData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         this.initializePipeDataJSON(file);
      }
   }
   */
   //
   //
   //
   private void setUpThread(){
      String name = new String("Pipe: "+this.stage+", ");
      name += this.tankNumber+", "+this.number;
      this._rt0 = new Thread(this, name);
      this._rt0.start();
   }

   ///////////////////Runnable Interface Implementation///////////////
   //
   //
   //
   public void run(){
      int counter   = 0;
      boolean check = false;
      try{
         while(true){
            if(this._kill){
               throw new InterruptedException();
            }
            if(this.getStateSubstate() != null){
               if(this.getStateSubstate().state() == INIT){
                  if(counter++%1500 == 0){
                     check = true;
                     counter = 1;//reset the counter
                  }
               }
            }
            if(check){
               /*
               this.monitorPipe();
               this.checkErrors();
               this.alertSubscribers();
               check = false;
               */
            }
            Thread.sleep(1);
         }
      }
      catch(InterruptedException ie){}
      catch(NullPointerException npe){
         //Should NEVER GET HERE!!!
         npe.printStackTrace();
         System.exit(0);
      }
   }
}
//////////////////////////////////////////////////////////////////////
