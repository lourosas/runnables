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

public class PumpDataPanel extends JPanel{
   //Use Anonymous Inner Classes
   
   private int _pump;
   private int _stage;

   {
      _pump   = 0;
      _stage  = 0;
   };

   ///////////////////////////Constructors////////////////////////////
   //
   //
   //
   public PumpDataPanel(int stage, int pump){
      super();
      if(stage > 0 && pump > 0){
         this._stage = stage;
         this._pump  = pump;
         this.setUpGUI();
      }
   }

   ///////////////////////////Public Mehtods//////////////////////////
   //
   //
   //
   public void setUpPumpData(StageData sd){
      java.util.List<PumpData> l = sd.fuelSystemData().pumpData();
      this.setUpStageNumber(sd);
      this.setUpTankPumpNumber(sd);
      this.setUpFuelType(sd);
      this.setUpTemperatureData(sd);
      this.setUpFlowRate(sd);
      this.setUpErrorData(sd);
   }

   //////////////////////////Private Methods//////////////////////////
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
            System.out.println(e);
         }
      });
      return panel;
   }

   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpPanel(2)); //Stage Number
      panel.add(this.setUpPanel(2)); //Tank  Number
      panel.add(this.setUpPanel(2)); //Fuel  Type
      panel.add(this.setUpPanel(2)); //Temperature
      panel.add(this.setUpPanel(2)); //Flow
      panel.add(this.setUpPanel(2)); //Error
      return panel;
   }

   //
   //
   //
   private void setUpErrorData(StageData sd){
      int right = SwingConstants.RIGHT;
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel ep    = (JPanel)panel.getComponent(5);
      java.util.List<PumpData> l = sd.fuelSystemData().pumpData();
      Iterator<PumpData> it = l.iterator();
      while(it.hasNext()){
         PumpData pd = it.next();
         if(pd.stage() == this._stage && pd.index() == this._pump){
            //Initialization
            if(ep.getComponentCount() == 0){
               JLabel error = new JLabel("Error: ", right);
               JLabel data  = new JLabel(""+pd.isError());
               error.setForeground(Color.BLUE);
               data.setForeground(Color.BLUE);
               if(pd.isError()){
                  error.setForeground(Color.RED);
                  data.setForeground(Color.RED);
               }
               ep.add(error);
               ep.add(data);
               //Indicate the Error with the Button Panel...
               //if(pd.isError()){
               //   this.activateButtonPanel("ERROR");
               //}
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

   //Flow Data in Pre-Launch NEEDS TO BE 0
   //Initialization shows what it is post pre-launch to separation...
   //
   private void setUpFlowRate(StageData sd){
      int right = SwingConstants.RIGHT;
      java.text.DecimalFormat df = null;
      df = new java.text.DecimalFormat("##,###.##");
      
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel fdp   = (JPanel)panel.getComponent(4);
      java.util.List<PumpData> l = sd.fuelSystemData().pumpData();
      Iterator<PumpData> it = l.iterator();
      while(it.hasNext()){
         PumpData pd = it.next();
         if(pd.stage() == this._stage && pd.index() == this._pump){
            //Initialization
            if(fdp.getComponentCount() == 0){
               fdp.add(new JLabel("Current Pump Rate: ",right));
               String rate = df.format(pd.flow());
               fdp.add(new JLabel(rate+"m^3/Sec"));
            }
            //Update
            else{}
         }
      }
   }

   //
   //
   //
   private void setUpFuelType(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel fdp   = (JPanel)panel.getComponent(2);
      java.util.List<PumpData> l = sd.fuelSystemData().pumpData();
      Iterator<PumpData> it = l.iterator();
      while(it.hasNext()){
         PumpData pd = it.next();
         if(pd.stage() == this._stage && pd.index() == this._pump){
            //Initialization
            if(fdp.getComponentCount() == 0){
               JLabel fuel = null;
               fuel = new JLabel("Fuel Type: ",SwingConstants.RIGHT);
               fdp.add(fuel);
               JLabel type = new JLabel(""+pd.type());
               fdp.add(type);
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
      java.util.List<PumpData> l = sd.fuelSystemData().pumpData();
      Iterator<PumpData> it = l.iterator();
      while(it.hasNext()){
         PumpData pd = it.next();
         //Pump<-->Tank One Pump per Tank...
         //May not need the Stage Check, but not sure at the moment...
         if(pd.stage() == this._stage && pd.index() == this._pump){
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

   //Tank and Pump are synonomous-->One in the Same in this setup
   //
   //
   private void setUpTankPumpNumber(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel tpdp  = (JPanel)panel.getComponent(1);
      java.util.List<PumpData> l = sd.fuelSystemData().pumpData();
      Iterator<PumpData> it = l.iterator();
      while(it.hasNext()){
         PumpData pd = it.next();
         if(pd.stage() == this._stage && pd.index() == this._pump){
            //Initialization
            if(tpdp.getComponentCount() == 0){
               JLabel pump = null;
               pump = new JLabel("Pump/Tank: ",SwingConstants.RIGHT);
               tpdp.add(pump);
               JLabel number = new JLabel(""+pd.index());
               tpdp.add(number);
            }
            //Update
            else{}
         }
      }
   }

   //
   //
   //
   private void setUpTemperatureData(StageData sd){
      int right = SwingConstants.RIGHT;
      java.text.DecimalFormat df = null;
      df = new java.text.DecimalFormat("###.##");

      JPanel panel = (JPanel)this.getComponent(0);
      JPanel tdp   = (JPanel)panel.getComponent(3);
      java.util.List<PumpData> l = sd.fuelSystemData().pumpData();
      Iterator<PumpData> it = l.iterator();
      while(it.hasNext()){
         PumpData pd = it.next();
         if(pd.stage() == this._stage && pd.index() == this._pump){
            //Initialization
            if(tdp.getComponentCount() == 0){
               tdp.add(new JLabel("Temperature: ",right));
               String temp = df.format(pd.temperature());
               tdp.add(new JLabel(temp+"K"));
            }
            //Update
            else{}
         }
      }
   }
}
//////////////////////////////////////////////////////////////////////
