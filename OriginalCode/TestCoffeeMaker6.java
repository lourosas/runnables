//////////////////////////////////////////////////////////////////////
//
//
//
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestCoffeeMaker6{
   public static void main(String [] args){
      new TestCoffeeMaker6();
   }

   public TestCoffeeMaker6(){
      System.out.println("Hello World");
      CoffeeMakerControllerV1_3 controller =
                                      new CoffeeMakerControllerV1_3();
      CoffeeMakerViewV1_3 view = 
                  new CoffeeMakerViewV1_3("Coffee Maker", controller);
      controller.addSubscriber(view);
      //MakerV1_2.instance();
   }
}
//////////////////////////////////////////////////////////////////////
