package FitnessFunction;

import java.util.Random;

public class SchedulerForGAPso {
  public static double[][] execTimeMatrix;
  public static double[][] commTimeMatrix;

  public static void initMatrices() {
    execTimeMatrix = new double[Constant.NO_OF_TASKS][Constant.NO_OF_DATA_CENTERS];
    commTimeMatrix = new double[Constant.NO_OF_TASKS][Constant.NO_OF_DATA_CENTERS];
    Random rand = new Random();

    for (int i = 0; i < Constant.NO_OF_TASKS; i++) {
      for (int j = 0; j < Constant.NO_OF_DATA_CENTERS; j++) {
        execTimeMatrix[i][j] = 10 + rand.nextDouble() * 90;
        commTimeMatrix[i][j] = 5 + rand.nextDouble() * 45;
      }
    }
  }

  public static double calculateMakespan(int[] mapping) {
    double[] vmTime = new double[Constant.NO_OF_DATA_CENTERS];
    for (int i = 0; i < mapping.length; i++) {
      int vm = mapping[i];
      vmTime[vm] += execTimeMatrix[i][vm] + commTimeMatrix[i][vm];
    }

    double makespan = 0;
    for (double t : vmTime)
      makespan = Math.max(makespan, t);
    return makespan;
  }

  public static double[][] getExecTimeMatrix() {
    return execTimeMatrix;
  }

  public static double[][] getCommunTimeMatrix() {
    return commTimeMatrix;
  }
}
