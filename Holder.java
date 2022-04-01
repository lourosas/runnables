/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class Holder{
   static int value = 32;

   static{
      System.out.println("Holder static Initializer:  "+value);

   };

   /*
   {
      System.out.println("Holder Initializer:  "+value);
      value = 38;
      System.out.println("Holder Initializer:  "+value);
   };
   */

   ///////////////////////////////////////////////////////////////////
   /**/
   public Holder(){
      //this.value = -1;
      System.out.print("Holder Constructor of no arguments:  ");
      System.out.println(value);
   }

   /**/
   public Holder(int value_){
      this.value = value_;
      System.out.print("Holder Constructor of int type argument:  ");
      System.out.println(value);
   }

   /**/
   public int value(){
      return this.value;
   }
}
