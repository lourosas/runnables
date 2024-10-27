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

public class MechanismSupportDataPanel extends JPanel{
   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public MechanismSupportDataPanel(){
      super();
      this.setUpGUI();
   }

   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void setUpData(MechanismSupportData data){
      this.setUpIDData(data);   
      this.setUpAngleData(data);
      this.setUpMeasuredForceData(data);
      this.setUpErrorData(data);
   }

   
   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void setUpAngleData(MechanismSupportData data){
      double angle = data.angle();
      JPanel panel = (JPanel)this.getComponent(1);
      JLabel label = (JLabel)panel.getComponent(1);
      label.setText("poop");
      //label.setText(""+angle);
      
      if(data.toString().contains("rad")){
         label.setText(label.getText() + " rad");
      }
   }
   
   //
   //
   //
   private JPanel setUpAnglePanel(){
      JPanel panel = new JPanel();

      JLabel angleLabel = new JLabel("Current Arm Angle: ");
      JLabel angleMeas  = new JLabel("<Arm Angle Value>");
      panel.add(angleLabel);
      panel.add(angleMeas);

      return panel;
   }

   //
   //
   //
   private JPanel setUpErrorPanel(){
      JPanel panel = new JPanel();

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
   private void setUpErrorData(MechanismSupportData data){}

   //
   //
   //
   private JPanel setUpForceVectorPanel(){
      JPanel panel = new JPanel();
      
      JLabel forceVectorLabel = new JLabel("Force Vector: ");
      JLabel forceVectorMeas  = new JLabel("<Force Vector Value>");
      panel.add(forceVectorLabel);
      panel.add(forceVectorMeas);

      return panel;
   }
   
   //
   //
   //
   private void setUpForceVectorData(MechanismSupportData data){}

   //
   //
   //
   private JPanel setUpIDPanel(){
      JPanel panel = new JPanel();

      JLabel idLabel = new JLabel("Support: ");
      JLabel idValue = new JLabel("<Support ID>");
      panel.add(idLabel);
      panel.add(idValue);

      return panel;
   }

   //
   //
   //
   private void setUpIDData(MechanismSupportData data){
      JPanel panel = (JPanel)this.getComponent(0);
      JLabel id    = (JLabel)panel.getComponent(1);
      id.setText("");
      id.setText("" + data.id());
   }

   //
   //
   //
   private JPanel setUpMeasuredForcePanel(){
      JPanel panel = new JPanel();
      
      JLabel measuredForceLabel = new JLabel("Force: ");
      JLabel measuredForceMeas  = new JLabel("<Force Value>");
      panel.add(measuredForceLabel);
      panel.add(measuredForceMeas);

      return panel;
   }
   
   //
   //
   //
   private void setUpMeasuredForceData(MechanismSupportData data){}

   //
   //
   //
   private void setUpGUI(){
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setLayout(new GridLayout(0,1));
      this.add(this.setUpIDPanel());
      this.add(this.setUpAnglePanel());
      //Add in, but for the time being, keep it blank, and do not
      //update
      this.add(this.setUpForceVectorPanel());
      this.add(this.setUpMeasuredForcePanel());
      this.add(this.setUpErrorPanel());
   }

}
//////////////////////////////////////////////////////////////////////
