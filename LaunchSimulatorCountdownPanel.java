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
   public void activateContinueSubState(){
      this.deactivateTextFields();
      this.deactivateButtons();
      this.activateHoldButton();
   }

   /**/
   public void activateHoldSubState(){
      this.deactivateTextFields();
      this.deactivateButtons();
      this.activateAbortButton();
      this.activateResumeButton();
   }

   /*
    *Fancy word for the look of the Panel afer System Startup
    * */
   public void activateNoStateStartUp(){
      this.clearTextField();
      this.clearTimeField();
      this.deactivateTextFields();
      this.deactivateButtons();
   }

   /**/
   public void activatePrelaunchTime(){
      this.clearTextField();
      this.activateTextFields();
      this.deactivateButtons();
      this.activateSetButton();
   }

   /**/
   public void activateSetSubState(){
      this.deactivateTextFields();
      this.deactivateButtons();
      this.activateStartButton();
   }

   /**/
   public void activateStart(){}

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
      Integer hours    = null;
      int count        = 0;
      boolean notFound = true;
      JTextField tf    = null;
      JPanel panel     = (JPanel)this.getComponent(1);
      while(count < panel.getComponentCount() && notFound){
         try{
            tf = (JTextField)panel.getComponent(count);
            if(tf.getName().toUpperCase().contains("SETHOURS")){
               notFound = false;
            }
         }
         catch(ClassCastException cce){}
         finally{
            ++count;
         }
      }
      try{
         hours = Integer.valueOf(tf.getText());
      }
      catch(NumberFormatException nfe){
         String error = "Please Enter a Valid\n";
         error += "number for Hours";
         JOptionPane.showMessageDialog(this,
                  error,
                  "Number Needed!",
                  JOptionPane.ERROR_MESSAGE);
         tf.requestFocus();
         tf.selectAll();
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      finally{
         return hours;
      }
   }
   
   /**/
   public Integer getMins(){
      Integer mins     = null;
      int count        = 0;
      boolean notFound = true;
      JTextField tf    = null;
      JPanel panel     = (JPanel)this.getComponent(1);
      while(count < panel.getComponentCount() && notFound){
         try{
            tf = (JTextField)panel.getComponent(count);
            if(tf.getName().toUpperCase().contains("SETMINS")){
               notFound = false;
            }
         }
         catch(ClassCastException cce){}
         finally{
            ++count;
         }
      }
      try{
         mins = Integer.valueOf(tf.getText());
      }
      catch(NumberFormatException npe){
         String error = "Please Enter a Valid\n";
         error += "number for Minutes";
         JOptionPane.showMessageDialog(this,
                  error,
                  "Number Needed!",
                  JOptionPane.ERROR_MESSAGE);
         tf.requestFocus();
         tf.selectAll();
      }
      finally{
         return mins;
      }
   }

   /**/
   public Integer getSecs(){
      Integer secs     = null;
      int count        = 0;
      boolean notFound = true;
      JTextField tf    = null;
      JPanel panel     = (JPanel)this.getComponent(1);
      while(count < panel.getComponentCount() && notFound){
         try{
            tf = (JTextField)panel.getComponent(count);
            if(tf.getName().toUpperCase().contains("SETSECS")){
               notFound = false;
            }
         }
         catch(ClassCastException cce){}
         finally{
            ++count;
         }
      }
      try{
         secs = Integer.valueOf(tf.getText());
      }
      catch(NumberFormatException nfe){
         String error = "Please Enter a Valid\n";
         error += "number for Seconds";
         JOptionPane.showMessageDialog(this,
                  error,
                  "Number Needed!",
                  JOptionPane.ERROR_MESSAGE);
         tf.requestFocus();
         tf.selectAll();
      }
      finally{
         return secs;
      }
   }

   /**/
   public void setCurrentCountdownTime(String time){
      JPanel panel = (JPanel)this.getComponent(2);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            //Only TWO components in the Panel!!
            JTextField tf = (JTextField)panel.getComponent(i);
            tf.setText(time);
         }
         catch(ClassCastException cce){}
      }
   }
   
   /**/
   public void wrongHourEntry(){
      int count        = 0;
      boolean notFound = true;
      JPanel panel = (JPanel)this.getComponent(1);
      while(count < panel.getComponentCount() && notFound){
         try{
            JTextField tf = (JTextField)panel.getComponent(count);
            if(tf.getName().toUpperCase().contains("SETHOURS")){
               tf.requestFocus();
               tf.selectAll();
            }
         }
         catch(ClassCastException cce){}
         finally{
            ++count;
         }
      }
   }

   /**/
   public void wrongMinEntry(){}

   /**/
   public void wrongSecEntry(){}
   ////////////////////////Private Methods////////////////////////////
   /**/
   private void activateAbortButton(){
      JPanel panel = (JPanel)this.getComponent(3);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         JButton b = (JButton)panel.getComponent(i);
         if(b.getActionCommand().toUpperCase().contains("ABORT")){
            b.setEnabled(true);
         }
      }
   }

   /**/
   private void activateCountdownTimeTextField(){
      JPanel panel = (JPanel)this.getComponent(2);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JTextField tf = (JTextField)panel.getComponent(i);
            tf.setEditable(true);
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   private void activateHoldButton(){
      JPanel panel = (JPanel)this.getComponent(3);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         JButton b = (JButton)panel.getComponent(i);
         if(b.getActionCommand().toUpperCase().contains("HOLD")){
            b.setEnabled(true);
         }
      }
   }

   /**/
   private void activateResumeButton(){
      JPanel panel = (JPanel)this.getComponent(3);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         JButton b = (JButton)panel.getComponent(i);
         if(b.getActionCommand().toUpperCase().contains("RESUME")){
            b.setEnabled(true);
         }
      }
   }

   /**/
   private void activateSetButton(){
      JPanel panel = (JPanel)this.getComponent(3);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         JButton b = (JButton)panel.getComponent(i);
         if(b.getActionCommand().toUpperCase().contains("SET")){
            b.setEnabled(true);
         }
      }
   }

   /**/
   private void activateStartButton(){
      JPanel panel = (JPanel)this.getComponent(3);
      for(int i=0; i < panel.getComponentCount(); ++i){
         JButton b = (JButton)panel.getComponent(i);
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
            if(i == 1){
               //Focus on the hours text field...
               tf.requestFocus();
            }
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   private void clearTextField(){
      JPanel panel = (JPanel)this.getComponent(1);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JTextField tf = (JTextField)panel.getComponent(i);
            tf.setText("");
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   private void clearTimeField(){
      JPanel panel = (JPanel)this.getComponent(2);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JTextField tf = (JTextField)panel.getComponent(i);
            tf.setText("");
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   private void deactivateButtons(){
      JPanel panel = (JPanel)this.getComponent(3);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         JButton b = (JButton)panel.getComponent(i);
         b.setEnabled(false);
      }
   }

   /**/
   private void deactivateTextFields(){
      JPanel panel = (JPanel)this.getComponent(1);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JTextField tf = (JTextField)panel.getComponent(i);
            tf.setEditable(false);
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   private JPanel setUpButtonPanel(){
      JPanel panel  = new JPanel();

      JButton set = new JButton("Set");
      set.setActionCommand("COUNTDOWNSET");
      set.setEnabled(false);
      panel.add(set);

      JButton start = new JButton("Start");
      start.setActionCommand("COUNTDOWNSTART");
      start.setEnabled(false);
      panel.add(start);

      JButton resume = new JButton("Resume");
      resume.setActionCommand("COUNTDOWNRESUME");
      resume.setEnabled(false);
      panel.add(resume);
      
      JButton hold = new JButton("Hold");
      hold.setActionCommand("COUNTDOWNHOLD");
      hold.setEnabled(false);
      panel.add(hold);

      JButton abort = new JButton("Abort");
      abort.setActionCommand("COUNTDOWNABORT");
      abort.setEnabled(false);
      panel.add(abort);

      if(this._controller != null){
         set.addActionListener(this._controller);
         set.addKeyListener(this._controller);
         start.addActionListener(this._controller);
         start.addKeyListener(this._controller);
         resume.addActionListener(this._controller);
         resume.addKeyListener(this._controller);
         hold.addActionListener(this._controller);
         hold.addKeyListener(this._controller);
         abort.addActionListener(this._controller);
         abort.addKeyListener(this._controller);
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
