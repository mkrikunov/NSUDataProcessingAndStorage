public class Main {

  public static void main(String[] args) throws InterruptedException {
    MyThread myThread = new MyThread();
    myThread.start();
    Thread.sleep(2000);
    myThread.interrupt();
  }
}