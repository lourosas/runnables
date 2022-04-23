/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class SampleBoundHashSet<T> implements Runnable{
   private BoundedHashSet<T> set;
   private Stack<T> stack;
   {
      set         = null;
   };

   /**/
   public SampleBoundHashSet(BoundedHashSet<T> set_){
      this.set = set_;
      this.stack = new Stack<T>();
   }

   /**/
   public void run(){
      boolean toRun = true;
      while(toRun){
         try{
            Thread.sleep(1000);
            //System.out.println(Thread.currentThread().getName());
            int i = (int)Math.round(Math.random()*1000);
            if(this.set.add(i)){
               this.stack.push(i);
            }
         }
         catch(InterruptedException ie){
            ie.printStackTrace();
            toRun = false;
         }
      }
   }
}
