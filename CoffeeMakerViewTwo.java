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
      //JPanel northPanel  = this.setUpNorthPanel();
      //JPanel southPanel  = this.setUpSouthPanel();
      //this.getContentPane().add(northPanel, BorderLayout.NORTH);
      this.getContentPane().add(centerPanel,BorderLayout.CENTER);
      //this.getContentPane().add(southPanel, BorderLayout.SOUTH);
      this.setVisible(true);
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
   ////////////////////Interface Methods//////////////////////////////
   //
   //
   //
   public void update(Object o){}

   //
   //
   //
   public void update(Object o, String s){}

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
