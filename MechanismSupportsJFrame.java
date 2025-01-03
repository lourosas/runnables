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
      JPanel panel = (JPanel)this.getContentPane();
      System.out.println(lmd.supportData());
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setUpGUI(JFrame parent){
      int WIDTH  = 425;
      int HEIGHT = 1000;

      this.setLayout(new GridLayout(0,1));
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
   public void setVisual(){}
   
}
//////////////////////////////////////////////////////////////////////
