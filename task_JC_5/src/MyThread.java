public class MyThread extends Thread {
  public void run() {
    while(!Thread.currentThread().isInterrupted()) {
      System.out.println("The child thread has not been interrupted yet");
    }
    System.out.println("I was interrupted, goodbye :(");
  }
}
