//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class SampleWidgetTest implements Runnable{
   private LoggingWidget lw = null;
   private Widget         w = null;
   ///////////////////Constructor/////////////////////////////////////
   /**/
   public SampleWidgetTest(){
      lw = new LoggingWidget();
      w  = new Widget();
   }

   ///////////////////Runnable implementation/////////////////////////
   /**/
   public void run(){
      lw.doSomething();
      //w.doSomething();
   }
}
