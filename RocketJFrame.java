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

public class RocketJFrame extends GenericJInteractionFrame{
   RocketData _rocketData; //The Current Rocket Data...

   {
      _rocketData = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public RocketJFrame(){
      this(null);
   }

   //
   //
   //
   public RocketJFrame(JFrame parent){
      this.setUpGUI(parent);
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void initialize(RocketData rd){
      this.setUpRocketData(rd);
      this.setVisual();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(0,2));
      return panel;
   }

   //
   //
   //
   private void setUpRocketData(RocketData rd){
      this._rocketData = rd;
      System.out.println("poop!"+this._rocketData);
      System.out.println("Fucking Count: "+this.getComponentCount());
      JPanel panel = (JPanel)this.getComponent(0);
      panel.add(new JLabel("poop"));
      this.setVisual();
   }

   //
   //
   //
   private JPanel setUpSouthPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      JButton stages = new JButton("Stages");
      stages.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //Test Prints for the time being...
            System.out.println(e);
         }
      });
      panel.add(stages);
      return panel;
   }

   //
   //
   //
   private void setUpGUI(JFrame parent){
      int WIDTH  = 425;
      int HEIGHT = 200;
      System.out.println("\nFucking setUpGUI()\n");
      this.setLayout(new BorderLayout());//allow for a button panel
      this.setSize(WIDTH, HEIGHT);
      //Do not set up the visualization until it is time to display
      //this.setVisible(true);
      if(parent != null){
         Point p = parent.getLocation();
         this.setLocation(p.x, p.y); //Right on top of the parent...
      }
      JPanel centerPanel = this.setUpCenterPanel();
      JPanel southPanel  = this.setUpSouthPanel();
      this.getContentPane().add(centerPanel,BorderLayout.CENTER);
      this.getContentPane().add(southPanel, BorderLayout.SOUTH);  
      this.setResizable(false);
      //this.setVisible(true);

   }

   //
   //
   //
   private void setVisual(){
      System.out.println("\nFucking setVisual()\n");
      this.setVisible(false);
      this.setSize(425,200);
      this.setVisible(true);
   }
}
//////////////////////////////////////////////////////////////////////
