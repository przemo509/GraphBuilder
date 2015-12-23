package pl.edu.pw.eiti.gis.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphEdge;
import pl.edu.pw.eiti.gis.model.GraphNode;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.List;

public class GraphDrawingUtils {

    private static final Logger logger = LogManager.getLogger();

    public static void drawGraph(Graphics g, Graph graph, int imageWidth, int imageHeight) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imageWidth, imageHeight);

        graph.getAdjacency().forEach((nodesIndexes, edgesList) -> paintEdges(edgesList, g));
        graph.getNodes().forEach((nodeIndex, node) -> paintNode(node, g));
    }

    private static void paintNode(GraphNode node, Graphics g) {
        g.setColor(node.getColor());
        Point nodePosition = node.getPosition();
        g.fillOval(nodePosition.x - GraphNode.SIZE / 2, nodePosition.y - GraphNode.SIZE / 2, GraphNode.SIZE, GraphNode.SIZE);

        g.setColor(Color.GREEN);
        FontMetrics fontMetrics = g.getFontMetrics();
        String nodeLabel = String.valueOf(node.getIndex());
        int stringWidth = fontMetrics.stringWidth(nodeLabel);
        g.drawString(nodeLabel, nodePosition.x - stringWidth / 2, nodePosition.y + fontMetrics.getHeight() / 4);
    }

    private enum EdgeShape {STRAIGHT_LINE, ARCH_1, ARCH_2}

    private static void paintEdges(List<GraphEdge> edges, Graphics g) {
        if(edges.size() > 0) {
            paintEdge(edges.get(0), g, EdgeShape.STRAIGHT_LINE);
        }
        if(edges.size() > 1) {
            paintEdge(edges.get(1), g, EdgeShape.ARCH_1);
        }
        if(edges.size() > 2) {
            paintEdge(edges.get(2), g, EdgeShape.ARCH_2);
        }
        if(edges.size() > 3) {
            GraphEdge edge = edges.get(3);
            int edgeIndex = edge.getIndex();
            int startNodeIndex = edge.getStartNode().getIndex();
            int endNodeIndex = edge.getEndNode().getIndex();
            logger.error("Rysowanie więcej niż 3 krawędzi nie jest zaimplementowane! (e{}: w{} -> w{})", edgeIndex, startNodeIndex, endNodeIndex);
        }
    }

    private static void paintEdge(GraphEdge edge, Graphics g, EdgeShape shape) {
        switch (shape) {
            case STRAIGHT_LINE:
                paintStraightEdge(edge, g);
                break;
            case ARCH_1:
                break;
            case ARCH_2:
                break;
        }
    }

    private static void paintStraightEdge(GraphEdge edge, Graphics g) {
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

    private static Point calculateEdgeLabelPosition(Line2D line, Graphics g) {
        Point middle = new Point((int) ((line.getX2() + line.getX1()) / 2), (int) ((line.getY2() + line.getY1()) / 2));
        double dx = line.getX2() - line.getX1();
        double dy = line.getY2() - line.getY1();
        double length = Math.sqrt(dx * dx + dy * dy);
        double distance = 15;
        dx = distance * dx / length; // normalize
        dy = -distance * dy / length; // normalize

        middle.translate((int) dy, (int) dx);

        int buffer = 5;
        int size = (int) (2 * distance - buffer);
        g.setColor(Color.GREEN);
        g.fillOval(middle.x - size / 2, middle.y - size / 2, size, size);
        return middle;
    }

}
