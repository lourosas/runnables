/////////////////////////////////////////////////////////////////////
/**/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.util.concurrent.locks.*;

/*
*/
public class Bookkeeper implements Runnable{
   private static final double MAX_AMOUNT      = 1000.0;
   private static final int    DELAY           = 10000;
   private int                 currentAccount_ = -1;
   private Bank                bank_           = null;

   /*
   */
   public Bookkeeper(Bank bank, int account){
      this.bank_           = bank;
      this.currentAccount_ = account;
   }

   public void run(){
      Random random = new Random();
      int count     = 0;
      try{
         while(true){
            int toAccount = random.nextInt() % this.bank_.size();
            toAccount = Math.abs(toAccount);
            //System.out.print(Thread.currentThread() + ": ");
            //System.out.println(toAccount);
            double amount = Math.random() * MAX_AMOUNT;
            this.bank_.transfer(this.currentAccount_,
                                toAccount,
                                amount);
            Thread.sleep(Math.abs(random.nextInt() % DELAY));
            ++count;
         }
         //System.out.println("Exiting:  " + Thread.currentThread());
      }
      catch(InterruptedException ie){}
   }
}
/////////////////////////////////////////////////////////////////////
