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
import java.text.NumberFormat;
import myclasses.*;
import javax.swing.border.*;

public class StageDataPanel extends JPanel{
   //Transfer this to the other Frames that are part of the Stage Data
   private JFrame  _parent;
   //Both TBD
   //private FuelSystemFrame _fuelSystem;
   //private EnginesFrame    _engines;
   //private ErrorFrame      _errors;

   {
      _parent       = null;
      //_fuelSystem = null;
      //_engines    = null;
      //_errors     = null;
   };
   //Going to use Anonumous Inner Classes for this...
   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public StageDataPanel(){
      this(null);
   }

   //
   //
   //
   public StageDataPanel(JFrame parent){
      super();
      this._parent = parent;
      this.setUpGUI();
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void update(StageData sd){
      this.updateDataPanel(sd);
      this.deactivateButtonPanel();
      if(sd.isError()){
         this.activateButtonPanel("ERROR");
      }
      else{
         //Keep this dialog around for now, may need it...
         this.activateButtonPanel("UPDATE");
      }

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
                  //if(this._errors == null){
                  //   b.setEnabled(true)
                  //}
                  //else if(!this._errors.isShowing()){
                  //   b.setEnabled(true);
                  //}
               }
            }
            else if(b.getText().toUpperCase().equals("FUEL SYSTEM")){
               //if(this._fuelSystem == null){
               //   b.setEnabled(true);
               //}
               //else if(!this._fuelSystem.isShowing()){
               //   b.setEnabled(true);
               //}
            }
            else if(b.getText().toUpperCase().equals("ENGINES")){
               //if(this._engines == null){
               //   b.setEnabled(true);
               //}
               //else if(!this._engines.isShowing()){
               //   b.setEnabled(true);
               //}
            }
         }
         catch(ClassCastException cce){
            cce.printStackTrace();
         }
      }
   }

   //
   //
   //
   private void deactivateButtonPanel(){
      JPanel bp = (JPanel)this.getComponent(1);
      for(int i = 0; i < bp.getComponentCount(); ++i){
         try{
            JButton b = (JButton)bp.getComponent(i);
            b.setEnabled(false);
         }
         catch(ClassCastException cce){
            cce.printStackTrace();
         }
      }
   }

   //
   //
   //
   private JPanel setUpButtonPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      JButton fuelSystem = new JButton("Fuel System");
      JButton engines    = new JButton("Engines");
      JButton error      = new JButton("Error");
      fuelSystem.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //Test Print
            System.out.println(e);
            //displayFuelSystemJFrame();
            //updateFuelSystemJFrame();
            //activateButtonPanel("Fuel System Pressed");
         }
      });
      engines.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //Test Print
            System.out.println(e);
            //displayEnginesJFrame();
            //updateEnginesJFrame)(;
            //activateButtonPanel("Engines Pressed");
         }
      });
      error.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //Test Print
            System.out.println(e);
         }
      });
      panel.add(fuelSystem);
      panel.add(engines);
      panel.add(error);
      return panel;
   }

   //
   //
   //
   private JPanel setUpDataPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.add(this.setUpPanel(2)); //Current Stage
      panel.add(this.setUpPanel(2)); //Current Model
      panel.add(this.setUpPanel(2)); //Number of Engines
      panel.add(this.setUpPanel(2)); //Current Weight
      panel.add(this.setUpPanel(2)); //Error Indicator
      return panel;
   }

   //
   //
   //
   private void setUpGUI(){
      this.setLayout(new BorderLayout());
      this.add(this.setUpDataPanel(),  BorderLayout.CENTER);
      this.add(this.setUpButtonPanel(),BorderLayout.SOUTH);
      this.deactivateButtonPanel();
   }

   //
   //
   //
   private JPanel setUpPanel(int columns){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(1, columns));
      return panel;
   }

   //
   //
   //
   private void updateCurrentModel(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel cp    = (JPanel)panel.getComponent(1);
      JLabel label = null;
      if(cp.getComponentCount() == 0){
         label = new JLabel("Model:  ",SwingConstants.RIGHT);
         cp.add(label);
         String model = String.format("0x%X",sd.model());
         label = new JLabel(model);
         cp.add(label);
      }
      else{}
   }

   //
   //
   //
   private void updateCurrentStagePanel(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel sp    = (JPanel)panel.getComponent(0);
      JLabel label = null;
      if(sp.getComponentCount() == 0){
         label = new JLabel("Current Stage: ",SwingConstants.RIGHT);
         sp.add(label);
         label = new JLabel("" + sd.stageNumber());
         sp.add(label);
      }
      else{
         //Will need to figure out what to de here...
      }
   }

   //
   //
   //
   private void updateError(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel ep    = (JPanel)panel.getComponent(4);
      JLabel label = null;
      if(ep.getComponentCount() == 0){
         label = new JLabel("Error: ",SwingConstants.RIGHT);
         ep.add(label);
         if(sd.isError()){ label.setForeground(Color.RED); }
         else{ label.setForeground(Color.BLUE); }
         label = new JLabel("" + sd.isError());
         if(sd.isError()){ label.setForeground(Color.RED); }
         else{ label.setForeground(Color.BLUE); }
         ep.add(label);
      }
      else{}
   }

   //
   //
   //
   private void updateDataPanel(StageData sd){
      this.updateCurrentStagePanel(sd);
      this.updateCurrentModel(sd);
      this.updateEngineNumber(sd);
      this.updateWeight(sd);
      this.updateError(sd);
   }

   //
   //
   //
   private void updateEngineNumber(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel ep    = (JPanel)panel.getComponent(2);
      JLabel label = null;
      if(ep.getComponentCount() == 0){
         label=new JLabel("Number of Engines: ",SwingConstants.RIGHT);
         ep.add(label);
         label = new JLabel("" + sd.numberOfEngines());
         ep.add(label);
      }
      else{}
   }
   
   //
   //
   //
   private void updateWeight(StageData sd){
      JPanel panel        = (JPanel)this.getComponent(0);
      JPanel wp           = (JPanel)panel.getComponent(3);
      JLabel label        = null;
      NumberFormat format = null;
      format = NumberFormat.getNumberInstance(Locale.US);
      if(wp.getComponentCount() == 0){
         String wght = new String("Measured Weight: ");
         label = new JLabel(wght,SwingConstants.RIGHT);
         wp.add(label);
         String n = format.format(sd.weight())+"N";
         label = new JLabel(n);
         wp.add(label);
      }
   }
}
//////////////////////////////////////////////////////////////////////
