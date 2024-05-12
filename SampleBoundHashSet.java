//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class SampleBoundHashSet implements Runnable{
   private BoundedHashSet<Integer> set;
   private Stack<Integer>          stack;
   private boolean                 toRemove;
   {
      set      = null;
      stack    = null;
      toRemove = false;
   };

   /**/
   public SampleBoundHashSet(BoundedHashSet<Integer> set_){
      this.set   = set_;
      this.stack = new Stack<Integer>();
   }

   /**/
   public void run(){
      boolean toRun = true;
      while(toRun){
         try{
            System.out.println("run() "+Thread.currentThread().getName());
            int i = (int)Math.round(Math.random()*1000);
            //if the value is in the 500s, remove the last value in
            //the set
            if(i > 499 && i < 600){
               try{
                  Integer integer = this.stack.pop();
                  this.set.remove(integer);
               }
               catch(EmptyStackException ese){}
            }
            if(this.set.add(Integer.valueOf(i))){
               this.stack.push(Integer.valueOf(i));
            }
            Thread.sleep(1000);
         }
         catch(InterruptedException ie){
            ie.printStackTrace();
            toRun = false;
         }
      }
   }

   /**/
   public void setRemoveMonitor(boolean monitor){
      this.monitorShouldRemove(monitor);
   }

   /**/
   public void setRemove(boolean remove){
      this.toRemove = remove;
      System.out.println(this.toRemove);
   }

   /**/
   private void monitorShouldRemove(boolean monitor){
      boolean run = monitor;
      while(run){
         try{
            Thread.sleep(500);

            if(this.toRemove){
               int i = (int)Math.round(Math.random()*1000);

               if(i > 499 && i < 600){
                  System.out.print("monitorShouldRemove() ");
                  System.out.print(Thread.currentThread().getName());
                  System.out.println(" value: "+i);
                  Integer integer = this.stack.pop();
                  if(!this.set.remove(integer)){
                     throw new InterruptedException("Not Good");
                  }
               }
            }
         }
         catch(EmptyStackException ese){}
         catch(InterruptedException ie){
            run = false;
         }
      }
   }
}
//////////////////////////////////////////////////////////////////////
