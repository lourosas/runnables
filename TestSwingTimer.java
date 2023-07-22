import java.lang.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.time.temporal.*;
import java.time.Duration;
import java.time.Instant;

//////////////////////////////////////////////////////////////////////
public class TestSwingTimer implements ActionListener{
   private Calendar cal = null;
   private long count = 0L;
   private Instant  start;
   private Timer timer = null;

   public static void main(String [] args){
      new TestSwingTimer();
      System.out.println("2: "+Thread.currentThread().getName());
   }

   public TestSwingTimer(){
      System.out.println("1: "+Thread.currentThread().getName());
      cal = Calendar.getInstance();
      cal.setTimeZone(TimeZone.getTimeZone("GMT"));
      System.out.println("Hello World");
      this.setUpTimer();
   }

   public void actionPerformed(ActionEvent e){
       ++count;
      //System.out.println(e.getSource());
      Instant now = Instant.now();
      System.out.println(Duration.between(this.start, now));
      System.out.println(Thread.currentThread().getName());
      /*
      System.out.println("D: "+Duration.between(this.start, now).toDays());
      System.out.println("H: "+Duration.between(this.start, now).toHours());
      System.out.println("M: "+Duration.between(this.start, now).toMinutes());
      System.out.println("TS: "+Duration.between(this.start,now).getSeconds());
      System.out.println("S: "+Duration.between(this.start,now).getSeconds()%60);
      System.out.println(Duration.between(this.start, now).toMillis());
      System.out.println(Duration.between(this.start, now).toMillis()%1000);
      if(count%100 == 0){
         this.timer.restart();
      }
      */
   }

   private void setUpTimer(){
      timer = new Timer(0,this);
      timer.setDelay(1000);
      this.start = Instant.now();
      timer.start();
      try{ Thread.sleep(1000);}
      catch(InterruptedException ie){}
      //timer.stop();
   }
}
//////////////////////////////////////////////////////////////////////
