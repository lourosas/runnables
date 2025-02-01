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

public class PumpDataPanel extends JPanel{
   //Use Anonymous Inner Classes
   
   private int _pump;
   private int _stage;

   {
      _pump   = 0;
      _stage  = 0;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public PumpDataPanel(int stage, int pump){
      super();
      if(stage > 0 && pump > 0){
         this._stage = stage;
         this._pump  = pump;
         this.setUpGUI();
      }
   }

   ///////////////////////////Public Mehtods//////////////////////////
   //
   //
   //
   public void setUpPumpData(StageData sd){
      java.util.List<PumpData> l = sd.fuelSystemData().pumpData();
      //Test Prints
      System.out.println("See if this fucking prints");
      System.out.println(l);
      System.out.println(l.size());
      this.setUpStageNumber(sd);
   }

   //////////////////////////Private Methods//////////////////////////
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
            System.out.println(e);
         }
      });
      return panel;
   }

   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpPanel(2)); //Stage Number
      panel.add(this.setUpPanel(2)); //Tank  Number
      panel.add(this.setUpPanel(2)); //Fuel  Type
      panel.add(this.setUpPanel(2)); //Temperature
      panel.add(this.setUpPanel(2)); //Flow
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
      this.add(this.setUpButtonPanel(), BorderLayout.SOUTH);
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

   //
   //
   //
   private void setUpStageNumber(StageData sd){}
}
//////////////////////////////////////////////////////////////////////
