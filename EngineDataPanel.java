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

public class EngineDataPanel extends JPanel{
   //Use Anonymous Inner classes
   
   private int _stage;
   private int _index; //Engine Number

   {
      _stage    = 0;
      _index    = 0;
   };

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public EngineDataPanel(int stage, int index){
      super();
      if(stage > 0 && index > 0){
         this._stage = stage;
         this._index = index;
         this.setUpGUI();
      }
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void setUpEngineData(StageData sd){
      this.setUpStageNumber(sd);
      this.setUpEngineNumber(sd);
      this.setUpModel(sd);
      this.setUpIsIgnited(sd);
      this.setUpTemperature(sd);
      this.setUpFuelFlowRate(sd);
      this.setUpExhaustFlowRate(sd);
      this.setUpError(sd);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void deactivateButtonPanel(){}

   //
   //
   //
   private JPanel setUpButtonPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      JButton error = new JButton("Error");
      error.addActionListener(new ActionListener(){
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
      panel.add(this.setUpPanel(2)); //Stage Number
      panel.add(this.setUpPanel(2)); //Engine Number (Index)
      panel.add(this.setUpPanel(2)); //Model
      panel.add(this.setUpPanel(2)); //Is Ingnited
      panel.add(this.setUpPanel(2)); //Temperature
      panel.add(this.setUpPanel(2)); //Fuel Flow Rate
      panel.add(this.setUpPanel(2)); //Exhaust Flow Rate
      panel.add(this.setUpPanel(2)); //Error
      return panel;
   }

   //
   //
   //
   private void setUpEngineNumber(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel enp   = (JPanel)panel.getComponent(1);
      java.util.List<EngineData> l = sd.engineData();
      Iterator<EngineData> it = l.iterator();
      while(it.hasNext()){
         EngineData ed = it.next();
         boolean isStage = ed.stage() == this._stage;
         boolean isIndex = ed.index() == this._index;
         if(isStage && isIndex){
            //Initialization
            if(enp.getComponentCount() == 0){
               JLabel engine = null;
               engine = new JLabel("Engine: ",SwingConstants.RIGHT);
               enp.add(engine);
               JLabel number = new JLabel(""+ed.index());
               enp.add(number);
            }
            //Update
            else{}
         }
      }
   }

   //
   //
   //
   private void setUpError(StageData sd){}

   //
   //
   //
   private void setUpExhaustFlowRate(StageData sd){}

   //
   //
   //
   private void setUpFuelFlowRate(StageData sd){}

   //
   //
   //
   private void setUpGUI(){
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setLayout(new BorderLayout());
      this.add(this.setUpCenterPanel(), BorderLayout.CENTER);
      this.add(this.setUpButtonPanel(),  BorderLayout.SOUTH);
      this.deactivateButtonPanel();
   }

   //
   //
   //
   private void setUpIsIgnited(StageData sd){
      int RIGHT = SwingConstants.RIGHT;
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel iip   = (JPanel)panel.getComponent(3);
      java.util.List<EngineData> l = sd.engineData();
      Iterator<EngineData> it = l.iterator();
      while(it.hasNext()){
         EngineData ed = it.next();
         if((ed.stage()==this._stage)&&(ed.index()==this._index)){
            //Initialization
            if(iip.getComponentCount() == 0){
               JLabel ignited = new JLabel("Ignition: ",RIGHT);
               iip.add(ignited);
               JLabel value = new JLabel(""+ed.isIgnited());
               iip.add(value);
            }
            //Update
            else{}
         }
      }
   }

   //
   //
   //
   private void setUpModel(StageData sd){
      JPanel panel = (JPanel)this.getComponent(0);
      JPanel mdp   = (JPanel)panel.getComponent(2);
      java.util.List<EngineData> l = sd.engineData();
      Iterator<EngineData> it = l.iterator();
      while(it.hasNext()){
         EngineData ed = it.next();
         boolean isStage = ed.stage() == this._stage;
         boolean isIndex = ed.index() == this._index;
         if(isStage && isIndex){
            //Initialization
            if(mdp.getComponentCount() == 0){
               JLabel model = null;
               model = new JLabel("Model: ",SwingConstants.RIGHT);
               mdp.add(model);
               String n = String.format("0x%X", ed.model());
               JLabel number = new JLabel(n);
               mdp.add(number);
            }
            //Update
            else{}
         }
      }
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
      java.util.List<EngineData> l = sd.engineData();
      Iterator<EngineData> it      = l.iterator();
      while(it.hasNext()){
         EngineData ed = it.next();
         boolean isStage = ed.stage() == this._stage;
         boolean isIndex = ed.index() == this._index;
         if(isStage && isIndex){
            //Initialization
            if(sdp.getComponentCount() == 0){
               JLabel stage = null;
               stage = new JLabel("Stage: ",SwingConstants.RIGHT);
               sdp.add(stage);
               JLabel number = new JLabel(""+ed.stage());
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
   private void setUpTemperature(StageData sd){}
}
//////////////////////////////////////////////////////////////////////
