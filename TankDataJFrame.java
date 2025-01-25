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

public class TankDataJFrame extends GenericJInteractionFrame{
   //Use Anonymous Inner Classes
   private JFrame       _parent;
   private StageData    _sd;

   {
      _parent  = null;
      _sd      = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TankDataJFrame(){
      this(null);
   }

   //
   //
   //
   public TankDataJFrame(JFrame parent){
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
      if(this._sd == null){
         //Only update the Title Once...upon first stage data(for now)
         this.updateTitle(data);
      }
      this._sd = data;
      //technically, do not need to save off the data Globally...
      this.updateData(data);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void setUpGUI(JFrame parent){
      int WIDTH    = 425;
      int HEIGHT   = 1000;
      JPanel panel = new JPanel();
      //Put all the Tank Panels one...
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
      //Do not set visible until all the Support Panels are set
      this.setVisible(false);
   }

   //
   //
   //
   private JPanel setUpTitle(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      String s = new String("Tanks Stage ");
      //Will need to add to this based on the Stage
      panel.add(new JLabel(s, SwingConstants.CENTER));
      return panel;
   }

   //
   //
   //
   private void setVisual(){
      //Definitely Temporary!!!!!!
      //More permanent once TankDataPanel(s) are created!!!
      int WIDTH  = 425;
      int HEIGHT = 150;//* number of TankDataPanel(s)
      this.setVisible(false);
      this.setSize(WIDTH, HEIGHT);
      this.setVisible(true);

      
   }

   //
   //
   //
   private void updateData(StageData sd){
      if(this._sd != null){
         System.out.println("Tank Data Test Prints");
         System.out.println(this._sd.fuelSystemData().tankData());
         this.updateStagePanels(sd);
      }
   }

   //
   //
   //
   private void updateStagePanels(StageData sd){
      if(this._sd != null){
         TankDataPanel tdp = null;
         JPanel panel=(JPanel)this.getContentPane().getComponent(1);
         if(panel.getComponentCount() == 0){
            java.util.List<TankData> t = null;
            t = this._sd.fuelSystemData().tankData();
            Iterator<TankData> it = t.iterator();
            while(it.hasNext()){
               if(this._sd.stageNumber() == it.next().stage()){
                  tdp = new TankDataPanel();
                  tdp.setUpTankData(this._sd);  //Send in all Stage Data
               }
            }
         }
         else{}
      }
   }

   //
   //
   //
   private void updateTitle(StageData sd){
      if(this._sd != null){
         JPanel panel = (JPanel)this.getContentPane().getComponent(0);
         JLabel label = (JLabel)panel.getComponent(0);
         label.setText(label.getText()+this._sd.stageNumber());
         System.out.println(label.getText());
      }
   }
}
//////////////////////////////////////////////////////////////////////
