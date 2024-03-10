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
   private enum State{STARTUP,NEWGAME,CLEARED,SOLVED,ERROR};
   private State        _state;
   private SudokuEngine _engine;
   List<Subscriber>     _subscribers;

   {
      _state       = State.STARTUP;
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
      RuntimeException re = new RuntimeException(error);
      try{
         String message      = new String(error);
         String state        = new String("No State");
         if(this._state == State.ERROR){
            state = new String("ERROR");
         }
         SudokuBlock[][] block = this._engine.getBlock();
         SudokuState ss        = new SudokuState(message,state,block);
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            it.next().error(re,ss);
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
            state = new String("CLEARED");
         }
         else if(this._state == State.SOLVED){
            state = new String("SOLVED");
         }
         else if(this._state == State.STARTUP){
            state = new String("STARTUP");
         }
         SudokuBlock[][] block = this._engine.getBlock();
         SudokuState ss        = new SudokuState(message,state,block);
         Iterator<Subscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            //Inform the Subscribers of a 2-D array
            //it.next().update(this._engine.getBlock(), "double");
            it.next().update(ss);
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
         int[][] array        = sfr.returnSudoku();
         //this._state          = State.NEWGAME;
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

   //
   //
   //
   private void setPuzzle(String[] input){
      int[][] grid = new int[9][9];
      int count    = 0;
      //this._state  = State.NEWGAME;
      for(int row = 0; row < 9; ++row){
         for(int col = 0; col < 9; ++col){
            int val = -1;
            try{
               val = Integer.parseInt(input[count]);
            }
            catch(NumberFormatException nfe){
               val = 0;
            }
            finally{
               ++count;
               grid[row][col] = val;
            }
         }
      }
      this.set(grid);
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
   public void clear(){
      if(this._engine.clearSudoku()){
         this._state = State.CLEARED;
         this.notifySubscribers();
      }
      else{
         this._state = State.ERROR;
         this.notifyErrors("Could Not Clear the Puzzle");
      }
   }

   //
   //
   //
   public void open(String pathAndFile){
      if(pathAndFile != null){
         this.setPuzzle(pathAndFile);
      }
   }

   //
   //
   //
   public void set(int[][] grid){
      if(this._state == State.ERROR||this._state == State.SOLVED){
         this.clear();
      }
      this._engine.setBlock(grid);
      if(this._state != State.NEWGAME){
         this._state = State.NEWGAME;
      }
      this.notifySubscribers();
   }

   //
   //
   //
   public void set(SudokuBlock[][] grid){
      if(this._state == State.ERROR||this._state == State.SOLVED){
         this.clear();
      }
      this._engine.setBlock(grid);
      if(this._state != State.NEWGAME){
         this._state = State.NEWGAME;
      }
      this.notifySubscribers();
   }

   //
   //
   //
   public void set(String[] input){
      this.setPuzzle(input);
   }

   //
   //
   //
   public void solve(){
      if(this._engine.solve()){
         this._state = State.SOLVED;
         this.notifySubscribers();
      }
      else{
         //If the Sudoku is not solvable, New Game, or Startup?
         this._state = State.ERROR;
         //this methoed will need to change...will need to think
         //about that...
         this.notifyErrors("No Solution Exists");
      }
   }
}
