//////////////////////////////////////////////////////////////////////
/*
Copyright 2023 Lou Rosas

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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import rosas.lou.runnables.*;

public class SudokuController implements ActionListener, KeyListener{
   private Subscriber      _subscriber;
   //private Sudoku     _sudoku;
   private SudokuInterface _sudoku;

   {
      _subscriber = null;
      _sudoku     = null;
   };

   //////////////////////////Constructor//////////////////////////////
   //
   //
   //
   public SudokuController(){}

   /////////////////////////Public Methods////////////////////////////
   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      this._subscriber = subscriber;
   }

   //
   //
   //
   public void addModel(Sudoku sudoku){
      this._sudoku = sudoku;
      this._sudoku.addSubscriber(this._subscriber);
   }

   //
   //
   //
   public void addModel(SudokuInterface sudoku){
      this._sudoku = sudoku;
      this._sudoku.addSubscriber(this._subscriber);
   }

   /////////////////Interface Implementation//////////////////////////
   ////////////////////////Action Listener////////////////////////////
   //
   //
   //
   public void actionPerformed(ActionEvent e){
      try{
         JButton b = (JButton)e.getSource();
         String command = b.getActionCommand().toUpperCase();
         if(command.equals("SOLVE")){
            this._sudoku.solve();
         }
         else if(command.equals("CLEAR")){
            System.out.println(command);
         }
         else if(command.equals("NEWGAME")){
            this._subscriber.update(this,command);
         }
         else if(command.equals("SAVE")){
            System.out.println(command);
         }
         else if(command.equals("OPENTEXTFILE")){
            this._subscriber.update(this,command); 
         }
	 else if(command.equals("MANUALENTER")){
            System.out.println(command);
         }
         else if(command.equals("TEXTFILEPATH")){
            //The Path to the text file to open
            this._sudoku.open(b.getText());
         }
      }
      catch(ClassCastException cce){}
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //////////////////////////Key Listener/////////////////////////////
   //
   //
   //
   public void keyPressed(KeyEvent k){
      if(k.getKeyCode() == KeyEvent.VK_ENTER){
         try{
            JButton b = (JButton)k.getSource();
            b.doClick(150);
         }
         catch(ClassCastException cce){}
      }
   }

   //
   //
   //
   public void keyReleased(KeyEvent k){}

   //
   //
   //
   public void keyTyped(KeyEvent k){}
}
//////////////////////////////////////////////////////////////////////
