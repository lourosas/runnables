import java.lang.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.time.temporal.*;
import java.time.Duration;
import java.time.Instant;

/////////////////////////////////////////////////////////////////////
public class TestInstantTimer implements Runnable{
   private Instant start;

   public static void main(String [] args){
      TestLTimer2 tlt = new TestLTimer2();
      Thread t        = new Thread(tlt);
      t.start();
   }

   public TestLTimer2(){
      System.out.println("Hello World");
   }

   public void run(){
      this.start = Instant.now();
      while(true){
         Instant now = Instant.now();
         try{
            Duration d = Duration.between(this.start, now);
            long totalMillis = d.toMillis();
            int  millis      = (int)totalMillis%1000;
            long totalSecs   = totalMillis/1000;
            int  secs        = (int)totalSecs%60;
            long totalMins   = totalSecs/60;
            int  mins        = (int)totalMins%60;
            long totalHours  = totalMins/60;
            int  hours       = (int)totalHours%24;
            int  days        = (int)totalHours/24;
            System.out.println(d);
            System.out.println("D: " + days);
            System.out.println("H: " + hours);
            System.out.println("M: " + mins);
            System.out.println("S: " + secs);
            System.out.println("MS: "+millis);
            Thread.sleep(1000);
         }
         catch(InterruptedException e){}
      }
   }
}
/////////////////////////////////////////////////////////////////////
