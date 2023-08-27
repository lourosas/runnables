//////////////////////////////////////////////////////////////////////
/*
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

public class CoffeeMakerViewTwo extends GenericJFrame
implements Subscriber{
   private static final int HEIGHT = 700;
   private static final int WIDTH  = 700;

   private String _powerString;
   private String _stateString;
   private String _carafeStateString;

   private ButtonGroup _powerGroup;
   private CoffeeMakerController _controller;

   {
      _powerString       = null;
      _stateString       = null;
      _carafeStateString = null;
      _controller        = null;
      _powerGroup        = null;   
   };

   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   public CoffeeMakerViewTwo(CoffeeMakerController controller){
      this("", controller);
   }

   //
   //
   //
   //
   public CoffeeMakerViewTwo
   (
      String title,
      CoffeeMakerController controller
   ){
      super(title);
      this._controller = controller;
      this.setUpGUI();
   }

   ////////////////////////Public Methods/////////////////////////////
   //////////////////////Protected Methods////////////////////////////
   ///////////////////////Private Methods/////////////////////////////
   //
   //
   //
   //
   private void disableSouthButton(String buttonName){
      String name = buttonName.trim().toUpperCase();
      JPanel panel = (JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         JButton b = (JButton)panel.getComponent(i);
         String bname = b.getText().trim().toUpperCase();
         if(bname.equals(name)){
            b.setEnabled(false);
         }
      }
   }

   //
   //
   //
   //
   private void enableSouthButton(String buttonName){
      String name = buttonName.trim().toUpperCase();
      JPanel panel = (JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         JButton b = (JButton)panel.getComponent(i);
         String bname = b.getText().trim().toUpperCase();
         if(bname.equals(name)){
            b.setEnabled(true);
         }
      }
   }

   //
   //
   //
   //
   private void handleCarafeUpdates(Object o, String s){
      if(s.contains("STATE")){
         this.setCarafeState(o);
      }
      else{
         try{
            Double amount = (Double)o;
            if(s.contains("CAPACITY")){
               this.setCarafeCapacity(amount);
            }
            else if(s.contains("QUANTITY")){
               this.setCarafeQuantity(amount);
            }
         }
         catch(ClassCastException cce){}
      }
   }

   //
   //
   //
   //
   private void handleCoffeeMakerPowerState(String powerState){
      if(powerState.contains("ON")){
         this._powerString = new String("ON");
      }
      else if(powerState.contains("OFF")){
         this._powerString = new String("OFF");
      }
   }

   //
   //
   //
   //
   private void handleCoffeeMakerState(String state){
      if(state.contains("READY")){
         this._stateString = new String("READY");
      }
      else if(state.contains("BREWING")){
         this._stateString = new String("BREWING");
      }
      this.reflectCoffeeMakerState();
   }

   //
   //
   //
   //
   private void handleCoffeeMakerUpdates(String update){
      if(update.contains("POWER")){
         this.handleCoffeeMakerPowerState(update);
      }
      else if(update.contains("STATE")){
         this.handleCoffeeMakerState(update);
      }
   }

   //
   //
   //
   //
   private void handleReservoirUpdates(Object o, String s){
      try{
         Double amount = (Double)o;
         if(s.contains("CAPACITY")){
            this.setReservoirCapacity(amount);
         }
         else if(s.contains("QUANTITY")){
            this.setReservoirQuantity(amount);
         }
      }
      catch(ClassCastException cce){}
   }

   //
   //
   //
   //
   private void reflectCoffeeMakerState(){
      int powerOnIndex      = 0;
      int powerOffIndex     = 1;
      int readyLabelIndex   = 2;
      int brewingLabelIndex = 3;
      JPanel top = (JPanel)this.getContentPane().getComponent(0);
      if(this._stateString.equals("READY")){
         top.getComponent(powerOnIndex).setEnabled(true);
         top.getComponent(powerOffIndex).setEnabled(true);
         top.getComponent(readyLabelIndex).setEnabled(true);
         //Turn Off Brewing
         top.getComponent(brewingLabelIndex).setEnabled(false);
      }
      //For current rendition, when the Coffee Maker is brewing,
      //turn off the ability to remove power--it would be easier in
      //that context...
      else if(this._stateString.equals("BREWING")){
         top.getComponent(powerOnIndex).setEnabled(false);
         top.getComponent(powerOffIndex).setEnabled(false);
         top.getComponent(readyLabelIndex).setEnabled(false);
         top.getComponent(brewingLabelIndex).setEnabled(true);
      }
   }

   //
   //
   //
   //
   private void setCarafeCapacity(Double amount){
      try{
         JPanel panel = (JPanel)this.getContentPane().getComponent(1);
         JPanel leftPanel = (JPanel)panel.getComponent(0);
         JPanel centerPanel = (JPanel)leftPanel.getComponent(1);
         JPanel statePanel = (JPanel)centerPanel.getComponent(0);
         JPanel capPanel = (JPanel)statePanel.getComponent(0);
         JLabel capacity = (JLabel)capPanel.getComponent(1);
         String cap = capacity.getText().substring(0,9);
         capacity.setText(cap + " " + amount.doubleValue());
         capacity.setEnabled(true);
         //Set the Right Side
         JPanel resPanel = (JPanel)centerPanel.getComponent(1);
         JProgressBar bar = (JProgressBar)resPanel.getComponent(1);
         bar.setMaximum(amount.intValue());
         this.getContentPane().validate();
         this.getContentPane().repaint();
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   //
   private void setCarafeQuantity(Double amount){
      try{
         JPanel panel = (JPanel)this.getContentPane().getComponent(1);
         JPanel leftPanel = (JPanel)panel.getComponent(0);
         JPanel centerPanel = (JPanel)leftPanel.getComponent(1);
         JPanel statePanel = (JPanel)centerPanel.getComponent(0);
         JPanel amountPanel = (JPanel)statePanel.getComponent(0);
         JLabel amountLabel = (JLabel)amountPanel.getComponent(0);
         String amnt = amountLabel.getText().substring(0,7);
         amountLabel.setText(amnt + " " + amount.doubleValue());
         amountLabel.setEnabled(true);
         //Set up the Right Side
         JPanel quantPanel = (JPanel)centerPanel.getComponent(1);
         JProgressBar bar = (JProgressBar)quantPanel.getComponent(1);
         bar.setValue(amount.intValue());
         this.getContentPane().validate();
         this.getContentPane().repaint();
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   //
   private void setCarafeState(Object o){
      try{
         String state = (String)o;
         this._carafeStateString = state.toUpperCase();
         //Now, set up the panel...
         JPanel panel = (JPanel)this.getContentPane().getComponent(1);
         JPanel leftPanel = (JPanel)panel.getComponent(0);
         JPanel centerPanel=(JPanel)leftPanel.getComponent(1);
         JPanel statePanel=(JPanel)centerPanel.getComponent(0);
         JPanel indPanel=(JPanel)statePanel.getComponent(1);
         JPanel buttonPanel=(JPanel)statePanel.getComponent(2);

         JLabel in   = (JLabel)indPanel.getComponent(0);
         JLabel out  = (JLabel)indPanel.getComponent(1);
         JLabel pour = (JLabel)indPanel.getComponent(2);

         JButton pouring = (JButton)buttonPanel.getComponent(0);
         JButton stop = (JButton)buttonPanel.getComponent(2);

         in.setEnabled(false);
         out.setEnabled(false);
         pour.setEnabled(false);
         pouring.setEnabled(false);
         stop.setEnabled(false);
         if(this._carafeStateString.trim().equals("HOME")){
            in.setEnabled(true);
         }
         else if(this._carafeStateString.trim().equals("PULLED")){
            out.setEnabled(true);
            pouring.setEnabled(true);
         }
         else if(this._carafeStateString.trim().equals("POURING")){
            pour.setEnabled(true);
            stop.setEnabled(true);
         }
         //Now set up the inputs the Coffee Maker can accept based on
         //the Carafe State
         this.setCoffeeMakerInput();
      }
      catch(ClassCastException cce){}
   }

   //
   //
   //
   //
   private void setCoffeeMakerInput(){
      String carafeState=this._carafeStateString.trim().toUpperCase();
      String makerState =this._stateString.trim().toUpperCase();
      String powerState =this._powerString.trim().toUpperCase();

      this.disableSouthButton("BREW");
      this.disableSouthButton("GET CARAFE");
      this.disableSouthButton("RETURN CARAFE");
      this.disableSouthButton("FILL RESERVOIR");

      if(powerState.equals("ON")){
         if(carafeState.equals("HOME")){
            this.enableSouthButton("BREW");
            this.enableSouthButton("GET CARAFE");
            this.enableSouthButton("FILL RESERVOIR");
         }
         else if(carafeState.equals("PULLED")){
            this.enableSouthButton("BREW");
            this.enableSouthButton("RETURN CARAFE");
            this.enableSouthButton("FILL RESERVOIR");
         }
         else if(carafeState.equals("POURING")){
            this.enableSouthButton("BREW");
            this.enableSouthButton("FILL RESERVOIR");
         }
      }
      else if(powerState.equals("OFF")){}
   }

   //
   //
   //
   //
   private void setReservoirCapacity(Double amount){
      try{
         JPanel panel = (JPanel)this.getContentPane().getComponent(1);
         JPanel rightPanel = (JPanel)panel.getComponent(1);
         JPanel centerPanel = (JPanel)rightPanel.getComponent(1);
         JPanel statePanel = (JPanel)centerPanel.getComponent(0);
         JPanel capPanel = (JPanel)statePanel.getComponent(0);
         JLabel capacity = (JLabel)capPanel.getComponent(1);
         String cap = capacity.getText().substring(0,9);
         capacity.setText(cap + " " + amount.doubleValue());
         capacity.setEnabled(true);
         //Set the Right side
         JPanel resPanel = (JPanel)centerPanel.getComponent(1);
         JProgressBar bar = (JProgressBar)resPanel.getComponent(1);
         bar.setMaximum(amount.intValue());
         this.getContentPane().validate();
         this.getContentPane().repaint();
      }
      catch(NullPointerException npe){}
   }

   //
   //
   //
   //
   private void setReservoirQuantity(Double amount){
      try{
         JPanel panel = (JPanel)this.getContentPane().getComponent(1);
         JPanel rightPanel = (JPanel)panel.getComponent(1);
         JPanel centerPanel = (JPanel)rightPanel.getComponent(1);
         JPanel statePanel = (JPanel)centerPanel.getComponent(0);
         JPanel amountPanel = (JPanel)statePanel.getComponent(0);
         JLabel amountLabel = (JLabel)amountPanel.getComponent(0);
         String amnt = amountLabel.getText().substring(0,7);
         amountLabel.setText(amnt + " " + amount.doubleValue());
         amountLabel.setEnabled(true);
         //Set up Right Side
         JPanel resPanel = (JPanel)centerPanel.getComponent(1);
         JProgressBar bar = (JProgressBar)resPanel.getComponent(1);
         bar.setValue(amount.intValue());
         this.getContentPane().validate();
         this.getContentPane().repaint();
      }
      catch(NullPointerException npe){ npe.printStackTrace(); }
   }

   //
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
   private void setUpGUI(){
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH,HEIGHT);
      this.setResizable(false);
      JPanel centerPanel = this.setUpCenterPanel();
      JPanel northPanel  = this.setUpNorthPanel();
      JPanel southPanel  = this.setUpSouthPanel();
      this.getContentPane().add(northPanel, BorderLayout.NORTH);
      this.getContentPane().add(centerPanel,BorderLayout.CENTER);
      this.getContentPane().add(southPanel, BorderLayout.SOUTH);
      this.setVisible(true);
   }

   //
   //
   //
   //
   //
   private JPanel setUpNorthPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      this._powerGroup = new ButtonGroup();

      JRadioButton power = new JRadioButton("Power", true);
      power.setActionCommand("POWER");
      this._powerGroup.add(power);
      power.addItemListener(this._controller);
      panel.add(power);

      JRadioButton off = new JRadioButton("Power Off");
      off.setActionCommand("OFF");
      this._powerGroup.add(off);
      panel.add(off);

      //What is below reflects the state of the Maker
      JLabel ready = new JLabel("Ready");
      ready.setForeground(Color.blue);
      ready.setEnabled(false);
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
   //
   private JPanel setUpSouthPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());

      JButton brew = new JButton("Brew");
      brew.setActionCommand("BREW");
      brew.setMnemonic(KeyEvent.VK_B);
      brew.setEnabled(false);
      brew.addActionListener(this._controller);
      brew.addKeyListener(this._controller);
      panel.add(brew);

      JButton carafe = new JButton("Get Carafe");
      carafe.setActionCommand("GET");
      carafe.setMnemonic(KeyEvent.VK_G);
      carafe.setEnabled(false);
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
      fill.setEnabled(false);
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
         String string = ((String)o).toUpperCase();
         if(string.contains("COFFEE MAKER")){
            this.handleCoffeeMakerUpdates(string);
         }
      }
      catch(ClassCastException cce){}
   }

   //
   //
   //
   public void update(Object o, String s){
      String string = s.toUpperCase();
      if(string.contains("RESERVOIR")){
         this.handleReservoirUpdates(o,string);
      }
      else if(string.contains("CARAFE")){
         this.handleCarafeUpdates(o,string);
      }
   }

   //
   //
   //
   public void error(RuntimeException re){}

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
