//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.util.*;
import java.lang.*;
import rosas.lou.runnables.Instrument;

public class Station implements Runnable{
   private Instrument thermometer = null;
   private Instrument hydrometer  = null;
   private Instrument barometer   = null;
   private Instrument anemometer  = null;
   private Instrument windvane    = null;

   //Test, remove after testing
   private static Object o = new Object();

   //////////////////////Constructors/////////////////////////////////
   //
   //
   //
   public Station(){
      this.thermometer = new Instrument("Thermometer");
      this.hydrometer  = new Instrument("Hygrometer");
      this.barometer   = new Instrument("Barometer");
      this.anemometer  = new Instrument("Anemometer");
      this.windvane    = new Instrument("Windvane");
      Thread t0        = new Thread(this.thermometer);
      t0.start();
      Thread t1        = new Thread(this.hydrometer);
      t1.start();
      Thread t2        = new Thread(this.barometer);
      t2.start();
      Thread t3        = new Thread(this.anemometer);
      t3.start();
      Thread t4        = new Thread(this.windvane);
      t4.start();
   }

   ///////////////////////Public Methods//////////////////////////////
   //
   //
   //
   public void runAnother(){
      int count = 0;
      try{
         while(count < 100){
            long id = Thread.currentThread().getId();
            System.out.println("\nMain:  " + id);
            Thread.sleep(4000);
            ++count;
         }
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
   }

   //////////////////////Private Methods//////////////////////////////
   //
   //
   //
   private void check(){
      long id = Thread.currentThread().getId();
      System.out.println("\nStation: "+id);
      this.thermometer.measure();
      this.hydrometer.measure();
      this.barometer.measure();
      this.anemometer.measure();
      this.windvane.measure();
      System.out.println("\nStation: "+id);
   }


   //////////////Runnable Interface Implementation////////////////////
   //
   //
   //
   public void run(){
      int count = 0;
      Random rand = new Random();
      int sleep = rand.nextInt(10001);
      try{
         while(count < 10000){
            Thread.sleep(sleep);
            //this.check();
            long id = Thread.currentThread().getId();
            //System.out.println("Thread: " + id);
            this.thermometer.measure();
            this.hydrometer.measure();
            this.barometer.measure();
            this.anemometer.measure();
            this.windvane.measure();
            ++count;
            sleep = rand.nextInt(10001);
         }
      }
      catch(InterruptedException ie){
         ie.printStackTrace();
      }
   }
}
//////////////////////////////////////////////////////////////////////
