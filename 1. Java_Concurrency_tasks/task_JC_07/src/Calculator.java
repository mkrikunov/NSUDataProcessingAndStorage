import java.util.concurrent.Callable;

public class Calculator implements Callable<Double> {
  private final int threadNum;
  private final int iterationNum;
  private final int currentThreadNum;

  public Calculator(int threadNum, int iterationNum, int currentThreadNum) {
    this.threadNum = threadNum;
    this.iterationNum = iterationNum;
    this.currentThreadNum = currentThreadNum;
  }

  @Override
  public Double call() throws Exception {
    Double ans = 0.0;
    int foldingNum = currentThreadNum;
    int sign;

    for (int i = 0; i < iterationNum; i++) {
      if (foldingNum % 2 == 0) {
        sign = 1;
      } else {
        sign = -1;
      }

      ans += sign * (1 / ((double)foldingNum*2 + 1));
      foldingNum += threadNum;
    }

    return ans;
  }
}
