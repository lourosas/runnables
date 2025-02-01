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

public class PumpDataJFrame extends GenericJInteractionFrame{
   //Use Anonymous Inner Classes
   private JFrame    _parent;
   private StageData _sd;

   {
      _parent = null;
      _sd     = null;
   }

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public PumpDataJFrame(){
      this(null);
   }

   //
   //
   //
   public PumpDataJFramee(JFrame parent){
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
      this.updateData(data);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setUpGUI(JFrame parent){
      int WIDTH     = 425;
      int HEIGHT    = 100;
      JPanel panel  = JPanel();
      panel.setLayout(new GridLayout(0,1));

      this.setLayout(new BorderLayout());
      this.add(this.setUpTitle(), BorderLayout.NORTH);
      this.add(panel, BorderLayout.CENTER);
      this.setSize(WIDTH, HEIGHT);
      if(parent != null){
         Point p = parent.getLocation();
         this.setLocation(p.x, p.y);
      }
      this.setResizable(false);
      //Do not ser visible until all the Data Panels are set
      this.setVisible(false);
   }

   //
   //
   //
   private void setUpTitle(){
      JPanel panel = new JPanel();
      panel.setBorderLayout(BorderFactory.createEtchedBorder());
      String s = new String("Pipes Stage ");
      panel.add(new JLabel(s, SwingConstants.CENTER));
      return panel;
   }
   
   //
   //
   //
   private void setVisual(){}

   //
   //
   //
   private void updateData(StageData sd){
      if(sd != null){
         this._sd = sd;
         //Continue adding stuff here...
         this.updateTitle(sd);
         this.updatePumpPanel(sd);
      }
   }

   //
   //
   //
   public void updatePumpPanel(StageData sd){}

   //
   //
   //
   private void updateTitle(StageData sd){}
}
//////////////////////////////////////////////////////////////////////
