package FitnessFunction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GAPSO {
  private final Chromosome[] particles = new Chromosome[Constant.POPULATION_SIZE];
  private Chromosome gBest;

  public int[] run() {
    Random rand = new Random();
    List<Double> makespanLog = new ArrayList<>();

    for (int i = 0; i < Constant.POPULATION_SIZE; i++) {
      particles[i] = new Chromosome(Constant.NO_OF_TASKS);
      for (int j = 0; j < Constant.NO_OF_TASKS; j++) {
        particles[i].genes[j] = rand.nextInt(Constant.NO_OF_DATA_CENTERS);
      }
    }

    for (int gen = 0; gen < 100; gen++) {
      for (Chromosome p : particles) {
        p.fitness = SchedulerForGAPso.calculateMakespan(p.genes);
        if (gBest == null || p.fitness < gBest.fitness) {
          gBest = p.clone();
        }
      }
      makespanLog.add(gBest.fitness);

      for (int i = 0; i < Constant.POPULATION_SIZE; i++) {
        Chromosome p1 = particles[rand.nextInt(Constant.POPULATION_SIZE)];
        Chromosome p2 = particles[rand.nextInt(Constant.POPULATION_SIZE)];
        Chromosome child = GeneticOperators.crossover(p1, p2);
        GeneticOperators.mutate(child, Constant.NO_OF_DATA_CENTERS);
        particles[i] = child;
      }
    }

    saveLogToCSV(makespanLog, "gapso_makespan.csv");
    return gBest.genes;
  }

  public void printBestFitness() {
    System.out.println("Best Makespan (Fitness): " + gBest.fitness);
  }

  public double[][] getExecTimeMatrix() {
    return SchedulerForGAPso.getExecTimeMatrix();
  }

  public double[][] getCommunTimeMatrix() {
    return SchedulerForGAPso.getCommunTimeMatrix();
  }

  private void saveLogToCSV(List<Double> log, String filename) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
      writer.write("Iteration,Makespan\n");
      for (int i = 0; i < log.size(); i++) {
        writer.write(i + "," + log.get(i) + "\n");
      }
      System.out.println("Saved GAPSO makespan log to " + filename);
    } catch (IOException e) {
      System.out.println("Error writing GAPSO log: " + e.getMessage());
    }
  }
}