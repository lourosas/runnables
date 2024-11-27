//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class NoVisibility{

   public static void main(String[] args){
      new NoVisibility();
   }

   public NoVisibility(){
      int count       = 0;
      ReaderThread rt = new ReaderThread();
      Thread t0       = new Thread(rt);
      t0.start();
      try{
         while(true){
            System.out.println("main()");
            Thread.sleep(1000);
            ++count;
            if(count == 5){
               rt.ready();
            }
         }
      }
      catch(InterruptedException ie){}
   }
}
