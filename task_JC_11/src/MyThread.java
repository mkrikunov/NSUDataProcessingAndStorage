import java.util.concurrent.Semaphore;

public class MyThread extends Thread {

  private final Semaphore sem1;
  private final Semaphore sem2;

  MyThread(Semaphore sem1, Semaphore sem2) {
    this.sem1 = sem1;
    this.sem2 = sem2;
  }

  @Override
  public void run() {
    for(int i = 0; i < 10; i++) {

      try{
        sem2.acquire();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      System.out.println("Thread №2, string№" + (i + 1));

      sem1.release();

    }
  }

}
