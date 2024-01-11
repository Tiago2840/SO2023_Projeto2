package so;

import java.util.Random;

public class Path {
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
}
