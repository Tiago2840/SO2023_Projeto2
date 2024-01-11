package so;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String filepath = "ex4.txt";
        MatrixFileReader fileReader = new MatrixFileReader(filepath);
        System.out.println(fileReader.getFileName());

        try {
            MatrixData matrixData = fileReader.readMatrixFromFile();
            int citiesNumber = matrixData.getCitiesNumber();
            int[][] distanceMatrix = matrixData.getDistanceMatrix();
            System.out.printf("Number of cities: " + citiesNumber + "\nCities matrix: \n" + Arrays.deepToString(distanceMatrix));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while reading the file");
        }
    }
}
