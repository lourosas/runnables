//////////////////////////////////////////////////////////////////////
/*
Copyright 2023 Lou Rosas

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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import rosas.lou.runnables.*;
import myclasses.*;
import rosas.lou.lgraphics.*;

public class CoffeeMakerViewV1_2 extends GenericJFrame
implements Subscriber{
   private static final int HEIGHT = 700;
   private static final int WIDTH  = 700;

   private CoffeeMakerControllerV1_2  _controller;
   private ButtonGroup                _powerGroup;

   {
      _controller = null;
      _powerGroup = null;
   };

   ///////////////////////Constructors////////////////////////////////
   //
   //
   //
   public CoffeeMakerViewV1_2(CoffeeMakerControllerV1_2 controller){
      this("",controller);
   }

   //
   //
   //
   public CoffeeMakerViewV1_2
   (
      String                    title,
      CoffeeMakerControllerV1_2 controller
   ){
      super(title);
      this._controller = controller;
      this.setUpGUI();
   }

   ///////////////////////Private Methods/////////////////////////////
   //
   //
   //
   private void disableSouthButton(String buttonName){
      String name  = buttonName.trim().toUpperCase();
      JPanel panel = (JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         JButton b    = (JButton)panel.getComponent(i);
         String bname = b.getText().trim().toUpperCase();
         if(bname.equals(name)){
            b.setEnabled(false);
	 }
      }
   }

   //
   //
   //
   private void enableSouthButton(String buttonName){
      String name  = buttonName.trim().toUpperCase();
      JPanel panel = (JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         JButton b    = (JButton)panel.getComponent(i);
         String bname = b.getText().trim().toUpperCase();
         if(bname.equals(name)){
            b.setEnabled(true);
         }
      }
   }

   //
   //
   //
   private void reflectStateInButtonPanel(TotalState ts){
      this.disableSouthButton("BREW");
      this.disableSouthButton("GET CARAFE");
      this.disableSouthButton("RETURN CARAFE");
      this.disableSouthButton("FILL RESERVOIR");
      try{
         System.out.println(ts);
         MakerState ms = ts.makerState();
         if(ms.power().toUpperCase().equals("OFF")){
            try{
               ContainerState cs = ts.carafeState();
               if(cs.state().toUpperCase().equals("HOME")){
                  this.enableSouthButton("GET CARAFE");
                  this.enableSouthButton("FILL RESERVOIR");
               }
               else if(cs.state().toUpperCase().equals("PULLED")){
                  this.enableSouthButton("RETURN CARAFE");
                  this.enableSouthButton("FILL RESERVOIR");
               }
               else if(cs.state().toUpperCase().equals("POURING")){}
            }
            catch(NullPointerException npe){
               npe.printStackTrace();
            }
         }
         else{
            try{
               ContainerState cs = ts.carafeState();
               if(ms.state().toUpperCase().equals("READY")){
                  String csString = cs.state().toUpperCase();
                  if(csString.equals("HOME")){
                     this.enableSouthButton("BREW");
                     this.enableSouthButton("GET CARAFE");
                     this.enableSouthButton("FILL RESERVOIR");
                  }
                  else if(csString.equals("PULLED")){
                     this.enableSouthButton("RETURN CARAFE");
                     this.enableSouthButton("FILL RESERVOIR");
                  }
                  else if(csString.equals("POURING")){
                  }
               }
               else if(ms.state().toUpperCase().equals("BREWING")){
                  String csString = cs.state().toUpperCase();
                  if(csString.equals("HOME")){
                     this.enableSouthButton("GET CARAFE");
                     this.enableSouthButton("FILL RESERVOIR");
                  }
                  else if(csString.equals("PULLED")){
                     this.enableSouthButton("RETURN CARAFE");
                  }
                  else if(csString.equals("POURING")){}
               }
            }
            catch(NullPointerException npe){
               npe.printStackTrace();
            }
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void reflectStateInCarafePanel(TotalState ts){
      MakerState ms = ts.makerState();
      try{
         System.out.println(ts);
         JPanel panel = (JPanel)this.getContentPane().getComponent(1);
         JPanel leftPanel = (JPanel)panel.getComponent(0);
         JPanel centerPanel = (JPanel)leftPanel.getComponent(1);
         JPanel statePanel = (JPanel)centerPanel.getComponent(0);
         JPanel resPanel = (JPanel)centerPanel.getComponent(1);
         JProgressBar bar= (JProgressBar)resPanel.getComponent(1);
         JPanel amountPanel= (JPanel)statePanel.getComponent(0);
         JLabel amountLabel= (JLabel)amountPanel.getComponent(0);
         JLabel capacityLabel=(JLabel)amountPanel.getComponent(1);
         String amnt = amountLabel.getText().substring(0,7);
         String cap  = capacityLabel.getText().substring(0,9);
         ContainerState cs = ts.carafeState();
         double capacity   = cs.capacity();
         double quantity   = cs.quantity();
         try{
            String power = ms.power().toUpperCase();
            if(power.equals("ON")){}
            else if(power.equals("OFF")){
               amountLabel.setEnabled(false);
               capacityLabel.setEnabled(false);
               amountLabel.setText(amnt);
               capacityLabel.setText(cap);
	    }
            if(capacity > 0.){
               bar.setMaximum((int)capacity);
            }
            bar.setValue((int)quantity);
            this.getContentPane().validate();
            this.getContentPane().repaint();
         }
         catch(NullPointerException npe){
            npe.printStackTrace();
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //Set the entire Reservoir State here...
   //
   private void reflectStateInReservoirPanel(TotalState ts){
      MakerState ms = ts.makerState();
      try{
         System.out.println(ts);
         //Set up the Left Side
         JPanel panel = (JPanel)this.getContentPane().getComponent(1);
         JPanel rightPanel  = (JPanel)panel.getComponent(1);
         JPanel centerPanel = (JPanel)rightPanel.getComponent(1);
         JPanel statePanel  = (JPanel)centerPanel.getComponent(0);
         JPanel resPanel    = (JPanel)centerPanel.getComponent(1);
         JProgressBar bar   = (JProgressBar)resPanel.getComponent(1);
         JPanel amountPanel = (JPanel)statePanel.getComponent(0);
         JLabel amountLabel = (JLabel)amountPanel.getComponent(0);
         JLabel capacityLabel=(JLabel)amountPanel.getComponent(1);
         String amnt = amountLabel.getText().substring(0,7);
         String cap  = capacityLabel.getText().substring(0,9);
         ContainerState rs  = ts.reservoirState();
         double capacity    = rs.capacity();
         double quantity    = rs.quantity();
         try{
            String power = ms.power().toUpperCase();
            if(power.equals("ON")){}
            else if(power.equals("OFF")){
               capacityLabel.setEnabled(false);
               amountLabel.setEnabled(false);
               amountLabel.setText(amnt);
               capacityLabel.setText(cap);
            }
            if(capacity > 0.){
               bar.setMaximum((int)capacity);
            }
            bar.setValue((int)quantity);
            this.getContentPane().validate();
            this.getContentPane().repaint();
         }
         catch(NullPointerException npe){
            npe.printStackTrace();
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private void reflectStateInTopPanel(TotalState ts){
      int powerOnIndex        = 0;
      int powerOffIndex       = 1;
      int readyLabelIndex     = 2;
      int brewingLabelIndex   = 3;
      JPanel top = (JPanel)this.getContentPane().getComponent(0);
      try{
         //Test Prints
	 System.out.println(ts);
         MakerState ms = ts.makerState();
         if(ms.power().toUpperCase().equals("OFF")){
            top.getComponent(powerOnIndex).setEnabled(true);
            top.getComponent(powerOffIndex).setEnabled(true);
            top.getComponent(readyLabelIndex).setEnabled(false);
            top.getComponent(brewingLabelIndex).setEnabled(false);
	 }
         else{
            if(ms.state().toUpperCase().equals("READY")){
               top.getComponent(powerOnIndex).setEnabled(true);
               top.getComponent(powerOffIndex).setEnabled(true);
               top.getComponent(readyLabelIndex).setEnabled(true);
               top.getComponent(brewingLabelIndex).setEnabled(false);
            }
            else if(ms.state().toUpperCase().equals("BREWING")){
               top.getComponent(powerOnIndex).setEnabled(false);
               top.getComponent(powerOffIndex).setEnabled(false);
               top.getComponent(readyLabelIndex).setEnabled(false);
               top.getComponent(brewingLabelIndex).setEnabled(true);
            }
         }
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   private JPanel setUpCarafeCenterLeftPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(3,1));

      JPanel topPanel = new JPanel();
      topPanel.setBorder(BorderFactory.createEtchedBorder());
      topPanel.setLayout(new GridLayout(2,1));
      JLabel amount = new JLabel("Amount: ");
      amount.setEnabled(false);
      topPanel.add(amount);
      JLabel capacity = new JLabel("Capacity: ");
      capacity.setEnabled(false);
      topPanel.add(capacity);
      panel.add(topPanel);

      JPanel centerPanel = new JPanel();
      centerPanel.setBorder(BorderFactory.createEtchedBorder());
      centerPanel.setLayout(new GridLayout(3,1));
      JLabel in = new JLabel("In");
      in.setForeground(Color.blue);
      in.setEnabled(false);
      centerPanel.add(in);
      JLabel out = new JLabel("Out");
      out.setForeground(Color.magenta);
      out.setEnabled(false);
      centerPanel.add(out);
      JLabel pouring = new JLabel("Pouring");
      pouring.setForeground(Color.red);
      pouring.setEnabled(false);
      centerPanel.add(pouring);
      panel.add(centerPanel);

      JPanel southPanel = new JPanel();
      southPanel.setBorder(BorderFactory.createEtchedBorder());
      southPanel.setLayout(new GridLayout(3,1));
      JButton pour = new JButton("Pour");
      pour.setActionCommand("POUR");
      pour.addActionListener(this._controller);
      pour.addKeyListener(this._controller);
      pour.setEnabled(false);
      southPanel.add(pour);
      JButton empty = new JButton(" ");
      empty.setEnabled(false);
      southPanel.add(empty);
      JButton stop = new JButton("Stop Pouring");
      stop.setActionCommand("STOPPOURING");
      stop.addActionListener(this._controller);
      stop.addKeyListener(this._controller);
      stop.setEnabled(false);
      southPanel.add(stop);
      panel.add(southPanel);

      return panel;
   }

   //
   //
   //
   private JPanel setUpCarafeCenterRightPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.setBorder(BorderFactory.createEtchedBorder());

      JPanel northPanel = new JPanel();
      JLabel amountLabel = new JLabel(" ");
      northPanel.add(amountLabel);
      panel.add(northPanel, BorderLayout.NORTH);

      JProgressBar carafeAmount = null;
      //This will need to be updated based on the initialization
      //the Coffee Maker...
      carafeAmount = new JProgressBar(SwingConstants.VERTICAL, 0, 32);
      carafeAmount.setValue(carafeAmount.getMinimum());
      carafeAmount.setStringPainted(true);
      panel.add(carafeAmount, BorderLayout.CENTER);

      return panel; 
   }

   //
   //
   //
   private JPanel setUpCarafePanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.setBorder(BorderFactory.createEtchedBorder());
      //Set Up North Part
      JPanel northPanel = new JPanel();
      northPanel.setBorder(BorderFactory.createEtchedBorder());
      JLabel northLabel = new JLabel("Carafe",SwingConstants.CENTER);
      northPanel.add(northLabel);
      panel.add(northPanel, BorderLayout.NORTH);
      //Set up Center Part
      JPanel centerPanel = new JPanel();
      centerPanel.setLayout(new GridLayout(1,2));
      JPanel centerLeft  = this.setUpCarafeCenterLeftPanel();
      centerPanel.add(centerLeft);
      JPanel centerRight = this.setUpCarafeCenterRightPanel();
      centerPanel.add(centerRight);
      panel.add(centerPanel, BorderLayout.CENTER);
      return panel;
   }

   //
   //
   //
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(1,2,10,10));
      panel.add(this.setUpCarafePanel());
      panel.add(this.setUpReservoirPanel());
      return panel;
   }
   
   
   //
   //
   //
   private void setUpGUI(){
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);
      JPanel centerPanel = this.setUpCenterPanel();
      JPanel northPanel  = this.setUpNorthPanel();
      JPanel southPanel  = this.setUpSouthPanel();
      this.getContentPane().add(northPanel,  BorderLayout.NORTH);
      this.getContentPane().add(centerPanel, BorderLayout.CENTER);
      this.getContentPane().add(southPanel,  BorderLayout.SOUTH);
      this.setVisible(true);
   }

   //
   //
   //
   private JPanel setUpNorthPanel(){
      JPanel panel     = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      this._powerGroup = new ButtonGroup();

      JRadioButton power = new JRadioButton("Power");
      power.setActionCommand("POWER");
      this._powerGroup.add(power);
      power.addItemListener(this._controller);
      panel.add(power);

      JRadioButton off = new JRadioButton("Power Off", true);
      off.setActionCommand("OFF");
      this._powerGroup.add(off);
      off.addItemListener(this._controller);
      panel.add(off);

      JLabel ready = new JLabel("Ready");
      ready.setForeground(Color.blue);
      panel.add(ready);

      JLabel brewing = new JLabel("Brewing");
      brewing.setForeground(Color.red);
      brewing.setEnabled(false);
      panel.add(brewing);

      return panel;
   }

   //
   //
   //
   private JPanel setUpReservoirCenterLeftPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(3,1));

      JPanel topPanel = new JPanel();
      topPanel.setBorder(BorderFactory.createEtchedBorder());
      topPanel.setLayout(new GridLayout(2,1));
      JLabel amount = new JLabel("Amount: ");
      amount.setEnabled(false);
      topPanel.add(amount);
      JLabel capacity = new JLabel("Capacity: ");
      capacity.setEnabled(false);
      topPanel.add(capacity);
      panel.add(topPanel);

      panel.add(new JPanel());
      panel.add(new JPanel());
      
      return panel;
   }

   //
   //
   //
   private JPanel setUpReservoirCenterRightPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.setBorder(BorderFactory.createEtchedBorder());

      JPanel northPanel = new JPanel();
      JLabel northLabel = new JLabel(" ");
      northPanel.add(northLabel);
      panel.add(northPanel, BorderLayout.NORTH);

      JProgressBar bar = null;
      bar = new JProgressBar(SwingConstants.VERTICAL, 0, 32);
      bar.setValue(bar.getMinimum());
      bar.setStringPainted(true);
      panel.add(bar, BorderLayout.CENTER);

      return panel;
   }

   //
   //
   //
   private JPanel setUpReservoirPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.setBorder(BorderFactory.createEtchedBorder());

      //North Panel
      JPanel northPanel = new JPanel();
      northPanel.setBorder(BorderFactory.createEtchedBorder());
      JLabel northLabel=new JLabel("Reservoir",SwingConstants.CENTER);
      northPanel.add(northLabel);
      panel.add(northPanel, BorderLayout.NORTH);

      //Center Panel
      JPanel centerPanel = new JPanel();
      centerPanel.setLayout(new GridLayout(1,2));
      JPanel centerLeft = this.setUpReservoirCenterLeftPanel();
      centerPanel.add(centerLeft);
      JPanel centerRight = this.setUpReservoirCenterRightPanel();
      centerPanel.add(centerRight);
      panel.add(centerPanel, BorderLayout.CENTER);

      return panel;
   }

   //
   //
   //
   private JPanel setUpSouthPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());

      JButton brew = new JButton("Brew");
      brew.setActionCommand("BREW");
      brew.setMnemonic(KeyEvent.VK_B);
      //brew.setEnabled(false); //May not be needed
      brew.addActionListener(this._controller);
      brew.addKeyListener(this._controller);
      panel.add(brew);

      JButton carafe = new JButton("Get Carafe");
      carafe.setActionCommand("GET");
      carafe.setMnemonic(KeyEvent.VK_G);
      //carafe.setEnabled(false);
      carafe.addActionListener(this._controller);
      carafe.addKeyListener(this._controller);
      panel.add(carafe);

      JButton returnCarafe = new JButton("Return Carafe");
      returnCarafe.setActionCommand("RETURN");
      returnCarafe.setMnemonic(KeyEvent.VK_R);
      returnCarafe.setEnabled(false);
      returnCarafe.addActionListener(this._controller);
      returnCarafe.addKeyListener(this._controller);
      panel.add(returnCarafe);

      JButton fill = new JButton("Fill Reservoir");
      fill.setActionCommand("RESERVOIR FILL");
      fill.setMnemonic(KeyEvent.VK_F);
      //fill.setEnabled(false);
      fill.addActionListener(this._controller);
      fill.addKeyListener(this._controller);
      panel.add(fill);
      return panel;
   }

   ////////////////////Interface Methods//////////////////////////////
   //
   //
   //
   public void update(Object o){
      try{
         TotalState ts = (TotalState)o;
         this.reflectStateInTopPanel(ts);
         this.reflectStateInCarafePanel(ts);
         this.reflectStateInReservoirPanel(ts);
         this.reflectStateInButtonPanel(ts);
      }
      catch(ClassCastException cce){}
      catch(NullPointerException npe){}
   }

   //
   //
   //
   public void update(Object o, String s){
   }

   //
   //
   //
   public void error(RuntimeException re){
      System.out.println(re.getMessage());
   }

   //
   //
   //
   public void error(RuntimeException re, Object o){}

   //
   //
   //
   public void error(String error){
   }
}
//////////////////////////////////////////////////////////////////////
