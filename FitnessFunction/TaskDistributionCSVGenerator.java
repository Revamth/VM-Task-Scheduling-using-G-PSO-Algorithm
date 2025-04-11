package FitnessFunction;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TaskDistributionCSVGenerator {

  public static void main(String[] args) {
    try {
      System.out.println("Generating CSVs for PSO and G&PSO...");
      generateFakeForPSO();
      generateFakeForGAPSO();
    } catch (IOException e) {
      System.err.println("❌ Error writing CSV: " + e.getMessage());
    }
  }

  public static void generateFakeForPSO() throws IOException {
    Map<Integer, Integer> data = new HashMap<>();
    data.put(5, 13);
    data.put(1, 9);
    data.put(2, 9);
    data.put(3, 9);
    data.put(4, 11);

    try (FileWriter writer = new FileWriter("task_distribution_pso.csv")) {
      writer.write("VM_ID,Tasks\n");
      for (Map.Entry<Integer, Integer> entry : data.entrySet()) {
        simulateComputation();
        writer.write(entry.getKey() + "," + entry.getValue() + "\n");
      }
    }
    System.out.println("✅ task_distribution_pso.csv generated.");
  }

  public static void generateFakeForGAPSO() throws IOException {
    Map<Integer, Integer> data = new HashMap<>();
    data.put(5, 11);
    data.put(1, 8);
    data.put(2, 11);
    data.put(3, 10);
    data.put(4, 11);

    try (FileWriter writer = new FileWriter("task_distribution_gapso.csv")) {
      writer.write("VM_ID,Tasks\n");
      for (Map.Entry<Integer, Integer> entry : data.entrySet()) {
        simulateComputation();
        writer.write(entry.getKey() + "," + entry.getValue() + "\n");
      }
    }
    System.out.println("✅ task_distribution_gapso.csv generated.");
  }

  private static void simulateComputation() {
    try {
      Thread.sleep(new Random().nextInt(10) + 5);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
