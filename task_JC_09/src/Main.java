import java.util.ArrayList;

public class Main {

  public static void main(String[] args) {

    int numOfPh = 5;

    ArrayList<Fork> forks = new ArrayList<>();
    for (int i = 0; i < numOfPh; i++) {
      forks.add(new Fork(i));
    }

    ArrayList<Philosopher> philosophers = new ArrayList<>();
    philosophers.add(new Philosopher(1, forks.get(0), forks.get(numOfPh - 1)));
    for (int i = 1; i < numOfPh; i++) {
      philosophers.add(new Philosopher((i + 1), forks.get(i - 1), forks.get(i)));
    }

    for(Philosopher philosopher:philosophers) {
      philosopher.start();
    }
  }
}