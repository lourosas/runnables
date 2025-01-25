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

public class TankDataPanel extends JPanel{
   //Use Anonymous Inner Classes

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TankDataPanel(){
      super();
      this.setUpGUI();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void activateButtonPanel(){}

   //
   //
   //
   private void deactivateButtonPanel(){
      JPanel bp = (JPanel)this.getComponent(1);
      for(int i = 0; i < bp.getComponentCount(); ++i){
         JButton b = (JButton)bp.getComponent(i);
         b.setEnabled(false);
      }
   }

   //
   //
   //
   private JPanel setUpButtonPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      JButton error = new JButton("Error");

      error.addActionListener(new ActionListener(){
         //TBD
         public void actionPerformed(ActionEvent e){
            System.out.println(e); //For the time being...
         }
      }
      return panel;
   }

   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpStageNumberPanel());
      panel.add(this.setUpTankNumberPanel());
      panel.add(this.setUpFuelPanel());
      panel.add(this.setUpTemperaturePanel());
      panel.add(this.setUpWeightPanel());
      panel.add(this.setUpEmptyRate());
      panel.add(this.setUpErrorPanel());
      return panel;
   }

   //
   //
   //
   private void setUpGUI(){
      this.setLayout(new BorderLayout());
      this.add(this.setUpCenterPanel(), BorderLayout.CENTER);
      //One Button--Error: based on isError() == true
      this.add(this.setUpButtonPanel(), BorderLayout.SOUTH);
   }

   //
   //
   //
   private JPanel setUpStageNumberPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1,2));
      JLabel stage = new JLabel("Stage: ",SwingConstants.RIGHT);
      panel.add(stage);
      panel.add(new JLabe());
      return panel;
   }

   //
   //
   //
   private JPanel setUpTankNumberPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1,2));
      JLabel tank = new JLabel("Tank: ",SwingConstants.RIGHT);
      panel.add(tank);
      panel.add(new JLabel());
      return panel;
   }
}
//////////////////////////////////////////////////////////////////////
