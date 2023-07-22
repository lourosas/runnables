//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
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
   public Mug(){
      this._view = new MugView(SIZE);
      this._view.setVisible(true);
   }

   //
   //
   //
   public Mug(int size){
      this._size = size;
      this._view = new MugView(this._size);
      this._view.setVisible(true);
   }

   /////////////////////////Public Methods////////////////////////////
   //
   //
   //
   public void fill(double amount){
      this._quantity += amount;
      if(this.quantity() > this.SIZE){
         //Alert the User via the View
         this._view.alertOverflowError();
         this._quantity = this.SIZE;
      }
      //Notify the View of the quantity...
      this._view.amount(this.quantity());
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

   ///////////////////////Interface Methods///////////////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
