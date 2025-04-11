package FitnessFunction;

import net.sourceforge.jswarm_pso.FitnessFunction;

public class Scheduler extends FitnessFunction {
    private static double[][] execTimeMatrix, communTimeMatrix;

    public Scheduler() {
        super(false);
        initMatrices();
    }

    @Override
    public double evaluate(double[] position) {
        return calculateMakespan(position);
    }

    public double calcMakespan(double[] position) {
        double makespan = 0;
        double[] dcWorkingTime = new double[Constant.NO_OF_DATA_CENTERS];

        for (int i = 0; i < Constant.NO_OF_TASKS; i++) {
            int dcId = (int) position[i];
            if (dcWorkingTime[dcId] != 0)
                --dcWorkingTime[dcId];

            dcWorkingTime[dcId] += execTimeMatrix[i][dcId] + communTimeMatrix[i][dcId];
            makespan = Math.max(makespan, dcWorkingTime[dcId]);
        }

        // Convert to seconds
        return makespan / 1000.0;
    }

    public static double[][] getExecTimeMatrix() {
        return execTimeMatrix;
    }

    public static double[][] getCommunTimeMatrix() { // ✅ fixed typo
        return communTimeMatrix;
    }

    public static void initMatrices() {
        System.out.println("Initializing input matrices (e.g. exec time & communication time matrices)");
        execTimeMatrix = new double[Constant.NO_OF_TASKS][Constant.NO_OF_DATA_CENTERS];
        communTimeMatrix = new double[Constant.NO_OF_TASKS][Constant.NO_OF_DATA_CENTERS];

        for (int i = 0; i < Constant.NO_OF_TASKS; i++) {
            for (int j = 0; j < Constant.NO_OF_DATA_CENTERS; j++) {
                execTimeMatrix[i][j] = Math.random() * 500;
                communTimeMatrix[i][j] = Math.random() * 500 + 20;
            }
        }
    }
}
