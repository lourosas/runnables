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

public class GenericPipe implements Pipe, Runnable{
   private static boolean TOPRINT = true;
   
   private LaunchStateSubstate.State INIT      = null; 
   private LaunchStateSubstate.State PRELAUNCH = null;
   private LaunchStateSubstate.State IGNITION  = null;
   private LaunchStateSubstate.State LAUNCH    = null;

   private boolean  _kill;
   private int      _tank;  //Tank Number (1,2)
   private int      _stage; //Stage Number (1...total stages)
   //Pipe Number for the Tank (1,2)--corresponds to the engine...
   private int      _number; 
   private double   _tolerance;

   private DataFeeder            _feeder;
   private List<ErrorListener>   _errorListeners;
   private List<SystemListeners> _systemListeners;
   private LaunchStateSubstate   _state;
   private Object                _obj;
   private PipeData              _pipeData;
   private PipeData              _measuredPipeData;
   private Thread                _rt0;

   {
      INIT      = LaunchStateSubstate.State.INITIALIZE;
      PRELAUNCH = LaunchStateSubstate.State.PRELAUNCH;
      IGNITION  = LaunchStateSubstate.State.IGNITION;
      LAUNCH    = LaunchStateSubstate.State.LAUNCH;

      _kill                = false;
      _tank                = -1;
      _stage               = -1;
      _start               = false;
      _number              = -1; //Engine
      _tolerance           = Double.NaN;

      _feeder              = null;
      _errorListeners      = null;
      _systemListeners     = null;
      _state               = null;
      _obj                 = null;
      _pipeData            = null;
      _measuredPipeData    = null;
      _rt0                 = null;
   };

   ///////////////////////////Constructor/////////////////////////////
   //Tank Number
   //Stage Number
   //Pipe Number
   public GenericPipe(int tank, int stage, int number){
      if(tank > 0){
         this._tank = tank;
      }
      if(stage > 0){
         this._stage = stage;
      }
      if(number > 0){
         //Essentially, this is the Rocket Engine the Pipe Feeds...
         this._number = number;
      }
      this._obj = new Object();
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private initializePipeDataJSON(String file)throws IOException{
      double flo = Double.NaN;    double num = Double.NaN;
      int stg = -1; int tnk = -1; double tol = Double.NaN;
      double temp = Double.NaN; String type = null; String err = null;
      boolean isE = false;
      //Test Print
      System.out.println("Generic Pipe:  "+file);
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lst = read.readPipeDataInfo();
         Iterator<Hashtable<String,String>> it = list.iterator();
         int number = 0;
         while(it.hasNext(){
            Hashtable<String,String> ht = it.next();
            ++number;
            try{ stg = Integer.parseInt(ht.get("stage")); }
            catch(NumberFormatException nfe){ stg = -1; }
            try{ num = Integer.parseInt(ht.get("tanknumber")); }
            catch(NumberFormatException nfe){ num = -1; }
            if(_stage == stg&&_tank == num&&_number == number){
               
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
         ioe.printStackTrace();
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
   private void pipeData(String file)throws IOException{
      if(file.toUpperCase().contains("INI")){
         LaunchSimulatorIniFileReader read = null;
         read = new LaunchSimulatorIniFileReader(file);
      }
      else if(file.toUpperCase().contains("JSON")){
         this.initializePipeDataJSON(file);
      }
   }

   //
   //
   //
   private void setPipeData(List<Hashtable<String,String>> data ){
      System.out.println("Pipes: "+data);
      //This will need to change similar to GenericStage!
      //Use PipeData
      for(int i = 0; i < data.size(); ++i){
         Hashtable<String,String> ht = data.get(i);
         try{
            int tk     = this._tank;   //Tank Number
            int st     = this._stage;  //Stage Number
            int num    = this._number; //Engine Number
            int tank   = Integer.parseInt(ht.get("tanknumber"));
            int stage  = Integer.parseInt(ht.get("stage"));
            if((tk == tank) && (st == stage)){
               System.out.println("Engine:  "+num);
               System.out.println(ht);
               double rate=Double.parseDouble(ht.get("rate"));
               double temp=Double.parseDouble(ht.get("temperature"));
               double tol = Double.parseDouble(ht.get("tolerance"));
               PipeData pd = new GenericPipeData(null,  //Error
                                                 rate,  //Flow
                                                 num,   //Engine Number
                                                 false, //isError
                                                 st,    //Stage Number
                                                 tk,    //Tank Number
                                                 temp,  //Temperature
                                                 tol,   //Tolerance
                                                 null   //Fuel Type
                                                 );
               this._pipeData = pd;
            }
         }
         catch(NumberFormatException nfe){
            this._pipeData = null;
         }
      }
   }

   //
   //
   //
   private void setUpThread(){
      String name = new String("Pipe: "+this._stage+", "+this._tank);
      name += ", "+this._number;
      this._rt0 = new Thread(this, name);
      this._rt0.start();
   }

   ////////////////////Pipe Interface Implementation//////////////////
   //
   //
   //
   public PipeData monitor(){
      synchronized(this._obj){
         return this._measuredPipeData;
      }
   }

   //
   //
   //
   public void initialize(String file)throws IOException{
      int tank  = this._tank;
      int stage = this._stage;
      int num   = this._number;
      if((tank > 0) && (stage > 0) && (num > 0)){
         String pdFile = file;
         if(this.isPathFile(pdFile)){
            LaunchSimulatorJsonFileReader read = null;
            read = new LaunchSimulatorJsonFileReader(pdFile);
            pdFile = read.readPathInfo().get("pipe");
         }
         this.pipeData(pdFile);
      }
   }

   //
   //
   //
   public void addDataFeeder(DataFeeder feeder){
      if(feeder != null){
         this._feeder = feeder;
      }
   }

   //
   //
   //
   public void addErrorListener(ErrorListener listener){}

   //
   //
   //
   public void addSystemListener(SystemListener listener){}

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate stateSubstate){
      this._state = stateSubstate;
   }

   ///////////////////Runnable Interface Implementation///////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            if(this._kill){
               throw new InterruptedException();
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
