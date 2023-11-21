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

public class CoffeeMakerViewV1_3 extends GenericJFrame
implements Subscriber{
   private static final int HEIGHT = 700;
   private static final int WIDTH  = 700;

   private String _state;
   private String _power;

   private CoffeeMakerControllerV1_3  _controller;
   private ButtonGroup                _powerGroup;

   {
      _controller = null;
      _powerGroup = null;
      _state      = null;
      _power      = null;
   };

   ///////////////////////Constructors////////////////////////////////
   //
   //
   //
   public CoffeeMakerViewV1_3(CoffeeMakerControllerV1_3 controller){
      this("",controller);
   }

   //
   //
   //
   public CoffeeMakerViewV1_3
   (
      String                    title,
      CoffeeMakerControllerV1_3 controller
   ){
      super(title);
      this._controller = controller;
      this.setUpGUI();
   }


   ////////////////////////Public Methods/////////////////////////////
   //
   //
   //
   public String power(){
      return this._power;
   }

   //
   //
   //
   public String state(){
      return this._state;
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
   private void handleEmptyReservoirException
   (
      EmptyReservoirException ere 
   ){
      String message = new String("Please Fill the Reservoir\n");
      message += "By Pressing the \"Fill Reservoir\"\n";
      message += "Button in the Button Panel";
      JOptionPane.showMessageDialog(this,
                                    message,
                                    "EMPTY RESERVOIR!!!",
                                    JOptionPane.WARNING_MESSAGE);
   }

   //
   //
   //
   private void handleCarafeUpdates(Double value, String message){
      String msg = message.toUpperCase();
      this.reflectCarafeData(value, msg);
   }

   //
   //
   //
   private void handleCarafeUpdates(String update, String message){
      String upd = update.toUpperCase();
      String msg = message.toUpperCase();
      this.reflectCarafeStateInCarafePanel(update,message);
      this.reflectCarafeStateInButtonPanel(update,message);
   }

   //
   //
   //
   private void handleMakerUpdates(String update, String message){
      //Figure out which one it is...
      this.power(update,message);
      this.state(update,message);
      this.reflectMakerStateInTopPanel(update, message);
   }

   //
   //
   //
   private void handleOverflowException(OverflowException oe){
      String message   = null;
      String error     = null;
      String exception = oe.getMessage().toUpperCase();
      if(exception.contains("CARAFE")){
         error   = new String("CARAFE OVERFLOWING!!");
         message = new String("Carafe is OVERFLOWING!!\n");
      }
      else if(exception.contains("RESERVOIR")){
         error   = new String("RESERVOIR OVERFLOWING!!");
         message = new String("Reservoir is OVERFLOWING!!\n");
         message += "Possible mess needing\n";
         message += "clean up...";
      }
      JOptionPane.showMessageDialog(this,
                                    message,
                                    error,
                                    JOptionPane.WARNING_MESSAGE);
   }

   //
   //
   //
   private void handleReservoirUpdates(Double value, String message){
      String msg = message.toUpperCase();
      this.reflectReservoirData(value, msg);
   }

   //
   //
   //
   private void reflectCarafeData(Double value, String message){
      String msg = message.toUpperCase();
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      JPanel leftPanel = (JPanel)panel.getComponent(0);
      JPanel centerPanel = (JPanel)leftPanel.getComponent(1);
      JPanel statePanel = (JPanel)centerPanel.getComponent(0);
      JPanel amountPanel = (JPanel)statePanel.getComponent(0);
      JLabel amountLabel = (JLabel)amountPanel.getComponent(0);
      JLabel capacityLabel=(JLabel)amountPanel.getComponent(1);
      JPanel carPanel = (JPanel)centerPanel.getComponent(1);
      JProgressBar bar = (JProgressBar)carPanel.getComponent(1);
      String amt = amountLabel.getText().substring(0,7);
      String cap = capacityLabel.getText().substring(0,9);
      try{
         double val = Double.NaN;
         if(!Double.isNaN(value)){
            val = value.doubleValue();
         }
         //The power is on
         if(this.power().equals("ON")){
            //May need to enable these here...
            //amountLabel.setEnabled(true);
            //capacityLabel.setEnabled(true);
            if(!Double.isNaN(val)){
               if(msg.contains("CAPACITY")){
                  capacityLabel.setText(cap+" "+val);
               }
               else if(msg.contains("QUANTITY")){
                  amountLabel.setText(amt+" "+val);
               }
            }
         }
         //The power is off
         else if(this.power().equals("OFF")){
            //amountLabel.setEnabled(false);
            //capacityLabel.setEnabled(false);
            amountLabel.setText(amt);
            capacityLabel.setText(cap);
         }
         if(!Double.isNaN(val)){
            if(msg.contains("CAPACITY")){
               bar.setMaximum((int)val);
            }
            else if(msg.contains("QUANTITY")){
               bar.setValue((int)val);
            }
         }
         this.getContentPane().validate();
         this.getContentPane().repaint();
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //
   //
   //
   private void reflectCarafeStateInButtonPanel
   (
      String update,
      String message
   ){
      String upd        = update.toUpperCase();
      String msg        = message.toUpperCase();
      this.disableSouthButton("BREW");
      this.disableSouthButton("GET CARAFE");
      this.disableSouthButton("RETURN CARAFE");
      this.disableSouthButton("FILL RESERVOIR");
      //If the Coffee Maker is Off, we do one thing...
      if(this.power().equals("OFF")){
         if(upd.contains("HOME") || msg.contains("HOME")){
            this.enableSouthButton("GET CARAFE");
            this.enableSouthButton("FILL RESERVOIR");
         }
         else if(upd.contains("PULLED")||msg.contains("PULLED")){
            this.enableSouthButton("RETURN CARAFE");
            this.enableSouthButton("FILL RESERVOIR");
         }
         else if(upd.contains("POURING")||msg.contains("POURING")){}
      }
      else if(this.state().equals("READY")){
         if(upd.contains("HOME")||msg.contains("HOME")){
            this.enableSouthButton("BREW");
            this.enableSouthButton("GET CARAFE");
            this.enableSouthButton("FILL RESERVOIR");
         }
         else if(upd.contains("PULLED")||msg.contains("PULLED")){
            this.enableSouthButton("RETURN CARAFE");
            this.enableSouthButton("FILL RESERVOIR");
         }
         else if(upd.contains("POURING")||msg.contains("POURING")){}
      }
      else if(this.state().equals("BREWING")){
         if(upd.contains("HOME")||msg.contains("HOME")){
            this.enableSouthButton("GET CARAFE");
            this.enableSouthButton("FILL RESERVOIR");
         }
         else if(upd.contains("PULLED")||msg.contains("PULLED")){
            this.enableSouthButton("RETURN CARAFE");
         }
         else if(upd.contains("POURING")||msg.contains("POURING")){}
      }
   }

   //
   //
   //
   private void reflectCarafeStateInCarafePanel
   (
      String update,
      String message
   ){
      String upd = update.toUpperCase();
      String msg = message.toUpperCase();
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      JPanel leftPanel = (JPanel)panel.getComponent(0);
      JPanel centerPanel=(JPanel)leftPanel.getComponent(1);
      JPanel statePanel = (JPanel)centerPanel.getComponent(0);
      JPanel resPanel = (JPanel)centerPanel.getComponent(1);
      JProgressBar bar = (JProgressBar)resPanel.getComponent(1);
      JPanel amountPanel = (JPanel)statePanel.getComponent(0);
      JLabel amountLabel = (JLabel)amountPanel.getComponent(0);
      JLabel capacityLabel=(JLabel)amountPanel.getComponent(1);
      JPanel carafePanel=(JPanel)statePanel.getComponent(1);
      JLabel in      = (JLabel)carafePanel.getComponent(0);
      JLabel out     = (JLabel)carafePanel.getComponent(1);
      JLabel pouring = (JLabel)carafePanel.getComponent(2);
      JPanel buttonPanel=(JPanel)statePanel.getComponent(2);
      JButton pour = (JButton)buttonPanel.getComponent(0);
      JButton stop = (JButton)buttonPanel.getComponent(1);
      amountLabel.setEnabled(false);
      capacityLabel.setEnabled(false);
      in.setEnabled(false);
      out.setEnabled(false);
      pouring.setEnabled(false);
      pour.setEnabled(false);
      stop.setEnabled(false);
      String amnt = amountLabel.getText().substring(0,7);
      String cap  = capacityLabel.getText().substring(0,9);
      try{
         if(this.power().equals("ON")){
            amountLabel.setEnabled(true);
            capacityLabel.setEnabled(true);
            if(upd.equals("HOME")||msg.equals("HOME")){
               in.setEnabled(true);
            }
            else if(upd.equals("PULLED")||msg.equals("PULLED")){
               out.setEnabled(true);
               pour.setEnabled(true);
            }
            else if(upd.equals("POURING")||msg.equals("POURING")){
               pouring.setEnabled(true);
               stop.setEnabled(true);
            }
	 }
         else if(this.power().equals("OFF")){
            amountLabel.setText(amnt);
            capacityLabel.setText(cap);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   //
   //
   //
   private void reflectMakerStateInTopPanel
   (
      String update, 
      String message
   ){
      int powerOnIndex      = 0;
      int powerOffIndex     = 1;
      int readyLabelIndex   = 2;
      int brewingLabelIndex = 3;
      JPanel top = (JPanel)this.getContentPane().getComponent(0);
      if(this.power().equals("OFF")){
         top.getComponent(powerOnIndex).setEnabled(true);
         top.getComponent(powerOffIndex).setEnabled(true);
         top.getComponent(readyLabelIndex).setEnabled(false);
         top.getComponent(brewingLabelIndex).setEnabled(false);
      }
      else if(this.power().equals("ON")){
         if(this.state().equals("READY")){
            top.getComponent(powerOnIndex).setEnabled(true);
            top.getComponent(powerOffIndex).setEnabled(true);
            top.getComponent(readyLabelIndex).setEnabled(true);
            top.getComponent(brewingLabelIndex).setEnabled(false);
         }
         else if(this.state().equals("BREWING")){
            top.getComponent(powerOnIndex).setEnabled(false);
            top.getComponent(powerOffIndex).setEnabled(false);
            top.getComponent(readyLabelIndex).setEnabled(false);
            top.getComponent(brewingLabelIndex).setEnabled(true);
         }
      }
   }

   //
   //
   //
   private void reflectReservoirData(Double value, String message){
      String msg = message.toUpperCase();
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      JPanel rightPanel = (JPanel)panel.getComponent(1);
      JPanel centerPanel = (JPanel)rightPanel.getComponent(1);
      JPanel statePanel = (JPanel)centerPanel.getComponent(0);
      JPanel resPanel = (JPanel)centerPanel.getComponent(1);
      JProgressBar bar = (JProgressBar)resPanel.getComponent(1);
      JPanel amountPanel = (JPanel)statePanel.getComponent(0);
      JLabel amountLabel = (JLabel)amountPanel.getComponent(0);
      JLabel capacityLabel=(JLabel)amountPanel.getComponent(1);
      String amt = amountLabel.getText().substring(0,7);
      String cap = capacityLabel.getText().substring(0,9);
      try{
         double val = Double.NaN;
         if(!Double.isNaN(value)){
            val = value.doubleValue();
         }
         //The Power is on
         if(this.power().equals("ON")){
            amountLabel.setEnabled(true);
            capacityLabel.setEnabled(true);
            if(!Double.isNaN(val)){
               if(msg.contains("CAPACITY")){
                  capacityLabel.setText(cap+" "+val);
               }
               else if(msg.contains("QUANTITY")){
                  amountLabel.setText(amt+" "+val);
               }
            }
         }
         //The Power is off
         else if(this.power().equals("OFF")){
            amountLabel.setEnabled(false);
            capacityLabel.setEnabled(false);
            amountLabel.setText(amt);
            capacityLabel.setText(cap);
         }
         if(!Double.isNaN(val)){
            if(msg.contains("CAPACITY")){
               bar.setMaximum((int)val);
            }
            else if(msg.contains("QUANTITY")){
               bar.setValue((int)val);
            }
         }
         this.getContentPane().validate();
         this.getContentPane().repaint();
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }


   //
   //
   //
   private void power(String update, String message){
      String upd = update.toUpperCase();
      String msg = message.toUpperCase();
      if(upd.contains("POWER") || msg.contains("POWER")){
         if(upd.contains("ON") || msg.contains("ON")){
            this._power = new String("ON");
         }
         else if(upd.contains("OFF") || msg.contains("OFF")){
            this._power = new String("OFF");
         }
      }
   }

   //
   //
   //
   private void state(String update, String message){
      String upd = update.toUpperCase();
      String msg = update.toUpperCase();
      if(upd.contains("STATE") || msg.contains("STATE")){
         if(upd.contains("READY") || msg.contains("READY")){
            this._state = new String("READY");
         }
         else if(upd.contains("BREWING") || msg.contains("BREWING")){
            this._state = new String("BREWING");
         }
      }
   }

   //
   //
   //
   private void setState(String update, String message){}

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
   public void update(Object o){}

   //
   //
   //
   public void update(Object o, String s){
      try{
         String update = ((String)o).toUpperCase();
         String message = s.toUpperCase();
         if(update.contains("MAKER") || message.contains("MAKER")){
            this.handleMakerUpdates(update,message);
         }
         else if(update.contains("CARAFE") ||
                 message.contains("CARAFE")){
            this.handleCarafeUpdates(update,message);
         }
      }
      catch(ClassCastException cce){}
      try{
         Double value   = (Double)o;
         String message = s.toUpperCase();
         if(message.contains("CARAFE")){
            this.handleCarafeUpdates(value,message);
         }
         else if(message.contains("RESERVOIR")){
            this.handleReservoirUpdates(value,message);
         }
      }
      catch(ClassCastException cce){}
   }

   //
   //
   //
   public void error(RuntimeException re){
      try{
          EmptyReservoirException ere = (EmptyReservoirException)re;
          this.handleEmptyReservoirException(ere);
      }
      catch(ClassCastException cce){}
      try{
         OverflowException oe = (OverflowException)re;
         String message = oe.getMessage().toUpperCase();
         this.handleOverflowException(oe);
      }
      catch(ClassCastException cce){}
      try{
         NotHomeException nhe = (NotHomeException)re;
      }
      catch(ClassCastException cce){}
   }

   //
   //
   //
   public void error(RuntimeException re, Object o){}

   //
   //
   //
   public void error(String error){}
}
//////////////////////////////////////////////////////////////////////
