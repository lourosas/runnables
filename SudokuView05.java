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

   private JButton[]             _buttonArray;
   private ActionListener        _aListener;
   private KeyListener           _kListener;
   private SudokuManualEntryView _mEntryView;

   {
      _buttonArray = new JButton[TOTAL];
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
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      Border border = BorderFactory.createLineBorder(Color.BLACK,5);
      panel.setBorder(border);
      panel.setLayout(new GridLayout(ROWS,COLS,5,5));
      for(int i = 0; i < TOTAL; ++ i){
         this._buttonArray[i] = new JButton();
         this._buttonArray[i].setEnabled(false);
         panel.add(this._buttonArray[i]);
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

   ///////////////////Interface Implementation////////////////////////
   //
   //
   //
   public void error(RuntimeException re){}

   //
   //
   //
   public void error(RuntimeException re, Object o){}

   //
   //
   //
   public void error(String error){}

   //
   //
   //
   public void update(Object o){}

   //
   //
   //
   public void update(Object o, String s){}
}

//////////////////////////////////////////////////////////////////////
