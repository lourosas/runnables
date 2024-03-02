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
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import rosas.lou.runnables.*;
import myclasses.*;
import rosas.lou.lgraphics.*;

/**/
public class SudokuView extends GenericJFrame implements Subscriber{
   private static final int HEIGHT = 700;
   private static final int WIDTH  = 700;
   private static final int TOTAL  = 81;
   private static final int ROWS   = 9;
   private static final int COLS   = 9;

   private JButton[]             _buttonArray;
   //private SudokuController      _controller;
   private ActionListener        _actionListener;
   private KeyListener           _keyListener; 
   private SudokuManualEntryView _manualEntryView;

   {
      _buttonArray     = new JButton[TOTAL];
      //_controller      = null;
      _actionListener  = null;
      _keyListener     = null;
      _manualEntryView = null;
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public SudokuView(SudokuController controller){
      this("", controller);
   }

   //
   //
   //
   public SudokuView(String title, SudokuController controller){
      super(title);
      //this._controller = controller;
      this._actionListener = (ActionListener)controller;
      this._keyListener    = (KeyListener)controller;
      this._buttonArray    = new JButton[TOTAL];
      this.setUpGui();
   }

   ///////////////////////Private Methods/////////////////////////////
   //
   //
   //
   private void displayManualSudokuEntry(){
      //System.out.println("Manual Enter");
      //this._manualEntryView = new SudokuManualEntryView("WTF?!");
   }

   //
   //
   //
   private void getNewGameInfo(){
      int n = JOptionPane.showConfirmDialog(
                 this,
                 "Enter A Text File Puzzle?",
                 "Text File for Manual Entry?",
                 JOptionPane.YES_NO_OPTION);
      if(n == 0){
         //this.openSudokuTextFile();
         JFileChooser chooser = new JFileChooser();
         FileNameExtensionFilter f=new FileNameExtensionFilter(
                                       "*.txt","*.text","txt","text");
      }
      else if(n == 1){
         //Something to this effect will be the best way
         this.displayManualSudokuEntry();
      }
      //any other values are ignored...
   }

   //
   //
   //
   private void handleNoSolutionError(String error){
      String message = new String("Sudoku Unsolvable!\n");
      message += "Please enter another puzzle";
      JOptionPane.showMessageDialog(this,
                                     message,
                                     "NO SOLUTION!",
                                     JOptionPane.ERROR_MESSAGE);
   }

   //
   //
   //
   private void openSudokuTextFile(){
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter f=new FileNameExtensionFilter(
                                         "*.txt,*.text","txt","text");
      chooser.setFileFilter(f);
      int value = chooser.showOpenDialog(this);
      if(value == 0){}
   }

   //
   //
   //
   private void reflectStateInButtonPanel(String state){
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      JButton solve= (JButton)panel.getComponent(0);
      JButton clear= (JButton)panel.getComponent(1);
      JButton newGame=(JButton)panel.getComponent(2);
      JButton save = (JButton)panel.getComponent(3);

      solve.setEnabled(false); clear.setEnabled(false);
      newGame.setEnabled(false);save.setEnabled(false);

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
   }

   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(9, 9, 5, 5));
      for(int i = 0; i < TOTAL; ++i){
         this._buttonArray[i] = new JButton();
         this._buttonArray[i].setEnabled(false);
         panel.add(this._buttonArray[i]);
      }
      return panel;
   }

   //
   //
   //
   private void setUpGui(){
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);
      JPanel centerPanel  = this.setUpCenterPanel();
      JPanel southPanel   = this.setUpSouthPanel();
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
      solve.addActionListener(this._actionListener);
      solve.addKeyListener(this._keyListener);
      solve.setEnabled(false);
      panel.add(solve);

      JButton clear = new JButton("Clear");
      clear.setActionCommand("CLEAR");
      clear.setMnemonic(KeyEvent.VK_C);
      clear.addActionListener(this._actionListener);
      clear.addKeyListener(this._keyListener);
      clear.setEnabled(false);
      panel.add(clear);

      JButton newGame = new JButton("New Game");
      newGame.setActionCommand("NEWGAME");
      newGame.setMnemonic(KeyEvent.VK_N);
      newGame.addActionListener(this._actionListener);
      newGame.addKeyListener(this._keyListener);
      panel.add(newGame);

      JButton save = new JButton("Save");
      save.setActionCommand("SAVE");
      save.setMnemonic(KeyEvent.VK_A);
      save.addActionListener(this._actionListener);
      save.addKeyListener(this._keyListener);
      save.setEnabled(false);
      panel.add(save);

      return panel;
   }

   //
   //
   //
   private void updateValues(SudokuBlock[] block){
      try{
         for(int i = 0; i < this._buttonArray.length; ++i){
            Integer val = block[i].value();
            if(val > 0){
               this._buttonArray[i].setText(""+val);
               Font f = new Font("Serif",Font.BOLD,36);
               this._buttonArray[i].setFont(f);
            }
         }
      }
      catch(ArrayIndexOutOfBoundsException oob){
         oob.printStackTrace();
      }
   }

   //
   //
   //
   private void updateValues(SudokuBlock[][] block){
      //Row, Col
      try{
         int idx = 0;
         for(int i = 0; i < ROWS; ++i){
            for(int j = 0; j < COLS; ++j){
               Integer val = block[i][j].value();
               if(val > 0){
                  this._buttonArray[ROWS*i+j].setText(val.toString());
                  Font f = new Font("Serif",Font.BOLD,36);
                  this._buttonArray[ROWS*i+j].setFont(f);
               }
            }
         }
      }
      catch(ArrayIndexOutOfBoundsException oob){
         oob.printStackTrace();
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }


   ///////////////////////Interface Methods///////////////////////////
   ///////////////////////Subscriber Interface////////////////////////
   //
   //
   //
   public void update(Object o){
      try{
         SudokuBlock block[] = (SudokuBlock[])o;
         this.updateValues(block);
      }
      catch(ClassCastException cce){}
      try{
         SudokuState state = (SudokuState)o;
         String sState = state.state().toUpperCase();
         if(sState.equals("NEWGAME")){
            if(state.block() != null){
               this.updateValues(state.block());
            }
            else if(state.singleBlock() != null){
               this.updateValues(state.singleBlock());
            }
            else if(state.intBlock() != null){}
         }
         else if(sState.equals("SOLVED")){
            if(state.block() != null){
               this.updateValues(state.block());
            }
            else if(state.singleBlock() != null){
               this.updateValues(state.singleBlock());
            }
            else if(state.intBlock() != null){}
         }
         else if(sState.contains("STARTUP")){
         }
         //Indicate the State of the System
         this.reflectStateInButtonPanel(sState);
      }
      catch(ClassCastException cce){}
   }

   //
   //
   //
   public void update(Object o, String s){
      if(s.toUpperCase().contains("SINGLE")){
         this.update(o);
      }
      else if(s.toUpperCase().contains("DOUBLE")){
         SudokuBlock[][] block = (SudokuBlock[][])o;
         this.updateValues(block);
      }
      //This will need to be removed...
      /*
      else if(s.toUpperCase().contains("NEWGAME")){
         this.getNewGameInfo();
      }
      */
      //This will need to be removed...
      else if(s.toUpperCase().contains("OPENTEXTFILE")){
         this.openSudokuTextFile();
      }
      else if(s.toUpperCase().contains("MANUAL_SET")){
         System.out.println(s);
      }
   }

   //
   //
   //
   public void error(RuntimeException re){}

   //
   //
   //
   public void error(RuntimeException re, Object o){
      try{
         SudokuState state = (SudokuState)o;
         String sState     = state.state().toUpperCase();
         String error      = state.message().toUpperCase();
         if(error.contains("NO SOLUTION")){
            this.handleNoSolutionError(error);
         }
         //Put this here for now...
         this.reflectStateInButtonPanel(sState);
      }
      catch(ClassCastException cce){}
   }

   //
   //I can keep it real simple
   //
   public void error(String error){
      if(error.toUpperCase().contains("NO SOLUTION")){
         this.handleNoSolutionError(error.toUpperCase());
      }
   }
}
//////////////////////////////////////////////////////////////////////
