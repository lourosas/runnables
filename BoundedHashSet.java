/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.util.concurrent.*;

public class BoundedHashSet<T>{
   private Set<T>    set;
   private Semaphore sem;

   {
      set = null;
      sem = null;
   };

   /**/
   public BoundedHashSet(int bound){
      this.set = Collections.synchronizedSet(new HashSet<T>());
      this.sem = new Semaphore(bound);
   }

   /**/
   public boolean add(T t){
      boolean wasAdded = false;
      try{
         System.out.println(Thread.currentThread().getName());
         System.out.println("0");
         this.sem.acquire();
         System.out.println("1");
         wasAdded = this.set.add(t);
         System.out.println("2");
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
      finally{
         if(!wasAdded){ this.sem.release(); }
         return wasAdded;
      }
   }

   /**/
   public boolean remove(T t){
      boolean wasRemoved = this.set.remove(t);
      if(wasRemoved){ this.sem.release(); }
      return wasRemoved;
   }
}
