package FitnessFunction;

import net.sourceforge.jswarm_pso.Swarm;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PSO {
    private static SchedulerParticle[] particles;
    private static Scheduler ff = new Scheduler();
    private static Swarm swarm = new Swarm(Constant.POPULATION_SIZE, new SchedulerParticle(), ff);

    public PSO() {
        initParticles();
    }

    public double[] run() {
        swarm.setMinPosition(0);
        swarm.setMaxPosition(Constant.NO_OF_DATA_CENTERS - 1);
        swarm.setMaxMinVelocity(0.5);
        swarm.setParticles(particles);
        swarm.setParticleUpdate(new SchedulerParticleUpdate(new SchedulerParticle()));

        List<Double> makespanLog = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            swarm.evolve();
            double fitness = Scheduler.calculateMakespan(swarm.getBestParticle().getBestPosition());
            makespanLog.add(fitness);
            if (i % 10 == 0) {
                System.out.printf("PSO Iteration %d: Makespan = %f\n", i, fitness);
            }
        }

        saveLogToCSV(makespanLog, "pso_makespan.csv");
        return swarm.getBestPosition();
    }

    public void printBestFitness() {
        double best = Scheduler.calculateMakespan(swarm.getBestParticle().getBestPosition());
        System.out.println("\nThe best fitness value: " + swarm.getBestFitness() + " Best makespan: " + best);
    }

    public double[][] getExecTimeMatrix() {
        return ff.getExecTimeMatrix();
    }

    public double[][] getCommunTimeMatrix() {
        return ff.getCommunTimeMatrix();
    }

    private void initParticles() {
        particles = new SchedulerParticle[Constant.POPULATION_SIZE];
        for (int i = 0; i < Constant.POPULATION_SIZE; ++i) {
            particles[i] = new SchedulerParticle();
        }
    }

    private void saveLogToCSV(List<Double> log, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Iteration,Makespan\n");
            for (int i = 0; i < log.size(); i++) {
                writer.write(i + "," + log.get(i) + "\n");
            }
            System.out.println("Saved PSO makespan log to " + filename);
        } catch (IOException e) {
            System.out.println("Error writing PSO log: " + e.getMessage());
        }
    }
}