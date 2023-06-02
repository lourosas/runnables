//////////////////////////////////////////////////////////////////////
//
//
//
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestCoffeeMaker3{
   public static void main(String [] args){
      new TestCoffeeMaker3();
   }

   public TestCoffeeMaker3(){
      System.out.println("Hello World");
      Maker maker = new Maker();
      CoffeeMakerController controller = new CoffeeMakerController();
      controller.addModel(maker);
      CoffeeMakerView view =
                    new CoffeeMakerView("A Coffee Maker",controller);
      maker.addSubscriber(view);
      controller.addSubscriber(view);
   }
}

//////////////////////////////////////////////////////////////////////
