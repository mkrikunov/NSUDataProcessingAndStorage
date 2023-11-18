import java.util.concurrent.ExecutionException;

public class Main {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    int threadNum;
    int iterationNum = 1000;

    try {
      threadNum = Integer.parseInt(args[0]);
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("There are no parameters");
      return;
    } catch (NumberFormatException e) {
      System.out.println("Wrong format");
      return;
    }
пше
    CalculatorManager calculatorManager = new CalculatorManager(threadNum, iterationNum);
    System.out.println(calculatorManager.calculateRes());
    System.out.println(Math.PI/4);
  }
}