//////////////////////////////////////////////////////////////////////
/*
Copyright 2025 Lou Rosas

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
import java.text.*;
import java.time.*;
import java.time.format.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class GenericSystemDataFeeder implements DataFeeder,Runnable{
   private LaunchStateSubstate.State INIT             = null;
   private LaunchStateSubstate.State PREL             = null;
   private LaunchStateSubstate.State IGNI             = null;
   private LaunchStateSubstate.State LAUN             = null;
   private LaunchStateSubstate.State ASCE             = null;
   private LaunchStateSubstate.PreLaunchSubstate SET  = null;
   private LaunchStateSubstate.PreLaunchSubstate CONT = null;
   private LaunchStateSubstate.PreLaunchSubstate FUEL = null;
   private LaunchStateSubstate.PreLaunchSubstate HOLD = null;
   private LaunchStateSubstate.IgnitionSubstate  IGN  = null;
   private LaunchStateSubstate.IgnitionSubstate  BUP  = null;
   private LaunchStateSubstate.AscentSubstate    STG  = null;
   private LaunchStateSubstate.AscentSubstate    IGNE = null;

   //dont need if have weight!
   private double                     _weight;
   private double                     _holdAngle;
   private LaunchingMechanismData     _mechData;
   private RocketData                 _rocketData;
   private List<MechanismSupportData> _suppData;
   //It doe not look as if this is needed...
   private List<StageData>            _stageData;//do not need?

   //Measured Data
   private LaunchingMechanismData     _measMechData;
   private RocketData                 _measRocketData;
   private List<MechanismSupportData> _measSuppData;
   private List<StageData>            _measStageData;//do not need?

   //Set Data
   private LaunchStateSubstate _cond;
   private Object              _obj;
   private Random              _random;
   private Thread              _rt0;
   private boolean             _start;
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
      
      _cond                      = null;
      _mechData                  = null;
      _measMechData              = null;
      _measRocketData            = null;
      _measSuppData              = null;
      _measStageData             = null;
      _obj                       = null;
      _random                    = null;
      _rocketData                = null;
      _rt0                       = null;
      _suppData                  = null;
      _stageData                 = null;
      _start                     = false;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public GenericSystemDataFeeder(){
      this._random = new Random();
      //Grab the Monitor for the Threads...
      this._obj = new Object();
      this.setUpThread();
   }

   //////////////////////////Private Methods//////////////////////////
   //KEEP
   //
   //
   private void readLaunchingMechanismData(String file)
   throws IOException{
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = null;
         ht = read.readLaunchingMechanismInfo();
         List<MechanismSupportData> l = null;
         int    m  = -1;         //Model
         int    nh = -1;         //Number of Holds
         double ha = Double.NaN; //Holds Angle
         double ho = Double.NaN; //Holds Tolerance
         double tt = Double.NaN; //Total Tolerance
         try{m = Integer.parseInt(ht.get("model"));}
         catch(NumberFormatException npe){ m = -1;}
         try{nh = Integer.parseInt(ht.get("number_of_holds"));}
         catch(NumberFormatException nfe){nh = -1;}
         try{ha = Double.parseDouble(ht.get("angle_of_holds"));}
         catch(NumberFormatException nfe){ha = Double.NaN;}
         try{ho = Double.parseDouble(ht.get("holds_tolerance"));}
         catch(NumberFormatException nfe){ho = Double.NaN;}
         try{tt = Double.parseDouble(ht.get("total_tolerance"));}
         catch(NumberFormatException nfe){tt = Double.NaN;}
         l = new LinkedList<MechanismSupportData>();
         for(int i = 0; i < nh; ++i){
            MechanismSupportData msd = null;
            msd = new GenericMechanismSupportData(ha,   //Hold Angle
                                                  null, //Error
                                                  null, //Force Vec
                                                  i,    //ID
                                                  false,//error
                                                  Double.NaN,//Force
                                                  ho);  //holds tol
            l.add(msd);
         }
         LaunchingMechanismData mech = null;
         mech = new GenericLaunchingMechanismData(
                                         null,       //error
                                         nh,         //number of holds
                                         false,      //Is Error
                                         Double.NaN, //Calc Weight
                                         m,          //model
                                         this._cond, //State/Substate
                                         tt,         //Total Tolerance
                                         l);         //Mech Supp List
         this._mechData = mech;
         this._suppData = l;//Get the Support Data, as well
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         this._mechData = null;
         this._suppData = null;
         throw ioe;
      }
   }

   //KEEP
   //
   //
   private void readMechanismSupportData(String file)
   throws IOException{
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = null;
         ht = read.readLaunchingMechanismInfo();
         List<MechanismSupportData> l = null;
         int     nh = -1;        //Number of Holds
         double  ha = Double.NaN;//Holds angle
         double  ho = Double.NaN;//Holds Tolerance
         try{nh = Integer.parseInt(ht.get("number_of_holds"));}
         catch(NumberFormatException nfe){nh = -1;}
         try{ha = Double.parseDouble(ht.get("angle_of_holds"));}
         catch(NumberFormatException nfe){ha = Double.NaN;}
         try{ho = Double.parseDouble(ht.get("holds_tolerance"));}
         catch(NumberFormatException nfe){ho = Double.NaN;}
         l = new LinkedList<MechanismSupportData>();
         for(int i = 0; i < nh; ++i){
            MechanismSupportData msd = null;
            msd = new GenericMechanismSupportData(ha,  //Hold Angle
                                                  null,//Error
                                                  null,//Force Vec
                                                  i,   //ID
                                                  false,//Error
                                                  Double.NaN,//Force
                                                  ho); //Hold tol
            l.add(msd);
         }
         this._suppData = l;
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         this._suppData = null;
         throw ioe;
      }
      catch(NullPointerException npe){npe.printStackTrace();}
   }

   //KEEP
   //
   //
   private void readRocketData(String file)throws IOException{
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         Hashtable<String,String> ht = null;
         ht = read.readRocketInfo();
         double ew = Double.NaN;  //Empty  Weight
         double lw = Double.NaN;  //Loaded Weight
         int    ns = -1;          //Number of Stages
         String m  = null;        //Model
         double t  = Double.NaN;  //Tolerance
         try{ew = Double.parseDouble(ht.get("empty_weight"));}
         catch(NumberFormatException nfe){ew = Double.NaN;}
         try{lw = Double.parseDouble(ht.get("loaded_weight"));}
         catch(NumberFormatException nfe){lw = Double.NaN;}
         try{ns = Integer.parseInt(ht.get("stages"));}
         catch(NumberFormatException nfe){ns = -1;}
         m = ht.get("model");
         try{Double.parseDouble(ht.get("tolerance"));}
         catch(NumberFormatException nfe){t = Double.NaN; }
         //Will need to read the Stage Data to get the Stage info...
         List<StageData> sd = this.readStageData(file);
         //Try to set up everything...
         this._rocketData = new GenericRocketData(m, //model
                                                 -1, //current stage
                                                  ns,//No. of Stages
                                                  ew,//empty
                                                  lw,//loaded
                                                  Double.NaN,//calc W
                                                  false,//isError
                                                  null,//Error
                                                  sd,//Stage Data
                                                  t);//Tolerance
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         throw ioe;
      }
   }

   //SO MUCH MORE TO DO...to get the ENGINE DATA AND the FUEL SYSTEM
   //DATA!!!! Need to put together the Fuel System Data, Engine Data
   //
   private List<StageData> readStageData(String file)
   throws IOException{
      List<StageData> l = null;
      try{
         LaunchSimulatorJsonFileReader read = null;
         read = new LaunchSimulatorJsonFileReader(file);
         List<Hashtable<String,String>> lht = read.readStageInfo();
         Hashtable<String,String>       sht = read.readRocketInfo();
         int totalStages = -1;
         try{totalStages = Integer.parseInt(sht.get("stages"));}
         catch(NumberFormatException nfe){ totalStages = -1; }
         Iterator<Hashtable<String,String>> it = lht.iterator();
         int count = 0;
         while(it.hasNext()){
            Hashtable<String,String> ht = it.next();
            int    cs = -1;         //current stage
            int    en = -1;         //engines
            double dw = Double.NaN; //Dryweight
            double mw = Double.NaN; //Maxweight
            long   md = -1;         //Model
            double tl = Double.NaN; //tolerance
            try{cs=Integer.parseInt(ht.get("number"));}
            catch(NumberFormatException nfe){cs = -1;}
            if(cs > 0 && cs < totalStages+1){
               //Save the data off...
               try{en=Integer.parseInt(ht.get("engines"));}
               catch(NumberFormatException nfe){en = -1;}
               try{dw=Double.parseDouble(ht.get("dryweight"));}
               catch(NumberFormatException nfe){dw = Double.NaN;}
               try{mw=Double.parseDouble(ht.get("maxweight"));}
               catch(NumberFormatException nfe){mw = Double.NaN;}
               try{md=Long.parseLong(ht.get("model"),16);}
               catch(NumberFormatException nfe){md = -1;}
               try{tl=Double.parseDouble(ht.get("tolerance"));}
               catch(NumberFormatException nfe){tl = Double.NaN;}
               StageData sd = new GenericStageData(dw,//Dry Weight
                                                   null,//error
                                                   md,//model
                                                   false,//isError
                                                   cs,//Current stage
                                                   en,//engines
                                                   mw,//Max Weight
                                                   tl,//tolerance
                                                   Double.NaN,//weight
                                                   null,//engines
                                                   null//fuel system
                                                   );
               if(l == null){l = new LinkedList<StageData>();}
               l.add(sd);
            }
         }
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         l = null;
         throw ioe;
      }
      finally{
         return l;
      }
   }

   //
   //
   //
   private void setMechanism(){
      double scale = Double.NaN;
      int min   = -1; int max   = -1;
      int value = -1; int  m    = -1; //Model
      int nh       = -1;         //Number of holds
      double  ah   = Double.NaN; //Angle of Holds
      double  ht   = Double.NaN; //Holds Tolerance
      double  tt   = Double.NaN; //Total Tolerance
      
      List<MechanismSupportData> l = null; //Mechanism Support Data
      List<MechanismSupportData> t = null;

      try{
         //Set the Mechanism Data
         synchronized(this._obj){
            l  = new LinkedList<MechanismSupportData>();
            m  = this._mechData.model();
            nh = this._mechData.holds();
            tt = this._mechData.tolerance();
            //Grab the supports!!!
            t = this._mechData.supportData();
            Iterator<MechanismSupportData> it = t.iterator();
            while(it.hasNext()){
               MechanismSupportData data = it.next();
               int id = data.id();
               ah     = data.angle();
               if(this._cond.state() == INIT){
                  scale = 0.01;
               }
               min   = (int)(ah*(1-scale));
               max   = (int)(ah*(1+scale));
               value = this._random.nextInt(max - min +1) + min; 
               ah    = (double)value;
               MechanismSupportData msd = null;
               msd = new GenericMechanismSupportData(ah,
                                                     null,
                                                     null,
                                                     id,
                                                     false,
                                                     Double.NaN,
                                                     ht);
               l.add(msd);
            }
         }
      }
      catch(NullPointerException npe){
         m = -1; nh = -1; ah = Double.NaN; ht = Double.NaN;
         tt = Double.NaN; l  = null;
      }
      finally{
         synchronized(this._obj){
            LaunchingMechanismData md = null;
            md = new GenericLaunchingMechanismData(
                                       null,      //error
                                       nh,        //number of holds
                                       false,     //Is Error
                                       Double.NaN,//Calc Weight
                                       m,         //model
                                       this._cond,//State/Substate
                                       tt,        //total tolerance
                                       l);        //Mech Supp List
            this._measMechData = md;
            this._measSuppData =  l;
         }
      }
   }

   //
   //
   //
   private void setMechanismSupports(){
      double scale = Double.NaN;
      int id    = -1; int min = -1;
      int value = -1; int max = -1;  
      double ah = Double.NaN; //Angle of Holds
      double ht = Double.NaN; //Holds Tolerance

      List<MechanismSupportData> l = null;
      List<MechanismSupportData> t = null;

      try{
         synchronized(this._obj){
            l = new LinkedList<MechanismSupportData>();
            t = this._suppData;
            Iterator<MechanismSupportData> it = t.iterator();
            while(it.hasNext()){
               MechanismSupportData data = it.next();
               id = data.id();
               ah = data.angle();
               if(this._cond.state() == INIT){
                  scale = 0.01;
               }
               min   = (int)(ah*(1-scale));
               max   = (int)(ah*(1+scale));
               value = this._random.nextInt(max - min + 1) + min;
               ah    = (double)value;
               MechanismSupportData msd = null;
               msd = new GenericMechanismSupportData(ah,
                                                     null,
                                                     null,
                                                     id,
                                                     false,
                                                     Double.NaN,
                                                     ht);
               l.add(msd);
            }
         }
      }
      catch(NullPointerException npe){
         ah = Double.NaN; ht = Double.NaN; id = -1; l = null;
      }
      finally{
         synchronized(this._obj){
            //Measured has GOT to be different than what is saved off
            this._measSuppData = l;
         }
      }
   }

   //
   //
   //
   private void setRocket(){
      double scale = Double.NaN;
      int min = -1;int max   = -1;int value = -1; String m = "";
      int st  = -1;double ew = Double.NaN;double lw = Double.NaN;
      double to = Double.NaN;double weight= Double.NaN;
      List<StageData> stgs = null;
      try{
         //set the rocket weight as needed
         synchronized(this._obj){
            m  = this._rocketData.model();
            st = this._rocketData.numberOfStages();
            ew = this._rocketData.emptyWeight();
            lw = this._rocketData.loadedWeight();
            to = this._rocketData.tolerance();
            if(this._cond.state() == INIT){
               scale = 0.01;
               min   = (int)(ew*(1-scale));
               max   = (int)(ew*(1+scale));
            }
            value  = (this._random.nextInt(max - min +1) + min);
            weight = (double)value;
            stgs   = this.setStage();
         }
      }
      catch(NullPointerException npe){
         m  = ""; st = -1; ew = Double.NaN; lw = Double.NaN;
         value = -1; to = Double.NaN; weight = Double.NaN;
         stgs = null;
      }
      finally{
         synchronized(this._obj){
            RocketData rd = null;
            rd = new GenericRocketData(m,       //model
                                       st,      //Current Stage
                                       st,      //Number of Stages
                                       ew,      //empty weight
                                       lw,      //loaded weight
                                       weight,  //Calculated weight
                                       false,   //Is Error
                                       null,    //Error
                                       stgs,    //Stages List
                                       to);     //tolerance
            Iterator<StageData> it = stgs.iterator();
            this._measRocketData = rd;
         }
      }
   }

   //
   //
   //
   private List<StageData> setStage(){
      List<StageData> list = null;
      double scale = Double.NaN;
      int min = -1; int max = -1; int value = -1;
      double dw = Double.NaN; double mw = Double.NaN;
      long model = -1; int en = -1; int sn = -1;
      double to = Double.NaN; 
      double weight = Double.NaN; //What needs calculation!
      List<StageData> temp = this._rocketData.stages();
      list = new LinkedList<StageData>();
      Iterator<StageData> it = temp.iterator();
      while(it.hasNext()){
         try{
            StageData sd = it.next();
            dw    = sd.dryWeight();   mw = sd.maxWeight();
            model = sd.model();       en = sd.numberOfEngines();
            sn    = sd.stageNumber(); to = sd.tolerance();
            /*TO BE REMOVED below*/
            //In Lieu of Engine and FuelSystem Data, the Weight value
            //and everything associated shall GO AWAY for ENGINE
            //SYSTEMS DATA COLLECTION AND FUEL SYSTEM data the
            //weight SHALL BE CALCULATED by the stage and compared
            //based on TOLERANCE!!!
            if(this._cond.state() == INIT){
               scale = 0.01;
               min   = (int)(dw*(1-scale));
               max   = (int)(dw*(1+scale));
            }
            value  = (this._random.nextInt(max-min+1)+min);
            weight = (double)value;
            //In Lieu of Engine and FuelSystem Data, the Weight value
            //and everything associated shall GO AWAY for ENGINE
            //SYSTEMS DATA COLLECTION AND FUEL SYSTEM data the
            //weight SHALL BE CALCULATED by the stage and compared
            //based on TOLERANCE!!!
            /*TO BE REMOVED above*/
         }
         catch(NullPointerException npe){
            dw = Double.NaN; mw = Double.NaN; model = -1; en = -1;
            sn = -1; to = Double.NaN; weight = Double.NaN;
         }
         finally{
            StageData sd = new GenericStageData(dw,    //Dry Weight
                                                null,  //Error
                                                model, //Model Number
                                                false, //isError
                                                sn,    //Stage Number
                                                en,    //No. Engines
                                                mw,    //Max Weight
                                                to,    //Tolerance
                                                weight,//Curr Weight
         /*Will change once developed*/         null,  //Engine Data
         /*Will change once developed*/         null   //FuelSystem
                                                );
            list.add(sd);
         }
      }
      return list;
   }

   //
   //
   //
   private void setUpThread(){
      this._rt0 = new Thread(this, "Generic System Data Feeder");
      this._rt0.start();
   }

   ////////////////DataFeeder Interface Implmentation/////////////////
   //
   //
   //
   public void initialize(String file)throws IOException{
      try{
         this.readRocketData(file);
         this.readLaunchingMechanismData(file);
         this.readMechanismSupportData(file);
         this._start = true;
         //this.readTankData(file);
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         throw ioe;
      }
   }

   //
   //
   //
   public LaunchingMechanismData launchMechData(){
      synchronized(this._obj){
         return this._measMechData;
      }
   }

   //Going to go ahead do this correctly and less complex
   //The MechanismSupportData is Separate!!!
   //
   //
   public List<MechanismSupportData> mechSuppData(){
      synchronized(this._obj){
         return this._measSuppData;
      }
   }

   //
   //
   //
   public RocketData rocketData(){
      synchronized(this._obj){
         return this._measRocketData;
      }
   }

   //
   //
   //
   public void setStateSubstate(LaunchStateSubstate cond){
      this._cond = cond;
   }

   //
   //
   //
   public String toString(){
      String s = new String(""+this.getClass().getName());
      /*  Definitely going to NEED TO CHANGE...
      s += "\nHolds Angle:       " + this.angleOfHolds();
      s += "\nEmpty Weight:      " + this.emptyWeight();
      s += "\nHolds Tolerance:   " + this.holdsTolerance();
      s += "\nLoaded Weight:     " + this.loadedWeight();
      s += "\nHolds:             " + this.numberOfHolds();
      s += "\nStages:            " + this.numberOfStages();
      s += "\nPlatform Tolerance " + this.platformTolerance();
      s += "\nWeight:            " + this.weight();
      */
      return s;
   }

   //////////////////Runnable Interface Implementation////////////////
   //
   //
   //
   public void run(){
      try{
         while(true){
            if(this._cond != null){
               //Do this right!!!
               this.setRocket();
               this.setMechanism();
               this.setMechanismSupports();
            }
            Thread.sleep(1);
            //Thread.sleep(1000); //Temp Value
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
