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
public class SudokuManualEntryView extends GenericJInteractionFrame{
   private static final int HEIGHT = 400;
   private static final int WIDTH  = 400;
   
   private SudokuController _controller;

   {
      _controller = null;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public SudokuManualEntryView(){
      this("Sudoku Manual Entry", null);
   }

   //
   //
   //
   public SudokuManualEntryView(String title, SudokuController contr){
      super(title);
      this._controller = contr;
      this.setUpGUI();
   }


   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(9,9,2,2));
      for(int i = 0; i < 81; ++i){
         JTextField tf = new JTextField();
         tf.addKeyListener(this._controller);
         panel.add(tf);
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
      
      JButton save = new JButton("Save");
      save.setActionCommand("MANUAL_SAVE");
      save.addActionListener(this._controller);
      save.addKeyListener(this._controller);
      panel.add(save);

      JButton clear = new JButton("Clear");
      clear.setActionCommand("MANUAL_CLEAR");
      clear.addActionListener(this._controller);
      clear.addKeyListener(this._controller);
      panel.add(clear);

      JButton cancel = new JButton("Cancel");
      cancel.setActionCommand("MANUAL_CANCEL");
      cancel.addActionListener(this._controller);
      cancel.addKeyListener(this._controller);
      panel.add(cancel);

      return panel;
   }

}
//////////////////////////////////////////////////////////////////////
