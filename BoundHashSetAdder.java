//////////////////////////////////////////////////////////////////////
/**/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class BoundHashSetAdder implements Runnable{
   private BoundedHashSet<Integer>  set;
   private Stack<Integer>           stack;
   private boolean                  toRun;
   private boolean                  toAdd;

   {
      set            = null;
      stack          = null;
      toRun          = false;
      toAdd          = false;
   };

   /**/
   public BoundHashSetAdder
   (
      BoundedHashSet<Integer> set_,
      Stack<Integer>          stack_
   ){
      this.set   = set_;
      this.stack = stack_;
   }

   //
   //
   //
   public void setAdd(boolean add){
      this.toAdd = add;
   }

   //
   //
   //
   public void setRun(boolean run){
      this.toRun = run;
   }

   /////////////////////Runnable Interface////////////////////////////
   //Will need to change at some point...
   //
   //
   public void run(){
      while(toRun){
         try{
            int sTime = (int)Math.round(Math.random()*10000);
            System.out.print("run() ");
            System.out.println(Thread.currentThread().getName());
            if(this.toAdd){
               int i = (int)Math.round(Math.random()*1000);
               if(this.set.add(Integer.valueOf(i))){
                  this.stack.push(Integer.valueOf(i));
               }
            }
            Thread.sleep(sTime);
         }
         catch(InterruptedException ie){
            ie.printStackTrace();
            toRun = false;
         }
      }
   }
}
//////////////////////////////////////////////////////////////////////
