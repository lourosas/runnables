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
import myclasses.*;
import javax.swing.border.*;

public class PipeDataPanel extends JPanel{
   //Use Anonymous Inner Classes
   
   private int _stage;
   private int _tank;
   private int _pipe;

   {
      _stage = 0;
      _pipe  = 0;
      _pipe  = 0; //PipeData.number()
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public PipeDataPanel(int stage, int tank, int number){
      super();
      if(stage > 0 && tank > 0 && number > 0){
         this._stage = stage;
         this._tank  = tank;
         this._pipe  = number;
         this.setUpGUI();
      }
   }

   //////////////////////////Public Methods///////////////////////////
   //
   //
   //
   public void setUpPipeData(StageData sd){
      this.setUpStageNumber(sd);
      this.setUpTankNumber(sd);
      this.setUpPipeNumber(sd);
      this.setUpFuelType(sd);
   }

   /////////////////////////Private Methods///////////////////////////
   //
   //
   //
   private void deactivateButtonPanel(){
      JPanel bp = (JPanel)this.getComponent(1);
      for(int i = 0; i < bp.getComponentCount(); ++i){
         JButton b = (JButton)bp.getComponent(i);
         b.setEnabled(false);
      }
   }

   //
   //
   //
   private JPanel setUpButtonPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      JButton error = new JButton("Error");
      error.addActionListener(new ActionListener(){
         //TBD
         public void actionPerformed(ActionEvent e){
            //TBD
            System.out.println(e);
         }
      });
      panel.add(error);
      return panel;
   }

   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpPanel(2));  //Stage Number
      panel.add(this.setUpPanel(2));  //Tank  Number
      panel.add(this.setUpPanel(2));  //Pipe Number (Index)
      panel.add(this.setUpPanel(2));  //Fuel Type
      panel.add(this.setUpPanel(2));  //Temperature
      panel.add(this.setUpPanel(2));  //Flow Rate
      panel.add(this.setUpPanel(2));  //Error
      return panel;
   }

   //
   //
   //
   private void setUpFuelType(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel ftp   = (JPanel)panel.getComponent(3);
      java.util.List<PipeData> l = sd.fuelSystemData().pipeData();
      Iterator<PipeData> it = l.iterator();
      while(it.hasNext()){
         PipeData pd = it.next();
         boolean isStage = pd.stage()  == this._stage;
         boolean isTank  = pd.tank()   == this._tank;
         boolean isPipe  = pd.number() == this._pipe;
         if(isStage && isTank && isPipe){
            //Initialization
            if(ftp.getComponentCount() == 0){
               JLabel fuel = null;
               fuel = new JLabel("Fuel:  ",SwingConstants.RIGHT);
               ftp.add(fuel);
               JLabel type = new JLabel(""+pd.type());
               ftp.add(type);
            }
            //Update
            else{}
         }
      }
   }

   //
   //
   //
   private void setUpGUI(){
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setLayout(new BorderLayout());
      this.add(this.setUpCenterPanel(), BorderLayout.CENTER);
      this.add(this.setUpButtonPanel(), BorderLayout.SOUTH);
      this.deactivateButtonPanel();
   }

   //
   //
   //
   private JPanel setUpPanel(int columns){
      int cols = columns > 0 ? columns : 1;
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,cols));
      return panel;
   }

   //
   //
   //
   private void setUpPipeNumber(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel pdp   = (JPanel)panel.getComponent(2);
      java.util.List<PipeData> l = sd.fuelSystemData().pipeData();
      Iterator<PipeData> it = l.iterator();
      while(it.hasNext()){
         PipeData pd = it.next();
         boolean isStage = pd.stage()  == this._stage;
         boolean isTank  = pd.tank()   == this._tank;
         boolean isPipe  = pd.number() == this._pipe;
         if(isStage && isTank && isPipe){
            //Initialization
            if(pdp.getComponentCount() == 0){
               JLabel pipe = null;
               pipe = new JLabel("Pipe:  ",SwingConstants.RIGHT);
               pdp.add(pipe);
               JLabel number = new JLabel(""+pd.number());
               pdp.add(number);
            }
            //Update
            else{}
         }
      }
   }

   //
   //
   //
   private void setUpStageNumber(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel sdp   = (JPanel)panel.getComponent(0);
      java.util.List<PipeData> l = sd.fuelSystemData().pipeData();
      Iterator<PipeData> it = l.iterator();
      while(it.hasNext()){
         PipeData pd = it.next(); 
         boolean isStage = pd.stage()  == this._stage;
         boolean isTank  = pd.tank()   == this._tank;
         boolean isPipe  = pd.number() == this._pipe;
         if(isStage && isTank && isPipe){
            //Initialization
            if(sdp.getComponentCount() == 0){
               JLabel stage = null;
               stage = new JLabel("Stage: ",SwingConstants.RIGHT);
               sdp.add(stage);
               JLabel number = new JLabel(""+pd.stage());
               sdp.add(number);
            }
            //Update
            else{}
         }
      }
   }

   //
   //
   //
   private void setUpTankNumber(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel tnp   = (JPanel)panel.getComponent(1);
      java.util.List<PipeData> l = sd.fuelSystemData().pipeData();
      Iterator<PipeData> it = l.iterator();
      while(it.hasNext()){
         PipeData pd = it.next();
         boolean isStage = pd.stage()  == this._stage;
         boolean isTank  = pd.tank()   == this._tank;
         boolean isPipe  = pd.number() == this._pipe;
         if(isStage && isTank && isPipe){
            //Initialization
            if(tnp.getComponentCount() == 0){
               JLabel tank = null;
               tank = new JLabel("Tank:  ",SwingConstants.RIGHT);
               tnp.add(tank);
               JLabel number = new JLabel(""+pd.tank());
               tnp.add(number);
            }
            //Update
            else{}
         }
      }
   }
}
//////////////////////////////////////////////////////////////////////
