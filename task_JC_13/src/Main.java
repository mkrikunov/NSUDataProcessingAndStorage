import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

  public static void main(String[] args) {
    int numOfPhilosophers = 5;

    ReentrantLock forks = new ReentrantLock();
    Condition condition = forks.newCondition();

    ArrayList<ReentrantLock> forksList = new ArrayList<>();
    ArrayList<Philosopher> philosophers = new ArrayList<>();

    for (int i = 0; i < numOfPhilosophers; i++) {
      forksList.add(new ReentrantLock());
    }

    for (int i = 1; i <= numOfPhilosophers; i++) {
      philosophers.add(
          new Philosopher(
              i,
              forks,
              forksList.get(i - 1),
              forksList.get(i % numOfPhilosophers),
              condition));
    }

    philosophers.forEach(Thread::start);
  }
}