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
//FOR THE TIME BEING, A LIVING FILE!
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import rosas.lou.runnables.*;
import rosas.lou.clock.*;

public class LaunchSimulatorController implements ActionListener,
KeyListener,ItemListener,WindowListener{
   private JFrame                  _frame;
   private LaunchSimulator         _simulator;
   private LaunchSystem            _system;
   private Subscriber              _subscriber;
   private ClockSubscriber         _clockSubscriber;
   private CountdownTimerInterface _countdownTimer;
   
   {
      _frame           = null;
      _simulator       = null;
      _system          = null;
      _subscriber      = null;
      _clockSubscriber = null;
      _countdownTimer  = null;
   };

   /////////////////////////Contstructors/////////////////////////////
   /**/
   public LaunchSimulatorController(){}
   ////////////////////////////Public Methods/////////////////////////
   /**/
   public void addCountdownTimer(CountdownTimerInterface ct){
      this._countdownTimer = ct;
   }

   /**/
   public void addClockSubscriber(ClockSubscriber cs){
      this._clockSubscriber = cs;
   }
   /**/
   public void addLaunchSimulator(LaunchSimulator ls){
      this._simulator = ls;
   }

   /**/
   public void addLaunchSystem(LaunchSystem ls){
      this._system = ls;
   }
   
   /**/
   public void addSubscriber(Subscriber s){
      this._subscriber = s;
   }
   ///////////////////////Interface Implementations///////////////////
   /////////////////////////////Action Listener///////////////////////
   /**/
   public void actionPerformed(ActionEvent e){
      this.handleJButton(e);
      this.handleJMenuItem(e);
      this.handleJTextField(e);
      this.handleJCheckBox(e);
   }
   ///////////////////////////////Key Listener////////////////////////
   /**/
   public void keyPressed(KeyEvent ke){
      this.handleJButton(ke);
      this.handleJCheckBox(ke);
   }

   /**/
   public void keyReleased(KeyEvent ke){}
   
   /**/
   public void keyTyped(KeyEvent ke){
      this.handleJTextField(ke);
   }
   /////////////////////////////Item Listener/////////////////////////
   /**/
   public void itemStateChanged(ItemEvent ie){
      this.handleJCheckBox(ie);
   }
   ///////////////////////////Window Listener/////////////////////////
   /**/
   public void windowActivated(WindowEvent we){}
   /**/
   public void windowClosed(WindowEvent we){}
   /**/
   public void windowClosing(WindowEvent we){}
   /**/
   public void windowDeactivated(WindowEvent we){}
   /**/
   public void windowDeiconified(WindowEvent we){}
   /**/
   public void windowIconified(WindowEvent we){}
   /**/
   public void windowOpened(WindowEvent we){}

   ////////////////////////////Private Methods////////////////////////
   /**/
   private void activateAbort(){
      //Kill the system completely
      this._system.abort();
   }
   /**/
   private void activatePrelaunchCountdown(){
      //this._simulator.startCountdown();
      this._system.startCountdown();
   }

   /**/
   private void activatePrelaunchCountdownAbort(){
      //this._simulator.abortCountdown();
      this._system.abortCountdown();
   }

   /**/
   private void activatePrelaunchCountdownHold(){
      //this._simulator.holdCountdown();
      this._system.holdCountdown();
   }

   /**/
   private void activatePrelaunchTime(){
      try{
         this._subscriber.update(null, "Set: Prelaunch");
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   /**/
   private void activatePrelaunchResumeCountdown(){
      //this._simulator.resumeCountdown();
      this._system.resumeCountdown();
   }

   /**/
   private void handleJButton(ActionEvent e){
      try{
         JButton b = (JButton)e.getSource();
         String command = b.getActionCommand().toUpperCase();
         if(command.equals("PRELAUNCH")){
            this.activatePrelaunchTime();
         }
         else if(command.equals("COUNTDOWNSET")){
            this.requestPrelaunchTime();
         }
         else if(command.equals("COUNTDOWNRESUME")){
            this.activatePrelaunchResumeCountdown();
         }
         else if(command.equals("COUNTDOWNSTART")){
            this.activatePrelaunchCountdown();
         }
         else if(command.equals("COUNTDOWNHOLD")){
            this.activatePrelaunchCountdownHold();
         }
         else if(command.equals("COUNTDOWNABORT")){
            this.activatePrelaunchCountdownAbort();
         }
         else if(command.equals("ABORT")){
            this.activateAbort();
         }
         else{
            System.out.println(command);
         }
      }
      catch(ClassCastException cce){}
   }

   /**/
   private void handleJButton(KeyEvent ke){
      try{
         if(ke.getKeyCode() == KeyEvent.VK_ENTER){
            JButton b = (JButton)ke.getSource();
            b.doClick(250);
         }
      }
      catch(ClassCastException cce){}
   }

   /**/
   private void handleJCheckBox(ActionEvent ae){}

   /**/
   private void handleJCheckBox(ItemEvent ie){
      try{
         JCheckBox jcb = (JCheckBox)ie.getSource();
         if(jcb.isSelected()){
            //Will need to set the Simulation State
            System.out.println(jcb); //Do a test print
            //System.out.println(ie); //Do a test print
         }
         else{
            //Clear the Simulation State...
            System.out.println("poop");
         }
      }
      catch(ClassCastException cce){}
   }

   /*Not really needed, but keeping, regardless*/
   private void handleJCheckBox(KeyEvent ke){
      try{
         JCheckBox jcb = (JCheckBox)ke.getSource();
         if(ke.getKeyCode() == KeyEvent.VK_ENTER){
            jcb.doClick();
         }
      }
      catch(ClassCastException cce){}
   }

   /**/
   private void handleJMenuItem(ActionEvent e){
      try{
         JMenuItem m = (JMenuItem)e.getSource();
         String command = m.getActionCommand().toUpperCase();
         if(command.equals("OPENINIFILE")){
            this.openIniFile();
         }
         else if(command.equals("OPENJSONFILE")){
            this.openJSONFile();
         }
      }
      catch(ClassCastException cce){}
   }

   /**/
   private void handleJTextField(ActionEvent e){
      JTextField jtf = null;
      try{
         jtf         = (JTextField)e.getSource();
         String name = jtf.getName().toUpperCase();
         if(name.equals("SETHOURS") ||
            name.equals("SETMINS")  ||
            name.equals("SETSECS")){
            //Do this part, for the time being...
            Integer.parseInt(jtf.getText());
         }
         this._subscriber.update(jtf,name);
      }
      catch(ClassCastException cce){}
      catch(NumberFormatException nfe){
         JOptionPane.showMessageDialog(jtf,
               "Please Enter a Valid Number",
               "Number Needed!!",
               JOptionPane.ERROR_MESSAGE);
      }
      catch(NullPointerException npe){ npe.printStackTrace(); }
   }

   /**/
   private void handleJTextField(KeyEvent ke){
      try{
         JTextField jtf = (JTextField)ke.getComponent();
         char c = ke.getKeyChar();
         if(!Character.isDigit(c)){
            ke.consume();
         }
      }
      catch(ClassCastException cce){}
      catch(NullPointerException npe){ npe.printStackTrace(); }
   }

   /**/
   private void openIniFile(){
      String i = "*.ini";
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter fnef = null;
      fnef = new FileNameExtensionFilter(i, "ini");
      chooser.setFileFilter(fnef);
      int value = chooser.showOpenDialog(this._frame);
      if(value == 0){
         String file = chooser.getSelectedFile().getPath();
         if(this._simulator != null){
            this._simulator.initialize(file);
         }
         if(this._system != null){
            this._system.initialize(file);
         }
      }
   }

   /**/
   private void openJSONFile(){
      String j = "*.json";
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter fnef = null;
      fnef = new FileNameExtensionFilter(j, "json");
      chooser.setFileFilter(fnef);
      int value = chooser.showOpenDialog(this._frame);
      if(value == 0){
         String file = chooser.getSelectedFile().getPath();
         if(this._simulator != null){
            this._simulator.initialize(file);
         }
         if(this._system != null){
            this._system.initialize(file);
         }
      }
   }

   /**/
   private void requestPrelaunchTime(){
      int secs = -1; int mins = -1; int hours = -1;
      try{
         java.util.List<Integer> l = null;
         l = this._countdownTimer.requestTimes();

         //Go ahead and get Seconds first...since it is saved first
         secs  = l.get(0).intValue();
         mins  = l.get(1).intValue();
         hours = l.get(2).intValue();

         //this._simulator.preLaunchTime(hours,mins,secs);
         this._system.preLaunchTime(hours,mins,secs);
      }
      catch(NullPointerException npe){
         //If the GUI is NULL, indicate that, otherwise, ignore
         //setting the state...
         if(this._countdownTimer == null){
            JOptionPane.showMessageDialog(null,
                  "NO GUI to set Prelaunch!",
                  "GUI-less!",
                  JOptionPane.ERROR_MESSAGE);
         }
         else{
            //This should never fucking happen!            
            if(hours > -1 && mins > -1 && secs > -1){
               npe.printStackTrace();
            }
         }
      }
   }
}
//////////////////////////////////////////////////////////////////////
