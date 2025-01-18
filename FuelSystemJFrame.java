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

public class FuelSystemJFrame extends GenericJInteractionFrame{
   //Use Anonymous Innerclasses

   private JFrame _parent;

   {
      _parent    = null;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public FuelSystemJFrame(){
      this(null);
   }

   //
   //
   //
   public FuelSystemJFrame(JFrame parent){
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
      this.updateFuelSystemData(data);
   }
   

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void activateButtonPanel(String action){
      this.deactivateButtonPanel();
      JPanel bp = (JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < bp.getComponentCount(); ++i){
         try{
            JButton b = (JButton)bp.getComponent(i);
            if(b.getText().toUpperCase().equals("ERROR")){
               if(action.toUpperCase().equals("ERROR")){
                  //Stop Gap
                  b.setEnabled(true);
                  //This is how to code it up...
                  //if(this._errors == null){
                  //   b.setEnabled(true);
                  //}
                  //else if(!this._errors.isShowing()){
                  //   b.setEnabled(true);
                  //}
               }
            }
            else if(b.getText().toUpperCase().equals("PIPE DATA")){
               //Stop Gap
               b.setEnabled(true);
               //if(this._pipeData == null){
               //   b.setEnabled(true);
               //}
               //else if(!this._pipeData.isShowing()){
               //   b.setEnabled(true);
               //}
            }
            else if(b.getText().toUpperCase().equals("PUMP DATA")){
               //Stop Gap
               b.setEnabled(true);
               //if(this._pumpData == null){
               //   b.setEnabled(true);
               //}
               //else if(!this._pumpData.isShowing()){
               //   b.setEnabled(true);
               //}
            }
            else if(b.getText().toUpperCase().equals("TANK DATA")){
               //Stop Gap
               b.setEnabled(true);
               //if(this._pipeData == null){}
               //else if(!this._pipeData.isShowing()){
               //   b.setEnabled(true);
               //}
            }
         }
         catch(ClassCastException cce){ cce.printStackTrace(); }
      }
   }

   //
   //
   //
   private void deactivateButtonPanel(){
      JPanel bp = (JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < bp.getComponentCount(); ++i){
         try{
            JButton b = (JButton)bp.getComponent(i);
            b.setEnabled(false);
         }
         catch(ClassCastException cce){ cce.printStackTrace(); }
      }
   }

   //
   //
   //
   private JPanel setUpButtonPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      JButton pipes = new JButton("Pipe Data");
      JButton pumps = new JButton("Pump Data");
      JButton tanks = new JButton("Tank Data");
      JButton error = new JButton("Error");

      pipes.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            System.out.println(e);
         }
      });
      pumps.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            System.out.println(e);
         }
      });
      tanks.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            System.out.println(e);
         }
      });
      error.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            System.out.println(e);
         }
      });

      panel.add(pipes);
      panel.add(pumps);
      panel.add(tanks);
      panel.add(error);
      return panel;
   }

   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));
      //TEMPORARY NOTE:  Stage will go in the Title Bar
      panel.add(this.setUpPanel(2)); //Number of Fuel Tanks
      panel.add(this.setUpPanel(2)); //Number of Pipes
      panel.add(this.setUpPanel(2)); //Number of Pumps
      panel.add(this.setUpPanel(2)); //Error Indicator
      return panel;
   }

   //
   //
   //
   private void setUpGUI(JFrame parent){
      //For the time being, no need to change the size
      int WIDTH   = 425;
      int HEIGHT  = 225;
      this.setLayout(new BorderLayout());

      this.add(this.setUpTitle(),       BorderLayout.NORTH);
      this.add(this.setUpCenterPanel(), BorderLayout.CENTER);
      this.add(this.setUpButtonPanel(), BorderLayout.SOUTH);
      if(parent != null){
         Point p = parent.getLocation();
         this.setLocation(p.x, p.y);
      }

      this.deactivateButtonPanel();
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);
      this.setVisible(false);
   }

   //
   //
   //
   private JPanel setUpPanel(int columns){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1,columns));
      return panel;
   }

   //
   //
   //
   private JPanel setUpTitle(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      String title = new String("Fuel System");
      panel.add(new JLabel(title, SwingConstants.CENTER));
      return panel;
   }

   //
   //
   //
   private void setVisual(){
      this.setVisible(true);
   }

   //
   //
   //
   private void updateButtonPanel(StageData data){
      FuelSystemData fsd = data.fuelSystemData();
      if(fsd.isError()){
         this.activateButtonPanel("ERROR");
      }
      else{
         this.activateButtonPanel("GOOD DATA");
      }
   }

   //
   //
   //
   private void updateCenterPanel(StageData data){
      this.updateFuelTankPanel(data.fuelSystemData());
      this.updatePipePanel(data.fuelSystemData());
      this.updatePumpPanel(data.fuelSystemData());
      this.updateErrorPanel(data);
   }


   //
   //
   //
   private void updateErrorPanel(StageData data){
      JPanel panel  = (JPanel)this.getContentPane().getComponent(1);
      JPanel epanel = (JPanel)panel.getComponent(3);
      //This is essentially panel initialization
      if(epanel.getComponentCount() == 0){
         String s     = new String("Error: ");
         JLabel label = new JLabel(s, SwingConstants.RIGHT);
         JLabel error = new JLabel("" + data.isError());
         label.setForeground(Color.BLUE);
         error.setForeground(Color.BLUE);
         if(data.isError()){
            label.setForeground(Color.RED);
            error.setForeground(Color.RED);
         }
         epanel.add(label);
         epanel.add(error);
      }
      else{}
   }

   //
   //
   //
   private void updateFuelSystemData(StageData data){
      //Test Print
      System.out.println("Fuel System Stage Data: "+data);
      this.updateTitle(data);
      this.updateCenterPanel(data);
      this.updateButtonPanel(data);
   }

   //
   //
   //
   private void updateFuelTankPanel(FuelSystemData fsd){
      JPanel panel   = (JPanel)this.getContentPane().getComponent(1);
      JPanel ftpanel = (JPanel)panel.getComponent(0);
      //This is essentially Panel Initialization
      if(ftpanel.getComponentCount() == 0){
         String s     = new String("Fuel Tanks: ");
         JLabel label = new JLabel(s, SwingConstants.RIGHT);
         java.util.List<TankData> l = fsd.tankData();
         JLabel tanks = new JLabel("" + l.size());
         ftpanel.add(label);
         ftpanel.add(tanks);
      }
      else{}
   }

   //
   //
   //
   private void updatePipePanel(FuelSystemData fsd){
      JPanel panel   = (JPanel)this.getContentPane().getComponent(1);
      JPanel pdpanel = (JPanel)panel.getComponent(1);
      //This is essentially Panel Initialization
      if(pdpanel.getComponentCount() == 0){
         String s     = new String("Number of Pipes: ");
         JLabel label = new JLabel(s, SwingConstants.RIGHT);
         java.util.List<PipeData> l = fsd.pipeData();
         JLabel pipes = new JLabel("" + l.size());
         pdpanel.add(label);
         pdpanel.add(pipes);
      }
      else{}
   }

   //
   //
   //
   private void updatePumpPanel(FuelSystemData fsd){
      JPanel panel   = (JPanel)this.getContentPane().getComponent(1);
      JPanel pmpanel = (JPanel)panel.getComponent(2);
      //This is essentially Panel initialization
      if(pmpanel.getComponentCount() == 0){
         String s     = new String("Number of Pumps: ");
         JLabel label = new JLabel(s, SwingConstants.RIGHT);
         java.util.List<PumpData> l = fsd.pumpData();
         JLabel pumps = new JLabel("" + l.size());
         pmpanel.add(label);
         pmpanel.add(pumps);
      }
      else{}
   }

   //
   //
   //
   private void updateTitle(StageData data){
      JPanel panel = (JPanel)this.getContentPane().getComponent(0);
      JLabel label = (JLabel)panel.getComponent(0);
      String s = label.getText();
      label.setText(s + " Stage: " + data.stageNumber());
   }
}
//////////////////////////////////////////////////////////////////////
