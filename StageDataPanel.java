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
import javax.swing.text.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import myclasses.*;
import javax.swing.border.*;

public class StageDataPanel extends JPanel{
   //Transfer this to the other Frames that are part of the Stage Data
   private JFrame  _parent;

   {
      _parent = null;
   };
   //Going to use Anonumous Inner Classes for this...
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public StageDataPanel(){
      this(null);
   }

   //
   //
   //
   public StageDataPanel(JFrame parent){
      super();
      this._parent = parent;
      this.setUpGUI();
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void update(StageData sd){
      this.updateDataPanel(sd);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void activateButtonPanel(String action){}

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
      JButton fuelSystem = new JButton("Fuel System");
      JButton engines    = new JButton("Engines");
      JButton error      = new JButton("Error");
      fuelSystem.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //Test Print
            System.out.println(e);
            //displayFuelSystemJFrame();
            //updateFuelSystemJFrame();
            //activateButtonPanel("Fuel System Pressed");
         }
      });
      engines.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //Test Print
            System.out.println(e);
            //displayEnginesJFrame();
            //updateEnginesJFrame)(;
            //activateButtonPanel("Engines Pressed");
         }
      });
      error.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //Test Print
            System.out.println(e);
         }
      });
      panel.add(fuelSystem);
      panel.add(engines);
      panel.add(error);
      return panel;
   }

   //
   //
   //
   private JPanel setUpDataPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpPanel(2)); //Current Stage
      panel.add(this.setUpPanel(2)); //Current Model
      panel.add(this.setUpPanel(2)); //Number of Engines
      panel.add(this.setUpPanel(2)); //Current Weight
      panel.add(this.setUpPanel(2)); //Error Indicator
      return panel;
   }

   //
   //
   //
   private void setUpGUI(){
      this.setLayout(new BorderLayout());
      this.add(this.setUpDataPanel(),  BorderLayout.CENTER);
      this.add(this.setUpButtonPanel(),BorderLayout.SOUTH);
      //this.deactivateButtonPanel();
   }

   //
   //
   //
   private JPanel setUpPanel(int columns){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1, columns));
      return panel;
   }

   //
   //
   //
   private void updateDataPanel(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      System.out.println(panel.getComponentCount());
   }
}
//////////////////////////////////////////////////////////////////////
