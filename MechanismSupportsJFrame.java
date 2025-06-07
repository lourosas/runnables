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

public class MechanismSupportsJFrame extends
GenericJInteractionFrame{
   private JFrame _parent;

   {
      _parent = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public MechanismSupportsJFrame(){
      this(null);
   }

   //
   //
   //
   public MechanismSupportsJFrame(JFrame parent){
      this.setUpGUI(parent);
   }

   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void updateSupportsData(LaunchingMechanismData lmd){
      this.updateLaunchingMechanismData(lmd);
      //this.setVisual();
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setUpGUI(JFrame parent){
      int WIDTH    = 425;
      int HEIGHT   = 1000;
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));

      //this.setLayout(new GridLayout(0,1));
      this.setLayout(new BorderLayout());
      //Set up the Title
      this.add(this.setUpTitle(), BorderLayout.NORTH);
      this.add(panel, BorderLayout.CENTER);
      this.setSize(WIDTH, HEIGHT);
      if(parent != null){
         Point p = parent.getLocation();
         this.setLocation(p.x, p.y);
      }
      this.setResizable(false);
      //Do not set visible until all the Suport Panels are set
      this.setVisible(false);
   }

   //
   //
   //
   private JPanel setUpTitle(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      String s = new String("Platform Supports");
      panel.add(new JLabel(s, SwingConstants.CENTER));
      return panel;
   }

   //
   //
   //
   private void setVisual(){
      JPanel panel= (JPanel)this.getContentPane().getComponent(1);
      int WIDTH   = 425;
      int HEIGHT  = 150*panel.getComponentCount();
      this.setVisible(false);
      this.setSize(WIDTH, HEIGHT);
      this.setVisible(true);
   }

   //
   //
   //
   private void updateLaunchingMechanismData
   (
      LaunchingMechanismData lmd
   ){
      MechanismSupportDataPanel mp = null;
      java.util.List<MechanismSupportData> list = lmd.supportData();
      int count = 0;
      int idx   = 0;
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      //This will need to be improved!!!
      //The first update, and nothing is created...
      if(panel.getComponentCount() > 0){
         System.out.println("Component Count: ");
         System.out.println(panel.getComponentCount());
         panel.removeAll();
         //panel.repaint();//So much more to do here!!
         //panel.revalidate();
      }
      if(panel.getComponentCount() == 0){
         while(count < list.size()){
            MechanismSupportData data = list.get(idx);
            ++idx;
            if(data.id() == count){
               mp = new MechanismSupportDataPanel();
               mp.setUpData(data);
               panel.add(mp);
               ++count;
               idx = 0;
            }
         }
      }
   }
}
//////////////////////////////////////////////////////////////////////
