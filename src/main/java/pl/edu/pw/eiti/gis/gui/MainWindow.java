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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }

    public void onMouseClick(Point position) {
        GraphNode clickedNode = graph.getNode(position);
        if (clickedNode != null) {
            selectNode(clickedNode);
        } else {
            addNode(position);
        }
    }

    private void selectNode(GraphNode clickedNode) {
        clickedNode.setColor(GraphNode.COLOR_SELECTED);
        repaint();
    }

    private void addNode(Point position) {
        graph.addNode(position);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g);
        graph.getNodes().stream().forEach(node -> paintNode(node, g));
    }

    private void paintNode(GraphNode node, Graphics g) {
        g.setColor(node.getColor());
        Point nodePosition = node.getPosition();
        g.fillOval(nodePosition.x - GraphNode.SIZE / 2, nodePosition.y - GraphNode.SIZE / 2, GraphNode.SIZE, GraphNode.SIZE);

        g.setColor(Color.GREEN);
        FontMetrics fontMetrics = g.getFontMetrics();
        String nodeLabel = String.valueOf(node.getIndex());
        int stringWidth = fontMetrics.stringWidth(nodeLabel);
        g.drawString(nodeLabel, nodePosition.x - stringWidth / 2, nodePosition.y + fontMetrics.getHeight() / 4);
    }
}
