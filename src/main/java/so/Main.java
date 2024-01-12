package so;

// TODO: Fix infinite execution
public class Main {
    public static void main(String[] args) {
        String filepath = "ex5.txt";
        int numberOfThreads = 3;
        long maxExecutionTime = 3;
        int populationSize = 2;
        double mutationProbability = 0.01;

        BaseVersion baseVersion = new BaseVersion(filepath, numberOfThreads, maxExecutionTime, populationSize, mutationProbability);
        baseVersion.runBaseVersion();
        Path bestPath = baseVersion.findBestPath();

        System.out.println("Best Path: " + bestPath);
    }
}
