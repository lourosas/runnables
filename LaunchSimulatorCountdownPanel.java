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

public class LaunchSimulatorCountdownPanel extends JPanel{
   //private JPanel _panel                         = null;
   private LaunchSimulatorController _controller = null;

   ///////////////////////////Constructors////////////////////////////
   /**/
   public LaunchSimulatorCountdownPanel(LaunchSimulatorController c){
      super();
      this._controller = c;
      this.setUpGUI();
   }

   /////////////////////////Public Methods////////////////////////////
   /**/
   public void activatePrelaunchTime(){
      this.activateTextFields();
      this.activateStartButton();
   }

   /**/
   public void requestNextFocus(JTextField jtf, String name){
      JPanel panel = (JPanel)this.getComponent(1);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JTextField tf = (JTextField)panel.getComponent(i);
            if(jtf == tf){
               int j = i+2;
               if(j < panel.getComponentCount()){
                  tf = (JTextField)panel.getComponent(j);
                  tf.requestFocus();
               }
               else{
                  JPanel btnPanel = (JPanel)this.getComponent(3);
                  //Set the focus on Start...
                  JButton b = (JButton)btnPanel.getComponent(0);
                  b.requestFocus();
               }
            }
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   public Integer getHours(){
      Integer hours  = null;
      JTextField htf = null;
      JPanel panel  = (JPanel)this.getComponent(1);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JTextField tf = (JTextField)panel.getComponent(i);
            if(tf.getName().toUpperCase().contains("SETHOURS")){
               try{
                  hours = Integer.valueOf(tf.getText());
               }
               catch(NumberFormatException nfe){
                  nfe.printStackTrace();
               }
            }
         }
         catch(ClassCastException cce){}
      }
      return hours;
   }

   /*
   public JPanel panel(){
      return this._panel;
   }
   */

   ////////////////////////Private Methods////////////////////////////
   /**/
   private void activateStartButton(){
      JPanel panel = (JPanel)this.getComponent(3);
      for(int i=0; i < panel.getComponentCount(); ++i){
         JButton b = (JButton)panel.getComponent(i);
         b.setEnabled(false);
         if(b.getActionCommand().toUpperCase().contains("START")){
            b.setEnabled(true);
         }
      }
   }

   /**/
   private void activateTextFields(){
      JPanel panel = (JPanel)this.getComponent(1);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JTextField tf = (JTextField)panel.getComponent(i);
            tf.setEditable(true);
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   private JPanel setUpButtonPanel(){
      JPanel panel  = new JPanel();

      JButton start = new JButton("Start");
      start.setActionCommand("COUNTDOWNSTART");
      start.setEnabled(false);
      panel.add(start);
      
      JButton hold = new JButton("Hold");
      hold.setActionCommand("COUNTDOWNHOLD");
      hold.setEnabled(false);
      panel.add(hold);

      if(this._controller != null){
         start.addActionListener(this._controller);
         start.addKeyListener(this._controller);
         hold.addActionListener(this._controller);
         hold.addKeyListener(this._controller);
      }

      return panel;
   }

   /**/
   private JPanel setUpCountdownTime(){
      int RIGHT    = SwingConstants.RIGHT;
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,2));

      JLabel countdownTime = new JLabel("Countdown Time: ",RIGHT);
      JTextField jtf       = new JTextField();
      jtf.setEditable(false);

      panel.add(countdownTime);
      panel.add(jtf);

      return panel;
   }

   /**/
   private void setUpGUI(){
      int CENTER = SwingConstants.CENTER;
      JLabel countdownLabel = new JLabel("Countdown Time",CENTER);
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setLayout(new GridLayout(0,1));
      this.add(countdownLabel);
      this.add(this.setUpTimeEntryPanel());
      this.add(this.setUpCountdownTime());
      this.add(this.setUpButtonPanel());
   }

   /**/
   private JPanel setUpTimeEntryPanel(){
      JPanel panel = new JPanel();
      
      JLabel hrs_Label     = new JLabel("Hours:  ");
      JLabel minsLabel     = new JLabel("Mins:  ");
      JLabel secsLabel     = new JLabel("Secs:  ");
      JTextField hrs_TF    = new JTextField(3);
      JTextField minsTF    = new JTextField(2);
      JTextField secsTF    = new JTextField(2);
      hrs_TF.setName("SETHOURS");
      minsTF.setName("SETMINS");
      secsTF.setName("SETSECS");
      //Set this upon Initialization--set editable when Countdown
      //time is entered...
      hrs_TF.setEditable(false);
      minsTF.setEditable(false);
      secsTF.setEditable(false);

      if(this._controller != null){
         hrs_TF.addActionListener(this._controller);
         minsTF.addActionListener(this._controller);
         secsTF.addActionListener(this._controller);
         hrs_TF.addKeyListener(this._controller);
         minsTF.addKeyListener(this._controller);
         secsTF.addKeyListener(this._controller);
      }
      
      panel.add(hrs_Label); panel.add(hrs_TF);
      panel.add(minsLabel); panel.add(minsTF);
      panel.add(secsLabel); panel.add(secsTF);

      return panel;
   }
}


//////////////////////////////////////////////////////////////////////
