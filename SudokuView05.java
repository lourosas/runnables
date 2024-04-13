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
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import rosas.lou.runnables.*;
import myclasses.*;
import rosas.lou.lgraphics.*;

public class SudokuView05 extends GenericJFrame implements Subscriber{
   private static final int HEIGHT = 700;
   private static final int WIDTH  = 700;
   private static final int TOTAL  =  81;
   private static final int ROWS   =   9;
   private static final int COLS   =   9;

   private JButton[][]           _buttonArray;
   private ActionListener        _aListener;
   private KeyListener           _kListener;
   private SudokuManualEntryView _mEntryView;

   {
      _buttonArray = new JButton[ROWS][COLS];
      _aListener   = null;
      _kListener   = null;
      _mEntryView  = null;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public SudokuView05(ActionListener a, KeyListener k){
      this("", a, k);
   }

   //
   //
   //
   public SudokuView05(String title, ActionListener a, KeyListener k){
      super(title);
      this._aListener = a;
      this._kListener = k;
      this.setUpGUI();
   }

   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private void handleFileNotFoundError(SudokuState state){
      String[] parts   = state.message().split(" ");
      String   nofile  = parts[4];
      String   message = "File: "+nofile+" not found!\n";
      message         += "Please enter another puzzle";
      String   error   = state.message();
      JOptionPane.showMessageDialog(this,
                                    message,
                                    "FILE NOT FOUND",
                                    JOptionPane.ERROR_MESSAGE);
   }

   //
   //
   //
   private void handleIOException(SudokuState state){
      String error   = null;
      String message = null;
      if(state.message().toUpperCase().contains("IO EXCEPTION")){
         error    = "IO Read Error!";
         message  = "Error in reading Puzzle File\n";
         message += "Please enter another puzzle";
      }
      else if(state.message().toUpperCase().contains("IO WRITE")){
         error    = "IO Write Error!";
         message  = "Error in writing Puzzle File\n";
      }
      JOptionPane.showMessageDialog(this,
                                    message,
                                    error,
                                    JOptionPane.ERROR_MESSAGE);
   }

   //
   //
   //
   private void handleNoSolution(SudokuState state){
      String error   = state.message().toUpperCase() + "!";
      String message = "Sudoku Unsolvable!\nPlease enter another";
      message += " puzzle";
      JOptionPane.showMessageDialog(this,
                                    message,
                                    error,
                                    JOptionPane.ERROR_MESSAGE);   
   }

   //
   //
   //
   private void handlePuzzleNoClearError(SudokuState state){
      String error   = state.message().toUpperCase()+"!";
      String message = state.message()+"\nPlease restart";
      JOptionPane.showMessageDialog(this,
                                    message,
                                    error,
                                    JOptionPane.ERROR_MESSAGE);
   
   }

   //
   //
   //
   private void reflectStateInButtonPanel(String state){
      JPanel panel   = (JPanel)this.getContentPane().getComponent(1);
      JButton solve  = (JButton)panel.getComponent(0);
      JButton clear  = (JButton)panel.getComponent(1);
      JButton newGame= (JButton)panel.getComponent(2);
      JButton save   = (JButton)panel.getComponent(3);

      solve.setEnabled(false);   clear.setEnabled(false);
      newGame.setEnabled(false); save.setEnabled(false);

      if(state.toUpperCase().equals("STARTUP")){
         newGame.setEnabled(true);
      }
      else if(state.toUpperCase().equals("NEWGAME")){
         solve.setEnabled(true); clear.setEnabled(true);
      }
      else if(state.toUpperCase().equals("SOLVED")){
         clear.setEnabled(true); save.setEnabled(true);
         newGame.setEnabled(true);
      }
      else if(state.toUpperCase().equals("ERROR")){
         clear.setEnabled(true); newGame.setEnabled(true);
      }
      else if(state.toUpperCase().equals("CLEARED")){
         newGame.setEnabled(true);
      }

   }

   //
   //
   //
   private JPanel setUpCenterPanel(){
      int tw;  //Top  Width
      int lw;  //Left Width
      JPanel panel = new JPanel();
      Border border = BorderFactory.createLineBorder(Color.BLACK,5);
      panel.setBorder(border);
      //panel.setLayout(new GridLayout(ROWS,COLS,5,5));
      panel.setLayout(new GridLayout(ROWS,COLS,0,0));
      for(int i = 0; i < ROWS; ++i){
         for(int j = 0; j < COLS; ++j){
            tw = 1; lw = 1;
            if((i%3==0) && i != 0){
               tw = 5;
            }
            if((j%3==0) && j != 0){
               lw = 5;
            }
            Color c = Color.BLUE;
            Border b=BorderFactory.createMatteBorder(tw,lw,0,0,c);
            this._buttonArray[i][j] = new JButton();
            this._buttonArray[i][j].setBorder(b);
            this._buttonArray[i][j].setEnabled(false);
            panel.add(this._buttonArray[i][j]);
         }
      }
      return panel;
   }

   //
   //
   //
   private void setUpGUI(){
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);
      JPanel centerPanel = this.setUpCenterPanel();
      JPanel southPanel  = this.setUpSouthPanel();
      this.getContentPane().add(centerPanel, BorderLayout.CENTER);
      this.getContentPane().add(southPanel,  BorderLayout.SOUTH);
      this.setVisible(true);
   }

   //
   //
   //
   private JPanel setUpSouthPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      
      JButton solve = new JButton("Solve");
      solve.setActionCommand("SOLVE");
      solve.setMnemonic(KeyEvent.VK_S);
      solve.addActionListener(this._aListener);
      solve.addKeyListener(this._kListener);
      solve.setEnabled(false);
      panel.add(solve);

      JButton clear = new JButton("Clear");
      clear.setActionCommand("CLEAR");
      clear.setMnemonic(KeyEvent.VK_C);
      clear.addActionListener(this._aListener);
      clear.addKeyListener(this._kListener);
      clear.setEnabled(false);
      panel.add(clear);

      JButton newGame = new JButton("New Game");
      newGame.setActionCommand("NEWGAME");
      newGame.setMnemonic(KeyEvent.VK_N);
      newGame.addActionListener(this._aListener);
      newGame.addKeyListener(this._kListener);
      panel.add(newGame);

      JButton save = new JButton("Save");
      save.setActionCommand("SAVE");
      save.setMnemonic(KeyEvent.VK_E);
      save.addActionListener(this._aListener);
      save.addKeyListener(this._kListener);
      save.setEnabled(false);
      panel.add(save);
      
      return panel;
   }

   //
   //
   //
   private void updateValues(int[][] block){}

   //
   //
   //
   private void updateValues(SudokuBlock[] block){
      try{
         for(int i = 0; i < ROWS; ++i){
            for(int j = 0; j < COLS; ++j){
               Integer val = block[ROWS*i+j].value();
               if(val > 0){
                  this._buttonArray[i][j].setText(val.toString());
                  Font f = new Font("Serif",Font.BOLD,36);
                  this._buttonArray[i][j].setFont(f);
               }
               else{
                  this._buttonArray[i][j].setText("");
                  Font f = new Font("Serif",Font.BOLD,36);
                  this._buttonArray[i][j].setFont(f);
               }
            }
         }
      }
      catch(ArrayIndexOutOfBoundsException oob){
         oob.printStackTrace();
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void updateValues(SudokuBlock[][] block){
      try{
         for(int i =  0; i < ROWS; ++i){
            for(int j = 0; j < COLS; ++j){
               Integer val = block[i][j].value();
               if(val > 0){
                  this._buttonArray[i][j].setText(val.toString());
                  Font f = new Font("Serif",Font.BOLD,36);
                  this._buttonArray[i][j].setFont(f);
               }
               else{
                  this._buttonArray[i][j].setText("");
                  Font f = new Font("Serif",Font.BOLD,36);
                  this._buttonArray[i][j].setFont(f);
               }
            }
         }
      }
      catch(ArrayIndexOutOfBoundsException oob){
         oob.printStackTrace();
      }
      catch(NullPointerException npe){}
   }

   ///////////////////Interface Implementation////////////////////////
   //
   //
   //
   public void error(RuntimeException re){}

   //
   //
   //
   public void error(RuntimeException re, Object o){
      try{
         SudokuState  state = (SudokuState)o;
         String stringState = state.stringState().toUpperCase();
         String error       = state.message().toUpperCase();
         if(error.contains("NO SOLUTION")){
            this.handleNoSolution(state);
         }
         else if(error.contains("NOT CLEAR THE PUZZLE")){
            this.handlePuzzleNoClearError(state);
         }
         else if(error.contains("FILE NOT FOUND")){
            this.handleFileNotFoundError(state);
         }
         else if(error.contains("IO EXCEPTION") ||
                 error.contains("IO WRITE")){
            this.handleIOException(state);
         }
      }
      catch(ClassCastException cce){}
   }

   //
   //
   //
   public void error(String error){}

   //
   //
   //
   public void update(Object o){
      try{
         SudokuState ss = (SudokuState)o;
         if(ss.stringState().toUpperCase().equals("NEWGAME") ||
            ss.stringState().toUpperCase().equals("CLEARED") ||
            ss.stringState().toUpperCase().equals("SOLVED")){
            this.updateValues(ss.block());
            this.updateValues(ss.singleBlock());
            this.updateValues(ss.intBlock());
         }
         else if(ss.stringState().toUpperCase().equals("STARTUP")){}
         this.reflectStateInButtonPanel(
                                      ss.stringState().toUpperCase());
      }
      catch(ClassCastException cce){}
   }

   //
   //
   //
   public void update(Object o, String s){}
}

//////////////////////////////////////////////////////////////////////
