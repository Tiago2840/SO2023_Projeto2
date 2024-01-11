package so;

import java.util.ArrayList;
import java.util.List;

public class AJAlgorithm {
    private List<Path> population;
    private int numberOfCities;
    private double mutationProbability;

    public AJAlgorithm(int populationSize, int numberOfCities, double mutationProbability) {
        this.mutationProbability = mutationProbability;
        this.numberOfCities = numberOfCities;
        initializePopulation(populationSize);
    }

    private void initializePopulation(int populationSize) {
        population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Path(numberOfCities));
        }
    }
}
