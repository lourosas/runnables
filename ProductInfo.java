//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.util.concurrent.*;
import rosas.lou.runnables.*;

public class ProductInfo{
   private double product = 0.;
   private long   key     = -1;
   private String name    = "A Product";

   public ProductInfo(){
      this.product = Math.random() * 1000;
      this.key=Math.round(product)+Math.round(Math.random()*2000);
      this.makeTheComputation();
   }

   public ProductInfo(double product_, long key_){
      this.product = product_;
      this.key     = key_;
   }

   public void makeTheComputation(){
      System.out.println("ProductInfo.makeTheComputation()");
      for(int i = 0; i < 1000000; ++i){
         for(int j = 0; j < 1000000; ++j){
            for(int k = 0; k < 1000000; ++k){
            //   for(int l = 0; i < 1000000; ++i){;}
               ;
            }
         }
      }
   }

   public String toString(){
      String _return = this.product+", "+this.key+" : "+this.name;
      return _return;
   }

   public ProductInfo requestProduct(){
      return new ProductInfo(this.product,this.key);
   }
}
//////////////////////////////////////////////////////////////////////
