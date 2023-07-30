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

/**/
public class CoffeeMakerView extends GenericJFrame
implements Subscriber{
   private static final int HEIGHT = 700;
   private static final int WIDTH  = 700;

   private ButtonGroup _powerGroup;
   private CoffeeMakerController _controller;

   {
      _controller = null;
      _powerGroup = null;

   };

   /////////////////////////Constructors//////////////////////////////
   /**/
   public CoffeeMakerView(CoffeeMakerController controller){
      this("", controller);
   }

   /**/
   public CoffeeMakerView
   (
      String title,
      CoffeeMakerController controller
   ){
      super(title);
      this._controller = controller;
      this.setUpGui();
   }

   ///////////////////////////Public Methods//////////////////////////
   /////////////////////////Protected Methods/////////////////////////
   ///////////////////////////Private Methods/////////////////////////
   /**/
   private void disableSouthButton(String button){
      JPanel panel = (JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         JButton b = (JButton)panel.getComponent(i);
         if(b.getText().toUpperCase().equals(button.toUpperCase())){
            b.setEnabled(false);
	 }
      }
   }

   /**/
   private void enableSouthButton(String button){
      JPanel panel = (JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         JButton b = (JButton)panel.getComponent(i);
         if(b.getText().toUpperCase().equals(button.toUpperCase())){
            b.setEnabled(true);
         }
      }
   }

   /**/
   private void handleCarafeErrors(String carafeError){
      String error = carafeError.toUpperCase();
      if(error.contains("OVERFLOWING")){
         String ovrfl = new String("Carafe Overflowing!");
         JOptionPane.showMessageDialog(this,
                                    ovrfl,
                                    carafeError,
                                    JOptionPane.WARNING_MESSAGE);
      }
      else if(error.contains("NOT HOME")){
         String title = new String("Carafe Not Home!");
         String body  = new String("Please Wait Until Carafe");
         body += "\nis returned";
         JOptionPane.showMessageDialog(this,
                                       body,
                                       title,
                                       JOptionPane.ERROR_MESSAGE);
      }
      else if(error.contains("POURING")){
         String title = new String("Pouring Error");
         JOptionPane.showMessageDialog(this,
                                       error,
                                       title,
                                       JOptionPane.ERROR_MESSAGE);
      }
      else if(error.contains("EMPTY")){
         String title = new String("Carafe Empty!");
         String body  = new String("Carafe is Empty. Please Refill");
         JOptionPane.showMessageDialog(this,
                                       body,
                                       title,
                                       JOptionPane.WARNING_MESSAGE);
      }
   }

   /**/
   private void handleCarafeUpdates(Object o, String s){
      String carafeType = s.split(" ")[1];
      if(carafeType.toUpperCase().equals("STATE")){
         try{
            String state = (String)o;
            this.setCarafeState(state);
         }
         catch(ClassCastException cce){}
      }
      else if(carafeType.toUpperCase().equals("CAPACITY")){
         try{
            Double cap = (Double)o;
            double capacity = cap.doubleValue();
            this.setCarafeCapacity(capacity);
         }
         catch(ClassCastException cce){}
      }
      else if(carafeType.toUpperCase().equals("QUANTITY")){
         try{
            Double quant    = (Double)o;
            double quantity = quant.doubleValue();
            this.setCarafeQuantity(quantity);
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   private void handleJRadioButton(JRadioButton jb){
      String command = jb.getActionCommand().toUpperCase();
      if(command.equals("OFF")){
         this.powerOff();
      }
      else if(command.equals("POWER")){
         this.powerOn();
      }
   }

   /**/
   private void handleMakerErrors(String message){
      String error = message.toUpperCase();
      if(error.contains("ALREADY") && error.contains("BREWING")){
         String issue = "The Coffee Maker is Already Brewing";
         JOptionPane.showMessageDialog(this,
                                       issue,
                                       message,
                                       JOptionPane.WARNING_MESSAGE);
      }
   }

   /**/
   private void handleMessage(String message){
      if(message.contains("State:")){
         this.reflectStateString(message.split(" ")[1]);
      }
   }

   /**/
   private void handleOverflowExceptions(OverflowException oe){
      JOptionPane.showMessageDialog(this,
                                    oe.getMessage(),
                                    "Overflow Exception",
                                    JOptionPane.WARNING_MESSAGE);
   }

   /**/
   private void handleReservoirErrors(String reservoirError){
      String error = reservoirError.toUpperCase();
      if(error.contains("EMPTY")){
         //Set up a JOption Pane indicating the error
         String fill = "Please fill the Reservoir\nBy pressing the";
         fill += "\"Fill Reservoir\" \nButton in the Button Panel";
         JOptionPane.showMessageDialog(this,
                                    fill,
                                    "Fill the Reservoir",
                                    JOptionPane.INFORMATION_MESSAGE);
      }
      else if(error.contains("OVERFLOWING")){
         String ovrfl = "Reservoir is OVERFLOWING!\nPossible mess ";
         ovrfl += "needing\nclean up!";
         JOptionPane.showMessageDialog(this,
                                    ovrfl,
                                    reservoirError,
                                    JOptionPane.WARNING_MESSAGE);
      }
   }

   /**/
   private void handleReservoirUpdates(Object o, String s){
      String message = s.split(" ")[1];
      if(message.toUpperCase().equals("CAPACITY")){
         try{
            Double cap = (Double)o;
            double capacity = cap.doubleValue();
            this.setReservoirCapacity(capacity);
         }
         catch(ClassCastException cce){}
      }
      else if(message.toUpperCase().equals("QUANTITY")){
         try{
            Double quant = (Double)o;
            double quantity = quant.doubleValue();
            this.setReservoirQuantity(quantity);
         }
         catch(ClassCastException cce){}
      }
      else if(message.toUpperCase().equals("STATE")){
         try{
            String state = (String)o;
            String situation = state.split(" ")[0];
            if(situation.toUpperCase().equals("ERROR")){
               String condition = state.split(" ")[1];
               //handle the reservoir error
               this.handleReservoirErrors(condition);
            }
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   private void powerOff(){
      this.disableSouthButton("Brew");
      this.disableSouthButton("Get Carafe");
      this.disableSouthButton("Return Carafe");
      this.disableSouthButton("Fill Reservoir");
   }

   /**/
   private void powerOn(){
      this.enableSouthButton("Brew");
      this.enableSouthButton("Get Carafe");
      this.disableSouthButton("Return Carafe");
      this.enableSouthButton("Fill Reservoir");
   }

   /**/
   private void reflectStateString(String state){
      int powerOnNumber      = 0;
      int powerOffNumber     = 1;
      int readyLabelNumber   = 2;
      int brewingLabelNumber = 3;
      JPanel top = (JPanel)this.getContentPane().getComponent(0);
      if(state.toUpperCase().equals("READY")){
         top.getComponent(powerOnNumber).setEnabled(true);
         top.getComponent(powerOffNumber).setEnabled(true);
         top.getComponent(readyLabelNumber).setEnabled(true);
         top.getComponent(brewingLabelNumber).setEnabled(false);
      }
      else if(state.toUpperCase().equals("BREWING")){
         top.getComponent(powerOnNumber).setEnabled(false);
	 top.getComponent(powerOffNumber).setEnabled(false);
         top.getComponent(readyLabelNumber).setEnabled(false);
         top.getComponent(brewingLabelNumber).setEnabled(true);
      }
   }

   /**/
   private void setCarafeCapacity(double capacity){
      //Set the Left panel
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      JPanel leftPanel = (JPanel)panel.getComponent(0);
      JPanel centerPanel = (JPanel)leftPanel.getComponent(1);
      JPanel statePanel = (JPanel)centerPanel.getComponent(0);
      JPanel amountPanel = (JPanel)statePanel.getComponent(0);
      JLabel cap = (JLabel)amountPanel.getComponent(1);
      cap.setText(cap.getText()+" "+(int)capacity);
      cap.setEnabled(true);
      //Set the right panel
      JPanel carafePanel = (JPanel)centerPanel.getComponent(1);
      JProgressBar bar = (JProgressBar)carafePanel.getComponent(1);
      bar.setMaximum((int)capacity);
      this.getContentPane().validate();
      this.getContentPane().repaint();
   }

   /**/
   private void setCarafeQuantity(double quantity){
      //Set the Left Panel
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      JPanel leftPanel = (JPanel)panel.getComponent(0);
      JPanel centerPanel = (JPanel)leftPanel.getComponent(1);
      JPanel statePanel = (JPanel)centerPanel.getComponent(0);
      JPanel amountPanel = (JPanel)statePanel.getComponent(0);
      JLabel amountLabel = (JLabel)amountPanel.getComponent(0);
      String amount = amountLabel.getText().substring(0,7);
      amountLabel.setText(amount + " " + quantity);
      amountLabel.setEnabled(true);
      //Set the Right Panel
      JPanel carafePanel = (JPanel)centerPanel.getComponent(1);
      JProgressBar bar = (JProgressBar)carafePanel.getComponent(1);
      bar.setValue((int)quantity);
      this.getContentPane().validate();
      this.getContentPane().repaint();
   }

   /**/
   private void setCarafeState(String state){
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      JPanel leftPanel = (JPanel)panel.getComponent(0);
      JPanel centerPanel = (JPanel)leftPanel.getComponent(1);
      JPanel statePanel  = (JPanel)centerPanel.getComponent(0);
      JPanel indicatorPanel = (JPanel)statePanel.getComponent(1);
      JPanel buttonPanel = (JPanel)statePanel.getComponent(2);

      JLabel in   = (JLabel)indicatorPanel.getComponent(0);
      JLabel out  = (JLabel)indicatorPanel.getComponent(1);
      JLabel pour = (JLabel)indicatorPanel.getComponent(2);

      JButton pouring = (JButton)buttonPanel.getComponent(0);
      JButton stop = (JButton)buttonPanel.getComponent(2);

      in.setEnabled(false);
      out.setEnabled(false);
      pour.setEnabled(false);
      pouring.setEnabled(false);
      stop.setEnabled(false);
      if(state.trim().toUpperCase().equals("HOME")){
         in.setEnabled(true);
         this.enableSouthButton("Brew");
         this.enableSouthButton("Get Carafe");
         this.enableSouthButton("Fill Reservoir");
         this.disableSouthButton("Return Carafe");
      }
      else if(state.trim().toUpperCase().equals("PULLED")){
         out.setEnabled(true);
         //Enable the Pour Button
         pouring.setEnabled(true);
         this.enableSouthButton("Brew");
         this.disableSouthButton("Get Carafe");
         this.enableSouthButton("Return Carafe");
         this.enableSouthButton("Fill Reservoir");
      }
      else if(state.trim().toUpperCase().equals("POURING")){
         pour.setEnabled(true);
         //Enable the Stop Pouring Button
         stop.setEnabled(true);
         this.enableSouthButton("Brew");
         this.disableSouthButton("Get Carafe");
         this.disableSouthButton("Return Carafe");
         this.enableSouthButton("Fill Reservoir");
      }
   }

   /**/
   private JPanel setUpCarafePanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.setBorder(BorderFactory.createEtchedBorder());
      //Set up the North Part
      JPanel northPanel = new JPanel();
      northPanel.setBorder(BorderFactory.createEtchedBorder());
      JLabel northLabel = new JLabel("Carafe",SwingConstants.CENTER);
      northPanel.add(northLabel);
      panel.add(northPanel,  BorderLayout.NORTH);
      /////
      JPanel centerPanel = new JPanel();
      centerPanel.setLayout(new GridLayout(1,2));
      JPanel centerLeft = this.setUpCarafeCenterLeftPanel();
      centerPanel.add(centerLeft);
      JPanel centerRight = this.setUpCarafeCenterRightPanel();
      centerPanel.add(centerRight);
      panel.add(centerPanel, BorderLayout.CENTER);
      return panel;
   }

   /**/
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
      centerPanel.setLayout(new GridLayout(3,1));
      centerPanel.setBorder(BorderFactory.createEtchedBorder());
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
      southPanel.setLayout(new GridLayout(3,1));
      southPanel.setBorder(BorderFactory.createEtchedBorder());
      JButton pour = new JButton("Pour");
      pour.setActionCommand("Pour");
      pour.addActionListener(this._controller);
      pour.addKeyListener(this._controller);
      pour.setEnabled(false);
      southPanel.add(pour);
      JButton empty = new JButton(" ");
      empty.setEnabled(false);
      southPanel.add(empty);
      JButton stop = new JButton("Stop Pouring");
      stop.setActionCommand("StopPouring");
      stop.addActionListener(this._controller);
      stop.addKeyListener(this._controller);
      stop.setEnabled(false);
      southPanel.add(stop);
      panel.add(southPanel);

      //panel.add(new JPanel());//Just put in an empty panel
      return panel;
   }

   /**/
   private void setReservoirCapacity(double capacity){
      //Set Left Panel
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      JPanel rightPanel = (JPanel)panel.getComponent(1);
      JPanel centerPanel = (JPanel)rightPanel.getComponent(1);
      JPanel statePanel = (JPanel)centerPanel.getComponent(0);
      JPanel amountPanel = (JPanel)statePanel.getComponent(0);
      JLabel cap = (JLabel)amountPanel.getComponent(1);
      cap.setText(cap.getText()+" "+(int)capacity);
      cap.setEnabled(true);
      //Set Right Panel
      JPanel reservoirPanel = (JPanel)centerPanel.getComponent(1);
      JProgressBar bar = (JProgressBar)reservoirPanel.getComponent(1);
      bar.setMaximum((int)capacity);
      this.getContentPane().validate();
      this.getContentPane().repaint();
   }

   /**/
   private void setReservoirQuantity(double quantity){
      //Set Left Panel
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      JPanel rightPanel = (JPanel)panel.getComponent(1);
      JPanel centerPanel = (JPanel)rightPanel.getComponent(1);
      JPanel statePanel = (JPanel)centerPanel.getComponent(0);
      JPanel amountPanel = (JPanel)statePanel.getComponent(0);
      JLabel amountLabel = (JLabel)amountPanel.getComponent(0);
      String amount = amountLabel.getText().substring(0,7);
      amountLabel.setText(amount + " " + quantity);
      amountLabel.setEnabled(true);
      //Set Right Panel
      JPanel carafePanel = (JPanel)centerPanel.getComponent(1);
      JProgressBar bar = (JProgressBar)carafePanel.getComponent(1);
      bar.setValue((int)quantity);
      this.getContentPane().validate();
      this.getContentPane().repaint();
   }


   /**/
   private JPanel setUpCarafeCenterRightPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.setBorder(BorderFactory.createEtchedBorder());
      //North Panel
      JPanel northPanel = new JPanel();
      JLabel amountLabel = new JLabel(" ");
      northPanel.add(amountLabel);
      panel.add(northPanel, BorderLayout.NORTH);
      //Center Panel
      //This will need to be updated (upper and lower limits) based on
      //the initialization of the Model (Coffee Maker)...
      JProgressBar carafeAmount =
                new JProgressBar(SwingConstants.VERTICAL, 0, 32);
      carafeAmount.setValue(carafeAmount.getMinimum());
      carafeAmount.setStringPainted(true);
      panel.add(carafeAmount, BorderLayout.CENTER);
      return panel;
   }

   /**/
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new GridLayout(1,2,10,10));
      panel.add(this.setUpCarafePanel());
      panel.add(this.setUpReservoirPanel());
      return panel;
   }

   /**/
   private void setUpGui(){
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH,HEIGHT);
      this.setResizable(false);
      JPanel centerPanel= this.setUpCenterPanel();
      JPanel northPanel = this.setUpNorthPanel();
      JPanel southPanel = this.setUpSouthPanel();
      this.getContentPane().add(northPanel, BorderLayout.NORTH);
      this.getContentPane().add(centerPanel,BorderLayout.CENTER);
      this.getContentPane().add(southPanel, BorderLayout.SOUTH);
      this.setVisible(true);
   }

   /**/
   private JPanel setUpNorthPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      this._powerGroup = new ButtonGroup();

      JRadioButton power = new JRadioButton("Power", true);
      power.setActionCommand("Power");
      this._powerGroup.add(power);
      power.addItemListener(this._controller);
      panel.add(power);

      JRadioButton off = new JRadioButton("Power Off");
      off.setActionCommand("Off");
      this._powerGroup.add(off);
      off.addItemListener(this._controller);
      panel.add(off);

      //Reflect the current state of the Maker
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

   /**/
   private JPanel setUpReservoirPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      panel.setLayout(new BorderLayout());
      //Set up North Part
      JPanel northPanel = new JPanel();
      northPanel.setBorder(BorderFactory.createEtchedBorder());
      JLabel northLabel=new JLabel("Reservoir",SwingConstants.CENTER);
      northPanel.add(northLabel);
      panel.add(northPanel,  BorderLayout.NORTH);
      //Set up the Center Part
      JPanel centerPanel = new JPanel();
      centerPanel.setLayout(new GridLayout(1,2));
      JPanel centerLeft = this.setUpReservoirCenterLeftPanel();
      centerPanel.add(centerLeft);
      JPanel centerRight = this.setUpReservoirCenterRightPanel();
      centerPanel.add(centerRight);
      panel.add(centerPanel,  BorderLayout.CENTER);
      return panel;
   }

   /**/
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

   /**/
   private JPanel setUpReservoirCenterRightPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.setBorder(BorderFactory.createEtchedBorder());

      //North Panel
      JPanel northPanel = new JPanel();
      JLabel northLabel = new JLabel(" ");
      northPanel.add(northLabel);
      panel.add(northPanel, BorderLayout.NORTH);

      //Center Panel
      //This will need to be updated (upper and lower limits) based on
      //the initialization of the Model (Coffee Maker)...
      JProgressBar bar=new JProgressBar(SwingConstants.VERTICAL,0,32);
      bar.setValue(bar.getMinimum());
      bar.setStringPainted(true);
      panel.add(bar, BorderLayout.CENTER);

      return panel;
   }

   /**/
   private JPanel setUpSouthPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());

      JButton brew = new JButton("Brew");
      brew.setActionCommand("Brew");
      brew.setMnemonic(KeyEvent.VK_B);
      brew.addActionListener(this._controller);
      brew.addKeyListener(this._controller);
      panel.add(brew);

      JButton carafe = new JButton("Get Carafe");
      carafe.setActionCommand("Get");
      carafe.setMnemonic(KeyEvent.VK_G);
      carafe.addActionListener(this._controller);
      carafe.addKeyListener(this._controller);
      panel.add(carafe);

      JButton returnCarafe = new JButton("Return Carafe");
      returnCarafe.setActionCommand("Return");
      returnCarafe.setMnemonic(KeyEvent.VK_R);
      returnCarafe.addActionListener(this._controller);
      returnCarafe.addKeyListener(this._controller);
      panel.add(returnCarafe);

      JButton fill = new JButton("Fill Reservoir");
      fill.setActionCommand("Reservoir Fill");
      fill.setMnemonic(KeyEvent.VK_F);
      fill.addActionListener(this._controller);
      fill.addKeyListener(this._controller);
      panel.add(fill);

      return panel;
   }
   
   ////////////////////////Interface Methods//////////////////////////
   /**/
   public void update(Object o){
      try{
         CarafeInterface ci = (CarafeInterface)o;
      }
      catch(ClassCastException cce){}
      try{
         ReservoirInterface ri = (ReservoirInterface)o;
      }
      catch(ClassCastException cce){}
      try{
         String message = (String)o;
         this.handleMessage(message);
      }
      catch(ClassCastException cce){}
      try{
         JRadioButton jb = (JRadioButton)o;
         this.handleJRadioButton(jb);
      }
      catch(ClassCastException cce){}
   }

   /**/
   public void update(Object o, String s){
      if(s.toUpperCase().contains("CARAFE ")){
         this.handleCarafeUpdates(o, s);
      }
      else if(s.toUpperCase().contains("RESERVOIR ")){
         this.handleReservoirUpdates(o, s);
      }
      else if(s.toUpperCase().contains("MUG")){}
   }

   /**/
   public void error(RuntimeException re){
   }

   /**/
   public void error(RuntimeException re, Object o){}

   /**/
   public void error(String error){
      if(error.toUpperCase().contains("RESERVOIR")){
         this.handleReservoirErrors(error);
      }
      else if(error.toUpperCase().contains("CARAFE")){
         this.handleCarafeErrors(error);
      }
      else if(error.toUpperCase().contains("MAKER")){
         this.handleMakerErrors(error);
      }
   }
}
//////////////////////////////////////////////////////////////////////
