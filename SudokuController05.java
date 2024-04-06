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
   }

   //////////////////////Private Methods//////////////////////////////
   //
   //
   //

   ////////////////////////Interface Implementation///////////////////
   ////////////////////////////Action Listener////////////////////////
   public void actionPerformed(ActionEvent e){}

   /////////////////////////////Key Listener//////////////////////////
   public void keyPressed(KeyEvent k){}

   public void keyReleased(KeyEvent k){}

   public void keyTyped(KeyEvent k){}
}

//////////////////////////////////////////////////////////////////////
