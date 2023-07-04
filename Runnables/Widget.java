/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class Widget{

   ////////////////////////////Constructor////////////////////////////
   /*
   */
   public Widget(){}

   ////////////////////////Public Methods/////////////////////////////
   public synchronized void doSomething(){
      String name = Thread.currentThread().getName();
      System.out.println(name + ":  " + toString());
   }
}
