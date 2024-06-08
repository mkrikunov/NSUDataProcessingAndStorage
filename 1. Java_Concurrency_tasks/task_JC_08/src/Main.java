import java.util.concurrent.ExecutionException;

public class Main {

  public static void main(String[] args) {
    int threadNum;
    try {
      threadNum = Integer.parseInt(args[0]);
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("There are no parameters");
      return;
    } catch (NumberFormatException e) {
      System.out.println("Wrong format");
      return;
    }

    CalculatorManager calculatorManager = new CalculatorManager(threadNum);
    Thread endThread = new Thread(() -> {
      try {
        System.out.println(calculatorManager.getRes());
        System.out.println(Math.PI/4);
      } catch (ExecutionException | InterruptedException e) {
        e.printStackTrace();
      }
    });

    Runtime.getRuntime().addShutdownHook(endThread);
    calculatorManager.calculateRes();
  }
}