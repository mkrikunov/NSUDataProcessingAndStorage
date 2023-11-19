import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher extends Thread {
  private int spaghetti_weight = 200;

  private final int id;
  private final ReentrantLock leftFork;
  private final ReentrantLock rightFork;
  private final ReentrantLock forks;
  private final Condition condition;

  public Philosopher(
      int id,
      ReentrantLock forks,
      ReentrantLock leftFork,
      ReentrantLock rightFork,
      Condition condition) {
    this.id = id;
    this.leftFork = leftFork;
    this.rightFork = rightFork;
    this.forks = forks;
    this.condition = condition;
  }

  private void thinking() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void eating() {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    spaghetti_weight -= 50;
  }

  @Override
  public void run() {
    while (Boolean.TRUE) {

      System.out.println("The philosopher " + id + " starts taking forks");
      forks.lock();

      try {
        while (leftFork.isLocked() || rightFork.isLocked()) {
          condition.await();
        }
        leftFork.lock();
        rightFork.lock();
        System.out.println("The philosopher " + id + " took the forks");
      } catch (InterruptedException | RuntimeException e) {
        e.printStackTrace();
      } finally {
        forks.unlock();
      }

      eating();
      System.out.println("The philosopher " + id + " ate");

      forks.lock();
      try {
        leftFork.unlock();
        rightFork.unlock();
        condition.signalAll();
      } finally {
        forks.unlock();
      }

      thinking();

      if (spaghetti_weight == 0) {
        System.out.println("The philosopher " + id + " finished");
        return;
      }
    }
  }
}
