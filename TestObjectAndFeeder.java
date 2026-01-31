//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import rosas.lou.runnables.*;

public class TestObjectAndFeeder{
   //
   //
   //
   public static void main(String[] args){
      new TestObjectAndFeeder();
   }

   //
   //
   //
   public TestObjectAndFeeder(){
      String file = null;
      System.out.println("Hello World");
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter fnef = null;
      fnef = new FileNameExtensionFilter("*.json","JSON");
      chooser.setFileFilter(fnef);
      int value = chooser.showOpenDialog(new JFrame());
      if(value == 0){
         file = chooser.getSelectedFile().getPath();
         System.out.println(file);
      }
      try{
         LaunchStateSubstate.State INIT = null;
         INIT = LaunchStateSubstate.State.INITIALIZE;

         LaunchStateSubstate state = null;
         state = new LaunchStateSubstate(INIT,null,null,null);

         DataFeeder tdf = new TankDataFeeder(1,1);
         tdf.initialize(file);
         tdf.setStateSubstate(state);

         DataFeeder pdf = new PumpDataFeeder(1,1);
         pdf.initialize(file);
         pdf.setStateSubstate(state);

         DataFeeder rdf = RocketDataFeeder.instance();
         rdf.initialize(file);
         rdf.setStateSubstate(state);
         /*
         Tank t = new GenericTank(1,1);
         t.initialize(file);
         t.setStateSubstate(state);
         //t.addDataFeeder(tdf);
         t.addDataFeeder(rdf);

         Pump p = new GenericPump(1,1);
         p.initialize(file);
         p.setStateSubstate(state);
         //p.addDataFeeder(pdf);
         p.addDataFeeder(rdf);

         Pipe pi = new GenericPipe(1,1,1);
         pi.initialize(file);
         pi.setStateSubstate(state);
         pi.addDataFeeder(rdf);

         FuelSystem fs = new GenericFuelSystem(1,1);
         fs.initialize(file);
         fs.addDataFeeder(rdf);
         fs.setStateSubstate(state);

         Engine e = new GenericEngine(0,1);
         e.initialize(file);
         e.addDataFeeder(rdf);
         e.setStateSubstate(state);
         */

         Stage s = new GenericStage(1);
         s.initialize(file);
         s.addDataFeeder(rdf);
         s.setStateSubstate(state);

         int counter = 0;
         while(counter++ < 50){
            System.out.println("\ntock");
            //System.out.println("Monitor: "+t.monitor());
            //System.out.println("Monitor: "+p.monitor());
            //System.out.println("Monitor: "+pi.monitor());
            //System.out.println("Monitor: "+fs.monitor());
            //System.out.println("Monitor: "+e.monitor());
            Thread.sleep(700);
         }
      }
      catch(IOException ioe){ ioe.printStackTrace(); }
      catch(InterruptedException ie){ ie.printStackTrace(); }
      finally{
         System.exit(0);
      }
   }
}
//////////////////////////////////////////////////////////////////////
