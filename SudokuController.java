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
import javax.swing.filechooser.*;
import rosas.lou.runnables.*;

public class SudokuController implements ActionListener, KeyListener{
   private Subscriber      _subscriber;
   private SudokuInterface _sudoku;
   private JFrame          _frame; 

   {
      _subscriber = null;
      _sudoku     = null;
      _frame      = null;
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
   public void addFrame(JFrame frame){
      this._frame = frame;
   }

   //
   //
   //
   public void addSubscriber(Subscriber subscriber){
      this._subscriber = subscriber;
   }

   //
   //
   //
   public void addModel(SudokuInterface sudoku){
      this._sudoku = sudoku;
      this._sudoku.addSubscriber(this._subscriber);
   }

   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private void getManualSaveToFileInfo(){
      java.io.File file         = null;
      String path               = null;
      boolean toShow            = true;
      JFileChooser chooser      = new JFileChooser();
      FileNameExtensionFilter f =
             new FileNameExtensionFilter("*.txt,*.text","txt","text");
      chooser.setFileFilter(f);
      while(toShow){
         toShow    = false;
         path      = null;
         int value = chooser.showSaveDialog(this._frame);
         if(value == JFileChooser.APPROVE_OPTION){
            file = chooser.getSelectedFile();
            path = file.getPath();
            if(file.exists()){
               String error    = new String("FILE EXISTS!");
               String message  = new String("File: "+path);
               message += "\nalready exists!\nOverwrite the file?";
               int ans = JOptionPane.showConfirmDialog(this._frame,
                             message,error,JOptionPane.YES_NO_OPTION);   
               toShow = (ans != JOptionPane.YES_OPTION);
   	      }
            if(!toShow){
               SudokuManualEntryView instance = null;
               instance   = SudokuManualEntryView.instance();
               //May not need to do this->Save and Display...
               //String[] s = instance.returnSudokuInput(true);
               String[] s = instance.returnSudokuInput(false);
               //Try this for now...
               this._sudoku.savePuzzle(file.getPath(),s);
            }
         }
      }
      /* This part is over complex bull shit
      try{
         if(path == null){
            throw new NullPointerException();
         }
         SudokuManualEntryView instance = null;
         instance   = SudokuManualEntryView.instance();
         String[] s = instance.returnSudokuInput(true);
         this._sudoku.savePuzzle(path, s);
      }
      catch(NullPointerException npe){}
      */
   }

   //
   //
   //
   private void getNewGameInfo(){
      int n = JOptionPane.showConfirmDialog(
                 this._frame,
                 "Enter A Text File Puzzle?",
                 "Text File for Manual Entry?",
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
   private void getSaveSolutionToFileInfo(){
      java.io.File file = null;
      String path       = null;
      boolean toShow    = true;
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter f=new FileNameExtensionFilter(
                             "*.txt,*.text,*.sln","txt","text","sln");
      chooser.setFileFilter(f);
      while(toShow){
         //This is not very elegant, but it works!!
         //Possibly remove and follow the logic below...
         toShow = false;
         int value = chooser.showSaveDialog(this._frame);
         if(value == JFileChooser.APPROVE_OPTION){
            //Do Something here...
            file = chooser.getSelectedFile();
            path = file.getPath();
            if(file.exists()){
               String error   = new String("FILE EXISTS!");
               String message = new String("File: " + path);
               message += "\nalready exists!\nOverwrite the file?";
               int ans = JOptionPane.showConfirmDialog(
                         this._frame, message, error,
                         JOptionPane.YES_NO_OPTION);
               toShow = !(ans == JOptionPane.YES_OPTION);
            }
            //else{
            //   toShow = false;
            //}
            if(!toShow){
               this._sudoku.saveSolution(file.getPath());
            }
         }
      }
   }

   //
   //
   //
   private void inputSudokuManually(){
      SudokuManualEntryView.instance("Manual Input",this,this._frame);
   }

   //
   //
   //
   private void inputSudokuTextFile(){
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter f=new FileNameExtensionFilter(
                                         "*.txt,*.text","txt","text");
      chooser.setFileFilter(f);
      int value = chooser.showOpenDialog(this._frame);
      if(value == 0){
         String path = chooser.getSelectedFile().getPath();
         this._sudoku.open(path);
      }
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
            this._sudoku.clear();
         }
         else if(command.equals("NEWGAME")){
            this.getNewGameInfo();
         }
         else if(command.equals("SAVE")){
            this.getSaveSolutionToFileInfo();
         }
         else if(command.equals("OPENTEXTFILE")){
            this._subscriber.update(this,command);
         }
         else if(command.contains("MANUAL_SET")){
            try{
               SudokuManualEntryView instance = null;
               instance = SudokuManualEntryView.instance();
               String[] s = instance.returnSudokuInput(false);
               this._sudoku.set(s);
            }
            catch(NullPointerException npe){}
         }
         else if(command.contains("MANUAL_SAVE")){
            this.getManualSaveToFileInfo();
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
