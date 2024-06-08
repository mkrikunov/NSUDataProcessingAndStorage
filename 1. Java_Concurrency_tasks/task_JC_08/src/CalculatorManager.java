import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class CalculatorManager {
  private final int threadNum;
  private ArrayList<Future<Double>> calculatorsList;
  private volatile boolean isRunning;
  private final AtomicLong maxIterationCounter;
  private final CyclicBarrier barrier;

  public CalculatorManager(int threadNum) {
    this.threadNum = threadNum;
    this.isRunning = true;
    this.calculatorsList = new ArrayList<>();
    this.maxIterationCounter = new AtomicLong(0);
    this.barrier = new CyclicBarrier(threadNum);
  }

  public void calculateRes() {
    ExecutorService executor = Executors.newFixedThreadPool(threadNum);
    for (int i = 0; i < threadNum; i++) {
      calculatorsList.add(i, executor.submit(new Calculator(threadNum, i)));
    }

    executor.shutdown();
  }

  public Double getRes() throws ExecutionException, InterruptedException {
    Double ans = 0.0;
    isRunning = false;
    for (int i = 0; i <threadNum; i++) {
      ans += calculatorsList.get(i).get();
    }
    return ans;
  }


  private class Calculator implements Callable<Double> {
    private final int threadNum;
    private final int currentThreadNum;
    private long iterationCounter;

    public Calculator(int threadNum, int currentThreadNum) {
      this.threadNum = threadNum;
      this.currentThreadNum = currentThreadNum;
      this.iterationCounter = 0;
    }

    @Override
    public Double call() throws Exception {
      Double ans = 0.0;
      int foldingNum = currentThreadNum;
      int sign;

      while(isRunning) {
        if (foldingNum % 2 == 0) {
          sign = 1;
        } else {
          sign = -1;
        }

        ans += sign * (1 / ((double)foldingNum*2 + 1));
        foldingNum += threadNum;
        iterationCounter++;
      }

      synchronized (maxIterationCounter) {
        if (maxIterationCounter.get() < iterationCounter) {
          maxIterationCounter.set(iterationCounter);
        }
      }

      barrier.await();

      while (iterationCounter <= maxIterationCounter.get()) {
        if (foldingNum % 2 == 0) {
          sign = 1;
        } else {
          sign = -1;
        }

        ans += sign * (1 / ((double)foldingNum*2 + 1));
        foldingNum += threadNum;

        iterationCounter++;
      }

      return ans;
    }
  }
}
