package so;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AJAlgorithm {
    private List<Path> population;
    private final int numberOfCities;
    private final double mutationProbability;
    private final MatrixData matrixData;
    private Path bestPath;

    public AJAlgorithm(int populationSize, int numberOfCities, double mutationProbability, MatrixData matrixData) {
        this.mutationProbability = mutationProbability;
        this.numberOfCities = numberOfCities;
        this.matrixData = matrixData;
        initializePopulation(populationSize);
    }

    private void initializePopulation(int populationSize) {
        population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Path(numberOfCities));
        }
        evaluatePopulation();
    }

    public void evaluatePopulation() {
        for (Path path : population) {
            path.calculateTotalDistance(matrixData);
        }

        Collections.sort(population);

        // Set the best path
        bestPath = population.get(0);
    }

    public Path getBestPath() {
        return bestPath;
    }

    private void pmxCrossover(Path parent1, Path parent2, Path child1, Path child2) {
        int n = numberOfCities;
        int cuttingPoint1 = new Random().nextInt(n);
        int cuttingPoint2 = new Random().nextInt(n);

        while (cuttingPoint1 == cuttingPoint2) {
            cuttingPoint2 = new Random().nextInt(n);
        }

        if (cuttingPoint1 > cuttingPoint2) {
            int swap = cuttingPoint1;
            cuttingPoint1 = cuttingPoint2;
            cuttingPoint2 = swap;
        }

        int[] replacement1 = new int[n + 1];
        int[] replacement2 = new int[n + 1];

        for (int i = cuttingPoint1; i <= cuttingPoint2; i++) {
            child1.getCitiesOrder()[i] = parent2.getCitiesOrder()[i];
            child2.getCitiesOrder()[i] = parent1.getCitiesOrder()[i];
            replacement1[parent2.getCitiesOrder()[i]] = parent1.getCitiesOrder()[i];
            replacement2[parent1.getCitiesOrder()[i]] = parent2.getCitiesOrder()[i];
        }

        // fill in remaining slots with replacements
        for (int i = 0; i < n; i++) {
            if (i < cuttingPoint1 || i > cuttingPoint2) {
                int n1 = parent1.getCitiesOrder()[i];
                int m1 = replacement1[n1];
                int n2 = parent2.getCitiesOrder()[i];
                int m2 = replacement2[n2];
                while (m1 != -1) {
                    n1 = m1;
                    m1 = replacement1[m1];
                }
                while (m2 != -1) {
                    n2 = m2;
                    m2 = replacement2[m2];
                }
                child1.getCitiesOrder()[i] = n1;
                child2.getCitiesOrder()[i] = n2;
            }
        }
    }

    public void runAlgorithm(long maxExecutionTime) {
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < maxExecutionTime * 1000) {
            // Check if the population size is at least 2
            if (population.size() >= 2) {
                // Select two best paths
                Path parent1 = population.get(0);
                Path parent2 = population.get(1);

                // Create two offspring paths
                Path child1 = new Path(numberOfCities);
                Path child2 = new Path(numberOfCities);

                // Perform PMX crossover
                pmxCrossover(parent1, parent2, child1, child2);

                // Apply exchange mutation to each offspring with a certain probability
                if (Math.random() < mutationProbability) {
                    child1.applyExchangeMutation();
                }
                if (Math.random() < mutationProbability) {
                    child2.applyExchangeMutation();
                }

                // Evaluate the offspring
                child1.calculateTotalDistance(matrixData);
                child2.calculateTotalDistance(matrixData);

                // Replace two worst paths with the new offspring
                population.set(population.size() - 2, child1);
                population.set(population.size() - 1, child2);

                // Evaluate the population and sort in each iteration
                evaluatePopulation();
            } else {
                // Handle the case when the population size is less than 2
                System.out.println("Population size must be at least 2 for crossover.");
                break;
            }
        }
    }
}
