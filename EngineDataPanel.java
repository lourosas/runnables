//////////////////////////////////////////////////////////////////////
/*
Copyright 2025 Lou Rosas

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
import javax.swing.text.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import myclasses.*;
import javax.swing.border.*;

public class EngineDataPanel extends JPanel{
   //Use Anonymous Inner classes
   
   private int _stage;
   private int _index; //Engine Number

   {
      _stage    = 0;
      _index    = 0;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public EngineDataPanel(int stage, int index){
      super();
      if(stage > 0 && index > 0){
         this._stage = stage;
         this._index = index;
         this.setUpGUI();
      }
   }

   ///////////////////////////Public Methods//////////////////////////
   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void deactivateButtonPanel(){}

   //
   //
   //
   private JPanel setUpButtonPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      JButton error = new JButton("Error");
      error.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //TBD
            System.out.println(e);
         }
      });
      panel.add(error);
      return panel;
   }

   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpPanel(2)); //Stage Number
      panel.add(this.setUpPanel(2)); //Engine Number (Index)
      panel.add(this.setUpPanel(1)); //Model
      panel.add(this.setUpPanel(2)); //Is Ingnited
      panel.add(this.setUpPanel(2)); //Temperature
      panel.add(this.setUpPanel(2)); //Fuel Flow Rate
      panel.add(this.setUpPanel(2)); //Exhaust Flow Rate
      panel.add(this.setUpPanel(2)); //Error
      return panel;
   }

   //
   //
   //
   private void setUpGUI(){
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setLayout(new BorderLayout());
      this.add(this.setUpCenterPanel(), BorderLayout.CENTER);
      this.add(this.setUpButtonPanel(),  BorderLayout.NORTH);
      this.deactivateButtonPanel();
   }

   //
   //
   //
   private JPanel setUpPanel(int columns){
      int cols = columns > 0 ? columns : 1;
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,cols));
      return panel;
   }
}
//////////////////////////////////////////////////////////////////////
