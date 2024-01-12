package so;

import java.util.Arrays;
import java.util.Random;

public class Path implements Comparable<Path> {
    private int[] citiesOrder;
    private double totalDistance;

    public Path(int numberOfCities) {
        citiesOrder = new int[numberOfCities];
        for (int i = 0; i < numberOfCities; i++) {
            citiesOrder[i] = i;
        }
        shuffleCitiesOrder();
    }

    private void shuffleCitiesOrder() {
        Random random = new Random();
        for (int i = citiesOrder.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = citiesOrder[i];
            citiesOrder[i] = citiesOrder[index];
            citiesOrder[index] = temp;
        }
    }

    public void calculateTotalDistance(MatrixData matrixData) {
        int[][] distanceMatrix = matrixData.getDistanceMatrix();
        totalDistance = 0;

        for (int i = 0; i < citiesOrder.length - 1; i++) {
            int fromCity = citiesOrder[i];
            int toCity = citiesOrder[i + 1];
            totalDistance += distanceMatrix[fromCity][toCity];
        }

        // Add distance from the last city back to the starting city
        totalDistance += distanceMatrix[citiesOrder.length - 1][citiesOrder[0]];
    }

    public void applyExchangeMutation() {
        Random random = new Random();
        int position1 = random.nextInt(citiesOrder.length);
        int position2 = random.nextInt(citiesOrder.length);

        // Ensure distinct positions for the exchange
        while (position1 == position2) {
            position2 = random.nextInt(citiesOrder.length);
        }

        // Swap cities at the selected positions
        int temp = citiesOrder[position1];
        citiesOrder[position1] = citiesOrder[position2];
        citiesOrder[position2] = temp;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public int[] getCitiesOrder() {
        return citiesOrder;
    }

    public int compareTo(Path alternativePath) {
        // Compare paths based on total distance
        return Double.compare(this.totalDistance, alternativePath.totalDistance);
    }

    public String toString() {
        return "Path {" +
                "\tCities order: " + Arrays.toString(citiesOrder) +
                ",\tTotal distance: " + totalDistance +
                "\t}";
    }
}
