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
import java.text.*;
import myclasses.*;
import rosas.lou.clock.*;

public class LaunchSimulatorRocketPanel extends JPanel{
   //Going to "do" anonymous inner classes

   ////////////////////////////Constructors///////////////////////////
   //
   //
   //
   public LaunchSimulatorRocketPanel(){
      super();
      this.setUpGUI();
   }

   ///////////////////////////Public Methods//////////////////////////
   //
   //
   //
   public void initialize(RocketData rd){
      this.activateButtonPanel("INITIALIZE");
      this.updateCenterPanel(rd);
   }

   //////////////////////////Private Methods//////////////////////////
   //
   //
   //
   private void activateButtonPanel(String action){
      this.deactivateButtonPanel();
      if(action.toUpperCase().equals("INITIALIZE")){
         JPanel bp = (JPanel)this.getComponent(1);
         for(int i = 0; i < bp.getComponentCount(); ++i){
            try{
               JButton b = (JButton)bp.getComponent(i);
               if(b.getText().toUpperCase().equals("STAGES")){
                  //Enable the Stages JButton
                  b.setEnabled(true);
               }
            }
            catch(ClassCastException cce){}
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
            //If this is triggered "we" are in trouble!!
            cce.printStackTrace();
         }
      }
   }

   //
   //
   //
   private JPanel setUpButtonPanel(){
      JPanel panel = new JPanel();
      //Add more Buttons as needed
      JButton stages = new JButton("Stages");
      stages.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            //Test Prints for the time being
            System.out.println(e);
         }
      });
      panel.add(stages);
      return panel;
   }

   //
   //
   //
   private JPanel setUpDataPanel(){
      JPanel panel = new JPanel();
      //panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(0,2));
      return panel;
   }

   //
   //
   //
   private void setUpGUI(){
      this.setBorder(BorderFactory.createEtchedBorder());
      //The Layout may need to change...to a BorderLayout...
      this.setLayout(new BorderLayout());
      this.add(this.setUpDataPanel(),  BorderLayout.CENTER);
      this.add(this.setUpButtonPanel(),BorderLayout.SOUTH);
      this.deactivateButtonPanel();
   }

   //
   //
   //
   private void updateCenterPanel(RocketData rd){
      try{
         NumberFormat format = NumberFormat.getInstance(Locale.US);
         JPanel rdp = (JPanel)this.getComponent(0);
         String n = new String();
         JLabel l=new JLabel("Rocket Model: ",SwingConstants.RIGHT);
         rdp.add(l);
         l = new JLabel(rd.model());
         rdp.add(l);

         l = new JLabel("Current Weight: ",SwingConstants.RIGHT);
         rdp.add(l);
         n = format.format(rd.calculatedWeight())+"N";
         l = new JLabel(n);
         rdp.add(l);

         l = new JLabel("Current Stage: ",SwingConstants.RIGHT);
         rdp.add(l);
         n = "" + rd.currentStage();
         l = new JLabel(n);
         rdp.add(l);

         l = new JLabel("Total Stages: ",SwingConstants.RIGHT);
         rdp.add(l);
         n = "" + rd.numberOfStages();
         l = new JLabel(n);
         rdp.add(l);
         
         l = new JLabel("Error: ",SwingConstants.RIGHT);
         if(rd.isError()){ l.setForeground(Color.RED); }
         else{ l.setForeground(Color.BLUE); }
         rdp.add(l);
         n = "" + rd.isError();
         l = new JLabel(n);
         if(rd.isError()){ l.setForeground(Color.RED); }
         else{ l.setForeground(Color.BLUE); }
         rdp.add(l);
         
         rdp.repaint();
         rdp.revalidate();
      }
      catch(ClassCastException cce){cce.printStackTrace(); }
      catch(Exception e){ e.printStackTrace(); }
   }
}
//////////////////////////////////////////////////////////////////////
