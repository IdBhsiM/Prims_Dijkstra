package prims;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.border.*;

public class Prims {

    static final double INF = Double.MAX_VALUE; 
    static final double EARTH_RADIUS = 6371; 

    static class City {
        String name;
        double latitude;
        double longitude;
        int population;

        public City(String name, double latitude, double longitude, int population) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.population = population;
        }
    }

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // Distance in kilometers
    }

    public static double[][] readCityData(String filename, List<City> cities) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        reader.readLine(); // Skip the header line

        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            if (values.length == 4) {
                String name = values[0];
                double latitude = Double.parseDouble(values[1]);
                double longitude = Double.parseDouble(values[2]);
                int population = Integer.parseInt(values[3]);
                cities.add(new City(name, latitude, longitude, population));
            }
        }
        reader.close();

        int n = cities.size();
        double[][] matrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double distance = haversine(cities.get(i).latitude, cities.get(i).longitude,
                                            cities.get(j).latitude, cities.get(j).longitude);
                matrix[i][j] = distance;
                matrix[j][i] = distance; // Ensure symmetry
            }
        }
        return matrix;
    }

    public static int[] primMST(double[][] graph, int startCity) {
        int n = graph.length;
        boolean[] inMST = new boolean[n];
        double[] key = new double[n];
        int[] parent = new int[n];

        Arrays.fill(key, INF);
        Arrays.fill(parent, -1);
        key[startCity] = 0;

        for (int count = 0; count < n - 1; count++) {
            int u = minKey(key, inMST);
            inMST[u] = true;

            for (int v = 0; v < n; v++) {
                if (graph[u][v] != 0 && !inMST[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        return parent;
    }

    public static int minKey(double[] key, boolean[] inMST) {
        double min = INF;
        int minIndex = -1;

        for (int v = 0; v < key.length; v++) {
            if (!inMST[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    // Method to display the detailed path information in the text area
    public static void displayPathInfo(List<Integer> pathIndices, List<City> cities, double[][] graph, JTextArea outputArea) {
        double totalDistance = 0.0;
        outputArea.append("Airways MST Path:\n");
        outputArea.append("--------------------------\n");

        for (int i = 0; i < pathIndices.size() - 1; i++) {
            int from = pathIndices.get(i);
            int to = pathIndices.get(i + 1);
            double distance = graph[from][to];
            totalDistance += distance;

            City cityFrom = cities.get(from);
            City cityTo = cities.get(to);

            outputArea.append(String.format("From %-15s --> %-15s | Distance: %.2f km\n",
                    cityFrom.name, cityTo.name, distance));
        }

        City finalCity = cities.get(pathIndices.get(pathIndices.size() - 1));
        outputArea.append(String.format("Reached Target City: %-15s\n", finalCity.name));

        outputArea.append("--------------------------\n");
        outputArea.append(String.format("Total Distance from start to target city: %.2f km\n", totalDistance));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Prim's Algorithm - Minimum Spanning Tree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);

        // Set a colorful background gradient for the JFrame
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 123, 255), 700, 0, new Color(0, 255, 255));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add the panel to the frame
        frame.add(panel);

        // Styling the Load Button with bright colors
        JButton loadButton = new JButton("Load City Data");
        loadButton.setBackground(new Color(102, 0, 102)); 
        loadButton.setForeground(Color.WHITE); 
        loadButton.setFont(new Font("Arial", Font.BOLD, 14));
        loadButton.setBorder(new LineBorder(new Color(255, 165, 0), 2)); 
        loadButton.setFocusPainted(false); 
        loadButton.setPreferredSize(new Dimension(200, 40));
        panel.add(loadButton);

        // Styling the output area with colorful background and text
        JTextArea outputArea = new JTextArea(15, 50);
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(255, 255, 255)); 
        outputArea.setForeground(new Color(0, 0, 0));
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        outputArea.setBorder(new LineBorder(new Color(255, 165, 0), 2)); 

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(new LineBorder(new Color(255, 165, 0), 2));
        panel.add(scrollPane);

        loadButton.addActionListener(e -> {
            // Use JFileChooser to select the file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select City Data File");

            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String filename = file.getAbsolutePath();

                try {
                    List<City> cities = new ArrayList<>();
                    double[][] graph = readCityData(filename, cities);
                    int startCity = 1; 
                    int targetCity = 34; 

                    int[] parent = primMST(graph, startCity);
                    List<Integer> pathIndices = new ArrayList<>();
                    for (int v = targetCity; v != -1; v = parent[v]) {
                        pathIndices.add(v);
                    }
                    Collections.reverse(pathIndices);

                    displayPathInfo(pathIndices, cities, graph, outputArea);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error reading file: " + ex.getMessage());
                }
            }
        });

        frame.setVisible(true);
    }
}
