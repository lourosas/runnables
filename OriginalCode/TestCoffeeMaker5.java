/////////////////////////////////////////////////////////////////////
//
//
//
import java.lang.*;
import java.util.*;
import rosas.lou.runnables.*;

public class TestCoffeeMaker5{
   enum POWER{OFF,ON};
   enum STATE{READY,BREWING};
   enum CSTATE{HOME,PULLED,POURING};
   enum RSTATE{STARTUP,EMPTY,FILLED,WASFILLED};
   public static void main(String [] args){
      new TestCoffeeMaker5();
   }

   public TestCoffeeMaker5(){
      System.out.println("Hello World");
      MakerState makerState = new MakerState(null,null);
      System.out.println(makerState.mask());
      System.out.println(makerState.power());
      System.out.println(makerState.state());
      makerState = new MakerState(""+POWER.OFF,null);
      System.out.println(makerState.mask());
      System.out.println(makerState.power());
      System.out.println(makerState.state());
      makerState = new MakerState(""+POWER.OFF,""+STATE.READY);
      System.out.println(makerState.mask());
      System.out.println(makerState.power());
      System.out.println(makerState.state());
      makerState = new MakerState(null,""+STATE.READY);
      System.out.println(makerState.mask());
      System.out.println(makerState.power());
      System.out.println(makerState.state());
      ContainerState cState = new CarafeState(null,0.,0.);
      System.out.println(cState.mask());
      System.out.println(cState.quantity());
      System.out.println(cState.capacity());
      System.out.println(cState.state());
      cState = new CarafeState(null,-1.,-1.);
      System.out.println(cState.mask());
      System.out.println(cState.quantity());
      System.out.println(cState.capacity());
      System.out.println(cState.state());
      cState = new CarafeState(""+CSTATE.HOME,-1.,-1.);
      System.out.println(cState.mask());
      System.out.println(cState.quantity());
      System.out.println(cState.capacity());
      System.out.println(cState.state());
      cState = new CarafeState(""+CSTATE.HOME,32.,13.);
      System.out.println(cState.mask());
      System.out.println(cState.quantity());
      System.out.println(cState.capacity());
      System.out.println(cState.state());
      cState = new CarafeState(""+CSTATE.POURING,32.,0.);
      System.out.println(cState.mask());
      System.out.println(cState.quantity());
      System.out.println(cState.capacity());
      System.out.println(cState.state());
      cState = new CarafeState(""+CSTATE.PULLED,0.,23.);
      System.out.println(cState.mask());
      System.out.println(cState.quantity());
      System.out.println(cState.capacity());
      System.out.println(cState.state());
      ContainerState rState = new ReservoirState(null,0.,0.);
      System.out.println(rState.mask());
      System.out.println(rState.quantity());
      System.out.println(rState.capacity());
      System.out.println(rState.state());
      rState = new ReservoirState("off",-1.,-3.);
      System.out.println(rState.mask());
      System.out.println(rState.quantity());
      System.out.println(rState.capacity());
      System.out.println(rState.state());
      rState = new ReservoirState(""+RSTATE.STARTUP,-1.,-3.);
      System.out.println(rState.mask());
      System.out.println(rState.quantity());
      System.out.println(rState.capacity());
      System.out.println(rState.state());
      rState = new ReservoirState(""+RSTATE.FILLED,32.,-3.);
      System.out.println(rState.mask());
      System.out.println(rState.quantity());
      System.out.println(rState.capacity());
      System.out.println(rState.state());
      rState = new ReservoirState(""+RSTATE.WASFILLED,32.,23.);
      System.out.println(rState.mask());
      System.out.println(rState.quantity());
      System.out.println(rState.capacity());
      System.out.println(rState.state());
      rState = new ReservoirState(""+RSTATE.EMPTY,0.,0.);
      System.out.println(rState.mask());
      System.out.println(rState.quantity());
      System.out.println(rState.capacity());
      System.out.println(rState.state());
      rState = new ReservoirState(null,32.,23.);
      System.out.println(rState.mask());
      System.out.println(rState.quantity());
      System.out.println(rState.capacity());
      System.out.println(rState.state());
   }
}
/////////////////////////////////////////////////////////////////////
