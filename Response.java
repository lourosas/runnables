//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class Response implements Runnable{
   private boolean _quit;
   private boolean _triggered;
   private int     _sleepTime;

   {
      _quit      = false;
      _triggered = true;
      _sleepTime = 500;
   };

   /*
   */
   public Response(){}

   /*
   */
   public void quit(boolean quit_){
      this._quit = quit_;
   }

   /*
   */
   public void sleepTime(int sleepTime_){
      this._sleepTime = sleepTime_;
   }

   /*
   */
   public void trigger(boolean trigger_){
      this._triggered = trigger_;
   }

   /////////////////Runnable Interface Implementation/////////////////
   /*
   */
   public void run(){}

}
