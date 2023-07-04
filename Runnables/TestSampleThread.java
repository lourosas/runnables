import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestSampleThread{
   public static void main(String [] args){
      try{
         new TestSampleThread(args[0]);
      }
      catch(ArrayIndexOutOfBoundsException oob){
         new TestSampleThread("0");
      }
   }

   public TestSampleThread(String number){
      int count = 0;
      try{
         count = Integer.parseInt(number);
      }
      catch(NumberFormatException nfe){
         count = 0;
      }
      finally{
         Thread t = new Thread(new SampleThread(count));
         t.start();
         try{
            System.out.println("Invoking join()");
            t.join(1000);
            System.out.println("Returned from join()");
            Thread.sleep(23000);
            System.out.println("poop");
         }
         catch(InterruptedException ie){
            ie.printStackTrace();
         }
      }
   }
}
