package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.gui.listener.MainWindowMouseListener;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphEdge;
import pl.edu.pw.eiti.gis.model.GraphNode;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

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
        Line2D line = new Line2D.Double(edge.getStartNode().getPosition(), edge.getEndNode().getPosition());
        g.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());

        Point edgeLabelPosition = calculateEdgeLabelPosition(line, g);

        g.setColor(Color.BLUE);
        FontMetrics fontMetrics = g.getFontMetrics();
        String nodeLabel = String.valueOf(edge.getIndex());
        int stringWidth = fontMetrics.stringWidth(nodeLabel);
        g.drawString(nodeLabel, edgeLabelPosition.x - stringWidth / 2, edgeLabelPosition.y + fontMetrics.getHeight() / 4);
    }

    private Point calculateEdgeLabelPosition(Line2D line, Graphics g) {
        Point middle = new Point((int) ((line.getX2() + line.getX1()) / 2), (int) ((line.getY2() + line.getY1()) / 2));
        double dx = line.getX2() - line.getX1();
        double dy = line.getY2() - line.getY1();
        double length = Math.sqrt(dx * dx + dy * dy);
        double distance = 15;
        dx = distance * dx / length; // normalize
        dy = -distance * dy / length; // normalize

        middle.translate((int) dy, (int) dx);

        int buffor = 5;
        int size = (int)(2*distance-buffor);
        g.setColor(Color.GREEN);
        g.fillOval(middle.x - size / 2, middle.y - size / 2, size, size);
        return middle;
    }
}
