import java.lang.*;
import java.util.*;

public class ThreadGeeks implements Runnable{

   public static void main(String [] args){
      ThreadGeeks tg = new ThreadGeeks();
      Thread t = new Thread(tg);
      t.start();
      try{
         Thread.sleep(2100);
      }
      catch(InterruptedException e){}
      System.out.println("Main Thread Completes");
      t.interrupt();
   }

   public ThreadGeeks(){}

   public void run(){
      for(int i = 0; i < 5; ++i){
         try{
            //System.out.println(Thread.currentThread().getId());
            System.out.println(i);
            Thread.sleep(2000);
         }
         catch(InterruptedException ie){
            System.out.println(ie.getMessage());
         }
      }
   }
}
