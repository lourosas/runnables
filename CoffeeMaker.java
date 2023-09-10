//////////////////////////////////////////////////////////////////////
//
//
//
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class CoffeeMaker{
   public static void main(String [] args){
      new CoffeeMaker();
   }

   public CoffeeMaker(){
      System.out.println("Hello World");
      CoffeeMakerController controller = new CoffeeMakerController();
      //CoffeeMakerView view =
      //            new CoffeeMakerView("A Coffee Maker",controller);
      CoffeeMakerViewTwo view2 =
                  new CoffeeMakerViewTwo("A Coffee Maker",controller);
      controller.addSubscriber(view2);
   }
}

//////////////////////////////////////////////////////////////////////
