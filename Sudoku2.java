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
   private enum State{NEWGAME,CLEARED,SOLVED,ERROR};
   private State        _state;
   private SudokuEngine _engine;
   List<Subscriber>     _subscribers;

   {
      _state       = State.NEWGAME;
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
         String message = new String("double");
         String state   = new String("No State");
         if(this._state == State.NEWGAME){
            state = new String("NEWGAME");
         }
         else if(this._state == State.CLEARED){
            state = new String("Cleared");
         }
         else if(this._state == State.SOLVED){
            state = new String("Solved");
         }
         SudokuBlock[][] block = this._engine.getBlock();
         SudokuState ss = new SudokuState(message,state,block);
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
      try{
         SudokuFileReader sfr = new SudokuFileReader(pathAndFile);
         int[][] array = sfr.returnSudoku();
         this._state = State.NEWGAME;
         this.set(array);
      }
      catch(FileNotFoundException fnfe){
         String message = fnfe.getMessage();
         this._state = State.ERROR;
         this.notifyErrors("File Not Found Exception: "+message);
      }
      catch(IOException ioe){
         this._state = State.ERROR;
         this.notifyErrors("IO Exception: "+ioe.getMessage());
      }
      catch(RuntimeException re){
         this._state = State.ERROR;
         this.notifyErrors("Runtime Exception: " + re.getMessage()); 
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
