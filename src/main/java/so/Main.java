package so;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // ********** Command Line Execution ********** \\
        // Check if command-line arguments are provided
//        if (args.length < 8) {
//            System.out.println("Usage: java -jar tsp2.jar <filename> <numThreads> <maxTime> <populationSize> " +
//                    "<mutationProbability> <updatePercentage> <testToExecute (a, b or g)> <customFile (true or false)>");
//            return;
//
//            int numThreads = Integer.parseInt(args[1]);
//            int maxTimeLimit = Integer.parseInt(args[2]);
//            int populationSize = Integer.parseInt(args[3]);
//            double mutationProbability = Double.parseDouble(args[4]);
//            double updatePercentage = Double.parseDouble(args[5]);
//            // *** Algorithms Tests *** \\
//            String fileName = args[0];
//            String testToExecute = args[6];
//            boolean customFile = Boolean.parseBoolean(args[7]);
//        }
        // ******************************************** \\


        // *** Algorithm *** \\
        int numThreads = 10;
        int maxTimeLimit = 450;
        int populationSize = 100;
        double mutationProbability = 0.1;
        double updatePercentage = 0.05;

        // *** Algorithms Tests *** \\
        String fileName = "ulysses22.txt";      // Test filename
        String testToExecute = "a";         // AdvancedVersion = a   BaseVersion = b     GenerateProblem = g
        boolean customFile = false;         // If file is contained in customProblems folder change to true else false
        int totalTestNumber = 10;           // Number of times the test will be executed
        int testNumber;
        int[][] distanceMatrix = customFile
                ? FileReader.readDistancesFromFile("src/main/resources/customProblems/" + fileName)
                : FileReader.readDistancesFromFile("src/main/resources/originalProblems/" + fileName);

        // *** File Generation *** \\
        int generationSeed = 9;                 // Seed for random generation
        int citiesNumber = 18;                  // Number of cities that will be generated in the file

        // *** Results File Generation *** \\
        boolean exportResultsToFile = true;     // True -> Export to file | False -> Print in console
        String outputFileName = "src/main/resources/executionResults/" + fileName.replace(".txt", "")
                + (testToExecute.equalsIgnoreCase("b") ? "_baseVersion" : "_advancedVersion") + "_results.txt";



        // Lists to store results for each run
        List<Long> executionTimes = new ArrayList<>();
        List<Integer> totalIterationsList = new ArrayList<>();


        // Check if the file already exists
        File outputFile = new File(outputFileName);
        if (outputFile.exists() && exportResultsToFile && !testToExecute.equalsIgnoreCase("g")) {
            // If the file exists, prompt the user for confirmation
            System.out.print("File already exists. Do you want to replace it? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine().trim().toLowerCase();

            if (!userInput.equalsIgnoreCase("y")) {
                System.out.println("Operation canceled. Results will not be exported.");
                return;
            }
        }

        try (PrintStream filePrintStream = new PrintStream(new FileOutputStream(outputFileName))) {
            // Redirect System.out to the filePrintStream if exportResultsToFile is true
            if (exportResultsToFile && !testToExecute.equalsIgnoreCase("g")) {
                System.setOut(filePrintStream);
            }

            switch (testToExecute.toLowerCase()) {
                case ("b"):
                    BaseVersion baseSolver = new BaseVersion(populationSize, maxTimeLimit, mutationProbability, numThreads, distanceMatrix);
                    testNumber = 1;
                    while (testNumber <= totalTestNumber) {
                        System.out.println("*************** TESTE " + testNumber + " - VERSÃO BASE - FICHEIRO (" + fileName + ") ***************");
                        baseSolver.execute();

                        // Store results for each run
                        executionTimes.add(baseSolver.getTotalExecutionTime());
                        totalIterationsList.add(baseSolver.getTotalIterations());

                        testNumber++;
                        System.out.println("\n");
                    }

                    // Calculate averages
                    double averageExecutionTimeB = calculateAverageExecutionTime(executionTimes);
                    double averageTotalIterationsB = calculateAverage(totalIterationsList);
                    System.out.println("Average execution time over " + totalTestNumber + " runs: " + averageExecutionTimeB + "ms");
                    System.out.println("Average total iterations over " + totalTestNumber + " runs: "
                            + String.format("%.2f", averageTotalIterationsB));
                    break;

                case ("a"):
                    AdvancedVersion advancedSolver = new AdvancedVersion(populationSize, maxTimeLimit, mutationProbability, numThreads, distanceMatrix, updatePercentage);
                    testNumber = 1;
                    while (testNumber <= totalTestNumber) {
                        System.out.println("*************** TESTE " + testNumber + " - VERSÃO AVANÇADA - FICHEIRO (" + fileName + ") ***************");
                        advancedSolver.execute();

                        // Store results for each run
                        executionTimes.add(advancedSolver.getTotalExecutionTime());
                        totalIterationsList.add(advancedSolver.getTotalIterations());

                        testNumber++;
                        System.out.println("\n");
                    }

                    // Calculate averages
                    double averageExecutionTimeA = calculateAverageExecutionTime(executionTimes);
                    double averageTotalIterationsA = calculateAverage(totalIterationsList);
                    System.out.println("Average execution time over " + totalTestNumber + " runs: " + averageExecutionTimeA + "ms");
                    System.out.println("Average total iterations over " + totalTestNumber + " runs: "
                            + String.format("%.2f", averageTotalIterationsA));
                    break;

                case ("g"):
                    ProblemGenerator.generateProblem(generationSeed, citiesNumber);
                    break;

                default:
                    // Reset System.out to its original value
                    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
                    System.out.println("The testToExecute var parameter should be 'a -> AdvancedVersion', 'b -> BaseVersion', 'g -> GenerateCustomFile'");
            }

            // Flush and close the filePrintStream
            filePrintStream.flush();
            filePrintStream.close();

            // Reset System.out to its original value
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

            System.out.println("Results exported to " + outputFileName);

        } catch (FileNotFoundException e) {
            System.err.println("Error exporting the results to " + outputFileName);
            throw new RuntimeException(e);
        }
    }

    // Generic method to calculate the average of a list of numbers
    private static <T extends Number> double calculateAverage(List<T> numbers) {
        double sum = 0;
        for (T number : numbers) {
            sum += number.doubleValue();
        }
        return sum / numbers.size();
    }

    private static double calculateAverageExecutionTime(List<Long> executionTimes) {
        long sum = 0;
        for (long time : executionTimes) {
            sum += time;
        }
        return (double) sum / executionTimes.size();
    }
}
