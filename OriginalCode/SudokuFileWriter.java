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

public class SudokuFileWriter{
   private FileOutputStream   fos;
   private OutputStreamWriter osw;
   private PrintWriter        pw;
   private String             pathAndFile;

   {
      fos         = null;
      osw         = null;
      pw          = null;
      pathAndFile = null;
   };

   ////////////////////////Constructors///////////////////////////////
   //
   //
   //
   public SudokuFileWriter(){}

   //
   //
   //
   public SudokuFileWriter(String fileAndPath){
      this.setPathAndFile(fileAndPath);
   }

   /////////////////////////Public Methods////////////////////////////
   //
   //
   //
   public void savePuzzle(SudokuBlock[][] block)
   throws RuntimeException{
      final int ROWS = 9;
      final int COLS = 9;
      int row = 0;
      int col = 0;
      FileOutputStream fos   = null;
      OutputStreamWriter osw = null;
      PrintWriter pw         = null;
      try{
         fos = new FileOutputStream(this.pathAndFile, false);
         osw = new OutputStreamWriter(fos);
         pw  = new PrintWriter(osw, true);
         pw.print("{ { ");
         for(int i = 0; i < ROWS; ++i){
            for(int j = 0; j < COLS; ++j){
               pw.print(block[i][j]);
               if(j < COLS - 1){
                  pw.print(", ");
               }
            }
            if(i < ROWS - 1){
               pw.print(" },\n  { ");
            }
            else{
               pw.print(" } ");
            }
         }
         pw.println("};");
      }
      catch(IOException ioe){
         throw new RuntimeException(ioe.getMessage());
      }
      catch(SecurityException se){
         throw new RuntimeException(se.getMessage());
      }
      finally{
         try{
            fos.close();
            pw.close();
         }
         catch(IOException ioe){}
         catch(NullPointerException npe){}
      }
   }

   //
   //
   //
   public void saveSolution(SudokuBlock[][] block)
   throws RuntimeException{
      final int ROWS = 9;
      final int COLS = 9;
      int row = 0;
      int col = 0;
      try{
         fos = new FileOutputStream(this.pathAndFile, false);
         osw = new OutputStreamWriter(fos);
         pw  = new PrintWriter(osw,true);
         for(int i = 0; i < ROWS; ++i){
            for(int j = 0; j < COLS; ++j){
               //pw.print(block[i][j].value());
               pw.print(block[i][j]);
               if(j < COLS - 1){
                  pw.print(", ");
               }
            }
            pw.println();
         }
      }
      catch(IOException ioe){
         throw new RuntimeException(ioe.getMessage());
      }
      catch(SecurityException se){
         throw new RuntimeException(se.getMessage());
      }
      finally{
         try{
            fos.close();
            pw.close();
         }
         catch(IOException ioe){}
      }
   }

   ///////////////////////Private Methods/////////////////////////////
   //
   //
   //
   private void setPathAndFile(String fileAndPath){
      this.pathAndFile = new String(fileAndPath);
   }
}

//////////////////////////////////////////////////////////////////////
