import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {

  public static void main(String[] args) {

    Semaphore a = new Semaphore(0, true);
    Semaphore b = new Semaphore(0, true);
    Semaphore c = new Semaphore(0, true);
    Semaphore module = new Semaphore(0, true);


    AtomicInteger numA = new AtomicInteger();
    new Thread(() -> {
      while (Boolean.TRUE) {
        try {
          Thread.sleep(1000);
          numA.getAndIncrement();
          System.out.println(numA + " A has been made");
          a.release();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();


    AtomicInteger numB = new AtomicInteger();
    new Thread(() -> {
      while (Boolean.TRUE) {
        try {
          Thread.sleep(2000);
          numB.getAndIncrement();
          System.out.println(numB + " B has been made");
          b.release();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();


    AtomicInteger numModules = new AtomicInteger();
    new Thread(() -> {
      while (Boolean.TRUE) {
        try {
          a.acquire();
          b.acquire();
          numModules.getAndIncrement();
          System.out.println(numModules + " module has been made");
          module.release();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();


    AtomicInteger numC = new AtomicInteger();
    new Thread(() -> {
      while (Boolean.TRUE) {
        try {
          Thread.sleep(3000);
          numC.getAndIncrement();
          System.out.println(numC + " C has been made");
          c.release();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();


    AtomicInteger numScrew = new AtomicInteger();
    new Thread(() -> {
      while (Boolean.TRUE) {
        try {
          c.acquire();
          module.acquire();
          numScrew.getAndIncrement();
          System.out.println(numScrew + " screw has been made");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();

  }
}