package so;

import java.util.Arrays;

public class BaseVersion {
    private final int populationSize;
    private final int maxTimeLimit;
    private final double mutationProbability;
    private final int numThreads;
    private final int[][] distanceMatrix;
    long startTime;
    long totalExecutionTime = 0;
    int totalIterations = 0;


    public BaseVersion(int populationSize, int maxTimeLimit, double mutationProbability, int numThreads, int[][] distanceMatrix) {
        this.populationSize = populationSize;
        this.maxTimeLimit = maxTimeLimit;
        this.mutationProbability = mutationProbability;
        this.numThreads = numThreads;
        this.distanceMatrix = distanceMatrix;
    }

    // Method to execute the algorithm
    public void execute() {
        // Record the start time of the execution
        startTime = System.nanoTime();

        // Reset total iterations
        totalIterations = 0;

        // Create an array of threads to run the algorithm concurrently
        AJEPPThread[] threads = new AJEPPThread[numThreads];

        // Initialize and execute the threads
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new AJEPPThread(AJEPPThread.deepCopy(distanceMatrix), populationSize, maxTimeLimit, mutationProbability);
            threads[i].start();
        }

        // Wait for all threads to finish before proceeding
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
        long maxTimeUntilBestFound = 0;
        int iterationsUntilBestFound = 0;

        // Iterate through threads to find the best solution and gather iteration/time information
        for (AJEPPThread thread : threads) {
            if (globalBestSolution == null || AJEPPThread.isBetterSolution(thread.getBestSolution(), globalBestSolution, distanceMatrix)) {
                globalBestSolution = AJEPPThread.copy(thread.getBestSolution());
                globalBestDistance = thread.getGlobalBestDistance();
                iterationsUntilBestFound = thread.getIterations();
            }

            totalIterations += thread.getIterations();
            maxTimeUntilBestFound = Math.max(maxTimeUntilBestFound, thread.getTimeUntilBestFound());
        }

        totalExecutionTime = (System.nanoTime() - startTime) / 1000000;
        // Display the final results
        System.out.println(displayResults(Arrays.toString(globalBestSolution), globalBestDistance, iterationsUntilBestFound, maxTimeUntilBestFound));
    }

    private String displayResults(String bestSolution, int bestDistance, int iterationsUntilBestFound, long maxTimeUntilBestFound) {
        return "Best solution: " + bestSolution + "\nDistance of the best solution: " + bestDistance + "\n" +
                "\nTotal execution time: " + totalExecutionTime + "ms" +
                "\nThreads used: " + numThreads +
                "\nPopulation size: " + populationSize +
                "\nMutation probability: " + mutationProbability * 100 + "%" +
                "\nIterations until best path found: " + iterationsUntilBestFound +
                "\nMax time until best path found: " + maxTimeUntilBestFound + "ms" +
                "\nTotal number of iterations: " + totalIterations;
    }

    public long getTotalExecutionTime() {
        return totalExecutionTime;
    }

    public int getTotalIterations() {
        return totalIterations;
    }
}
