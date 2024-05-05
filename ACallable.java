//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.util.concurrent.*;
import rosas.lou.runnables.*;

public class ACallable<ProductInfo> implements Callable<ProductInfo>{
   private ProductInfo pi = null;

   /**/
   public ACallable(ProductInfo pi_){ 
      this.pi = pi_;
   }
   ///////////////////////////////////////////////////////////////////
   /**/
   public ProductInfo call() throws Exception{
      System.out.println("call()");
      System.out.println(Thread.currentThread().getName());
      return this.loadType();
   }

   /**/
   private ProductInfo loadType(){
      //Delay here
      //return this.pi.requestProduct();
      return this.pi;
   }
}
//////////////////////////////////////////////////////////////////////
