/////////////////////////////////////////////////////////////////////
/**/
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.util.concurrent.locks.*;

/*
*/
public class Bank{
   private double [] accounts;
   private Lock      bankLock_;
   private Condition sufficientFunds_;

   /*
   */
   public Bank(int num_of_accounts, double initial_balance){
      this.accounts = new double[num_of_accounts];
      Arrays.fill(accounts, initial_balance);
      this.bankLock_        = new ReentrantLock();
      this.sufficientFunds_ = this.bankLock_.newCondition();
   }

   /*
   */
   public void transfer(int from, int to, double amount){
      this.bankLock_.lock();
      try{
         if(from != to){
            while(this.accounts[from] < amount){
               System.out.print("Insufficient Funds ");
               System.out.println(Thread.currentThread());
               System.out.printf("%10.2f, %10.2f  ",
                                 this.accounts[from],
                                 amount);
               System.out.println();
               this.sufficientFunds_.await();
            }
            System.out.println(Thread.currentThread());
            this.accounts[from] -= amount;
            this.accounts[to]   += amount;
            System.out.printf("%10.2f from %d to %d",
                               amount,
                               from,
                               to);
            System.out.printf("%10.2f, %10.2f  ",
                              this.accounts[from],
                              this.accounts[to]);
            System.out.printf(" Total Balance: %10.2f \n",
                              this.getTotalBalance());
         }
      }
      catch(InterruptedException ie){}
      finally{
         this.sufficientFunds_.signalAll();
         this.bankLock_.unlock();
      }
   }

   /*
   */
   public double getTotalBalance(){
      this.bankLock_.lock();
      double sum = 0;
      try{
         for(int i = 0; i < this.size(); i++){
            sum += this.accounts[i];
         }
      }
      catch(Exception e){
         e.printStackTrace();
      }
      finally{
         this.bankLock_.unlock();
         return sum;
      }
   }

   /*
   */
   public int size(){
      return this.accounts.length;
   }
}
/////////////////////////////////////////////////////////////////////
