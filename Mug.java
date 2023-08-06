//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.awt.event.*;
import rosas.lou.runnables.*;

public class Mug implements Runnable{
   //Set the size to 8 Oz. for the moment
   private final int SIZE = 8;
   private double    _quantity;
   private int       _size;
   private MugView   _view;

   {
      _quantity = 0;
      _size     = SIZE;
      _view     = null;
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public Mug(WindowListener wl){
      this._view = new MugView(SIZE,wl);
      this._view.setVisible(true);
   }

   //
   //
   //
   public Mug(int size, WindowListener wl){
      this._size = size;
      this._view = new MugView(this._size, wl);
      this._view.setVisible(true);
   }

   //
   //
   //
   public void noLongerNeeded(){
      this._view.setVisible(false);
   }

   /////////////////////////Public Methods////////////////////////////
   //
   //
   //
   public void fill(double amount){
      final double FUDGEFACTOR = 0.05;
      final double MIN         = this.size() - FUDGEFACTOR;
      final double MAX         = this.size() + FUDGEFACTOR;

      this.quantity(this.quantity() + amount);
      this._view.amount(this.quantity());
      if(this.quantity() > this.size()){
         this.quantity(this.size());
         this._view.amount(this.quantity());
         this._view.alertOverflowError();
	 try{
            Thread.sleep(1500);
         }
         catch(InterruptedException ie){}
      }
      else if(this.quantity() >= MIN){
         try{
            Thread.sleep(1500);
         }
         catch(InterruptedException ie){}
      }
   }

   //
   //
   //
   public double quantity(){
      return this._quantity;
   }

   //
   //
   //
   public int size(){
      return this._size;
   }

   ///////////////////////Private Methods/////////////////////////////
   //
   //
   //
   private void quantity(double quant){
      this._quantity = quant;
   }

   ///////////////////////Interface Methods///////////////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
