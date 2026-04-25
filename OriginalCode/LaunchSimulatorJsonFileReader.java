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
   public List<Hashtable<String,String>>
   readEngineDataInfo()throws IOException{
      try{ 
         String jsonData = this.grabJSONFileData();
         return this.parseEngineData(jsonData);
      }
      catch(IOException ioe){
         this.closeFile();
         throw ioe;
      }
   }

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
   public Hashtable<String,String>readPathInfo()throws IOException{
      try{
         Hashtable<String,String> data=new Hashtable<String,String>();

         String fileData = this.grabJSONFileData();
         return this.parsePathData(fileData);
      }
      catch(IOException ioe){
         this.closeFile();
         throw ioe;
      }
   }

   //
   //
   //
   public Hashtable<String,String>readPayloadInfo()throws IOException{
      try{
         String jsonData = this.grabJSONFileData();
         return this.parsePayloadData(jsonData);
      }
      catch(IOException ioe){
         this.closeFile();
         throw ioe;
      }
   }

   //
   //
   //
   public List<Hashtable<String,String>>
   readPipeDataInfo()throws IOException{
      try{
         String jsonData = this.grabJSONFileData();
         return this.parsePipeData(jsonData);
      }
      catch(IOException ioe){
         this.closeFile();
         throw ioe;
      }
   }

   //
   //
   //
   public List<Hashtable<String,String>>
   readPumpDataInfo()throws IOException{
      try{
         String jsonData = this.grabJSONFileData();
         return this.parsePumpData(jsonData);
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

   //
   //
   //
   public List<Hashtable<String,String>>
   readStageInfo()throws IOException{
      try{
         String jsonData = this.grabJSONFileData();
         return this.parseStageData(jsonData);
      }
      catch(IOException ioe){
         this.closeFile();
         throw ioe;
      }
   }

   //
   //
   //
   public List<Hashtable<String,String>>
   readTankDataInfo()throws IOException{
      try{
         String jsonData = this.grabJSONFileData();
         return this.parseTankData(jsonData);
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
   private List<Hashtable<String,String>>
   parseEngineData(String data){
      boolean found = data.contains("engine");
      List<Hashtable<String,String>> li = null;
      if(found){
         li = new LinkedList<Hashtable<String,String>>();
         String[] array = data.split("\"engine\"");
         for(int i = 0; i < array.length; ++i){
            String[] saves = new String[data.length()];
            int savesCount = 0;
            Hashtable<String,String> ht = null;
            String current = array[i].strip();
            char char0 = current.charAt(0);
            char char1 = current.charAt(1);
            if(char0==':' && (char1==' ' || char1=='{')){
               ht = new Hashtable<String, String>();
               int first = current.indexOf("{");
               int sec   = current.indexOf("}");
               current   = current.substring(first+1, sec).strip();
               String[] engData = current.split(",");
               for(int j = 0; j < engData.length; ++j){
                  engData[j] = engData[j].strip();
                  String[] temp = engData[j].split(":");
                  for(int k = 0; k < temp.length; ++k){
                     temp[k]=temp[k].strip();
                     //Remove the "...from the beginning and end
                     temp[k]=temp[k].substring(1,temp[k].length()-1);
                     if(temp[k].length() > 0){
                        char c = temp[k].charAt(0);
                        boolean isChar = Character.isLetter(c);
                        boolean isNum  = Character.isDigit(c);
                        if(isChar || isNum || c == '.'){
                           saves[savesCount] = temp[k];
                        }
                        else{
                           saves[savesCount] = "<no data>";
                        }
                        ++savesCount;
                     }
                  }
               }
               for(int j = 0; j < savesCount; j += 2){
                  try{
                     ht.put(saves[j],saves[j+1]);
                  }
                  catch(ArrayIndexOutOfBoundsException e){
                     ht.put(saves[j],new String("<no data>"));
                  }
                  catch(NullPointerException npe){
                     ht.put(saves[j],new String("<no data>"));
                  }
               }
               li.add(ht);
            }
         }
      }
      return li;
   }

   //
   //
   //
   private Hashtable<String,String> 
   parseLaunchingMechanismData(String data){
      boolean found = data.contains("launching_mechanism");

      String[] saves              = new String[data.length()];
      int savesCount              = 0;
      Hashtable<String,String> ht = null;
      if(found){
         ht = new Hashtable<String,String>();
         String[] array = data.split("\"launching_mechanism\"");
         for(int i = 0; i < array.length; ++i){
            String current = array[i].strip();
            if(current.charAt(0) == ':'){
               int first = current.indexOf("{");
               int sec   = current.indexOf("}");
               current   = current.substring(first+1, sec);
               String[] mechData = current.split(",");
               for(int j = 0; j < mechData.length; ++j){
                  mechData[j] = mechData[j].strip();
                  String[] temp = mechData[j].split(":");
                  for(int k = 0; k < temp.length; ++k){
                     temp[k]      = temp[k].strip();
                     String[] sel = temp[k].split("\"");
                     for(int l = 0; l < sel.length; ++l){
                        sel[l] = sel[l].strip();
                        if(sel[l].length() > 0){
                           char c = sel[l].charAt(0);
                           if(Character.isLetter(c) ||
                              Character.isDigit(c) || c=='.'){
                              saves[savesCount] = sel[l];
                           }
                           else{
                              saves[savesCount] = "<no data>";
                           }
                           ++savesCount;
                        }
                     }
                  }
               }
               break;
            }
         }
      }
      for(int i = 0; i < savesCount; i+=2){
         try{
            ht.put(saves[i], saves[i+1]);
         }
         catch(ArrayIndexOutOfBoundsException e){
            ht.put(saves[i], new String("<no data>"));
         }
         catch(NullPointerException npe){
            ht.put(saves[i], new String("<no data>"));
         }
      }
      return ht;
   }

   //
   //
   //
   private Hashtable<String,String> parsePathData(String data){
      Hashtable<String,String> ht = null;
      if(data.contains("parameter")){
         ht = new Hashtable<String,String>();
         for(int i = 0; i < data.split(",").length; ++i){
            String key     = null;
            String value   = null;
            String current = data.split(",")[i];
            if(current.contains("{")){
               int len = current.split(" ").length;
               current = current.split(" ")[len-1].strip();
            }
            else if(current.contains("}")){
               int len = current.split(" ").length;
               current = current.split(" ")[len-1];
               current = current.substring(0,current.length()-1);
            }
            try{
               key    = current.split(":")[0].strip();
               value  = current.split(":")[1].strip();
               value += ":"+current.split(":")[2].strip();
            }
            catch(ArrayIndexOutOfBoundsException e){
               if(value.length() == 0){
                  value = new String("<No Data>");
               }
            }
            catch(NullPointerException npe){
               value = new String("<No Data>");
            }
            finally{
               //Strip off the quotes
               key    = key.substring(1,key.length()-1);
               value  = value.substring(1,value.length()-1);
               ht.put(key,value);
            }
         }
      }
      return ht;
   }

   //
   //
   //
   private Hashtable<String,String> parsePayloadData(String data){
      Hashtable<String,String> ht = null;
      boolean found               = data.contains("payload");
      if(found){
         ht             = new Hashtable<String,String>();
         String[] array = data.split("\"payload\"");
         for(int i = 0; i < array.length; ++i){
            String[] saves = new String[data.length()];
            int savesCount = 0;
            String current = array[i].strip();
            char char0     = current.charAt(0);
            char char1     = current.charAt(1);
            if(char0 == ':' && (char1 == ' ' || char1 == '{')){
               ht = new Hashtable<String,String>();
               int first      = current.indexOf("{");
               int sec        = current.indexOf("}");
               current        = current.substring(first+1, sec);
               String[] payld = current.split(",");
               for(int j = 0; j < payld.length; ++j){
                  payld[j] = payld[j].strip();
                  String[] temp = payld[j].split(":");
                  for(int k = 0; k < temp.length; ++k){
                     int beg = 1;
                     int end = temp[k].length() - 1;
                     temp[k] = temp[k].substring(beg, end);
                     if(temp[k].length() > 0){
                        char c         = temp[k].charAt(0);
                        boolean isChar = Character.isLetter(c);
                        boolean isNum  = Character.isDigit(c);
                        if(isChar || isNum || c == '.'){
                           saves[savesCount] = temp[k];
                        }
                        else{
                           saves[savesCount] = "<No Data>";
                        }
                        ++savesCount;
                     }
                  }
               }
               for(int j = 0; j < savesCount; j += 2){
                  try{
                     ht.put(saves[j], saves[j+1]);
                  }
                  catch(ArrayIndexOutOfBoundsException e){
                     ht.put(saves[j], "<No Data>");
                  }
                  catch(NullPointerException npe){
                     ht.put(saves[j], "<No Data>");
                  }
               }
            }
         }
      }
      return ht;
   }

   //
   //
   //
   private List<Hashtable<String,String>>parsePipeData(String data){
      boolean found                    = data.contains("pipe");
      List<Hashtable<String,String>> li = null;

      if(found){
         li = new LinkedList<Hashtable<String,String>>();
         String[] array = data.split("\"pipe\"");
         for(int i = 0; i < array.length; ++i){
            String[] saves = new String[data.length()];
            int savesCount = 0;
            Hashtable<String,String> ht = null;
            String current = array[i].strip();
            char char0     = current.charAt(0);
            char char1     = current.charAt(1);
            if(char0 == ':' && (char1 == ' ' || char1 == '{')){
               ht = new Hashtable<String,String>();
               int first = current.indexOf("{");
               int sec   = current.indexOf("}");
               current   = current.substring(first+1,sec);
               String[] pipeData = current.split(",");
               for(int j = 0; j < pipeData.length; ++j){
                  pipeData[j] = pipeData[j].strip();
                  String[] temp = pipeData[j].split(":");
                  for(int k = 0; k < temp.length; ++k){
                     int beg = 1;
                     int end = temp[k].length() - 1;
                     temp[k] = temp[k].substring(beg, end);
                     if(temp[k].length() > 0){
                        char c         = temp[k].charAt(0);
                        boolean isChar = Character.isLetter(c);
                        boolean isNum  = Character.isDigit(c);
                        if(isChar || isNum || c == '.'){
                           saves[savesCount] = temp[k];
                        }
                        else{
                           saves[savesCount] = "<No Data>";
                        }
                        ++savesCount;
                     }
                  }
               }
               for(int j = 0; j < savesCount; j += 2){
                  try{
                     ht.put(saves[j],saves[j+1]);
                  }
                  catch(ArrayIndexOutOfBoundsException e){
                     ht.put(saves[j],"<no data>");
                  }
                  catch(NullPointerException npe){
                     ht.put(saves[j],"<no data>");
                  }
               }
               li.add(ht);
            }
         }
      }
      return li;
   }

   //
   //
   //
   private List<Hashtable<String,String>> parsePumpData(String data){
      boolean found = data.contains("pump");
      List<Hashtable<String,String>> li = null;

      if(found){
         li = new LinkedList<Hashtable<String,String>>();
         String[] array = data.split("\"pump\"");
         for(int i = 0; i < array.length; ++i){
            String[] saves = new String[data.length()];
            int savesCount = 0;
            Hashtable<String,String> ht = null;
            String current = array[i].strip();
            char char0     = current.charAt(0);
            char char1     = current.charAt(1);
            if(char0 == ':' && (char1 == ' ' || char1 == '{')){
               ht        = new Hashtable<String,String>();
               int first = current.indexOf("{");
               int sec   = current.indexOf("}");
               current   = current.substring(first+1,sec);
               String[] pumpData= current.split(",");
               for(int j = 0; j < pumpData.length; ++j){
                  pumpData[j]   = pumpData[j].strip();
                  String[] temp = pumpData[j].split(":");
                  for(int k = 0; k < temp.length; ++k){
                     int beg = 1;
                     int end = temp[k].length() - 1;
                     temp[k] = temp[k].substring(beg,end);
                     if(temp[k].length() > 0){
                        char c         = temp[k].charAt(0);
                        boolean isChar = Character.isLetter(c);
                        boolean isNum  = Character.isDigit(c);
                        if(isChar || isNum || c == '.'){
                           saves[savesCount] = temp[k];
                        }
                        else{
                           saves[savesCount] = "<No Data>";
                        }
                        ++savesCount;
                     }
                  }
               }
               for(int j = 0; j < savesCount; j += 2){
                  try{
                     ht.put(saves[j], saves[j+1]);
                  }
                  catch(ArrayIndexOutOfBoundsException e){
                     ht.put(saves[j],"<no data>");
                  }
                  catch(NullPointerException npe){
                     ht.put(saves[j],"<no data>");
                  }
               }
               li.add(ht);
            }
         }
      }
      return li;
   }

   //
   //
   //
   private Hashtable<String,String> parseRocketData(String data){
      boolean found               = data.contains("rocket");
      String  rocketS             = null;
      String[] saves              = new String[data.length()];
      int savesCount              = 0;
      Hashtable<String,String> ht = null;
      if(found){
         ht = new Hashtable<String,String>();
         String[] array = data.split("\"rocket\"");
         for(int i = 0; i < array.length; ++i){
            String current = array[i].strip();
            if(current.charAt(0) == ':'){
               int first = current.indexOf('{');
               int sec   = current.indexOf('}');
               current   = current.substring(first+1, sec);
               String[] rocketdata = current.split(",");
               for(int j = 0; j < rocketdata.length; ++j){
                  rocketdata[j] = rocketdata[j].strip();
                  String[] temp = rocketdata[j].split(":");
                  for(int k = 0; k < temp.length; ++k){
                     temp[k]      = temp[k].strip();
                     String[] sel = temp[k].split("\"");
                     for(int l = 0; l < sel.length; ++l){
                        sel[l] = sel[l].strip();
                        if(sel[l].length() > 0){
                           char c = sel[l].charAt(0);
                           boolean isChar = Character.isLetter(c);
                           boolean isNum  = Character.isDigit(c);
                           if(isChar||isNum||c=='.'){
                              saves[savesCount] = sel[l];
                           }
                           else{
                              saves[savesCount] = "<no data>";
                           }
                           ++savesCount;
                        }
                     }
                  }
               }
               break;
            }
         }
      }
      for(int i = 0; i < savesCount; i += 2){
         try{
            ht.put(saves[i],saves[i+1]);
         }
         catch(ArrayIndexOutOfBoundsException e){
            ht.put(saves[i],new String("<no data>"));
         }
         catch(NullPointerException npe){
            ht.put(saves[i],new String("<no data>"));
         }
      }
      return ht;
   }

   //To allow for multible Stages--store the data contiguously...
   //
   //
   private List<Hashtable<String,String>>parseStageData(String data){
      //There will need to be some testing....
      boolean found               = data.contains("stage");
      List<Hashtable<String,String>> li = null;
      if(found){
         li = new LinkedList<Hashtable<String,String>>();
         String array[] = data.split("\"stage\"");
         for(int i = 0; i < array.length; ++i){
            String[] saves = new String[data.length()];
            int savesCount = 0;
            Hashtable<String,String> ht = null;
            String current = array[i].strip();
            char char0 = current.charAt(0);
            char char1 = current.charAt(1);
            if(char0 == ':' && (char1 == ' '|| char1 == '{')){
               ht = new Hashtable<String,String>();
               int first = current.indexOf("{");
               int sec   = current.indexOf("}");
               current = current.substring(first+1,sec);
               String[] stageData = current.split(",");
               for(int j = 0; j < stageData.length; ++j){
                  stageData[j] = stageData[j].strip();
                  String[] temp = stageData[j].split(":");
                  for(int k = 0; k < temp.length; ++k){
                     temp[k]      = temp[k].strip();
                     String[] sel = temp[k].split("\"");
                     for(int l = 0; l < sel.length; ++l){
                        sel[l] = sel[l].strip();
                        if(sel[l].length() > 0){
                           char c = sel[l].charAt(0);
                           boolean isChar = Character.isLetter(c);
                           boolean isNum  = Character.isDigit(c);
                           if(isChar||isNum||c=='.'){
                              saves[savesCount] = sel[l];
                           }
                           else{
                              saves[savesCount] = "<no data>";
                           }
                           ++savesCount;
                        }
                     }
                  }
               }
               for(int j = 0; j < savesCount; j += 2){
                  try{
                     ht.put(saves[j],saves[j+1]);
                  }
                  catch(ArrayIndexOutOfBoundsException e){
                     ht.put(saves[j],new String("<no data>"));
                  }
                  catch(NullPointerException npe){
                     ht.put(saves[j],new String("<no data>"));
                  }
               }
               li.add(ht);
            }
         }
      }
      return li;
   }

   //
   //
   //
   private List<Hashtable<String,String>>parseTankData(String data){
      boolean found                     = data.contains("tank");
      List<Hashtable<String,String>> li = null;
      if(found){
         li = new LinkedList<Hashtable<String,String>>();
         String[] array = data.split("\"tank\"");
         for(int i = 0; i < array.length; ++i){
            String[] saves = new String[data.length()];
            int savesCount = 0;
            Hashtable<String,String> ht = null;
            String current = array[i].strip();
            char char0     = current.charAt(0);
            char char1     = current.charAt(1);
            if(char0 == ':' && (char1 == ' '|| char1 == '{')){
               ht = new Hashtable<String,String>();
               int first = current.indexOf("{");
               int sec   = current.indexOf("}");
               current   = current.substring(first+1,sec);
               String[] tankData = current.split(",");
               for(int j = 0; j < tankData.length; ++j){
                  tankData[j]   = tankData[j].strip();
                  String[] temp = tankData[j].split(":");
                  for(int k = 0; k < temp.length; ++k){
                     temp[k]=temp[k].substring(1,temp[k].length()-1);
                     if(temp[k].length() > 0){
                        char c = temp[k].charAt(0);
                        boolean isChar = Character.isLetter(c);
                        boolean isNum  = Character.isDigit(c);
                        if(isChar || isNum ||c == '.'){
                           saves[savesCount] = temp[k];
                        }
                        else{
                           saves[savesCount] = "<no data>";
                        }
                        ++savesCount;
                     }
                  }
               }
               for(int j = 0; j < savesCount; j += 2){
                  try{
                     ht.put(saves[j],saves[j+1]);
                  }
                  catch(ArrayIndexOutOfBoundsException e){
                     ht.put(saves[j],"<no data>");
                  }
                  catch(NullPointerException npe){
                     ht.put(saves[j],"<no data>");
                  }
               }
               li.add(ht);
            }
         }
      }
      return li;
   }

   //
   //
   //
   private void setPathAndFile(String file){
      this._pathAndFile = file;
   }
}
//////////////////////////////////////////////////////////////////////
