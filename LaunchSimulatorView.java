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
   public void update(Object o, String s){}
   /**/
   public void error(RuntimeException re){}
   /**/
   public void error(RuntimeException re, Object o){}
   /**/
   public void error(String error){}

   /////////////////////////Private Methods///////////////////////////
   /**/
   private JPanel setUpCenterPanel(){
      JPanel panel        = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      return panel;
   }

   /**/
   private void setUpGUI(){
      int WIDTH  = 700;
      int HEIGHT = 700;

      this.setLayout(new BorderLayout());
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);
      JPanel centerPanel = this.setUpCenterPanel();
      JPanel southPanel  = this.setUpSouthPanel();
      this.getContentPane().add(centerPanel,  BorderLayout.CENTER);
      this.getContentPane().add(southPanel,   BorderLayout.SOUTH);

      this.setVisible(true);
   }

   /**/
   private JPanel setUpSouthPanel(){
      JPanel panel     = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      
      JButton preLaunch = new JButton("Pre Launch");
      preLaunch.setActionCommand("PRELAUNCH");
      preLaunch.setMnemonic(KeyEvent.VK_P);
      preLaunch.addActionListener(this._controller);
      preLaunch.addKeyListener(this._controller);
      panel.add(preLaunch);
      return panel;
   }
}
//////////////////////////////////////////////////////////////////////
