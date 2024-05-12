//////////////////////////////////////////////////////////////////////
/**/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class BoundHashSetRemover implements Runnable{
   private BoundedHashSet<Integer> set;
   private Stack<Integer>          stack;
   private boolean                 toRemove;
   private boolean                 monitor;

   {
      set       = null;
      stack     = null;
      toRemove  = false;
      monitor   = false;
   };

   ////////////////////////Constructors//////////////////////////////
   //
   //
   //
   public BoundHashSetRemover
   (
      BoundedHashSet<Integer> set_,
      Stack<Integer>          stack_
   ){
      this.set    = set_;
      this.stack  = stack_;
   
   }

   //
   //
   //
   public void setMonitor(boolean monitor){
      this.monitor = monitor;
   }

   //
   //
   //
   public void setRemove(boolean remove){
      this.toRemove = remove;
   }

   //////////////////////////Runnable Interface///////////////////////
   //
   //
   //
   public void run(){
      while(this.monitor){
         try{
            int sTime = (int)Math.round(Math.random()*1000);
            Thread.sleep(sTime);
            if(this.toRemove){
               int i = (int)Math.round(Math.random()*1000);
               if(i > 499 && i < 600){
                  System.out.print("Remove ");
                  System.out.print(Thread.currentThread().getName());
                  System.out.println(" value "+i);
                  Integer integer = this.stack.pop();
                  if(!this.set.remove(integer)){
                     throw new InterruptedException("Not Good");
                  }
               }
            }
         }
         catch(EmptyStackException ese){}
         catch(InterruptedException ie){
            ie.printStackTrace();
            this.monitor = false;
         }
      }
   }
}

//////////////////////////////////////////////////////////////////////
