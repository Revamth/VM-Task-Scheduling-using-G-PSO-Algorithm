package FitnessFunction;

import java.util.Arrays;

public class Chromosome {
  // genes: Array where each element represents which data center a task is
  // assigned to
  public int[] genes;
  // fitness: Stores the quality of this solution (makespan time)
  public double fitness;

  public Chromosome(int length) {
    genes = new int[length];
  }

  public Chromosome clone() {
    // Creates a deep copy for preserving good solutions during evolution
    Chromosome clone = new Chromosome(genes.length);
    clone.genes = Arrays.copyOf(this.genes, genes.length);
    clone.fitness = this.fitness;
    return clone;
  }
}
