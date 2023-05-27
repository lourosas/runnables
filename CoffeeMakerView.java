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
   private ButtonGroup _stateGroup;
   private CoffeeMakerController _controller;

   {
      _controller = null;
      _powerGroup = null;
      _stateGroup = null;
      
   };

   /////////////////////////Constructors//////////////////////////////
   /**/
   public CoffeeMakerView(){
      this("");
   }

   /**/
   public CoffeeMakerView(String title){
      super(title);
      this.setUpGui();
   }

   ///////////////////////////Public Methods//////////////////////////
   /**/
   public void addController(CoffeeMakerController controller){
      this._controller = controller;
   }

   /////////////////////////Protected Methods/////////////////////////
   ///////////////////////////Private Methods/////////////////////////
   /**/
   private void handleMessage(String message){
      if(message.contains("State:")){
         this.reflectStateString(message.split(" ")[1]);
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
   private void setUpGui(){
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH,HEIGHT);
      this.setResizable(false);
      JPanel northPanel = this.setUpNorthPanel();
      JPanel southPanel = this.setUpSouthPanel();
      this.getContentPane().add(northPanel, BorderLayout.NORTH);
      this.getContentPane().add(southPanel, BorderLayout.SOUTH);

      this.setVisible(true);
   }

   /**/
   private JPanel setUpNorthPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      this._powerGroup = new ButtonGroup();
      this._stateGroup = new ButtonGroup();

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
   public void update(Object o, String s){}

   /**/
   public void error(RuntimeException re){}

   /**/
   public void error(RuntimeException re, Object o){}

   /**/
   public void error(String error){}
}
//////////////////////////////////////////////////////////////////////
