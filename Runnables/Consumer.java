/////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;

public class Consumer implements Runnable{
   Producer whoAmITalkingTo = null;

   ///////////////////////Constructors///////////////////////////////
   public Consumer(Producer who){
      this.whoAmITalkingTo = who;
   }

   ///////////////Implementation of the Runnable Interface///////////
   public void run(){
      Random random  = new Random();
      while(true){
         System.out.println("A--"+Thread.currentThread().getName());
         //String result = this.whoAmITalkingTo.consume();
         //System.out.println("Consumed:  " + result);
         this.whoAmITalkingTo.consume();
         int randomtime = random.nextInt() % 10000;
         try{
            Thread.sleep(randomtime);
         }
         catch(InterruptedException ie){}
         catch(IllegalArgumentException iae){}
      }
   }
}
