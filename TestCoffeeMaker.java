//////////////////////////////////////////////////////////////////////
//
//
//
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestCoffeeMaker{
   public static void main(String [] args){
      new TestCoffeeMaker();
   }

   public TestCoffeeMaker(){
      System.out.println("Hello World");
      Maker maker = new Maker();
      try{
         Thread.sleep(1600);
         maker.brew(100.);
         Thread.sleep(20550);
         maker.brew(10.);
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
