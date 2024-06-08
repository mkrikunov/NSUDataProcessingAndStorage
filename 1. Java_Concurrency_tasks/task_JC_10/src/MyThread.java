public class MyThread extends Thread {

  @Override
  public void run() {

    synchronized (Main.obj) {

      while (!Main.flag) {
        try {
          Main.obj.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

    }

    for(int i = 0; i < 10; i++) {
      System.out.println("Thread 2, string " + (i + 1));

      synchronized (Main.obj) {
        Main.flag = !Main.flag;
        Main.obj.notify();

        if (i == 9) return;

        while(!Main.flag) {
          try {
            Main.obj.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

      }

    }


  }

}
