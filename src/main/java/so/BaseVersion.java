package so;

import java.util.Arrays;

public class BaseVersion {
    private final int populationSize;
    private final int maxTimeLimit;
    private final double mutationProbability;
    private final int numThreads;
    private final int[][] distanceMatrix;

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
        long startTime = System.nanoTime();

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

        // Display the results
        System.out.println("Best solution: " + Arrays.toString(globalBestSolution) + "\tDistance of the best solution: " + globalBestDistance + "\n");

        System.out.println("Total execution time: " + (System.nanoTime() - startTime) / 1000000 + "ms");
        System.out.println("Threads used: " + numThreads);
        System.out.println("Population size: " + populationSize);
        System.out.println("Mutation probability: " + mutationProbability * 100 + "%");
        // Accessing time and iteration information
        System.out.println("Total iterations until best path found: " + totalIterations);
        System.out.println("Max time until best path found: " + maxTimeUntilBestFound + "ms");
    }
}
