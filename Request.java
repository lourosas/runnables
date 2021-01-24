//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class Request implements Runnable{
   private boolean  _quit;
   private boolean  _active;
   private int      _sleepTime;
   private Response _response;

   {
      _quit      = false;
      _active    = true;
      _sleepTime = 1000;
      _response  = null;
   };
   
   /*
   */
   public Request(){
      this._response = new Response();
   }

   /*
   */
   public Request(int sleepTime_){
      this();
      this.sleepTime(sleepTime_);
   }

   /*
   */
   public void active(boolean active_){
      this._active = active_;
   }

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

   ///////////////////Runnable Interface Implementation///////////////
   /*
   */
   public void run(){}
}
