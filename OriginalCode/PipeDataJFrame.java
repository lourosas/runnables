//////////////////////////////////////////////////////////////////////
/*
Copyright 2025 Lou Rosas

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

public class PipeDataJFrame extends GenericJInteractionFrame{
   private JFrame     _parent;
   private StageData  _sd;

   {
      _parent    = null;
      _sd        = null;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public PipeDataJFrame(){
      this(null);
   }

   //
   //
   //
   public PipeDataJFrame(JFrame parent){
      super();
      this._parent = parent;
      this.setUpGUI(parent);
   }

   //////////////////////////Public Methods///////////////////////////
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

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void setUpGUI(JFrame parent){
      int WIDTH     = 425;
      int HEIGHT    = 100;
      JPanel panel  = new JPanel();
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
      //Do not set visible until all the Data Panels are set...
      this.setVisible(false);
   }

   //
   //
   //
   private void setVisual(){
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      int WIDTH    = 425;
      int HEIGHT   = 250*panel.getComponentCount();
      this.setVisible(false);
      this.setSize(WIDTH, HEIGHT);
      this.setVisible(true);
   }

   //
   //
   //
   private JPanel setUpTitle(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      String s = new String("Pipes Stage ");
      panel.add(new JLabel(s, SwingConstants.CENTER));
      return panel;
   }

   //
   //
   //
   private void updateData(StageData sd){
      if(sd != null){
         this._sd = sd;
         this.updateTitle(sd);
         this.updatePipePanels(sd);
      }
   }

   //
   //
   //
   private void updatePipePanels(StageData sd){
      if(this._sd != null){
         PipeDataPanel pdp = null;
         JPanel panel=(JPanel)this.getContentPane().getComponent(1);
         //Initialize
         if(panel.getComponentCount() == 0){
            //Do a series of Test Prints first off...
            java.util.List<PipeData> l = null;
            l = this._sd.fuelSystemData().pipeData();
            Iterator<PipeData> it = l.iterator();
            while(it.hasNext()){
               PipeData pd = it.next();
               //Stage Data only for the Stage!!!
               if(this._sd.stageNumber() == pd.stage()){
                  int stage = pd.stage();
                  int tank  = pd.tank();
                  int idx   = pd.number();
                  pdp = new PipeDataPanel(stage,tank,idx);
                  pdp.setUpPipeData(this._sd);
                  panel.add(pdp);
               }
            }
         }
         //Update
         else{}
      }
   }

   //
   //
   //
   private void updateTitle(StageData sd){
      if(sd != null){
         JPanel panel = (JPanel)this.getContentPane().getComponent(0);
         JLabel label = (JLabel)panel.getComponent(0);
         label.setText("Pipes Stage "+sd.stageNumber());
      }
   }
}
//////////////////////////////////////////////////////////////////////
