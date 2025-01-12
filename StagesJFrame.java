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

public class StagesJFrame extends GenericJInteractionFrame{
   private JFrame _parent;

   {
      _parent = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public StagesJFrame(){
      this(null);
   }

   //
   //
   //
   public StagesJFrame(JFrame parent){
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
   public void update(RocketData data){
      this.updateStageData(data);
   }
   
   //////////////////////////Private Methods/////////////////////////
   //
   //
   //
   private void setUpGUI(JFrame parent){
      int WIDTH  = 425;
      int HEIGHT = 1000;
      this.setLayout(new BorderLayout());

      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));
      
      this.add(this.setUpTitle(), BorderLayout.NORTH);
      this.add(panel, BorderLayout.CENTER);
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
   private JPanel setUpTitle(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      String s = new String("Stages");
      panel.add(new JLabel(s, SwingConstants.CENTER));
      return panel;
   }

   //
   //
   //
   private void setVisual(){
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      int WIDTH    = 425;
      int HEIGHT   = 225*panel.getComponentCount();
      this.setSize(WIDTH, HEIGHT);
      this.setVisible(true);
   }

   //
   //
   //
   private void updateStageData(RocketData data){
      StageDataPanel sdp = null;
      java.util.List<StageData> list = data.stages();
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      if(panel.getComponentCount() == 0){
         Iterator<StageData> it = list.iterator();
         while(it.hasNext()){
            StageData sd = (StageData)it.next();
            sdp = new StageDataPanel(this._parent);
            sdp.update(sd);
            panel.add(sdp);
         }
      }
      else{
         //TBD...if the panel is already there, will somehow need
         //to update the data
         //Test Prints==>Keep this for NOW!!!
         System.out.print("StagesJFrame Component Count: ");
         System.out.println(panel.getComponentCount());
      }
   }
}

//////////////////////////////////////////////////////////////////////
