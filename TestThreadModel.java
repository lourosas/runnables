//////////////////////////////////////////////////////////////////////
package rosas.lou.runnables;

import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.time.temporal.*;
import java.time.Instant;
import java.time.Duration;

public class TestThreadModel implements ActionListener{
   private boolean run             = false;
   private Instant start           = null;
   private Duration current        = null;
   private javax.swing.Timer timer = null;

   public TestThreadModel(){
      System.out.println("M 1: "+Thread.currentThread().getName());
      this.timer = new javax.swing.Timer(0,this);
      this.timer.setDelay(1000);
      this.current = Duration.ZERO;
      //this.start = Instant.now();
      //this.timer.start();
      System.out.println("M 2: "+Thread.currentThread().getName());
   }

   public void actionPerformed(ActionEvent ae){
      Instant now = Instant.now();
      //Will eventually need to change to
      //Duration.between(this.start, now).plus(this.current);
      //In addition, this will have to indicate when a reset
      //is peformed, as well...
      System.out.println(Duration.between(this.start, now));
      System.out.println(Thread.currentThread().getName());
   }

   public void start(){
      //Going to have to come up with a better way to do this
      //evantually
      this.timer.start();
      //Probably will not need this conditional...will need to think
      //about this a little
      if(this.start == null){
         this.start = Instant.now();
      }
      this.run = true;
   }
}
//////////////////////////////////////////////////////////////////////
