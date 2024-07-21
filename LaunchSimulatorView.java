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
implements Subscriber, ClockSubscriber{
   private LaunchSimulatorController _controller;

   {
      _controller       = null;
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

   //////////////////////////Public Methods//////////////////////////
   ////////////////////////Interface Methods//////////////////////////
   /////////////////////ClockSubscriber Methods///////////////////////
   /**/
   public void error(String type, String message){}
   /**/
   public void update(String time){}
   /**/
   public void update(java.time.Instant instant){}
   /**/
   public void update(boolean isRunning){}
   /**/
   public void update(String time, String type){}
   /**/
   public void update(java.util.List<?> list){}
   ////////////////////////Subscriber Methods/////////////////////////
   /**/
   public void update(Object o){}
   /**/
   public void update(Object o, String s){
      if(o != null){}
      else{
         this.getMessage(s);
      }
   }
   
   /**/
   public void error(RuntimeException re){}
   /**/
   public void error(RuntimeException re, Object o){}
   /**/
   public void error(String error){}

   /////////////////////////Private Methods///////////////////////////
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
   }

   /**/
   private void setPrelaunchTime(){
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      JPanel nwPanel=(JPanel)panel.getComponent(0);
      LaunchSimulatorCountdownPanel p =
               (LaunchSimulatorCountdownPanel)nwPanel.getComponent(0);
      System.out.println(p.getComponentCount());
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
