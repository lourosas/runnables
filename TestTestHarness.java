//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestTestHarness{
   public static void main(String[] args){
      new TestTestHarness();
   }

   public TestTestHarness(){
      int threads = 10;
      try{
         long t=new TestHarness().timeTasks(threads,new ARunnable());
         System.out.println(t);
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
   }
}
//////////////////////////////////////////////////////////////////////
