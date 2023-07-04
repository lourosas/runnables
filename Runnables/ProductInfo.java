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
   private String name    = "A Prodcut";

   public ProductInfo(){
      this.product = Math.random() * 1000;
      this.key = Math.round(product) + Math.round(Math.random()*2000);
   }

   public ProductInfo(double product_, long key_){
      this.product = product_;
      this.key     = key_;
   }

   public void makeTheComputation(){}

   public String toString(){
      String _return = this.product+","+this.key+":"+this.name;
      return _return;
   }

   public ProductInfo requestProduct(){
      return new ProductInfo(this.product,this.key);
   }
}
