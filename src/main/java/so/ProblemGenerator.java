package so;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class ProblemGenerator {

    public static void generateProblem(int seed, int n) {
        if (isInvalidProblemSize(n)) {
            // Display a message for invalid problem sizes
            System.out.println("File will not be created for " + n + " cities. Please use a valid number of cities (18-50, excluding 22, 24, 26, 42, 48).");
            return;
        }

        // Generate new problem
        int[][] newDistanceMatrix = generateRandomProblem(seed, n);

        // Inform the user that problem generation and solving are completed
        System.out.println("Problem with " + n + " cities generated successfully!");
    }

    private static boolean isInvalidProblemSize(int n) {
        // Check if the problem size is invalid
        return n < 18 || n > 50 || Arrays.asList(22, 24, 26, 42, 48).contains(n);
    }

    private static int[][] generateRandomProblem(int seed, int n) {
        // Create a new distance matrix for the problem
        int[][] newDistanceMatrix = new int[n][n];
        Random random = new Random(seed);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    // Generate random coordinates using Gaussian distribution
                    double x = nextGaussian(60, 30, random);
                    double y = nextGaussian(60, 30, random);

                    // Calculate Euclidean distance between cities
                    newDistanceMatrix[i][j] = (int) Math.round(
                            Math.sqrt(Math.pow(x - n / 2, 2) + Math.pow(y - n / 2, 2))
                    );
                }
            }
        }

        // Save the generated problem to a file
        saveProblemToFile(n, newDistanceMatrix);

        return newDistanceMatrix;
    }

    private static double nextGaussian(double mean, double stdDev, Random random) {
        // Generate random numbers using Gaussian distribution
        return mean + stdDev * random.nextGaussian();
    }

    private static void saveProblemToFile(int n, int[][] distanceMatrix) {
        // Save the problem to a text file
        String fileName = "src/main/resources/customProblems/ex_gau" + n + ".txt";
        File file = new File(fileName);

        if (file.exists()) {
            // If the file already exists, ask the user if they want to replace it
            System.out.println("File already exists: " + fileName);
            System.out.print("Do you want to replace it? (yes/no): ");
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine().trim().toLowerCase();

            if (!userInput.equalsIgnoreCase("yes")) {
                // If the user does not want to replace the file, return
                System.out.println("File not replaced.");
                return;
            }
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            // Write the number of cities to the file
            writer.write(n + "\n");

            // Write the distance matrix to the file
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    writer.write(distanceMatrix[i][j] + " ");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing on file " + fileName + ": ");
            e.printStackTrace();
        }
    }

    private static void solveProblem(int n, int[][] distanceMatrix) {
        System.out.println("Results for ex_gau" + n + ".txt:");
    }
}
