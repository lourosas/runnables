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
import java.text.*;
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

   ////////////////////////////Public Methods/////////////////////////
   //
   //
   //
   public void setUpData(LaunchingMechanismData data){
      //Make test prints for now...will need to handle the Error
      //message...
      System.out.println(data);
      this.setUpModelData(data);
      this.setUpHoldsData(data);
      this.setUpWeightData(data);
      this.setUpErrorData(data);
      
   }

   //
   //
   //
   public void setUpData(String state, LaunchingMechanismData data){
      System.out.println(state);
      this.setUpData(data);
   }

   ////////////////////////////Private Methods////////////////////////
   //Clear the State of the input data...
   //Currently, not maintainable...
   //
   private void clearState(){}

   //Set the State of the input data...
   //Currently, not maintainable
   //
   private void setState(String state){}

   //
   //
   //
   private void setUpErrorData(LaunchingMechanismData data){
      JPanel panel  = (JPanel)this.getComponent(4);
      JLabel error  = (JLabel)panel.getComponent(0);
      JLabel errorS = (JLabel)panel.getComponent(1);
      error.setText("");
      errorS.setText("");
      if(data.isError()){
         error.setText("Error ");
         errorS.setText(data.error());
         error.setForeground(Color.RED);
         errorS.setForeground(Color.RED);
      }
   }

   //
   //
   //
   private JPanel setUpErrorPanel(){
      JPanel panel = new JPanel();

      //ONLY SHOW when there is an error!!
      JLabel errorLabel   = new JLabel("Error!!!");
      JLabel errorMessage = new JLabel("<message>");
      errorLabel.setForeground(Color.RED);
      errorMessage.setForeground(Color.RED);

      panel.add(errorLabel);
      panel.add(errorMessage);

      return panel;
   }

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
      this.add(this.setUpWeightPanel());
      this.add(this.setUpErrorPanel());
   }

   //
   //
   //
   private void setUpHoldsData(LaunchingMechanismData data){
      JPanel panel = (JPanel)this.getComponent(2);
      JLabel label = (JLabel)panel.getComponent(1);
      label.setText("" + data.holds());
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
   private void setUpModelData(LaunchingMechanismData data){
      JPanel panel = (JPanel)this.getComponent(1);
      JLabel label = (JLabel)panel.getComponent(1);
      label.setText("" + data.model());
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

   //
   //
   //
   private void setUpWeightData(LaunchingMechanismData data){
      NumberFormat nf = null;
      nf = NumberFormat.getNumberInstance(Locale.US);
      JPanel panel = (JPanel)this.getComponent(3);
      JLabel label = (JLabel)panel.getComponent(1);
      label.setText(nf.format(data.measuredWeight()) + " N");
   }

   //
   //
   //
   private JPanel setUpWeightPanel(){
      JPanel panel = new JPanel();

      JLabel weightLabel  = new JLabel("Measured Weght:  ");
      //Put the Measured Weight
      JLabel weightValue  = new JLabel("<weight>");
      panel.add(weightLabel);
      panel.add(weightValue);

      return panel;
   }
}
//////////////////////////////////////////////////////////////////////
