class Data {
   private String packet;
   private boolean transfer = true;

   public synchronized String receive() {
      while (transfer) {
           try {
               wait();
           } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               System.err.println("Thread Interrupted");
           }
      }
      transfer = true;
      notifyAll();
      return packet;
   }

   public synchronized void send(String packet) {
      while (!transfer) {
           try {
               wait();
           } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              System.err.println("Thread Interrupted");
           }
      }
      transfer = false;
      this.packet = packet;
      notifyAll();
   }
}

class Sender implements Runnable {
   private Data data;

   public Sender(Data data) {
      this.data = data;
   }

   public void run() {
      String packets[] = {
         "First packet",
         "Second packet",
         "Third packet",
         "Fourth packet",
         "End"
      };

      for (String packet : packets) {
           data.send(packet);
           System.out.println("Sender sent: " + packet);
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e)  {
              Thread.currentThread().interrupt();
              System.err.println("Thread Interrupted");
           }
      }
   }
}

class Receiver implements Runnable {
   private Data data;

   public Receiver(Data data) {
      this.data = data;
   }

   public void run() {
      for (String receivedMessage = data.receive(); !receivedMessage.equals("End"); receivedMessage = data.receive()) {
           System.out.println("Receiver received: " + receivedMessage);
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              System.err.println("Thread Interrupted");
           }
      }
   }
}

public class Main {
   public static void main(String[] args) {
      Data data = new Data();
      Thread sender = new Thread(new Sender(data));
      Thread receiver = new Thread(new Receiver(data));

      sender.start();
      receiver.start();
   }
}
