package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.gui.listener.MainWindowMouseListener;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphEdge;
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
        graph.selectNode(clickedNode);
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
        graph.getEdges().stream().forEach(edge -> paintEdge(edge, g));
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

    private void paintEdge(GraphEdge edge, Graphics g) {
        g.setColor(GraphEdge.COLOR_NEW);
        int startX = edge.getStartNode().getPosition().x;
        int startY = edge.getStartNode().getPosition().y;
        int endX = edge.getEndNode().getPosition().x;
        int endY = edge.getEndNode().getPosition().y;
        g.drawLine(startX, startY, endX, endY);

        g.setColor(Color.GREEN);
        FontMetrics fontMetrics = g.getFontMetrics();
        String nodeLabel = String.valueOf(edge.getIndex());
        int stringWidth = fontMetrics.stringWidth(nodeLabel);
        g.drawString(nodeLabel, (startX + endX) / 2 - stringWidth / 2, (startY + endY) / 2 + fontMetrics.getHeight() / 4);
    }
}
