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
      try{
         Thread.sleep(1);
         this.pi = pi_;
      }
      catch(InterruptedException ie){}
   }
   ///////////////////////////////////////////////////////////////////
   /**/
   public ProductInfo call() throws Exception{
      System.out.println("call()");
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
