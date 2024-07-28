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
   private LaunchSimulator         _simulator;
   private Subscriber              _subscriber;
   private ClockSubscriber         _clockSubscriber;
   private CountdownTimerInterface _countdownTimer;
   
   {
      _simulator       = null;
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
   public void addSubscriber(Subscriber s){
      this._subscriber = s;
   }
   ///////////////////////Interface Implementations///////////////////
   /////////////////////////////Action Listener///////////////////////
   /**/
   public void actionPerformed(ActionEvent e){
      this.handleJButton(e);
      this.handleJTextField(e);
   }
   ///////////////////////////////Key Listener////////////////////////
   /**/
   public void keyPressed(KeyEvent ke){
      this.handleJButton(ke);
   }

   /**/
   public void keyReleased(KeyEvent ke){}
   
   /**/
   public void keyTyped(KeyEvent ke){
      this.handleJTextField(ke);
   }
   /////////////////////////////Item Listener/////////////////////////
   /**/
   public void itemStateChanged(ItemEvent ie){}
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
   private void activatePrelaunchCountdown(){
      this._simulator.startCountdown();
   }

   /**/
   private void activatePrelaunchTime(){
      try{
         this._subscriber.update(null,"Set:  Prelaunch");
      }
      catch(NullPointerException npe){
         JOptionPane.showMessageDialog(null,
               "NO GUI to set Prelaunch!",
               "GUI-less!",
               JOptionPane.ERROR_MESSAGE);
      }
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
         else if(command.equals("COUNTDOWNSTART")){
            this.activatePrelaunchCountdown();
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
   private void requestPrelaunchTime(){
      try{
         java.util.List<Integer> l =
                                  this._countdownTimer.requestTimes();
         int hours = l.get(0).intValue();
         int mins  = l.get(1).intValue();
         int secs  = l.get(2).intValue();
         this._simulator.preLaunchTime(hours,mins,secs);
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
      }
   }
}
//////////////////////////////////////////////////////////////////////
