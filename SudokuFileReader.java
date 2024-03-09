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

public class SudokuFileReader{
   private FileReader     fr;
   private BufferedReader br;
   private String         pathAndFile;

   {
      fr          = null;
      br          = null;
      pathAndFile = null;
   };

   /////////////////////////////Constructors//////////////////////////
   //
   //
   //
   public SudokuFileReader(){}

   //
   //
   //
   public SudokuFileReader(String fileAndPath){
      this.setPathAndFile(fileAndPath);
   }

   ////////////////////////////Public Methods/////////////////////////
   //
   //
   //
   public void setPathAndFile(String fileAndPath){
      this.pathAndFile = fileAndPath;
   }

   //
   //
   //
   public int[][] returnSudoku() throws FileNotFoundException,
   IOException, RuntimeException{
      int     row   = 0;
      int     col   = 0;
      int[][] block = new int[9][9];
      fr            = new FileReader(this.pathAndFile);
      br            = new BufferedReader(fr);
      String line   = new String();
      while((line = br.readLine()) != null){
         String[] current = line.split(",");
         for(int i = 0; i < current.length; ++i){
            String temp  = current[i].trim();
            String[] arr = temp.split(" ");
            String cur   = null;
            for(int j = 0; j < arr.length; ++j){
               try{
                  Integer val = Integer.valueOf(arr[j]);
                  block[row][col] = val.intValue();
                  ++col;
               }
               catch(NumberFormatException nfe){
                  if(!(arr[j].contains("{") || 
                       arr[j].contains("}") ||
                       arr[j].contains(";"))){
                     String message =
                                  new String(" Non-number entry in ");
                     message += "puzzle";
                     throw new RuntimeException(
                                          nfe.getMessage() + message);
               
                  }
               }
               catch(ArrayIndexOutOfBoundsException oobe){
                  String message=new String(" Too many entries for ");
                  message += "Sudoku ";
                  throw new RuntimeException(
                                           oobe.getMessage()+message);
               }
            }
         }
         //Let me see if this will simply do the trick...
         //if not, more complex logic
         col = 0;
         ++row;
      }
      /*
      Alternative Below...
      int val;
      while((val = br.read()) != -1){
         if((val >= '0' && val <= '9')){
            int num = val - 0x30;
            //System.out.print(num+" ");
            System.out.printf("%d ",val-0x30);
         }
      }
      br.close();
      */
      try{ br.close();}
      catch(IOException ioe){}
      return block;
   }
}

//////////////////////////////////////////////////////////////////////
