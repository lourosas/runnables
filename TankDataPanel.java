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
import myclasses.*;
import javax.swing.border.*;

public class TankDataPanel extends JPanel{
   //Use Anonymous Inner Classes

   private int _tankNumber;

   {
      _tankNumber = 0;
   };
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public TankDataPanel(int tankNumber){
      super();
      if(tankNumber > 0){
         this._tankNumber = tankNumber;
      }
      this.setUpGUI();
   }

   //
   //
   //
   public void setUpTankData(StageData sd){
      this.setUpStageNumber(sd);
      this.setUpTankNumber(sd);
      this.setUpFuelType(sd);
      this.setUpTemperatureData(sd);
      this.setUpWeightData(sd);
      this.setUpEmptyRateData(sd);
      this.setUpErrorData(sd);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void activateButtonPanel(String action){
      this.deactivateButtonPanel();
      JPanel bp = (JPanel)this.getComponent(1);
      for(int i = 0; i < bp.getComponentCount(); ++i){
         try{
            JButton b = (JButton)bp.getComponent(i);
            if(b.getText().toUpperCase().equals("ERROR")){
               if(action.toUpperCase().equals("ERROR")){
                  //Stop Gap
                  b.setEnabled(true);
                  //This is how to code it up...
                  //_errors is the Error Frame!!!
                  //if(this._errors == null){
                  //   b.setEnabled(true);
                  //}
                  //else if(!this._errors.isShowing()){
                  //   b.setEnabled(true);
                  //}
               }
            }
         }
         catch(ClassCastException cce){ cce.printStackTrace(); }
      }
   }

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
            System.out.println(e); //For the time being...
            System.out.println(_tankNumber);
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
      panel.add(this.setUpPanel(2));  //Fuel  Type
      panel.add(this.setUpPanel(2));  //Temperature
      panel.add(this.setUpPanel(2));  //Weight--Newtons
      panel.add(this.setUpPanel(2));  //Empty Rate--N/s
      panel.add(this.setUpPanel(2));  //Error
      return panel;
   }

   //
   //
   //
   private void setUpEmptyRateData(StageData sd){
      java.text.DecimalFormat df = null;
      df = new java.text.DecimalFormat("###,###,###.##");
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel erp   = (JPanel)panel.getComponent(5);
      java.util.List<TankData> t = sd.fuelSystemData().tankData();
      Iterator<TankData> it = t.iterator();
      while(it.hasNext()){
         TankData td = it.next();
         if(td.number() == this._tankNumber){
            if(erp.getComponentCount() == 0){
               String rate = new String("Curreent Empty Rate: ");
               erp.add(new JLabel(rate,SwingConstants.RIGHT));
               String emptyRate = df.format(td.emptyRate());
               erp.add(new JLabel(emptyRate+"m^3/sec"));
            }
            else{}
         }
      }
   }

   //
   //
   //
   private void setUpErrorData(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel ep    = (JPanel)panel.getComponent(6);
      java.util.List<TankData> t = sd.fuelSystemData().tankData();
      Iterator<TankData> it = t.iterator();
      while(it.hasNext()){
         TankData td = it.next();
         if(td.number() == this._tankNumber){
            if(ep.getComponentCount() == 0){
               JLabel error = null;
               error = new JLabel("Error: ", SwingConstants.RIGHT);
               JLabel data = new JLabel(td.isError()+"");
               error.setForeground(Color.BLUE);
               data.setForeground(Color.BLUE);
               if(td.isError()){
                  error.setForeground(Color.RED);
                  data.setForeground(Color.RED);
               }
               ep.add(error);
               ep.add(data);
               //Indicate the Error with the Button Panel...
               if(td.isError()){
                  this.activateButtonPanel("ERROR");
               }
            }
            else{}
         }
      }
   }

   //
   //
   //
   private void setUpFuelType(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel ftp   = (JPanel)panel.getComponent(2);
      java.util.List<TankData> t =  sd.fuelSystemData().tankData();
      Iterator<TankData> it = t.iterator();
      while(it.hasNext()){
         TankData td = it.next();
         if(td.number() == this._tankNumber){
            if(ftp.getComponentCount() == 0){
               ftp.add(new JLabel("Fuel: ",SwingConstants.RIGHT));
               ftp.add(new JLabel(""+td.fuel()));
            }
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
      //One Button--Error: based on isError() == true
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
   private void setUpStageNumber(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel sdp   = (JPanel)panel.getComponent(0);
      //Get the Tank Data
      java.util.List<TankData> t = null;
      t = sd.fuelSystemData().tankData();
      Iterator<TankData> it = t.iterator();
      while(it.hasNext()){
         TankData td = it.next();
         if(td.number() == this._tankNumber){
            if(sdp.getComponentCount() == 0){
               JLabel stage = null;
               stage = new JLabel("Stage: ",SwingConstants.RIGHT);
               sdp.add(stage);
               JLabel number = new JLabel("" + td.stage());
               sdp.add(number);
            }
            else{}
         }
      }
   }

   //
   //
   //
   private void setUpTankNumber(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel tdp   = (JPanel)panel.getComponent(1);
      java.util.List<TankData> t = null;
      t = sd.fuelSystemData().tankData();
      Iterator<TankData> it = t.iterator();
      while(it.hasNext()){
         TankData td = it.next();
         if(td.number() == this._tankNumber){
            if(tdp.getComponentCount() == 0){
               tdp.add(new JLabel("Tank: ",SwingConstants.RIGHT));
               tdp.add(new JLabel(""+td.number()));
            }
            else{}
         }
      }
   }
   
   //
   //
   //
   private void setUpTemperatureData(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel tp    = (JPanel)panel.getComponent(3);
      java.util.List<TankData> t = sd.fuelSystemData().tankData();
      Iterator<TankData> it = t.iterator();
      while(it.hasNext()){
         TankData td = it.next();
         if(td.number() == this._tankNumber){
            if(tp.getComponentCount() == 0){
               String temp = new String("Temperature: ");
               tp.add(new JLabel(temp,SwingConstants.RIGHT));
               //Temperature is measured in Kelvin!!!
               tp.add(new JLabel(""+td.temperature()+"K"));
            }
         }
      }
   }

   //
   //
   //
   private void setUpWeightData(StageData sd){
      java.text.DecimalFormat df = null;
      df = new java.text.DecimalFormat("###,###,###.##");
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel wp    = (JPanel)panel.getComponent(4);
      java.util.List<TankData> t = sd.fuelSystemData().tankData();
      Iterator<TankData> it = t.iterator();
      while(it.hasNext()){
         TankData td = it.next();
         if(td.number() == this._tankNumber){
            if(wp.getComponentCount() == 0){
               wp.add(new JLabel("Weight: ",SwingConstants.RIGHT));
               String weight = df.format(td.weight());
               wp.add(new JLabel(weight+"N"));
            }
            else{}
         }
      }
   }
}
//////////////////////////////////////////////////////////////////////
