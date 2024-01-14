package so;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class FileReader {
    public static int[][] readDistancesFromFile(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            int size = scanner.nextInt();
            int[][] distances = new int[size][size];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    distances[i][j] = scanner.nextInt();
                }
            }

            scanner.close();
            return distances;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
