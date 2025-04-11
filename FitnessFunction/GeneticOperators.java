package FitnessFunction;

import java.util.Random;

public class GeneticOperators {
  private static final Random rand = new Random();

  public static Chromosome crossover(Chromosome p1, Chromosome p2) {
    Chromosome child = new Chromosome(p1.genes.length);
    for (int i = 0; i < p1.genes.length; i++) {
      child.genes[i] = rand.nextBoolean() ? p1.genes[i] : p2.genes[i];
    }
    return child;
  }

  public static void mutate(Chromosome chrom, int numVMs) {
    int i = rand.nextInt(chrom.genes.length);
    chrom.genes[i] = rand.nextInt(numVMs);
  }
}
