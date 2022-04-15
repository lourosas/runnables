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
      return null;
   }
}
