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

public class MugView extends GenericJInteractionFrame{
   private static final int WIDTH  = 320;
   private static final int HEIGHT = 450;
   private int    _capacity;
   private double _quantity;
   private JLabel _amountLabel;
   private JLabel _capacityLabel;
   private JProgressBar _bar;
   {
      _capacity      = 0;
      _amountLabel   = null;
      _capacityLabel = null;
      _bar           = null;

   };

   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   public MugView(int capacity){
      super("Mug");
      this.capacity(capacity);
      this.setGUI();
   }

   ////////////////////////Public Methods/////////////////////////////
   //
   //
   //
   public void alertOverflowError(){
      String overflow = new String("Cup is Overflowing!\n");
      overflow = overflow.concat("And spilling everwhere!");
      JOptionPane.showMessageDialog(this,
                                    overflow,
                                    "Overflow Error!",
                                    JOptionPane.WARNING_MESSAGE);
   }

   //
   //
   //
   public void amount(double quantity){
      try{
         String amount = this._amountLabel.getText().substring(0,7);
         this._amountLabel.setText(amount + " " +quantity);
         this._amountLabel.setEnabled(true);
         this._bar.setValue((int)quantity);
         this.getContentPane().validate();
         this.getContentPane().repaint();
      }
      catch(NullPointerException npe){}
   }

   ///////////////////////Private Methods/////////////////////////////
   //
   //
   //
   private void setGUI(){
      this.setLayout(new GridLayout(1,2));
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);
      this.add(this.setLeftPanel());
      this.add(this.setRightPanel());
   }

   //
   //
   //
   private JPanel setLeftPanel(){
      JPanel left = new JPanel();
      left.setBorder(BorderFactory.createEtchedBorder());
      left.setLayout(new GridLayout(3,1));
      JPanel topLeft = new JPanel();
      topLeft.setBorder(BorderFactory.createEtchedBorder());
      topLeft.setLayout(new GridLayout(2,1));
      this._amountLabel = new JLabel("Amount: ");
      this._amountLabel.setEnabled(false);
      topLeft.add(this._amountLabel);
      this._capacityLabel = new JLabel("Capacity: "+this.capacity());
      topLeft.add(this._capacityLabel);
      left.add(topLeft);
      left.add(new JPanel());
      left.add(new JPanel());
      return left;
   }

   //
   //
   //
   private JPanel setRightPanel(){
      JPanel right = new JPanel();
      right.setLayout(new BorderLayout());
      right.setBorder(BorderFactory.createEtchedBorder());
      int sz = this.capacity();
      /*
      JProgressBar bar=new JProgressBar(SwingConstants.VERTICAL,0,sz);
      bar.setValue(bar.getMinimum());
      bar.setStringPainted(true);
      right.add(bar, BorderLayout.CENTER);
      */
      this._bar = new JProgressBar(SwingConstants.VERTICAL,0,sz);
      this._bar.setValue(this._bar.getMinimum());
      this._bar.setStringPainted(true);
      right.add(this._bar, BorderLayout.CENTER);
      return right;
   }

   //
   //
   //
   private int capacity(){
      return this._capacity;
   }

   //
   //
   //
   private void capacity(int cap){
      this._capacity = cap;
   }
}
//////////////////////////////////////////////////////////////////////
