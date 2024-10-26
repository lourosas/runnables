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

public class LaunchingMechanismDataPanel extends JPanel{
   //Literally NO Actor input to control:  this is not needed!
   //private LaunchSimulatorController _controller = null;

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public LaunchingMechanismDataPanel(){
      super();
      this.setUpGUI();
   }

   ////////////////////////////Private Methods////////////////////////
   //
   //
   //
   private void setUpGUI(){
      int CENTER = SwingConstants.CENTER;
      JLabel platformLabel = new JLabel("Platform", CENTER);
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setLayout(new GridLayout(0,1));
      this.add(platformLabel);
      this.add(this.setUpModelPanel());
      this.add(this.setUpHoldsPanel());
      this.add(this.setUpMechanismPanel());
   }

   //
   //
   //
   private JPanel setUpHoldsPanel(){
      JPanel panel = new JPanel();

      JLabel numHolds     = new JLabel("No. of Holds: ");
      //put the number of holds
      JLabel totalHolds   = new JLabel("<no.>");
      panel.add(numHolds);
      panel.add(totalHolds);

      return panel;
   }

   //
   //
   //
   private JPanel setUpMechanismPanel(){
      JPanel panel = new JPanel();

      JLabel weightLabel  = new JLabel("Measured Weght:  ");
      //Put the Measured Weight
      JLabel weightValue  = new JLabel("<weight>");
      JLabel errorLabel   = new JLabel("Error!!!(In red)");
      JLabel errorMessage = new JLabel("<message>");

      panel.add(weightLabel); panel.add(weightValue);
      panel.add(errorLabel);  panel.add(errorMessage);

      return panel;
   }

   //
   //
   //
   private JPanel setUpModelPanel(){
      JPanel panel = new JPanel();

      JLabel modelLabel = new JLabel("Model:  ");
      //Will be updated as needed...
      JLabel modelValue = new JLabel("<model>");
      panel.add(modelLabel);
      panel.add(modelValue);

      return panel;
   }
}
//////////////////////////////////////////////////////////////////////
