package so;

import java.util.Arrays;

public class AdvancedVersion {
    private final int populationSize;
    private final int maxTimeLimit;
    private final double mutationProbability;
    private final int numThreads;
    private final int[][] distanceMatrix;
    private final double updatePercentage;
    long startTime;

    // Constructor to initialize the parameters
    public AdvancedVersion(int populationSize, int maxTimeLimit, double mutationProbability, int numThreads, int[][] distanceMatrix, double updatePercentage) {
        this.populationSize = populationSize;
        this.maxTimeLimit = maxTimeLimit;
        this.mutationProbability = mutationProbability;
        this.numThreads = numThreads;
        this.distanceMatrix = distanceMatrix;
        this.updatePercentage = updatePercentage;
    }

    // Method to execute the advanced algorithm
    public void execute() {
        startTime = System.nanoTime();
        long lastUpdateTime = startTime;
        AJEPPThread[] threads = new AJEPPThread[numThreads];

        // Initialize and execute the threads
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new AJEPPThread(AJEPPThread.deepCopy(distanceMatrix), populationSize, maxTimeLimit, mutationProbability);
            threads[i].start();
        }

        // Wait for all threads to finish
        try {
            for (AJEPPThread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: ");
            e.printStackTrace();
        }

        // Combine the results of the threads and choose the best global solution
        int[] globalBestSolution = null;
        int globalBestDistance = Integer.MAX_VALUE;
        int totalIterations = 0;
        long maxTimeUntilBestFound = 0;

        // Iterate through threads to find the best solution and gather iteration/time information
        for (AJEPPThread thread : threads) {
            if (globalBestSolution == null || AJEPPThread.isBetterSolution(thread.getBestSolution(), globalBestSolution, distanceMatrix)) {
                globalBestSolution = AJEPPThread.copy(thread.getBestSolution());
                globalBestDistance = thread.getGlobalBestDistance();
            }

            totalIterations += thread.getIterations();
            maxTimeUntilBestFound = Math.max(maxTimeUntilBestFound, thread.getTimeUntilBestFound());
        }

        // Update populations in multiples of the percentage of total time
        while (System.nanoTime() - startTime < maxTimeLimit * 1000000000L) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastUpdateTime;

            if (elapsedTime >= (updatePercentage * maxTimeLimit * 1000000000L)) {
                // Combine populations of all threads into a single population
                int[][] combinedPopulation = combinePopulations(threads);

                // Sort the combined population and choose the best paths
                int[][] newPopulation = selectBestPaths(combinedPopulation);

                // Update all threads with the new population
                updateThreadsPopulation(threads, newPopulation);

                // Update the time of the last update operation
                lastUpdateTime = currentTime;
            }
        }

        // Display the final results
        System.out.println(displayResults(globalBestSolution, globalBestDistance, System.nanoTime(), totalIterations, maxTimeUntilBestFound));
    }

    // Method to display the final results as a formatted string
    private String displayResults(int[] bestSolution, int bestDistance, long currentTime, int totalIterations, long maxTimeUntilBestFound) {
        return "Best solution: " + Arrays.toString(bestSolution) + "\tDistance of the best solution: " + bestDistance + "\n" +
                "\nTotal execution time: " + (currentTime - startTime) / 1000000 + "ms" +
                "\nThreads used: " + numThreads +
                "\nPopulation size: " + populationSize +
                "\nMutation probability: " + mutationProbability * 100 + "%" +
                "\nTotal iterations until best path found: " + totalIterations +
                "\nMax time until best path found: " + maxTimeUntilBestFound + "ms";
    }

    // Method to combine populations from all threads into a single population
    private int[][] combinePopulations(AJEPPThread[] threads) {
        int totalPopulationSize = populationSize * numThreads;
        int[][] combinedPopulation = new int[totalPopulationSize][];
        int currentIndex = 0;

        for (AJEPPThread thread : threads) {
            for (int[] solution : thread.getPopulation()) {
                combinedPopulation[currentIndex++] = Arrays.copyOf(solution, solution.length);
            }
        }

        return combinedPopulation;
    }

    // Method to select the best paths from a combined population
    private int[][] selectBestPaths(int[][] population) {
        // Sort the combined population
        Arrays.sort(population, (solution1, solution2) -> {
            int distance1 = AJEPPThread.calculateTotalDistance(solution1, distanceMatrix);
            int distance2 = AJEPPThread.calculateTotalDistance(solution2, distanceMatrix);
            return Integer.compare(distance1, distance2);
        });

        // Choose the best paths (populationSize best)
        int[][] newPopulation = new int[populationSize][];
        for (int i = 0; i < populationSize; i++) {
            newPopulation[i] = Arrays.copyOf(population[i], population[i].length);
        }

        return newPopulation;
    }

    // Method to update all threads with a new population
    private void updateThreadsPopulation(AJEPPThread[] threads, int[][] newPopulation) {
        for (AJEPPThread thread : threads) {
            thread.setPopulation(AJEPPThread.deepCopy(newPopulation));
        }
    }
}
