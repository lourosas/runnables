/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.Widget;

public class LoggingWidget extends Widget{
   ////////////////////////Constructor////////////////////////////////
   /*
   */
   public LoggingWidget(){}

   ////////////////////////PUblic Methods/////////////////////////////
   public synchronized void doSomething(){
      String name = Thread.currentThread().getName();
      System.out.println(name + ":  " + toString() + " subclass");
      super.doSomething();
   }
}
