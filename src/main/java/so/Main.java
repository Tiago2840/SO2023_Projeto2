package so;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Algorithm Execution
        String fileName = "";
        boolean customFile = false;
        int numThreads = 0, maxTimeLimit = 0, populationSize = 0, totalTestNumber = 0, testNumber;
        double mutationProbability = 0, updatePercentage = 0;
        // File Generation
        int generationSeed = 0, citiesNumber = 0;

        String testToExecute = args[0].toLowerCase();   // AdvancedVersion = a  BaseVersion = b originalVersion = o GenerateProblem = g

        switch (testToExecute) {
            case ("b"):
            case ("o"):
                validateArguments(args, 7, "b/o", "<filename> <customFile> " +
                        "<numThreads> <maxTime> <populationSize> " +
                        "<mutationProbability> <(Optional)totalTestNumber>");
                fileName = args[1];                                                                     // Test filename
                customFile = Boolean.parseBoolean(args[2]);                                             // If file is contained in customProblems folder change to true else false
                numThreads = validatePositiveInteger(args[3], "Number of threads");
                maxTimeLimit = validatePositiveInteger(args[4], "Maximum time limit");
                populationSize = validatePositiveInteger(args[5], "Population size");
                mutationProbability = validateProbability(args[6]);
                totalTestNumber = args.length == 7 ?
                        totalTestNumber = 1 :
                        validatePositiveInteger(args[7], "Total test number");              // Number of times the test will be executed
                break;

            case ("a"):
                validateArguments(args, 8, "a", "<filename> <customFile> " +
                        "<numThreads> <maxTime> <populationSize> <mutationProbability> " +
                        "<updatePercentage> <(Optional)totalTestNumber>");
                fileName = args[1];                                                                    // Test filename
                customFile = Boolean.parseBoolean(args[2]);                                            // If file is contained in customProblems folder change to true else false
                numThreads = validatePositiveInteger(args[3], "Number of threads");
                maxTimeLimit = validatePositiveInteger(args[4], "Maximum time limit");
                populationSize = validatePositiveInteger(args[5], "Population size");
                mutationProbability = validateProbability(args[6]);
                updatePercentage = validateProbability(args[7]);
                totalTestNumber = args.length == 8 ?
                        totalTestNumber = 1 :
                        validatePositiveInteger(args[8], "Total test number");              // Number of times the test will be executed
                break;

            case ("g"):
                validateArguments(args, 3, "g", "<generationSeed> <citiesNumber>");
                generationSeed = validatePositiveInteger(args[1], "Generation seed");       // Seed for random generation
                citiesNumber = validatePositiveInteger(args[2], "Number of cities");        // Number of cities that will be generated in the file
                break;

            default:
                System.out.println("\nInvalid arguments. Expected: ");
                System.out.println(" • java -jar tsp2.jar <b/o> <filename> <customFile> <numThreads> <maxTime> <populationSize> " +
                        "<mutationProbability> <(Optional)totalTestNumber>");
                System.out.println("\tOR");
                System.out.println(" • java -jar tsp2.jar <a> <filename> <customFile> <numThreads> <maxTime> <populationSize> " +
                        "<mutationProbability> <updatePercentage> <(Optional)totalTestNumber>");
                System.out.println("\tOR");
                System.out.println(" • java -jar tsp2.jar <g> <generationSeed> <citiesNumber>");
                System.out.println("\n* NOTES: " +
                        "\n  <a/b/o/g> -> AdvancedVersion = a\tBaseVersion = b\tOriginalVersion = o\tGenerateProblem = g" +
                        "\n  <customFile> -> True = Files generated with GenerateProblem\tFalse -> Default files" +
                        "\n  <(Optional)totalTestNumber> -> Number of times the test will execute");
                return;
        }

        int[][] distanceMatrix = customFile
                ? FileReader.readDistancesFromFile("resources/customProblems/" + fileName)
                : FileReader.readDistancesFromFile("resources/originalProblems/" + fileName);


        // Lists to store results for each run
        List<Long> executionTimes = new ArrayList<>();
        List<Integer> totalIterationsList = new ArrayList<>();

        System.out.println();
        switch (testToExecute) {
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
                AdvancedVersion advancedSolver = new AdvancedVersion(populationSize, maxTimeLimit, mutationProbability,
                        numThreads, distanceMatrix, updatePercentage);
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

            case ("o"):
                OriginalVersion originalVersion = new OriginalVersion(populationSize, maxTimeLimit, mutationProbability, numThreads, distanceMatrix);
                testNumber = 1;
                while (testNumber <= totalTestNumber) {
                    System.out.println("*************** TESTE " + testNumber + " - VERSÃO ORIGINAL - FICHEIRO (" + fileName + ") ***************");
                    originalVersion.execute();

                    // Store results for each run
                    executionTimes.add(originalVersion.getTotalExecutionTime());
                    totalIterationsList.add(originalVersion.getTotalIterations());

                    testNumber++;
                    System.out.println("\n");
                }

                // Calculate averages
                double averageExecutionTimeO = calculateAverageExecutionTime(executionTimes);
                double averageTotalIterationsO = calculateAverage(totalIterationsList);
                System.out.println("Average execution time over " + totalTestNumber + " runs: " + averageExecutionTimeO + "ms");
                System.out.println("Average total iterations over " + totalTestNumber + " runs: "
                        + String.format("%.2f", averageTotalIterationsO));
                break;


            case ("g"):
                ProblemGenerator.generateProblem(generationSeed, citiesNumber);
                break;

            default:
                break;
        }
        System.out.println();
    }


    // *** Methods to calculate the averages *** \\
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


    // *** Validation methods *** \\
    private static void validateArguments(String[] args, int expectedLength, String testType, String argsFormat) {
        if (args.length != expectedLength) {
            System.out.println("Invalid number of arguments for " + testType + ". Expected " + expectedLength + " arguments in the format: " + argsFormat);
            System.exit(1);
        }
    }

    private static int validatePositiveInteger(String arg, String paramName) {
        try {
            int value = Integer.parseInt(arg);
            if (value <= 0) {
                System.out.println(paramName + " must be a positive integer.");
                System.exit(1);
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println(paramName + " must be a valid integer.");
            System.exit(1);
            return 0;  // Unreachable, but required for compilation
        }
    }

    private static double validateProbability(String arg) {
        try {
            double value = Double.parseDouble(arg);
            if (value < 0 || value > 1) {
                System.out.println("Probability values must be between 0 and 1.");
                System.exit(1);
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Probability must be a valid decimal number.");
            System.exit(1);
            return 0.0;  // Unreachable, but required for compilation
        }
    }
}