//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;
import myclasses.*;

public class MugView extends GenericJInteractionFrame{
   private static final int WIDTH  = 320;
   private static final int HEIGHT = 450;
   private int    _capacity;
   private JLabel _amountLabel;
   private JLabel _capacityLabel;
   {
      _capacity      = 0;
      _amountLabel   = null;
      _capacityLabel = null;

   };

   //////////////////////////Constructors/////////////////////////////
   //
   //
   //
   public MugView(int capacity){
      super("Mug");
      this._capacity = capacity;
      this.setGUI();
   }

   ////////////////////////Public Methods/////////////////////////////
   //
   //
   //
   public void amount(double quantity){
      try{
         String amount = this._amountLabel.getText().substring(0,7);
         this._amountLabel.setText(amount + " " +(int)quantity);
	 this._amountLabel.setEnabled(true);
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
      this._capacityLabel = new JLabel("Capacity: "+this._capacity);
      topLeft.add(this._capacityLabel);
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
      int sz = this._capacity;
      JProgressBar bar=new JProgressBar(SwingConstants.VERTICAL,0,sz);
      bar.setValue(bar.getMinimum());
      bar.setStringPainted(true);
      right.add(bar, BorderLayout.CENTER);
   }

   //
   //
   //
   private int capacity(){
      return this._capacity;
   }

}
//////////////////////////////////////////////////////////////////////
