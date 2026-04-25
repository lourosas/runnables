import java.lang.*;
import java.util.*;

public class ThreadGeeks2 implements Runnable{

   public static void main(String [] args){
      ThreadGeeks2 tg = new ThreadGeeks2();
      Thread t = new Thread(tg);
      t.start();
      try{
         Thread.sleep(2700);
         t.interrupt();
         System.out.println("Main Thread Completes");
      }
      catch(RuntimeException e){
         System.out.println("Exception handled");
      }
      catch(InterruptedException ie){}
   }

   public ThreadGeeks2(){}

   public void run(){
      for(int i = 0; i < 10; ++i){
         try{
            System.out.println(i);
            Thread.sleep(2000);
         }
         catch(InterruptedException ie){
            throw new RuntimeException("Thread Interrupted");
         }
      }
   }
}
