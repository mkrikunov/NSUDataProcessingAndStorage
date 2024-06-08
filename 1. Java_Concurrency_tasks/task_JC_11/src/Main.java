import java.util.concurrent.Semaphore;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Semaphore sem1 = new Semaphore(1);
    Semaphore sem2 = new Semaphore(1);

    MyThread myThread = new MyThread(sem1, sem2);

    sem2.acquire();
    myThread.start();

    for(int i = 0; i < 10; i++) {
      sem1.acquire();
      System.out.println("Thread №1, string№" + (i + 1));
      sem2.release();
    }
  }
}