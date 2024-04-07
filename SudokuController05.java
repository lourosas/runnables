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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import rosas.lou.runnables.*;

public class SudokuController05 implements ActionListener,KeyListener{
   private Subscriber      _subscriber;
   private SudokuInterface _sudoku;
   private JFrame          _frame;

   {
      _subscriber = null;
      _sudoku     = null;
      _frame      = null;
   };

   ///////////////////////Constructors////////////////////////////////
   //
   //
   //
   public SudokuController05(){}

   ///////////////////////Public Methods//////////////////////////////
   //
   //
   //
   public void addFrame(JFrame frame){
      this._frame = frame;
   }

   //
   //
   //
   public void addModel(SudokuInterface sudoku){
      this._sudoku = sudoku;
   }

   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      this._subscriber = subscriber;
      try{
         this._sudoku.addSubscriber(this._subscriber);
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //////////////////////Private Methods//////////////////////////////
   //
   //
   //
   private void getNewGameInfo(){
      String input = "Type \"Yes\" to open a Sudoku Puzzle\n";
      input += "Type \"No\" to input a Puzzle Manually";
      String title = "Open File or Input Manually?";
      int n = JOptionPane.showConfirmDialog(
                 this._frame,
                 input,
                 title,
                 JOptionPane.YES_NO_OPTION);
      if(n == 0){
         this.inputSudokuTextFile();
      }
      else if(n == 1){
         this.inputSudokuManually();
      }
   }

   //
   //
   //
   private void getSaveSolutionToFileInfo(){}

   //
   //
   //
   private void inputSudokuManually(){}

   //
   //
   //
   private void inputSudokuTextFile(){
      String t = "*.txt,*.text";
      JFileChooser chooser      = new JFileChooser();
      FileNameExtensionFilter f =
                          new FileNameExtensionFilter(t,"txt","text");
      chooser.setFileFilter(f);
      int value = chooser.showOpenDialog(this._frame);
      if(value == 0){
         String path = chooser.getSelectedFile().getPath();
         this._sudoku.open(path);
      }
   }

   ////////////////////////Interface Implementation///////////////////
   ////////////////////////////Action Listener////////////////////////
   public void actionPerformed(ActionEvent e){
      try{
         JButton b      = (JButton)e.getSource();
         String command = b.getActionCommand().toUpperCase();
         if(command.equals("SOLVE")){
            this._sudoku.solve();
         }
         else if(command.equals("CLEAR")){
            this._sudoku.clear();
         }
         else if(command.equals("NEWGAME")){
            this.getNewGameInfo();
         }
         else if(command.equals("SAVE")){
            this.getSaveSolutionToFileInfo();
         }
	 else if(command.equals("MANUAL_SET")){}
	 else if(command.equals("MANUAL_SAVE")){}
      }
      catch(ClassCastException cce){}
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   /////////////////////////////Key Listener//////////////////////////
   public void keyPressed(KeyEvent k){}

   public void keyReleased(KeyEvent k){}

   public void keyTyped(KeyEvent k){}
}

//////////////////////////////////////////////////////////////////////
