/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class SampleBoundHashSet implements Runnable{
   private BoundedHashSet<Integer> set;
   private Stack<Integer>          stack;
   {
      set   = null;
      stack = null;
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
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName());
            int i = (int)Math.round(Math.random()*1000);
            if(this.set.add(Integer.valueOf(i))){
               this.stack.push(Integer.valueOf(i));
            }
         }
         catch(InterruptedException ie){
            ie.printStackTrace();
            toRun = false;
         }
      }
   }
}
