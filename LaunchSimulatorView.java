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
import javax.swing.text.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import javax.swing.border.*;
import java.time.*;
import myclasses.*;
import rosas.lou.clock.*;

public class LaunchSimulatorView extends GenericJFrame
implements Subscriber, ClockSubscriber, CountdownTimerInterface{
   private LaunchSimulatorController _controller;
   private LaunchSimulatorStateSubstate _lsss;

   {
      _controller = null;
      _lsss       = null;      
   };

   //////////////////////////Constructors/////////////////////////////
   /**/
   public LaunchSimulatorView(){
      this("",null);
   }

   /**/
   public LaunchSimulatorView
   (
      String title,
      LaunchSimulatorController controller
   ){
      super(title);
      this._controller = controller;
      this.setUpGUI();
   }

   //////////////////////////Public Methods///////////////////////////
   ////////////////////////Interface Methods//////////////////////////
   /////////////////////ClockSubscriber Methods///////////////////////
   /**/
   public void error(String type, String message){}
   /**/
   public void update(String time){
      try{
         LaunchSimulatorStateSubstate.State s = this._lsss.state();
         LaunchSimulatorStateSubstate.PreLaunchSubstate pl =
                                       this._lsss.prelaunchSubstate();
         if(s==LaunchSimulatorStateSubstate.State.PRELAUNCH){
            LaunchSimulatorCountdownPanel p= this.getCountdownPanel();
            p.setCurrentCountdownTime(time);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }
   /**/
   public void update(State state){}
   /**/
   public void update(java.time.Instant instant){}
   /**/
   public void update(Duration duration){}
   /**/
   public void update(Duration duration, boolean isRunning){}
   /**/
   public void update(Duration duration, State state){}
   /**/
   public void update(Duration duration, State state, String type){}
   /**/
   public void update(boolean isRunning){}
   /**/
   public void update(String time, String type){}
   /**/
   public void update(java.util.List<?> list){
      for(int i = 0; i < list.size(); ++i){
         String s = list.get(i).toUpperCase();
         if(s.contains(":")){
            //This way we know it is a time value
            this.update(s);
         }
         else if(s.contains("RESET")){}
         else if(s.contains("START")){}
         else if(s.contains("STOP")){}
      }
   }
   /**/
   public void updateElapsed(Duration duration){}
   /**/
   public void updateLap(Duration duration){}
   /**/
   public void updateLaps(java.util.List<?> list){}
   /**/
   public void updateRun(){}
   /**/
   public void updateStop(){}
   /**/
   public void updateReset(){}
   //////////////////CountdownTimerInterface Methods//////////////////
   /**/
   public java.util.List<Integer> requestTimes(){
      java.util.List<Integer> list = new LinkedList<Integer>();
      list.add(this.requestHours());
      list.add(this.requestMins());
      list.add(this.requestSecs());
      return list;
   }

   /**/
   public Integer requestHours(){
      Integer hours = null;
      LaunchSimulatorCountdownPanel panel = this.getCountdownPanel();
      hours = panel.getHours();
      return hours;
   }

   /**/
   public Integer requestMins(){
      Integer mins = 0;
      LaunchSimulatorCountdownPanel panel = this.getCountdownPanel();
      mins = panel.getMins();
      return mins;
   }

   /**/
   public Integer requestSecs(){
      Integer secs = 0;
      LaunchSimulatorCountdownPanel panel = this.getCountdownPanel();
      secs = panel.getSecs();
      return secs;
   }
   ////////////////////////Subscriber Methods/////////////////////////
   /**/
   public void update(Object o){}
   /**/
   public void update(Object o, String s){
      if(o != null){
         try{
            JTextField jtf = (JTextField)o;
            this.handleJTextFieldEntry(jtf,s);
         }
         catch(ClassCastException cce){}
         try{
            LaunchSimulatorStateSubstate lss =
                                      (LaunchSimulatorStateSubstate)o;
            this.handleStateSubstate(lss, s);
         }
         catch(ClassCastException cce){}
      }
      else{
         this.getMessage(s);
      }
   }

   /**/
   public void error(RuntimeException re){}
   /**/
   public void error(RuntimeException re, Object o){}
   /**/
   public void error(String error){
      if(error.toUpperCase().contains("TIME ENTRY")){
         this.handleTimeEntryErrors(error);
      }
   }

   /////////////////////////Private Methods///////////////////////////
   /**/
   private void displayState(LaunchSimulatorStateSubstate.State s){
      JPanel panel=(JPanel)this.getContentPane().getComponent(0);
      JLabel label=(JLabel)panel.getComponent(0);
      label.setText("" + s);
   }

   /**/
   private LaunchSimulatorCountdownPanel getCountdownPanel(){
      JPanel panel=(JPanel)this.getContentPane().getComponent(1);
      JPanel nwPanel=(JPanel)panel.getComponent(0);
      LaunchSimulatorCountdownPanel p =
              (LaunchSimulatorCountdownPanel)nwPanel.getComponent(0);
      return p;
   }

   /*
    *It is going to be some sort of message, so go ahead and get it
    first
    * */
   private void getMessage(String m){
      String message = m.toUpperCase();
      if(message.contains("SET")){
         if(message.contains("PRELAUNCH")){
            this.setPrelaunchTime();
         }
      }
      //Don't fucking need this!!! Fucking Remove!!!!!
      else if(message.contains("READY")){
         if(message.contains("PRELAUNCH")){
            this.setForCountdownStart();
         }
      }
   }

   /**/
   private void handleJTextFieldEntry(JTextField jtf, String s){
      String test = s.toUpperCase();
      if(test.equals("SETHOURS") ||
         test.equals("SETMINS")  ||
         test.equals("SETSECS")){
         LaunchSimulatorCountdownPanel p = this.getCountdownPanel();
         p.requestNextFocus(jtf, s);
      }
   }

   /**/
   private void handleIgnitionSubstate(){
   }

   /**/
   private void handleLaunchSubstate(){
   }

   /**/
   private void handlePrelaunchSubstate(){
      LaunchSimulatorCountdownPanel p = this.getCountdownPanel();
      if(this._lsss.prelaunchSubstate() != null){
         LaunchSimulatorStateSubstate.PreLaunchSubstate pl =
                                       this._lsss.prelaunchSubstate();
         if(pl == 
             LaunchSimulatorStateSubstate.PreLaunchSubstate.CONTINUE){
            p.activateContinueState();    
         }
      }
   }

   /**/
   private void handlePrelaunchSubstate
   (
      LaunchSimulatorStateSubstate.PreLaunchSubstate sub
   ){
      LaunchSimulatorCountdownPanel p = this.getCountdownPanel();
      if(sub ==
             LaunchSimulatorStateSubstate.PreLaunchSubstate.CONTINUE){
         p.activateContinueState();
      }
   }

   /**/
   private void handleStateSubstate
   (
      LaunchSimulatorStateSubstate lsss,
      String                       message
   ){
      this._lsss = lsss;
      this.displayState(this._lsss.state());
      this.handlePrelaunchSubstate();
      this.handleIgnitionSubstate();
      this.handleLaunchSubstate();
   }

   /**/
   private void handleTimeEntryErrors(String error){
      if(error.toUpperCase().contains("TIME ENTRY")){
         JOptionPane.showMessageDialog(this,
               error,
               "Time Entry Error",
               JOptionPane.ERROR_MESSAGE);
         LaunchSimulatorCountdownPanel p = this.getCountdownPanel();
         p.wrongHourEntry();
      }
   }

   /**/
   private void setForCountdownStart(){
      LaunchSimulatorCountdownPanel p = this.getCountdownPanel();
      p.activateReadyForStart();
   }

   /**/
   private void setPrelaunchTime(){
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      JPanel nwPanel=(JPanel)panel.getComponent(0);
      JPanel btnPanel=(JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < btnPanel.getComponentCount(); ++i){
         JButton b = (JButton)btnPanel.getComponent(i);
         b.setEnabled(false);
         if(b.getActionCommand().toUpperCase().contains("ABORT")){
            b.setEnabled(true);
         }
      }
      LaunchSimulatorCountdownPanel p = this.getCountdownPanel();
      p.activatePrelaunchTime();
   }

   /**/
   private JPanel setUpCenterPanel(){
      JPanel panel        = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(2,2,5,5));
      panel.add(this.setUpNorthWestPanel());
      panel.add(new JPanel());
      panel.add(new JPanel());
      panel.add(new JPanel());
      return panel;
   }

   /**/
   private JPanel setUpCountdownPanel(){
      LaunchSimulatorController contr = this._controller;
      return new LaunchSimulatorCountdownPanel(contr);
   }

   /**/
   private void setUpGUI(){
      int WIDTH  = 700;
      int HEIGHT = 700;

      this.setLayout(new BorderLayout());
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);

      JPanel northPanel  = this.setUpNorthPanel();
      JPanel centerPanel = this.setUpCenterPanel();
      JPanel southPanel  = this.setUpSouthPanel();
      this.getContentPane().add(northPanel,   BorderLayout.NORTH);
      this.getContentPane().add(centerPanel,  BorderLayout.CENTER);
      this.getContentPane().add(southPanel,   BorderLayout.SOUTH);

      this.setVisible(true);
   }

   /**/
   private JPanel setUpNorthWestPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,1));
      panel.add(this.setUpCountdownPanel());
      //Remove as Development progresses
      panel.add(new JPanel());
      return panel;
   }

   /**/
   private JPanel setUpNorthPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());

      JLabel stateLabel = new JLabel("(Current State)");
      panel.add(stateLabel);

      return panel;
   }

   /**/
   private JPanel setUpSouthPanel(){
      JPanel panel     = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      
      JButton preLaunch = new JButton("Pre-Launch");
      preLaunch.setActionCommand("PRELAUNCH");
      preLaunch.setMnemonic(KeyEvent.VK_P);
      preLaunch.addActionListener(this._controller);
      preLaunch.addKeyListener(this._controller);
      panel.add(preLaunch);

      JButton ignition = new JButton("Ignition");
      ignition.setActionCommand("IGNITION");
      ignition.setMnemonic(KeyEvent.VK_I);
      ignition.addActionListener(this._controller);
      ignition.addKeyListener(this._controller);
      ignition.setEnabled(false);
      panel.add(ignition);

      JButton launch = new JButton("Launch");
      launch.setActionCommand("LAUNCH");
      launch.setMnemonic(KeyEvent.VK_L);
      launch.addActionListener(this._controller);
      launch.addKeyListener(this._controller);
      launch.setEnabled(false);
      panel.add(launch);

      JButton abort = new JButton("Abort");
      abort.setActionCommand("ABORT");
      abort.setMnemonic(KeyEvent.VK_A);
      abort.addActionListener(this._controller);
      abort.addKeyListener(this._controller);
      abort.setEnabled(false);
      panel.add(abort);

      return panel;
   }
}
//////////////////////////////////////////////////////////////////////
