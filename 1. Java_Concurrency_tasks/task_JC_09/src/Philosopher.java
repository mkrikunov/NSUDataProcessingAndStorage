public class Philosopher extends Thread {

  private int spaghetti_weight = 200;
  private Integer phId;
  private Fork leftFork;
  private Fork rightFork;

  public Philosopher(Integer phId, Fork leftFork, Fork rightFork) {
    this.phId = phId;
    this.leftFork = leftFork;
    this.rightFork = rightFork;
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
    while(Boolean.TRUE) {
      System.out.println("The philosopher " + phId + " began to think");
      thinking();
      System.out.println("The philosopher " + phId + " finished thinking");

      synchronized (leftFork) {
        System.out.println("The philosopher " + phId + " took the left fork");
        synchronized (rightFork) {
          System.out.println("The philosopher " + phId + " took the right fork");
          eating();

          if (spaghetti_weight == 0) {
            System.out.println("The philosopher " + phId + " finished");
            return;
          }

          System.out.println("The philosopher " + phId + " ate");
        }
      }
    }
  }
}
