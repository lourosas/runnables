//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import java.util.concurrent.*;
import rosas.lou.runnables.*;

public class TestHashSetRemoveAdd{
   /**/
   public static void main(String[] args){
      new TestHashSetRemoveAdd();
   }

   /**/
   public TestHashSetRemoveAdd(){
      System.out.println("Hello World");
      Stack<Integer> stack        = new Stack<Integer>();
      BoundedHashSet<Integer> bhs = new BoundedHashSet<Integer>(1);
      BoundHashSetAdder hsa = new BoundHashSetAdder(bhs, stack);
      BoundHashSetRemover hsr=new BoundHashSetRemover(bhs, stack);
      Thread t0 = new Thread(hsa); t0.start();
      Thread t1 = new Thread(hsr); t1.start();
      Thread t2 = new Thread(hsa); t2.start();
      Thread t3 = new Thread(hsr); t3.start();

      hsa.setRun(true);
      hsr.setMonitor(true);
      try{
         Thread.sleep(1000);
         hsa.setAdd(true);
         hsr.setRemove(true);
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
