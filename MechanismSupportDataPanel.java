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
   private JFrame _parent = null;

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public MechanismSupportDataPanel(){
      super();
      this.setUpGUI(null);
   }

   //
   //
   //
   public MechanismSupportDataPanel(JFrame parent){
      super();
      this.setUpGUI(parent);
   }

   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void setUpData(MechanismSupportData data){
      this.setUpIDData(data);   
      this.setUpAngleData(data);
      this.setUpMeasuredForceData(data);
      this.setUpForceVectorData(data);
      this.setUpErrorData(data);
   }

   //
   //
   //
   public void setUpError(RuntimeException re, ErrorEvent e){
      /*
       * Needs to be fucking better!
       * Need to do this CORRECTLY!!!!!!!!!!!!!!!!
      System.out.println("Error");
      System.out.println(this);
      JOptionPane.showMessageDialog(this._parent,
                                    re,
                                    re.getMessage(),
                                    JOptionPane.ERROR_MESSAGE);
      */
   }

   
   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void setUpAngleData(MechanismSupportData data){
      double angle = data.angle();
      JPanel panel = (JPanel)this.getComponent(1);
      JLabel label = (JLabel)panel.getComponent(1);
      
      if(data.toString().contains("rad")){
         java.text.DecimalFormat df = null;
         df = new java.text.DecimalFormat("#.###");
         label.setText(df.format(angle));
         label.setText(label.getText() + " rad");
      }
      else{
         label.setText(""+angle);
         label.setText(label.getText()+'\u00B0');
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

      JLabel errorLabel   = new JLabel("Error: ");
      JLabel errorMessage = new JLabel("<Error Message>");
      errorLabel.setForeground(Color.RED);

      panel.add(errorLabel);
      panel.add(errorMessage);

      return panel;
   }

   //
   //
   //
   private void setUpErrorData(MechanismSupportData data){
      boolean isError = data.isError();
      JPanel panel        = (JPanel)this.getComponent(4);
      JLabel errorLabel   = (JLabel)panel.getComponent(0);
      JLabel errorMessage = (JLabel)panel.getComponent(1);
      System.out.println("Mechanism Support Data Panel");
      System.out.println("Count: "+panel.getComponentCount());
      System.out.println("Id:    "+data.id());
      //System.out.println("Error: "+data.isError());
      System.out.println("Data:  "+data.error());
      System.out.println("Time:  "+data.time());

      errorMessage.setText(""+data.isError());
      if(data.isError()){
         errorLabel.setForeground(Color.RED);
         errorMessage.setForeground(Color.RED);
         String err = data.error();
         if(err.toUpperCase().contains("ANGLE")){
            panel        = (JPanel)this.getComponent(1);
            errorLabel   = (JLabel)panel.getComponent(0);
            errorMessage = (JLabel)panel.getComponent(1);
            errorLabel.setForeground(Color.RED);
            errorMessage.setForeground(Color.RED);
         }
         if(err.toUpperCase().contains("MAGNITUDE")){
            panel        = (JPanel)this.getComponent(3);
            errorLabel   = (JLabel)panel.getComponent(0);
            errorMessage = (JLabel)panel.getComponent(1);
            errorLabel.setForeground(Color.RED);
            errorMessage.setForeground(Color.RED);
         }
         if(err.toUpperCase().contains("MECHANISM SUPPORT")){
            panel        = (JPanel)this.getComponent(2);
            errorLabel   = (JLabel)panel.getComponent(0);
            errorLabel.setForeground(Color.RED);
            if(err.toUpperCase().contains("X DIRECTION")){
               errorMessage = (JLabel)panel.getComponent(1);
               errorMessage.setForeground(Color.RED);
            }
            if(err.toUpperCase().contains("Y DIRECTION")){
               errorMessage = (JLabel)panel.getComponent(2);
               errorMessage.setForeground(Color.RED);
            }
            if(err.toUpperCase().contains("Z DIRECTION")){
               errorMessage = (JLabel)panel.getComponent(3);
               errorMessage.setForeground(Color.RED);
            }
         }
      }
      else{
         errorLabel.setForeground(Color.BLUE);
         errorMessage.setForeground(Color.BLUE);
      }
   }

   //
   //
   //
   private JPanel setUpForceVectorPanel(){
      JPanel panel = new JPanel();

      JLabel forceVectorLble  = new JLabel("Force Vector: ");
      panel.add(forceVectorLble);
      JLabel x = new JLabel(" <X> ");
      panel.add(x);
      JLabel y = new JLabel(" <Y> ");
      panel.add(y);
      JLabel z = new JLabel(" <Z> ");
      panel.add(z);

      return panel;
   }
   
   //
   //
   //
   private void setUpForceVectorData(MechanismSupportData data){
      java.text.DecimalFormat df = null;
      df = new java.text.DecimalFormat("###,###,###.##");

      ForceVector fv = data.forceVector();
      double x       = fv.x();
      double y       = fv.y();
      double z       = fv.z();

      JPanel panel   = (JPanel)this.getComponent(2);
      JLabel xl      = (JLabel)panel.getComponent(1);
      String xs      = df.format(x);
      xl.setText("x: " + xs + "N, ");
      JLabel yl      = (JLabel)panel.getComponent(2);
      String ys      = df.format(y);
      yl.setText("y: " + ys + "N, ");
      JLabel zl      = (JLabel)panel.getComponent(3);
      String zs      = df.format(z);
      zl.setText("z: " + zs + "N");
   }

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
      
      JLabel measuredForceLabel = new JLabel("Total Force: ");
      JLabel measuredForceMeas  = new JLabel("<Force Value>");
      panel.add(measuredForceLabel);
      panel.add(measuredForceMeas);

      return panel;
   }
   
   //
   //
   //
   private void setUpMeasuredForceData(MechanismSupportData data){
      ForceVector fv = data.forceVector();
      double force = fv.magnitude();
      java.text.DecimalFormat df = null;
      df = new java.text.DecimalFormat("###,###,###.##");
      JPanel panel = (JPanel)this.getComponent(3);
      JLabel label = (JLabel)panel.getComponent(1);
      label.setText(df.format(force)+"N");
   }

   //
   //
   //
   private void setUpGUI(JFrame parent){
      this._parent = parent;
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
