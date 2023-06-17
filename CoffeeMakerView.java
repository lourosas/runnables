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

   /*
   TO BE REMOVED!!!
   */
   private void handleEmptyReservoirException
   (
      EmptyReservoirException ere
   ){
      String fill = "Please fill the Reservoir\nBy pressing the";
      fill += "\"Fill Reservoir\" \nButton in the Button Panel";
      JOptionPane.showMessageDialog(this,
                                    fill,
                                    "Fill the Reservoir",
                                    JOptionPane.WARNING_MESSAGE);

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
   private void reflectStateString(String state){
      int readyLabelNumber   = 2;
      int brewingLabelNumber = 3;
      JPanel top = (JPanel)this.getContentPane().getComponent(0);
      if(state.toUpperCase().equals("READY")){
         top.getComponent(readyLabelNumber).setEnabled(true);
         top.getComponent(brewingLabelNumber).setEnabled(false);
      }
      else if(state.toUpperCase().equals("BREWING")){
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
      amountLabel.setText(amount + " " + (int)quantity);
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
      JLabel in   = (JLabel)indicatorPanel.getComponent(0);
      JLabel out  = (JLabel)indicatorPanel.getComponent(1);
      JLabel pour = (JLabel)indicatorPanel.getComponent(2);
      in.setEnabled(false);
      out.setEnabled(false);
      pour.setEnabled(false);
      if(state.trim().toUpperCase().equals("HOME")){
         in.setEnabled(true);
      }
      else if(state.trim().toUpperCase().equals("PULLED")){
         out.setEnabled(true);
      }
      else if(state.trim().toUpperCase().equals("POURING")){
         pour.setEnabled(true);
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
      out.setForeground(Color.orange);
      out.setEnabled(false);
      centerPanel.add(out);
      JLabel pouring = new JLabel("Pouring");
      pouring.setForeground(Color.red);
      pouring.setEnabled(false);
      centerPanel.add(pouring);
      panel.add(centerPanel);

      panel.add(new JPanel());//Just put in an empty panel
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
      amountLabel.setText(amount + " " + (int)quantity);
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
      brewing.setForeground(Color.blue);
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
      JLabel northLabel = new JLabel("Reservoir",SwingConstants.CENTER);
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
   }

   /**/
   public void update(Object o, String s){
      if(s.contains("Carafe ")){
         this.handleCarafeUpdates(o, s);
      }
      else if(s.contains("Reservoir ")){
         this.handleReservoirUpdates(o, s);
      }
   }

   /**/
   public void error(RuntimeException re){
      /*
      try{
         OverflowException oe = (OverflowException)re;
         this.handleOverflowExceptions(oe);
      }
      catch(ClassCastException cce){}
      try{
         EmptyReservoirException ere = (EmptyReservoirException)re;
         this.handleEmptyReservoirException(ere);
      }
      catch(ClassCastException cce){}
      */
   }

   /**/
   public void error(RuntimeException re, Object o){}

   /**/
   public void error(String error){
      if(error.toUpperCase().contains("RESERVOIR")){
         this.handleReservoirErrors(error);
      }
   }
}
//////////////////////////////////////////////////////////////////////
