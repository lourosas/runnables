//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestLaunchSimulator{
   /**/
   public static void main(String[] args){
     new TestLaunchSimulator();
   }

   /**/
   public TestLaunchSimulator(){
      System.out.println("Hello World");
      //Set up a Rocket
      LaunchSimulatorController lsc = new LaunchSimulatorController();
      LaunchSimulatorView lsv = new LaunchSimulatorView("View",lsc);
      LaunchSimulator ls = new LaunchSimulatorZero(lsv,lsv);
      lsc.addClockSubscriber(lsv);
      lsc.addCountdownTimer(lsv);
      lsc.addSubscriber(lsv);
      lsc.addLaunchSimulator(ls);
      //ls.preLaunchTime(1,0,24);
      //ls.setRocket(new CurrentRocket());
      //Thread t0 = new Thread(ls);
      //t0.start();
      //ls.startSimulator();
      //try{
      //   Thread.sleep(10000);
      //   ls.killSimulation();
      //}
      //catch(InterruptedException ie){}/
   }
}

//////////////////////////////////////////////////////////////////////
