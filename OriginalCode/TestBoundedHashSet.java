//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestBoundedHashSet{
   static public void main(String[] args){
      new TestBoundedHashSet();
   }

   public TestBoundedHashSet(){
      System.out.println("Hello World");
      BoundedHashSet<Integer> hs = new BoundedHashSet<Integer>(1);
      BoundedHashSetRunnable hsr = new BoundedHashSetRunnable(hs);
      Thread t0 = new Thread(hsr);
      Thread t1 = new Thread(hsr);
      t0.start();
      t1.start();
   }
}
//////////////////////////////////////////////////////////////////////
