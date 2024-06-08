import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CalculatorManager {
  private final int threadNum;
  private final int iterationNum;

  public CalculatorManager(int threadNum, int iterationNum) {
    this.threadNum = threadNum;
    this.iterationNum = iterationNum;
  }

  public Double calculateRes() throws ExecutionException, InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(threadNum);
    ArrayList<Future<Double>> calculatorsList = new ArrayList<>();

    for (int i = 0; i < threadNum; i++) {
      calculatorsList.add(i, executor.submit(new Calculator(threadNum, iterationNum, i)));
    }

    executor.shutdown();

    Double ans = 0.0;
    for (int i = 0; i <threadNum; i++) {
      ans += calculatorsList.get(i).get();
    }

    return ans;
  }
}
