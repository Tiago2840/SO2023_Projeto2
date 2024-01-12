package so;

import java.io.*;

public class MatrixFileReader {
    private final String fileName;

    public MatrixFileReader(String fileName) {
        this.fileName = fileName;
    }

    public MatrixData readMatrixFromFile() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                // Read the number of cities
                int citiesNumber = Integer.parseInt(reader.readLine().trim());

                // Read the distance matrix
                int[][] distanceMatrix = new int[citiesNumber][citiesNumber];
                for (int i = 0; i < citiesNumber; i++) {
                    String[] distances = reader.readLine().trim().split("\\s+");
                    for (int j = 0; j < citiesNumber; j++) {
                        distanceMatrix[i][j] = Integer.parseInt(distances[j]);
                    }
                }

                return new MatrixData(citiesNumber, distanceMatrix);
            }
        }
    }

    public String getFileName() {
        return fileName;
    }
}
