/////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class TestRange implements Runnable{
   private long posPrime;
   private long to;
   private long from;

   {
      posPrime = 0;
      to       = 0;
      from     = 0;
   };
   ///////////////////////Constructor////////////////////////////////
   public TestRange(long argFrom, long argposPrime){
      this.posPrime = argposPrime;
      if(argFrom == 0){
         this.from = 2;
      }
      else{
         this.from = argFrom;
      }
      this.to = argFrom + 99;
   }


   //////////////////////Interface Implementations///////////////////
   /**/
   public void run(){
      for(long i=this.from; i<this.to && i<this.posPrime;++i){
         //try{
            if((this.posPrime % i) == 0){
               System.out.print("factor " + i + " found by Thread: ");
	            System.out.println(Thread.currentThread().getName());
	            //Thread.currentThread().join(1);
               Thread.currentThread().stop();
	         }
	         //Thread.currentThread().yield();
         //}
         //catch(InterruptedException ie){
         //   ie.printStackTrace();
         //}
      }
   }
}
