//////////////////////////////////////////////////////////////////////
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import rosas.lou.runnables.*;

public class TestLaunchSimulator{
   /**/
   public static void main(String[] args){
     new TestLaunchSimulator();
   }

   /**/
   public TestLaunchSimulator(){
      System.out.println("Hello World");
      //Get the fucking data feeder with a fucking JFileChooser
      //Set up a Rocket
      /*
      String file = null;
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter fnef = null;
      fnef = new FileNameExtensionFilter("*.json","json");
      chooser.setFileFilter(fnef);
      int value = chooser.showOpenDialog(null);
      if(value == 0){
         file = chooser.getSelectedFile().getPath();
         System.out.println(file);
      }
      //If this fucking works, set up the DataFeeder!!!
      DataFeeder df = new GenericSystemDataFeeder();
      try{
         df.initialize(file);
      }
      catch(IOException ioe){
         JOptionPane.showMessageDialog(
               null,
               ioe.getMessage(),
               "Open File Error",
               JOptionPane.ERROR_MESSAGE);
         df = null;
      }
      catch(NullPointerException npe){
         JOptionPane.showMessageDialog(
               null,
               "Running In Non-Simulation Mode",
               "No File To Open",
               JOptionPane.INFORMATION_MESSAGE);
         df = null;
      }
      */
      LaunchSimulatorController lsc = new LaunchSimulatorController();
      LaunchSimulatorView lsv = new LaunchSimulatorView("View",lsc);
      //LaunchSimulator ls = new LaunchSimulatorZero(lsv,lsv);
      //LaunchSystem ls = new LaunchSystemZero(df,lsv,lsv);
      LaunchSystem ls = new LaunchSystemZero(null,lsv,lsv);
      lsc.addClockSubscriber(lsv);
      lsc.addCountdownTimer(lsv);
      lsc.addSubscriber(lsv);
      //lsc.addLaunchSimulator(ls);
      lsc.addLaunchSystem(ls);
      //ls.preLaunchTime(1,0,24);
      //ls.setRocket(new CurrentRocket());
      //Thread t0 = new Thread(ls);
      //t0.start();
      //ls.startSimulator();
      //try{
      //   Thread.sleep(10000);
      //   ls.killSimulation();
      //}
      //catch(InterruptedException ie){}
   }
}

//////////////////////////////////////////////////////////////////////
