public class Main {
  public static void main(String []args) throws InterruptedException {
    MyThread myThread = new MyThread();
    myThread.start();
    myThread.join();
    for(int i = 0; i < 10; i++) {
      System.out.println("It is a thread 2");
    }
  }
}