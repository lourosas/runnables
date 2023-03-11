//////////////////////////////////////////////////////////////////////
/*
 * */
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Instrument implements Runnable{
   private int count = 0;
   private int currentMeasure = -1;
   private String _name = null;
   private static Object o = new Object();
   //try static, first
   //private Lock lock = new ReentrantLock();
   private static Lock lock = new ReentrantLock();

   //////////////////////Constructors/////////////////////////////////
   /*
    * */
   public Instrument(String name){
      this._name = name;
   }

   /////////////////////Public Methods////////////////////////////////
   /*
    * */
   public String name(){
      return this._name;
   }

   //public synchronized void measure(){
   public void measure(){
      lock.lock();
      long id = Thread.currentThread().getId();
      //System.out.println("\n"+this.name()+":  "+id);
      //synchronized(o){
      System.out.print("\n"+this.name()+", ");
      System.out.println("id:  "+id+" Count:  "+count);
      System.out.println(this.getMeasure());
      ++count;
      lock.unlock();
      //}
   }


   /////////////////////Protected Methods/////////////////////////////
   /////////////////////Private Methods///////////////////////////////
   //
   //
   //
   private int getMeasure(){
      long id = Thread.currentThread().getId();
      return this.currentMeasure;
   }

   //
   //
   //
   private void setMeasure(){
      Random rand = new Random();
      long id = Thread.currentThread().getId();
      lock.lock();
      this.currentMeasure = rand.nextInt(90000);
      lock.unlock();
   }
   ///////////Runnable Implementation Methods/////////////////////////
   //
   //
   //
   public void run(){
      Random rand = new Random();
      int sleep    = rand.nextInt(1500);
      int runCount = 0;
      try{
         while(runCount < 10000){
            this.setMeasure();
            Thread.sleep(sleep);
            ++runCount;
         }
      }
      catch(InterruptedException ie){}
   }
}
//////////////////////////////////////////////////////////////////////
