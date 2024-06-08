public class Main {

  final static Object obj = new Object();
  static Boolean flag = Boolean.FALSE;

  public static void main(String[] args) {
    
    MyThread thread = new MyThread();
    thread.start();
    for (int i = 0; i < 10; i++) {
      System.out.println("Thread 1, string " + (i + 1));

      synchronized (obj) {
        flag = !flag;
        obj.notify();

        while(flag) {
          try {
            obj.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

      }

    }
  }

}