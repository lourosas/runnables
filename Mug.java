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

   {
      _quantity = 0;
      _size     = SIZE;
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public Mug(){}

   //
   //
   //
   public Mug(int size){
      this._size = size;
   }

   /////////////////////////Public Methods////////////////////////////
   //
   //
   //
   public void fill(double amount)throws OverflowException{}

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
