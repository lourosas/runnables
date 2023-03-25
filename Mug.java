//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class Mug implements Runnable{
   private final int SIZE = 8;//set the size to 8 Oz for the moment
   private int _quantity;

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
   public void fillCompletely(){}

   //
   //
   //
   public void fill(int amount){}

   //
   //
   //
   public int quantity(){
      return this._quantity;
   }

   ///////////////////////Interface Methods///////////////////////////
   //
   //
   //
   public void run(){}
}
//////////////////////////////////////////////////////////////////////
