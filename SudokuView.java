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

   private JButton[]        _buttonArray;
   private SudokuController _controller;

   {
      _buttonArray = new JButton[TOTAL];
      _controller  = null;
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
      this._controller = controller;
      this._buttonArray = new JButton[TOTAL];
      this.setUpGui();
   }

   ///////////////////////Private Methods/////////////////////////////
   //
   //
   //
   private void displayManualSudokuEntry(){
      System.out.println("Manual Enter");
      SudokuManualEntryView v =
                 new SudokuManualEntryView("WTF?!", this._controller);
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
         JButton yes = new JButton("Yes");
         //Yes...indicates to enter a text file as the puzzle
         yes.setActionCommand("OPENTEXTFILE");
         yes.addActionListener(this._controller);
         yes.doClick();
      }
      else if(n == 1){
         JButton no = new JButton("No");
         //No...means to manually enter the puzzle
         no.setActionCommand("MANUALENTER");
         no.addActionListener(this._controller);
         no.doClick();
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
      if(value == 0){
         String path = chooser.getSelectedFile().getPath();
         JButton b = new JButton(path);
         b.setActionCommand("TEXTFILEPATH");
         b.addActionListener(this._controller);
         b.doClick();
      }
   }

   //
   //
   //
   private void reflectStateInButtonPanel(String state){
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         ((JButton)panel.getComponent(i)).setEnabled(false);
      }
      if(state.toUpperCase().equals("STARTUP")){
         for(int i = 0; i < panel.getComponentCount(); ++i){
            JButton b = (JButton)panel.getComponent(i);
            if(b.getActionCommand().toUpperCase().equals("NEWGAME")){
               b.setEnabled(true);
            }
         }
      }
      else if(state.toUpperCase().equals("NEWGAME")){
         for(int i = 0; i < panel.getComponentCount(); ++i){
            JButton b  = (JButton)panel.getComponent(i);
            String act = b.getActionCommand().toUpperCase();
            if(act.equals("SOLVE") || 
               act.equals("CLEAR")){
               b.setEnabled(true);
            }
         }
      }
      else if(state.toUpperCase().equals("SOLVED")){
         for(int i = 0; i < panel.getComponentCount(); ++i){
            JButton b  = (JButton)panel.getComponent(i);
            String act = b.getActionCommand().toUpperCase();
            if(act.equals("CLEAR")||act.equals("SAVE")){
               b.setEnabled(true);
            }
         }
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
      solve.addActionListener(this._controller);
      solve.addKeyListener(this._controller);
      solve.setEnabled(false);
      panel.add(solve);

      JButton clear = new JButton("Clear");
      clear.setActionCommand("CLEAR");
      clear.setMnemonic(KeyEvent.VK_C);
      clear.addActionListener(this._controller);
      clear.addKeyListener(this._controller);
      clear.setEnabled(false);
      panel.add(clear);

      JButton newGame = new JButton("New Game");
      newGame.setActionCommand("NEWGAME");
      newGame.setMnemonic(KeyEvent.VK_N);
      newGame.addActionListener(this._controller);
      newGame.addKeyListener(this._controller);
      panel.add(newGame);

      JButton save = new JButton("Save");
      save.setActionCommand("SAVE");
      save.setMnemonic(KeyEvent.VK_A);
      save.addActionListener(this._controller);
      save.addKeyListener(this._controller);
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
            if(sState.contains("MANUALENTER")){
               System.out.println(sState);
               this.displayManualSudokuEntry();
            }
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
      else if(s.toUpperCase().contains("NEWGAME")){
         this.getNewGameInfo();
      }
      else if(s.toUpperCase().contains("OPENTEXTFILE")){
         this.openSudokuTextFile();
      }
      else if(s.toUpperCase().contains("MANUALENTER")){
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
   public void error(RuntimeException re, Object o){}

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
