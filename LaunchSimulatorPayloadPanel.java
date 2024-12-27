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
import javax.swing.border.*;
import java.time.*;
import java.text.*;
import myclasses.*;
import rosas.lou.clock.*;

public class LaunchSimulatorPayloadPanel extends JPanel{
   //Again, continue to use Anonymous Inner Classes
   
   //////////////////////Constructor//////////////////////////////////
   //
   //
   //
   public LaunchSimulatorPayloadPanel(){
      super();
      this.setUpGUI();
   }

   ///////////////////////////Public Methods//////////////////////////
   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void deactivatePanel(){
      JPanel bp = (JPanel)this.getComponent(1);
      for(int i = 0; i < bp.getCompoenentCount(); ++i){
         try{
            JButton b = (JButton)bp.getComponent(i);
            b.setEnabled(false);
         }
         catch(ClassCastException cce){
            //If this is caught, that is bad...
            cce.printStackTrace();
         }
      }
   }

   //
   //
   //
   private JPanel setUpButtonPanel(){
      JPanel panel  = new JPanel();
      JButton error = new JButton("Error");
      error.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            System.out.println(e);
         }
      });
      panel.add(error);
      return panel;
   }

   //
   //
   //
   private JPanel setUpDataPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,2));
      return panel;
   }

   //
   //
   //
   private void setUpGUI(){
      this.setBorder(new BorderLayyout());
      this.add(this.setUpDataPanel(),  BorderLayout.CENTER);
      this.add(this.setUpButtonPanel(),BorderLayout.SOUTH);
      this.deactivateButtonPanel();
   }
}

//////////////////////////////////////////////////////////////////////
