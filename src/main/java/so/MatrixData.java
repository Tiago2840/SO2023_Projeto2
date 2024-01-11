package so;

public class MatrixData {
    private final int citiesNumber;
    private final int[][] distanceMatrix;

    public MatrixData(int citiesNumber, int[][] distanceMatrix) {
        this.citiesNumber = citiesNumber;
        this.distanceMatrix = distanceMatrix;
    }

    public int getCitiesNumber() {
        return citiesNumber;
    }

    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }
}
