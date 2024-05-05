//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import rosas.lou.runnables.*;

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
         System.out.println("add(...) Set Size:  "+this.set.size()+" "+Thread.currentThread().getName());
         System.out.println("add(...) starting permits "+this.sem.availablePermits()+" "+Thread.currentThread().getName());
         this.sem.acquire();
         System.out.println("add(...) remaining permits "+this.sem.availablePermits()+" "+Thread.currentThread().getName());
         wasAdded = this.set.add(t);
         System.out.println("add(...) value "+t+" "+Thread.currentThread().getName());
         System.out.println(wasAdded+" "+Thread.currentThread().getName());
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
      if(wasRemoved){
         System.out.println("remove(...) pre-release "+Thread.currentThread().getName());
         this.sem.release();
         System.out.println("remove(...) value "+t+" "+Thread.currentThread().getName());
         //System.out.println("remove(...) Thread Name "+Thread.currentThread().getName());
         System.out.println("remove(...) permits "+this.sem.availablePermits()+" "+Thread.currentThread().getName());
      }
      return wasRemoved;
   }
}
//////////////////////////////////////////////////////////////////////
