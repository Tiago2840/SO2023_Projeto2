package so;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class AJEPPThread extends Thread {
    private static int[][] cities = new int[0][];
    private final int maxTimeLimit;
    private final double mutationProbability;
    private int[] bestSolution;
    private int globalBestDistance = Integer.MAX_VALUE;
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    private int iterations = 0;
    private long timeUntilBestFound;
    private int[][] population;

    public AJEPPThread(int[][] cities, int populationSize, int maxTimeLimit, double mutationProbability) {
        AJEPPThread.cities = cities;
        this.maxTimeLimit = maxTimeLimit;
        this.mutationProbability = mutationProbability;
        this.bestSolution = null;
    }

    @Override
    public void run() {
        // Initialize the current solution with a random arrangement of cities
        int[] currentSolution = generateRandomSolution(cities.length);
        int currentDistance = calculateTotalDistance(currentSolution);

        // Record the start time for measuring execution time
        long startTime = System.nanoTime();
        long timeLimit = maxTimeLimit * 1000000000L;
        long endTime = startTime + timeLimit;

        // Run the algorithm until the time limit is reached
        while (System.nanoTime() < endTime) {
            iterations++;

            // Generate a new solution using crossover and mutation
            int[] newSolution = pmxCrossover(currentSolution, selectRandomSolution());
            int newDistance = calculateTotalDistance(exchangeMutation(newSolution));

            // Update the current solution if the new one is better
            if (newDistance < currentDistance) {
                currentSolution = newSolution;
                currentDistance = newDistance;

                // Update the time when the best solution is found
                timeUntilBestFound = System.nanoTime() - startTime;
            }

            // Update the global best solution
            if (bestSolution == null || newDistance < calculateTotalDistance(bestSolution)) {
                bestSolution = newSolution;
                globalBestDistance = calculateTotalDistance(bestSolution);
            }
        }
    }

    // Generates a random solution by shuffling the list of cities
    private int[] generateRandomSolution(int size) {
        ArrayList<Integer> citiesList = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            citiesList.add(i);
        }

        int[] solution = new int[size];
        for (int i = 0; i < size; i++) {
            int randomIndex = random.nextInt(citiesList.size());
            solution[i] = citiesList.remove(randomIndex);
        }

        return solution;
    }

    // Calculates the total distance of a solution by summing up the distances between consecutive cities
    public static int calculateTotalDistance(int[] solution) {
        int distance = 0;
        for (int i = 0; i < solution.length - 1; i++) {
            distance += cities[solution[i] - 1][solution[i + 1] - 1];
        }
        distance += cities[solution[solution.length - 1] - 1][solution[0] - 1]; // Return to the starting city
        return distance;
    }

    // Performs partially-mapped crossover (PMX) between two parent solutions
    private int[] pmxCrossover(int[] parent1, int[] parent2) {
        int[] child = new int[parent1.length];

        // Select a random segment of the parents and copy it to the child
        int start = random.nextInt(parent1.length);
        int end = random.nextInt(parent1.length - start) + start;
        for (int i = start; i <= end; i++) {
            child[i] = parent1[i];
        }

        // Fill in the remaining positions by mapping from the second parent
        for (int i = 0; i < child.length; i++) {
            if (i < start || i > end) {
                for (int j = 0; j < parent2.length; j++) {
                    if (!contains(child, parent2[j])) {
                        child[i] = parent2[j];
                        break;
                    }
                }
            }
        }

        return child;
    }

    // Checks if an array contains a specific element
    private boolean contains(int[] array, int element) {
        for (int value : array) {
            if (value == element) {
                return true;
            }
        }
        return false;
    }

    // Performs exchange mutation on a solution with a certain probability
    private int[] exchangeMutation(int[] solution) {
        if (Math.random() < mutationProbability) {
            int index1 = new Random().nextInt(solution.length);
            int index2;
            do {
                index2 = new Random().nextInt(solution.length);
            } while (index1 == index2);

            // Swap two elements to perform mutation
            int temp = solution[index1];
            solution[index1] = solution[index2];
            solution[index2] = temp;
        }
        return solution;
    }

    // Selects a random solution by shuffling the list of cities
    private int[] selectRandomSolution() {
        List<Integer> solutionList = new ArrayList<>();
        for (int i = 1; i <= cities.length; i++) {
            solutionList.add(i);
        }
        Collections.shuffle(solutionList);
        return solutionList.stream().mapToInt(Integer::intValue).toArray();
    }

    // Creates a copy of an array
    public static int[] copy(int[] array) {
        return Arrays.copyOf(array, array.length);
    }

    // Checks if a solution1 is better than solution2 in terms of total distance
    public static boolean isBetterSolution(int[] solution1, int[] solution2, int[][] distances) {
        int distance1 = calculateTotalDistance(solution1, distances);
        int distance2 = calculateTotalDistance(solution2, distances);

        // Shorter distance is the better solution
        return distance1 < distance2;
    }

    // Calculates the total distance of a solution given a distance matrix
    static int calculateTotalDistance(int[] solution, int[][] distances) {
        int distance = 0;
        for (int i = 0; i < solution.length - 1; i++) {
            distance += distances[solution[i] - 1][solution[i + 1] - 1];
        }
        distance += distances[solution[solution.length - 1] - 1][solution[0] - 1]; // Return to the starting city
        return distance;
    }

    // Creates a deep copy of a 2D array
    public static int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    // *** Getters & Setters *** \\
    // Getter method for iterations
    public int getIterations() {
        return iterations;
    }

    // Getter method for time until the best solution is found
    public long getTimeUntilBestFound() {
        return timeUntilBestFound / 1000000; // Convert nanoseconds to milliseconds
    }

    // Getter method for the best solution
    public int[] getBestSolution() {
        return bestSolution;
    }

    // Getter method for the population
    public int[][] getPopulation() {
        return population;
    }

    // Getter method for the global best distance
    public int getGlobalBestDistance() {
        return globalBestDistance;
    }

    // Setter method for the population
    public void setPopulation(int[][] newPopulation) {
        this.population = newPopulation;
    }
}
