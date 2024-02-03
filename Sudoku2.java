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

public class Sudoku2 implements SudokuInterface{
   private SudokuEngine _engine;
   List<Subscriber>     _subscribers;

   {
      _engine      = null;
      _subscribers = null;
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public Sudoku2(){
      this._engine = new SudokuEngine();
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void notifyErrors(String error){
      try{
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            //Inform the subscribers of the error
            it.next().error(error);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //
   //
   //
   private void notifySubscribers(){
      try{
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            //Inform the Subscribers of a 2-D array
            it.next().update(this._engine.getBlock(), "double");
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //
   //
   //
   private void setPuzzle(String pathAndFile){
      FileReader fr     = null;
      BufferedReader br = null;
      try{
         fr          = new FileReader(pathAndFile);
         br          = new BufferedReader(fr);
         String line = new String();
         while((line = br.readLine()) != null){
            String[] current = line.split(",");
            for(int i = 0; i < current.length; ++ i){
               String temp;
               current[i] = current[i].trim();
               if(current[i].length() > 1){
                  for(int i = 0; i < current[i].length(); ++i){
                     char val = current[i].charAt(i);
                     if((val >= '0' && val <= '9' || val=='-'){}
                  }
               }
            }
            System.out.println();
         }
         /*
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
      }
      catch(FileNotFoundException fnfe){
         try{ br.close(); }
         catch(IOException ioe){}
         this.notifyErrors(fnfe.getMessage());
      }
      catch(IOException ioe){
         try{ br.close(); }
         catch(IOException io){}
         this.notifyErrors(ioe.getMessage());
      }
   }

   /////////////////////Interface Implementations/////////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      try{
         this._subscribers.add(subscriber);
      }
      catch(NullPointerException npe){
         this._subscribers = new LinkedList<Subscriber>();
         this._subscribers.add(subscriber);
      }
      finally{
         this.notifySubscribers();
      }
   }

   //
   //
   //
   public void open(String pathAndFile){
      this.setPuzzle(pathAndFile);
   }

   //
   //
   //
   public void set(int[][] grid){
      this._engine.setBlock(grid);
      this.notifySubscribers();
   }

   //
   //
   //
   public void set(SudokuBlock[][] grid){
      this._engine.setBlock(grid);
      this.notifySubscribers();
   }

   //
   //
   //
   public void solve(){
      if(this._engine.solve()){
         this.notifySubscribers();
      }
      else{
         this.notifyErrors("No Solution Exists");
      }
   }
}
