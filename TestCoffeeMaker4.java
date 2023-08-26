//////////////////////////////////////////////////////////////////////
//
//
//
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestCoffeeMaker4{
   public static void main(String [] args){
      new TestCoffeeMaker4();
   }

   public TestCoffeeMaker4(){
      System.out.println("Hello World");
      CoffeeMakerController controller = new CoffeeMakerController();
      CoffeeMakerViewTwo view =
                 new CoffeeMakerViewTwo("A Coffee Maker",controller);
      controller.addSubscriber(view);
   }
}

//////////////////////////////////////////////////////////////////////
