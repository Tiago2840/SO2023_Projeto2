package so;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseVersion {
    private final int numberOfThreads;
    private final long maxExecutionTime;
    private final int populationSize;
    private final double mutationProbability;
    private MatrixData matrixData;
    List<AJAlgorithm> algorithms;
    private Path bestPath;


    public BaseVersion(String fileName, int numberOfThreads, long maxExecutionTime, int populationSize, double mutationProbability) {
        this.numberOfThreads = numberOfThreads;
        this.maxExecutionTime = maxExecutionTime;
        this.populationSize = populationSize;
        this.mutationProbability = mutationProbability;

        // Read the matrix data from the file
        try {
            MatrixFileReader fileReader = new MatrixFileReader(fileName);
            this.matrixData = fileReader.readMatrixFromFile();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        initializeAlgorithms();
    }

    private void initializeAlgorithms() {
        algorithms = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            algorithms.add(new AJAlgorithm(populationSize, matrixData.getCitiesNumber(), mutationProbability, matrixData));
        }
    }

    public void runBaseVersion() {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadIndex = i;
            Thread thread = new Thread(() -> runAlgorithm(threadIndex));
            thread.start();
            threads.add(thread);
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Evaluate the population and find the best path
        for (AJAlgorithm algorithm : algorithms) {
            algorithm.evaluatePopulation();
        }

        // Find the best path among all algorithms
        bestPath = findBestPath();
        System.out.println("Central Best Path: " + bestPath);
    }

    private void runAlgorithm(int algorithmIndex) {
        algorithms.get(algorithmIndex).runAlgorithm(maxExecutionTime);
    }

    public Path findBestPath() {
        Path bestPath = algorithms.get(0).getBestPath();
        for (int i = 1; i < numberOfThreads; i++) {
            Path currentPath = algorithms.get(i).getBestPath();
            if (currentPath.compareTo(bestPath) < 0) {
                bestPath = currentPath;
            }
        }
        return bestPath;
    }
}
