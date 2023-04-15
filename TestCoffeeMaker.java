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
      maker.brew(10.);
      try{
         Thread.sleep(3000);
         try{
            maker.brew(15);
         }
         catch(AlreadyBrewingException e){
            System.out.println(e.getMessage());
         }
         Thread.sleep(15000);
         try{
            maker.brew(32);
         }
         catch(AlreadyBrewingException e){
            e.getMessage();
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
