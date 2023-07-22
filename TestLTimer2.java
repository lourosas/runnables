//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;
import rosas.lou.clock.*;

public class TestLTimer2{
   public static void main(String [] args){
      new TestLTimer2();
   }

   public TestLTimer2(){
      System.out.println("Hello World");
      LTimer2 ltimer2 = new LTimer2();
      LTimer2Controller controller = new LTimer2Controller(ltimer2);
      LTimer2View view = new LTimer2View("LTimer2", controller);
      controller.addClockSubscriber(view);
   }
}

//////////////////////////////////////////////////////////////////////
