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
      panel.add(solve);

      JButton clear = new JButton("Clear");
      clear.setActionCommand("CLEAR");
      clear.setMnemonic(KeyEvent.VK_C);
      clear.addActionListener(this._controller);
      clear.addKeyListener(this._controller);
      panel.add(clear);

      JButton newGame = new JButton("New Game");
      newGame.setActionCommand("NEWGAME");
      newGame.setMnemonic(KeyEvent.VK_N);
      newGame.addActionListener(this._controller);
      newGame.addKeyListener(this._controller);
      panel.add(newGame);

      return panel;
   }


   ///////////////////////Interface Methods///////////////////////////
   //
   //
   //
   public void update(Object o){}

   //
   //
   //
   public void update(Object o, String s){}

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
}
//////////////////////////////////////////////////////////////////////