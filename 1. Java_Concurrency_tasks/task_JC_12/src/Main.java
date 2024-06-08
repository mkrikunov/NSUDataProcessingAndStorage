import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

  public static void main(String[] args) {
    MyList myList = new MyList();
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        synchronized (myList) {
          myList.sort();
        }
      }
    }, 5000, 5000);

    Scanner scanner = new Scanner(System.in);
    while (Boolean.TRUE) {
      String string = scanner.nextLine();
      if (string.isEmpty()) {
        synchronized (myList) {
          myList.print();
        }
      } else {
        synchronized (myList) {
          myList.add(string);
        }
      }
    }
  }
}