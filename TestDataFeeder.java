//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import rosas.lou.runnables.*;

public class TestDataFeeder{
   //
   //
   //
   public static void main(String[] args){
      new TestDataFeeder();
   }

   //
   //
   //
   public TestDataFeeder(){
      System.out.println("Hello World");
      String file = null;
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter fnef = null;
      fnef = new FileNameExtensionFilter("*.json","json");
      chooser.setFileFilter(fnef);
      int value = chooser.showOpenDialog(null);
      //This is just a test!  Do not do this "at home"
      if(value == 0){
         file = chooser.getSelectedFile().getPath();
      }
      //Construct first, since needs to be injected
      /*
      DataFeeder rdf = RocketDataFeeder.instance();
      try{
         rdf.initialize(file);
         LaunchStateSubstate ss = null;
         ss=new LaunchStateSubstate(
               LaunchStateSubstate.State.INITIALIZE,null,null,null);
         rdf.setStateSubstate(ss);
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         System.exit(0);
      }
      */
      try{
         /*
         DataFeeder tdf = new TankDataFeeder(1,1);
         tdf.initialize(file);
         */
         DataFeeder rdf = RocketDataFeeder.instance();
         rdf.initialize(file);
         LaunchStateSubstate state = null;
         state = new LaunchStateSubstate(
               LaunchStateSubstate.State.INITIALIZE,null,null,null);
         /*
         tdf.setStateSubstate(state);
         DataFeeder pdf = new PipeDataFeeder(1,1,1);
         pdf.initialize(file);
         pdf.setStateSubstate(state);
         DataFeeder pumpdf = new PumpDataFeeder(1,1);
         pumpdf.initialize(file);
         DataFeeder fsf = new FuelSystemDataFeeder(1,1);
         fsf.initialize(file);
         fsf.setStateSubstate(state);
         DataFeeder sdf = new StageDataFeeder(1);
         sdf.initialize(file);
         sdf.setStateSubstate(state);
         */
         rdf.setStateSubstate(state);
         int counter = 0;
         while(counter++ < 5){
            //System.out.println((FuelSystemData)fsf.monitor());
            //System.out.println((StageData)sdf.monitor());
            System.out.println((RocketData)rdf.monitor());
            Thread.sleep(700);
         }
         //Thread.sleep(10000);
         System.exit(0);
      }
      catch(IOException ioe){
         ioe.printStackTrace();
         System.exit(0);
      }
      catch(InterruptedException ie){
         System.exit(0);
      }
   }
}
//////////////////////////////////////////////////////////////////////
