package so;

import java.util.*;
import java.util.concurrent.*;
import java.util.Random;

public class OriginalVersion {
    private final int populationSize;
    private final int maxTimeLimit;
    private final double mutationProbability;
    private final int numThreads;
    private final int[][] distanceMatrix;
    long startTime;
    private int totalIterations = 0;
    long totalExecutionTime;
    private int[] globalBestSolution;
    private int globalBestDistance = Integer.MAX_VALUE;
    private long maxTimeUntilBestFound;

    private Random random = new Random();


    public OriginalVersion(int populationSize, int maxTimeLimit, double mutationProbability, int numThreads, int[][] distanceMatrix) {
        this.populationSize = populationSize;
        this.maxTimeLimit = maxTimeLimit;
        this.mutationProbability = mutationProbability;
        this.numThreads = numThreads;
        this.distanceMatrix = distanceMatrix;
    }

    public void execute() {
        startTime = System.nanoTime();
        long endTime = startTime + (maxTimeLimit * 1000000000L);

        // Generate initial population using Nearest Neighbor heuristic
        int[][] initialPopulation = generateInitialPopulation();

        // Perform genetic algorithm on the initial population
        int[][] finalPopulation = initialPopulation; // Initialize with the initial population
        int iterationsUntilBestFound = 0;

        // Initialize ExecutorService with a fixed number of threads
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        while (System.nanoTime() < endTime) {
            List<Future<int[][]>> futures = new ArrayList<>();

            // Divide the population into subsets for parallel processing
            int[][][] populationSubsets = dividePopulation(finalPopulation, numThreads);

            // Submit tasks to the executor
            for (int i = 0; i < numThreads; i++) {
                int[][] subset = populationSubsets[i];
                Callable<int[][]> task = () -> performGeneticAlgorithm(subset);
                futures.add(executorService.submit(task));
            }

            // List to store the results of each task
            List<int[][]> results = new ArrayList<>();

            // Retrieve the results from completed tasks
            for (Future<int[][]> future : futures) {
                try {
                    results.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            // Combine the results from all tasks
            finalPopulation = combineResults(results);

            iterationsUntilBestFound++;

            // Update global best solution if needed
            updateGlobalBest(finalPopulation);

        }

        // Shut down the executor
        executorService.shutdown();

        totalExecutionTime = (System.nanoTime() - startTime) / 1000000;

        // Display the final results
        System.out.println(displayImprovedResults(globalBestSolution, globalBestDistance, iterationsUntilBestFound, System.nanoTime(), maxTimeUntilBestFound));
    }


    private int[][] generateInitialPopulation() {
        int[][] population = new int[populationSize][];
        for (int i = 0; i < populationSize; i++) {
            population[i] = generateIndividual();
        }
        return population;
    }

    private int[] generateIndividual() {
        int size = distanceMatrix.length;
        int[] individual = new int[size];
        boolean[] visited = new boolean[size];
        int currentCity = ThreadLocalRandom.current().nextInt(size) + 1; // Start from a random city

        for (int i = 0; i < size; i++) {
            individual[i] = currentCity;
            visited[currentCity - 1] = true;

            int nextCity = findNearestNeighbor(currentCity, visited);
            currentCity = nextCity;
        }

        return individual;
    }

    private int findNearestNeighbor(int currentCity, boolean[] visited) {
        int size = distanceMatrix.length;
        int nearestNeighbor = -1;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < size; i++) {
            if (!visited[i] && distanceMatrix[currentCity - 1][i] < minDistance) {
                minDistance = distanceMatrix[currentCity - 1][i];
                nearestNeighbor = i + 1;
            }
        }

        return nearestNeighbor;
    }


    private int getRandomCrossoverPoint(int arrayLength) {
        int crossoverPoint1 = random.nextInt(arrayLength);
        int crossoverPoint2;
        do {
            crossoverPoint2 = random.nextInt(arrayLength);
        } while (crossoverPoint1 == crossoverPoint2);
        return crossoverPoint1;
    }


    private int[][] performGeneticAlgorithm(int[][] initialPopulation) {
        int[][] currentPopulation = initialPopulation;
        int[][] offspring = new int[currentPopulation.length][];

        // Check for null arrays in initialPopulation
        for (int i = 0; i < currentPopulation.length; i++) {
            if (currentPopulation[i] == null) {
                // Handle null array, e.g., by creating a new individual
                currentPopulation[i] = generateIndividual();
            }
        }

        // Iterate over pairs of individuals for crossover
        for (int i = 0; i < currentPopulation.length; i += 2) {
            int[] parent1 = currentPopulation[i];
            int[] parent2 = currentPopulation[i + 1];

            // Check for null arrays in parent1 and parent2
            if (parent1 == null) {
                parent1 = generateIndividual();
            }
            if (parent2 == null) {
                parent2 = generateIndividual();
            }

            // Determine a random crossover point
            int crossoverPoint = getRandomCrossoverPoint(parent1.length);

            // Perform crossover for the pair of individuals
            int[] child1 = performCrossover(parent1, parent2, crossoverPoint);
            int[] child2 = performCrossover(parent2, parent1, crossoverPoint);

            // Store the children in the offspring array
            offspring[i] = child1;
            offspring[i + 1] = child2;
        }

        // Perform mutation on the offspring
        performMutation(offspring);

        // Combine the current population and offspring
        int[][] combinedPopulation = combinePopulations(currentPopulation, offspring);

        totalIterations++; // Increment the totalIterations counter

        // Select the best paths from the combined population
        return selectBestPaths(combinedPopulation);
    }


    private int[] performCrossover(int[] parent1, int[] parent2, int crossoverPoint) {
        int[] child = new int[parent1.length];
        Set<Integer> visitedCities = new HashSet<>();

        // Copy the first part from parent1 to child
        for (int i = 0; i <= crossoverPoint; i++) {
            child[i] = parent1[i];
            visitedCities.add(parent1[i]);
        }

        // Copy the remaining unique cities from parent2 to child
        int index = crossoverPoint + 1;
        for (int city : parent2) {
            if (!visitedCities.contains(city)) {
                child[index++] = city;
                visitedCities.add(city);
            }
        }

        return child;
    }

    private int[][] crossover(int[] parent1, int[] parent2) {
        // Check if parents have valid lengths for crossover
        if (parent1.length < 2 || parent2.length < 2) {
            return new int[][]{parent1.clone(), parent2.clone()};
        }


        // For simplicity, let's use a basic one-point crossover as a placeholder
        int maxCrossoverPoint = Math.min(parent1.length, parent2.length); // Ensure crossover point does not exceed array length
        int crossoverPoint = ThreadLocalRandom.current().nextInt(1, maxCrossoverPoint);


        int[] child1 = crossoverOnePoint(parent1, parent2, crossoverPoint);
        int[] child2 = crossoverOnePoint(parent2, parent1, crossoverPoint);


        return new int[][]{child1, child2};
    }

    private int[] crossoverOnePoint(int[] parent1, int[] parent2, int crossoverPoint) {
        int[] child = new int[parent1.length];


        // Check for valid crossover point
        if (crossoverPoint < 0 || crossoverPoint >= parent1.length) {
            return child;
        }

        System.arraycopy(parent1, 0, child, 0, crossoverPoint);

        if (crossoverPoint < parent2.length) {
            System.arraycopy(parent2, crossoverPoint, child, crossoverPoint, parent2.length - crossoverPoint);
        }


        return child;
    }


    private boolean contains(int[] array, int element) {
        for (int value : array) {
            if (value == element) {
                return true;
            }
        }
        return false;
    }

    private void performMutation(int[][] population) {
        for (int i = 0; i < population.length; i++) {
            if (Math.random() < mutationProbability) {
                int mutationPoint1 = ThreadLocalRandom.current().nextInt(population[i].length);
                int mutationPoint2;
                do {
                    mutationPoint2 = ThreadLocalRandom.current().nextInt(population[i].length);
                } while (mutationPoint1 == mutationPoint2);

                // Swap two elements to perform mutation
                int temp = population[i][mutationPoint1];
                population[i][mutationPoint1] = population[i][mutationPoint2];
                population[i][mutationPoint2] = temp;
            }
        }

    }

    private int[][] combinePopulations(int[][] population1, int[][] population2) {
        int[][] combinedPopulation = new int[population1.length + population2.length][];
        System.arraycopy(population1, 0, combinedPopulation, 0, population1.length);
        System.arraycopy(population2, 0, combinedPopulation, population1.length, population2.length);
        return combinedPopulation;
    }

    private int[][] selectBestPaths(int[][] population) {
        Arrays.sort(population, Comparator.nullsFirst((solution1, solution2) -> {
            if (solution1 == null || solution2 == null) {
                // Handle null solutions
                return Integer.compare(solution1 == null ? 1 : 0, solution2 == null ? 1 : 0);
            }

            int distance1 = calculateTotalDistance(solution1);
            int distance2 = calculateTotalDistance(solution2);

            return Integer.compare(distance1, distance2);
        }));

        return Arrays.copyOfRange(population, 0, populationSize);
    }


    private int calculateTotalDistance(int[] solution) {
        try {
            if (solution == null) {
                // Handle null solution array (return a default or special value)
                return Integer.MAX_VALUE; // For example, you can return a large value
            }

            // Print the content of the solution array for debugging

            int distance = 0;

            // Check city indices range before accessing distanceMatrix
            for (int cityIndex : solution) {
                assert cityIndex >= 1 && cityIndex <= distanceMatrix.length : "Invalid city index: " + cityIndex;
            }

            for (int i = 0; i < solution.length - 1; i++) {
                int city1 = solution[i] - 1;
                int city2 = solution[i + 1] - 1;

                // Check bounds before accessing distanceMatrix
                if (city1 < 0 || city1 >= distanceMatrix.length || city2 < 0 || city2 >= distanceMatrix.length) {
                    throw new ArrayIndexOutOfBoundsException("Invalid city indices in solution array.");
                }

                distance += distanceMatrix[city1][city2];
            }

            // Check bounds before accessing distanceMatrix
            int lastCityIndex = solution[solution.length - 1] - 1;
            if (lastCityIndex < 0 || lastCityIndex >= distanceMatrix.length || solution[0] - 1 < 0 || solution[0] - 1 >= distanceMatrix.length) {
                throw new ArrayIndexOutOfBoundsException("Invalid city indices in solution array.");
            }
            distance += distanceMatrix[lastCityIndex][solution[0] - 1]; // Return to the starting city

            return distance;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    private int[] applyLocalSearch(int[] solution) {
        int[] improvedSolution = Arrays.copyOf(solution, solution.length);
        boolean improved;

        do {
            improved = false;

            for (int i = 1; i < improvedSolution.length - 2; i++) {
                for (int j = i + 1; j < improvedSolution.length - 1; j++) {
                    int[] reversedSegment = reverseSegment(improvedSolution, i, j);

                    int originalDistance = calculateTotalDistance(improvedSolution);
                    int reversedDistance = calculateTotalDistance(reversedSegment);

                    if (reversedDistance < originalDistance) {
                        System.arraycopy(reversedSegment, 0, improvedSolution, i, j - i + 1);
                        improved = true;
                    }
                }
            }

        } while (improved);

        return improvedSolution;
    }

    private int[] reverseSegment(int[] solution, int start, int end) {
        int[] reversedSegment = Arrays.copyOf(solution, solution.length);
        while (start < end) {
            int temp = reversedSegment[start];
            reversedSegment[start] = reversedSegment[end];
            reversedSegment[end] = temp;
            start++;
            end--;
        }
        return reversedSegment;
    }

    public String displayImprovedResults(int[] bestSolution, int bestDistance, int iterationsUntilBestFound, long currentTime, long maxTimeUntilBestFound) {
        return "Best solution: " + Arrays.toString(bestSolution) + "\nDistance of the best solution: " + bestDistance + "\n" +
                "\nTotal execution time: " + (currentTime - startTime) / 1000000 + "ms" +
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

    private int[][][] dividePopulation(int[][] population, int numThreads) {
        int[][][] populationSubsets = new int[numThreads][][];
        int subsetSize = population.length / numThreads;

        for (int i = 0; i < numThreads - 1; i++) {
            populationSubsets[i] = Arrays.copyOfRange(population, i * subsetSize, (i + 1) * subsetSize);
        }

        // Last subset includes remaining elements
        populationSubsets[numThreads - 1] = Arrays.copyOfRange(population, (numThreads - 1) * subsetSize, population.length);

        return populationSubsets;
    }

    private void updateGlobalBest(int[][] population) {
        int[] localBestSolution = selectBestPaths(population)[0];
        int localBestDistance = calculateTotalDistance(localBestSolution);

        synchronized (this) {
            if (localBestDistance < globalBestDistance) {
                globalBestSolution = Arrays.copyOf(localBestSolution, localBestSolution.length);
                globalBestDistance = localBestDistance;
                maxTimeUntilBestFound = (System.nanoTime() - startTime) / 1000000;
            }
        }
    }

    private int[][] combineResults(List<int[][]> results) {
        int totalPopulationSize = results.stream().mapToInt(result -> result.length).sum();
        int[][] combinedPopulation = new int[totalPopulationSize][];
        int index = 0;

        for (int[][] result : results) {
            System.arraycopy(result, 0, combinedPopulation, index, result.length);
            index += result.length;
        }

        return combinedPopulation;
    }


}