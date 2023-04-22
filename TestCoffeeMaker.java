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
         try{
            maker.brew(100.);
         }
         catch(AlreadyBrewingException ab){
            System.out.println(ab.getMessage());
         }
         Thread.sleep(20550);
         /*
         try{
            maker.brew(10.);
         }
         catch(AlreadyBrewingException ab){
            System.out.println(ab.getMessage());
         }
         */
         maker.pullCarafe();
         Thread.sleep(3200);
         maker.returnCarafe();
         Thread.sleep(4110);
      }
      catch(InterruptedException ie){}
      finally{
         maker.power(false);
      }
   }
}
//////////////////////////////////////////////////////////////////////
