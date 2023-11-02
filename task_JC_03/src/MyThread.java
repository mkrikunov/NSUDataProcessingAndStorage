import java.util.ArrayList;
import java.util.List;

public class MyThread extends Thread {


  private final int num;

  public MyThread(int num) {
    this.num = num;
  }

  private void printList(List<String> list) {
    for(String string : list) {
      System.out.println(string);
    }
  }

  @Override
  public void run() {
    List<String> strings = new ArrayList<>();
    for(int i = 1; i <= 5; i++) {
      strings.add("Thread number " + num + ", string " + i);
    }
    printList(strings);
  }
}
