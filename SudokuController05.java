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
   private void getManualSaveToFileInfo(){
      java.io.File file         = null;
      String path               = null;
      boolean toShow            = false;
      JFileChooser chooser      = new JFileChooser();
      FileNameExtensionFilter f = new FileNameExtensionFilter(
                                                       "*.txt,*.text",
                                                       "txt",
                                                       "text");
      chooser.setFileFilter(f);
      do{
         int value = chooser.showSaveDialog(this._frame);
         if(value == JFileChooser.APPROVE_OPTION){
            file = chooser.getSelectedFile();
            if(file.exists()){
               String error   = "FILE EXISTS!";
               String message = new String("File: "+file.getPath());
               message += "\nalready exists!\nOverwrite the file?";
               int ans  = JOptionPane.showConfirmDialog(
                                           this._frame,
                                           message,
                                           error,
                                           JOptionPane.YES_NO_OPTION);
               toShow = (ans != JOptionPane.YES_OPTION);
            }
            else{
               toShow = false;
            }
            if(!toShow){
               SudokuManualEntryView2 instance = null;
               instance   = SudokuManualEntryView2.instance();
               String[] s = instance.returnSudokuInput(false);
               this._sudoku.savePuzzle(file.getPath(),s);
            }
         }
      }while(toShow);
   }

   //
   //
   //
   private void getManualSetInfo(){
      try{
         SudokuManualEntryView2 instance = null;
         instance = SudokuManualEntryView2.instance();
         String[] s = instance.returnSudokuInput(false);
         this._sudoku.set(s);
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void getNewGameInfo(){
      String input = "Press \"Yes\" to open a Sudoku Puzzle\n";
      input += "Press \"No\" to input a Puzzle Manually";
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
   private void inputSudokuManually(){
      SudokuManualEntryView2.instance("Manual Input",
                                      this,
                                      this._frame);
   }

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
         else if(command.equals("MANUAL_SET")){
            this.getManualSetInfo();
         }
         else if(command.equals("MANUAL_SAVE")){
            this.getManualSaveToFileInfo();
         }
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
