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
   
   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public SudokuManualEntryView(){
      this("Sudoku Manual Entry");
   }

   //
   //
   //
   public SudokuManualEntryView(String title){
      super(title);
      this.addWindowListener(new WindowAdapter(){
         public void windowClosing(WindowEvent w){
            clearOutTheEntries();
            setVisible(false);
         }
      });
   }

   ////////////////////////Private Methods////////////////////////////
   //
   //
   //
   private void clearOutTheEntries(){
      JPanel panel = (JPanel)this.getContentPane().getComponent(0);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         ((JTextField)panel.getComponent(i)).setText("");
      }
   }

   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(9,9,2,2));
      for(int i = 0; i < 81; ++i){
         JTextField tf = new JTextField();
         tf.addKeyListener(new KeyAdapter(){
            //this is the easiest way to do when want to keep the
            //Text field editable!!!
            public void keyTyped(KeyEvent k){
               char c = k.getKeyChar();
               String text = tf.getText();
               if(c < '0' || c > '9' || !text.isEmpty()){
                  k.consume();
               }
            }
         });
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
      
      JButton save = new JButton("Set");
      panel.add(save);

      JButton clear = new JButton("Clear");
      clear.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            clearOutTheEntries();
         }
      });
      panel.add(clear);

      JButton cancel = new JButton("Cancel");
      cancel.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            clearOutTheEntries();
            setVisible(false);
         }
      });
      panel.add(cancel);

      return panel;
   }
}
//////////////////////////////////////////////////////////////////////
