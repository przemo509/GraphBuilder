package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.gui.listener.MainWindowMouseListener;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphNode;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final Graph graph;

    public MainWindow(Graph graph) {
        super("GraphBuilder");
        this.graph = graph;

        setSize(800, 600);
        centerWindow();

        addMouseListener(new MainWindowMouseListener(this));
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }

    public void addGraphNode(Point position) {
        GraphNode node = graph.addNode(position);
        Graphics g = getGraphics();
        g.setColor(Color.DARK_GRAY);
        g.fillOval(position.x, position.y, GraphNode.SIZE, GraphNode.SIZE);
        g.setColor(Color.GREEN);
        g.drawString(String.valueOf(node.getIndex()), position.x+GraphNode.SIZE/2, position.y+GraphNode.SIZE/2);
    }
}
