/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.util.concurrent.*;
import rosas.lou.runnables.*;

public class Preloader{
   private final FutureTask<ProductInfo> future =
      new FutureTask<ProductInfo>(
                       new ACallable<ProductInfo>(new ProductInfo()));
   private final Thread thread = new Thread(future);

   public Preloader(){}

   public void start(){ this.thread.start(); }

   public ProductInfo get() throws Exception{
      try{
         return this.future.get();
      }
      catch(Exception e){
         throw this.launderThrowable(e.getCause());
      }
   }

   private Exception launderThrowable(Throwable t){
      if(t instanceof RuntimeException){
         return (RuntimeException)t;
      }
      else if(t instanceof Error){
         System.out.println("Error Type Throwable");
         return new Exception(t);
      }
      else{
         return new IllegalStateException("What just happened?!");
      }
   }
}
