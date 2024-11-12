package Dijkstra;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MatrixToEdgeList {
    // Function to read the distance matrix from a file
    public static int[][] readMatrixFromFile(String filename) throws IOException {
        List<int[]> matrixList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            int[] row = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                row[i] = Integer.parseInt(parts[i]);
            }
            matrixList.add(row);
        }
        br.close();

        int[][] matrix = new int[matrixList.size()][];
        for (int i = 0; i < matrixList.size(); i++) {
            matrix[i] = matrixList.get(i);
        }
        return matrix;
    }

    // Function to write the edge list to a file
    public static void writeEdgeListToFile(int[][] matrix, String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != 0) {  // Only write non-zero distances
                    bw.write(i + " " + j + " " + matrix[i][j]);
                    bw.newLine();
                }
            }
        }
        bw.close();
    }

    public static void main(String[] args) {
        try {
            // Read matrix from matrix.txt
            int[][] matrix = readMatrixFromFile("matrix.txt");

            // Write edge list with distances to edge.txt
            writeEdgeListToFile(matrix, "edge.txt");

            System.out.println("Edge list with distances has been written to edge.txt.");
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
