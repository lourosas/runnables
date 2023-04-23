//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class Mug implements Runnable{
   private final double SIZE = 8.0;//set the size to 8 Oz for the moment
   private double _quantity;

   {
      _quantity = 0;
   };

   /////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public Mug(){}

   /////////////////////////Public Methods////////////////////////////
   //
   //
   //
   public void fill(double amount){}

   //
   //
   //
   public double quantity(){
      return this._quantity;
   }

   ///////////////////////Interface Methods///////////////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
