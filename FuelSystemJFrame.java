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
import myclasses.*;
import rosas.lou.clock.*;

public class FuelSystemJFrame extends GenericJInteractionFrame{
   //Use Anonymous Innerclasses

   private JFrame _parent;

   {
      _parent    = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public FuelSystemJFrame(){
      this(null);
   }

   //
   //
   //
   public FuelSystemJFrame(JFrame parent){
      super();
      this._parent = parent;
      this.setUpGUI(parent);
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void requestDisplay(){
      this.setVisual();
   }

   //
   //
   //
   public void update(StageData data){
      this.updateFuelSystemData(data);
   }
   

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void activateButtonPanel(String action){}

   //
   //
   //
   private void deactivateButtonPanel(){
      JPanel bp = (JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < bp.getComponentCount(); ++i){
         try{
            JButton b = (JButton)bp.getComponent(i);
            b.setEnabled(false);
         }
         catch(ClassCastException cce){ cce.printStackTrace(); }
      }
   }

   //
   //
   //
   private JPanel setUpButtonPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      JButton pipes = new JButton("Pipe Data");
      JButton pumps = new JButton("Pump Data");
      JButton tanks = new JButton("Tank Data");
      JButton error = new JButton("Error");

      pipes.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            System.out.println(e);
         }
      });
      pumps.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            System.out.println(e);
         }
      });
      tanks.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            System.out.println(e);
         }
      });
      error.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            System.out.println(e);
         }
      });

      panel.add(pipes);
      panel.add(pumps);
      panel.add(tanks);
      panel.add(error);
      return panel;
   }

   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpPanel(2));//Stage Number
      panel.add(this.setUpPanel(2));//Error
      return panel;
   }

   //
   //
   //
   private void setUpGUI(JFrame parent){
      //For the time being, no need to change the size
      int WIDTH   = 425;
      int HEIGHT  = 225;
      this.setLayout(new BorderLayout());

      this.add(this.setUpTitle(),       BorderLayout.NORTH);
      this.add(this.setUpCenterPanel(), BorderLayout.CENTER);
      this.add(this.setUpButtonPanel(), BorderLayout.SOUTH);
      if(parent != null){
         Point p = parent.getLocation();
         this.setLocation(p.x, p.y);
      }

      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);
      this.setVisible(false);
   }

   //
   //
   //
   private JPanel setUpPanel(int columns){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1,columns));
      return panel;
   }

   //
   //
   //
   private JPanel setUpTitle(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      String title = new String("Fuel System");
      panel.add(new JLabel(title, SwingConstants.CENTER));
      return panel;
   }

   //
   //
   //
   private void setVisual(){
      this.setVisible(true);
   }

   //
   //
   //
   private void updateFuelSystemData(StageData data){
      //Test Print
      System.out.println("Fuel System Stage Data: "+data);
   }
}
//////////////////////////////////////////////////////////////////////
