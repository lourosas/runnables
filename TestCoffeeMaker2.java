//////////////////////////////////////////////////////////////////////
//
//
//
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestCoffeeMaker2{
   public static void main(String [] args){
      new TestCoffeeMaker2();
   }

   public TestCoffeeMaker2(){
      System.out.println("Hello World");
      Maker maker = new Maker();
      maker.addSubscriber(new MakerSubscriber());
      try{
         //Intentionally Overflow the Maker...see what happens...
         maker.brew(100.0);
         Thread.sleep(16000);
         maker.brew(22.3);
         Thread.sleep(10000);
      }
      catch(InterruptedException ie){}
      finally{
         maker.power(false);
      }
   }
}
//////////////////////////////////////////////////////////////////////
