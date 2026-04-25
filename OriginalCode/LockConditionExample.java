import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class SharedResource {
   private final Lock lock = new ReentrantLock();
   private final Condition condition = lock.newCondition();
   private int data;
   private boolean dataReady = false;


   public void produceData(int newData) throws InterruptedException {
      lock.lock();
      try {
           while (dataReady) {
               condition.await(); // Wait if data is not consumed yet
           }
           this.data = newData;
           this.dataReady = true;
           System.out.println("Produced: " + data);
           condition.signal(); // Signal consumer that data is ready
      } finally {
           lock.unlock();
      }
   }


   public int consumeData() throws InterruptedException {
      lock.lock();
      try {
           while (!dataReady) {
               condition.await(); // Wait if data is not ready
           }
           this.dataReady = false;
           System.out.println("Consumed: " + data);
           condition.signal(); // Signal producer that data is consumed
           return data;
      } finally {
           lock.unlock();
      }
   }
}


public class ConditionExample {
   public static void main(String[] args) {
      SharedResource sharedResource = new SharedResource();


      Thread producerThread = new Thread(() -> {
           try {
               for (int i = 1; i <= 5; i++) {
                   sharedResource.produceData(i);
                   Thread.sleep(100); // Simulate work
               }
           } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
           }
      });


      Thread consumerThread = new Thread(() -> {
           try {
               for (int i = 1; i <= 5; i++) {
                   sharedResource.consumeData();
                   Thread.sleep(100); // Simulate work
               }
           } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
           }
      });


      producerThread.start();
      consumerThread.start();
   }
}
