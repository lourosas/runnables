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
import java.text.*;
import java.io.File;
import javax.swing.border.*;
import java.time.*;
import myclasses.*;
import rosas.lou.clock.*;

public class CompletePayloadJFrame extends GenericJInteractionFrame{
   private JFrame      _parent;
   private PayloadData _data;

   {
      _parent = null;
      _data   = null;
   };

   ////////////////////////////Constructors//////////////////////////
   //
   //
   //
   public CompletePayloadJFrame(){
      this(null);
   }

   //
   //
   //
   public CompletePayloadJFrame(JFrame parent){
      this.setUpGUI(parent);
   }

   ///////////////////////////Public Methods/////////////////////////
   //
   //
   //
   public void updatePayloadData(PayloadData pd){
      this.updateCenterPanel(pd);
   }

   //////////////////////////Private Methods/////////////////////////
   //
   //
   //
   private void initializeCenterPanel(){
      JPanel p     = (JPanel)this.getContentPane();
      JPanel cp    = (JPanel)p.getComponent(0);
      
      JPanel panel = (JPanel)cp.getComponent(0);
      panel.setBorder(BorderFactory.createEtchedBorder());
      JLabel l = new JLabel("Payload Data", SwingConstants.CENTER);
      panel.add(l);

      panel = (JPanel)cp.getComponent(1);
      l = new JLabel("Payload Type: ", SwingConstants.RIGHT);
      panel.add(l);
      panel.add(new JLabel());

      panel = (JPanel)cp.getComponent(2);
      l = new JLabel("Model: ", SwingConstants.RIGHT);
      panel.add(l);
      panel.add(new JLabel());

      panel = (JPanel)cp.getComponent(3);
      l = new JLabel("Crew: ", SwingConstants.RIGHT);
      panel.add(l);
      panel.add(new JLabel());

      panel = (JPanel)cp.getComponent(4);
      l = new JLabel("Empty Weight: ", SwingConstants.RIGHT);
      panel.add(l);
      panel.add(new JLabel());

      panel = (JPanel)cp.getComponent(5);
      l = new JLabel("Measured Weight: ", SwingConstants.RIGHT);
      panel.add(l);
      panel.add(new JLabel());

      panel = (JPanel)cp.getComponent(6);
      l = new JLabel("Maximum Weight: ", SwingConstants.RIGHT);
      panel.add(l);
      panel.add(new JLabel());

      panel = (JPanel)cp.getComponent(7);
      l = new JLabel("Temperature: ", SwingConstants.RIGHT);
      panel.add(l);
      panel.add(new JLabel());

      panel = (JPanel)cp.getComponent(8);
      l = new JLabel("Error: ", SwingConstants.RIGHT);
      l.setForeground(Color.BLUE);
      panel.add(l);
      panel.add(new JLabel());
   }

   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpPanel(1)); //Title Panel
      panel.add(this.setUpPanel(2)); //Payload (Type) Panel
      panel.add(this.setUpPanel(2)); //Model Panel
      panel.add(this.setUpPanel(2)); //Crew Panel
      panel.add(this.setUpPanel(2)); //Dry Weight
      panel.add(this.setUpPanel(2)); //Measured Weight
      panel.add(this.setUpPanel(2)); //Max Weight
      panel.add(this.setUpPanel(2)); //Temperature Panel
      panel.add(this.setUpPanel(2)); //Error Panel
      return panel;
   }

   //
   //
   //
   private void setUpGUI(JFrame parent){
      int WIDTH  = 425;
      int HEIGHT = 325;
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH, HEIGHT);
      if(parent != null){
         Point p = parent.getLocation();
         this.setLocation(p.x, p.y);
      }
      JPanel centerPanel = this.setUpCenterPanel();
      this.getContentPane().add(centerPanel,BorderLayout.CENTER);
      this.initializeCenterPanel();
      this.setResizable(false);
      this.setVisible(true);
   }

   //
   //
   //
   private JPanel setUpPanel(int columns){
      JPanel panel = new JPanel();
      if(columns > 0){
         panel.setLayout(new GridLayout(1,columns));
      }
      else{
         panel.setLayout(new GridLayout(1,1));
      }
      return panel;
   }

   //
   //
   //
   private void updateCenterPanel(PayloadData pd){
      NumberFormat format = NumberFormat.getInstance(Locale.US);
      String n            = null;
      JPanel p            = (JPanel)this.getContentPane();
      JPanel cp           = (JPanel)p.getComponent(0);

      JPanel panel = (JPanel)cp.getComponent(1);
      JLabel l = (JLabel)panel.getComponent(1);
      l.setText(pd.type());

      panel = (JPanel)cp.getComponent(2);
      l = (JLabel)panel.getComponent(1);
      l.setText(pd.model());

      panel = (JPanel)cp.getComponent(3);
      l = (JLabel)panel.getComponent(1);
      if(pd.crew() < 1){
         l.setText("N/A");
      }
      else{
         l.setText("" + pd.crew());
      }

      panel = (JPanel)cp.getComponent(4);
      n = format.format(pd.dryWeight())+"N";
      l = (JLabel)panel.getComponent(1);
      l.setText(n);

      //Measured Weight IS Current Weight
      panel = (JPanel)cp.getComponent(5);
      n = format.format(pd.currentWeight()) +"N";
      l = (JLabel)panel.getComponent(1);
      l.setText(n);

      panel = (JPanel)cp.getComponent(6);
      n = format.format(pd.maxWeight()) +"N";
      l = (JLabel)panel.getComponent(1);
      l.setText(n);

      panel = (JPanel)cp.getComponent(7);
      n = format.format(pd.currentTemp())+"K";
      l = (JLabel)panel.getComponent(1);
      l.setText(n);

      panel = (JPanel)cp.getComponent(8);
      l = (JLabel)panel.getComponent(1);
      l.setText("" + pd.isError());
      //Color for Component 0 already set to Blue
      l.setForeground(Color.BLUE);
      if(pd.isError()){
         l = (JLabel)panel.getComponent(0);
         l.setForeground(Color.RED);
         l = (JLabel)panel.getComponent(1);
         l.setForeground(Color.RED);
      }

      p.repaint();
      p.revalidate();
   }
}
