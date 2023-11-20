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
        myList.sort();
      }
    }, 5000, 5000);

    Scanner scanner = new Scanner(System.in);
    while (Boolean.TRUE) {
      String string = scanner.nextLine();
      if (string.isEmpty()) {
        myList.print();
      } else {
        myList.add(string);
      }
    }
  }
}