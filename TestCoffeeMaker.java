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
      maker.brewCoffee();
      try{
         Thread.sleep(10000);
      }
      catch(InterruptedException ie){}
      Carafe carafe = maker.getCarafe();
      System.out.println("Carafe:  "+carafe);
   }
}
//////////////////////////////////////////////////////////////////////
