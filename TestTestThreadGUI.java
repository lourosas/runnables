//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;
import rosas.lou.runnables.*;

public class TestTestThreadGUI{
   public static void main(String [] args){
      new TestTestThreadGUI();
   }

   public TestTestThreadGUI(){
      System.out.println("1: "+Thread.currentThread().getName());
      TestThreadModel model = new TestThreadModel();
      TestThreadGUIController controller
                                 = new TestThreadGUIController(model);
      TestThreadGUI testThreadGUI = new TestThreadGUI(controller);
      System.out.println("2: "+Thread.currentThread().getName());
   }
}
//////////////////////////////////////////////////////////////////////
