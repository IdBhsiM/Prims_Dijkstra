//  Dependencies: EdgeWeightedDigraph.java IndexMinPQ.java Stack.java DirectedEdge.java
// dependencies needed for each class is written above all classes
// original dataSet is matrix.txt
// Matrix to list formed data is in :- edge.txt (main text file to run code, after executing MatrixToEdge.java, include "128 source_node Destination_node" in 1st row of edge.txt and save )
// City.txt contains name of 128 cities serial number wise 
// main code run from 0 to 127 while serial number is 1 to 128 by default . so, always enter number-1  

//***Main java class is Dijktra.java***

package Dijkstra;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class DijkstraSP {
    private double[] distTo;
    private DirectedEdge[] edgeTo;
    private IndexMinPQ<Double> pq;

    public DijkstraSP(EdgeWeightedDigraph G, int s, List<String> cityList) {
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        validateVertex(s);

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }

        assert check(G, s);
    }

    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else pq.insert(w, distTo[w]);
        }
    }

    public double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }

    private boolean check(EdgeWeightedDigraph G, int s) {
        return true;
    }

    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dijkstra's Algorithm");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(outputArea);
            frame.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());

            JButton selectCityFileButton = new JButton("Select City File");
            JButton selectEdgeFileButton = new JButton("Select Edge File");
            JButton runButton = new JButton("Run Dijkstra");

            buttonPanel.add(selectCityFileButton);
            buttonPanel.add(selectEdgeFileButton);
            buttonPanel.add(runButton);

            frame.add(buttonPanel, BorderLayout.NORTH);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

            final List<String> cityList = new ArrayList<>();
            final EdgeWeightedDigraph[] G = {null};
            final int[] startVertex = {-1};
            final List<String> lines = new ArrayList<>();

            selectCityFileButton.addActionListener(e -> {
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File cityFile = fileChooser.getSelectedFile();
                    try {
                        BufferedReader cityReader = new BufferedReader(new FileReader(cityFile));
                        cityList.clear();
                        String line;
                        while ((line = cityReader.readLine()) != null) {
                            cityList.add(line.trim());
                        }
                        cityReader.close();
                        showCustomMessage(frame, "City file loaded successfully!", Color.CYAN);
                    } catch (IOException ex) {
                        showCustomMessage(frame, "Error loading city file.", Color.RED);
                    }
                }
            });

            selectEdgeFileButton.addActionListener(e -> {
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File edgeFile = fileChooser.getSelectedFile();
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(edgeFile));
                        lines.clear();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            lines.add(line);
                        }
                        reader.close();

                        String[] firstLine = lines.get(0).split(" ");
                        int V = Integer.parseInt(firstLine[0]);
                        startVertex[0] = Integer.parseInt(firstLine[1]);

                        G[0] = new EdgeWeightedDigraph(V);
                        for (int i = 1; i < lines.size(); i++) {
                            String[] edgeData = lines.get(i).split(" ");
                            int v = Integer.parseInt(edgeData[0]);
                            int w = Integer.parseInt(edgeData[1]);
                            double weight = Double.parseDouble(edgeData[2]);
                            DirectedEdge e1 = new DirectedEdge(v, w, weight);
                            G[0].addEdge(e1);
                        }
                        showCustomMessage(frame, "Edge file loaded successfully!", Color.GREEN);
                    } catch (IOException ex) {
                        showCustomMessage(frame, "Error loading edge file.", Color.RED);
                    }
                }
            });

            runButton.addActionListener(e -> {
                if (cityList.isEmpty() || G[0] == null || startVertex[0] == -1) {
                    showCustomMessage(frame, "Please load both city and edge files first.", Color.YELLOW);
                    return;
                }

                try {
                    // Get the destination node from the edge file (3rd column in first line)
                    int destinationNode = Integer.parseInt(lines.get(0).split(" ")[2]);

                    // Run Dijkstra's algorithm for the start node
                    DijkstraSP sp = new DijkstraSP(G[0], startVertex[0], cityList);
                    outputArea.setText("");

                    if (sp.hasPathTo(destinationNode)) {
                        outputArea.append(String.format("%s to %s (%.2f)  \n",
                                cityList.get(startVertex[0]), cityList.get(destinationNode), sp.distTo(destinationNode)));
                        for (DirectedEdge e1 : sp.pathTo(destinationNode)) {
                            outputArea.append(cityList.get(e1.from()) + " -> " + cityList.get(e1.to()) + "   ");
                        }
                        outputArea.append("\n");
                    } else {
                        outputArea.append(String.format("%s to %s         no path\n",
                                cityList.get(startVertex[0]), cityList.get(destinationNode)));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            frame.setVisible(true);
        });
    }

    // Custom method to show creative and colorful messages
    private static void showCustomMessage(JFrame frame, String message, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        JLabel label = new JLabel(message);
        label.setFont(new Font("Serif", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        panel.add(label);

        JOptionPane.showMessageDialog(frame, panel, "Message", JOptionPane.PLAIN_MESSAGE);
    }
}
