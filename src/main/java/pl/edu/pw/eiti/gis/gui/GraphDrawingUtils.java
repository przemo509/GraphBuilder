package pl.edu.pw.eiti.gis.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphEdge;
import pl.edu.pw.eiti.gis.model.GraphNode;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

public class GraphDrawingUtils {

    private static final Logger logger = LogManager.getLogger();

    public static void drawGraph(Graphics2D g, Graph graph, int imageWidth, int imageHeight) {
        clearPlane(g, imageWidth, imageHeight);

        graph.getAdjacency().forEach((nodesIndexes, edgesList) -> drawEdges(edgesList, g));
        graph.getNodes().forEach((nodeIndex, node) -> drawNode(node, g));
    }

    private static void clearPlane(Graphics2D g, int imageWidth, int imageHeight) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imageWidth, imageHeight);
    }

    private static void drawNode(GraphNode node, Graphics2D g) {
        drawPoint(g, node.getPosition(), GraphNode.SIZE, node.getColor());
        drawString(g, String.valueOf(node.getIndex()), node.getPosition(), Color.GREEN);

    }

    private static void drawPoint(Graphics2D g, Point2D point, int size, Color color) {
        Ellipse2D circle = new Ellipse2D.Double(point.getX() - size / 2, point.getY() - size / 2, size, size);
        g.setColor(color);
        g.fill(circle);
    }

    private static void drawString(Graphics2D g, String string, Point2D point, Color color) {
        FontMetrics fontMetrics = g.getFontMetrics();
        double x = point.getX() - fontMetrics.stringWidth(string) / 2;
        double y = point.getY() + fontMetrics.getHeight() / 4;
        g.setColor(color);
        g.drawString(string, (int) x, (int) y);
    }

    private static void drawEdges(List<GraphEdge> edges, Graphics2D g) {
        if (edges.size() > 0) {
            drawStraightEdge(edges.get(0), g);
        }
        if (edges.size() > 1) {
            drawArcEdge(edges.get(1), g, -1);
        }
        if (edges.size() > 2) {
            drawArcEdge(edges.get(2), g, 1);
        }
        if (edges.size() > 3) {
            GraphEdge edge = edges.get(3);
            int edgeIndex = edge.getIndex();
            int startNodeIndex = edge.getStartNode().getIndex();
            int endNodeIndex = edge.getEndNode().getIndex();
            logger.error("Rysowanie więcej niż 3 krawędzi nie jest zaimplementowane! (e{}: w{} -> w{})", edgeIndex, startNodeIndex, endNodeIndex);
        }
    }

    private static void drawStraightEdge(GraphEdge edge, Graphics2D g) {
        Line2D line = new Line2D.Double(edge.getStartNode().getPosition(), edge.getEndNode().getPosition());
        Point2D edgeLabelPosition = calculatePointAboveLine(line, 10, 0.5);

        g.setColor(GraphEdge.COLOR_NEW);
        g.draw(line);
        drawPoint(g, edgeLabelPosition, 20, Color.GREEN);
        drawString(g, String.valueOf(edge.getIndex()), edgeLabelPosition, Color.BLUE);
    }

    private static Point2D calculatePointAboveLine(Line2D line, int distanceAbove, double distanceToEdgeStart) {
        Point2D point = new Point2D.Double(
                line.getX1() * distanceToEdgeStart + line.getX2() * (1.0 - distanceToEdgeStart),
                line.getY1() * distanceToEdgeStart + line.getY2() * (1.0 - distanceToEdgeStart));
        double dx = line.getX2() - line.getX1();
        double dy = line.getY2() - line.getY1();
        double length = Math.sqrt(dx * dx + dy * dy);
        dx = distanceAbove * dx / length;
        dy = -distanceAbove * dy / length;

        return new Point2D.Double(point.getX() + dy, point.getY() + dx);
    }

    private static void drawArcEdge(GraphEdge edge, Graphics2D g, double middlePointMoved) {
        Point2D arcStart = edge.getStartNode().getPosition();
        Point2D arcEnd = edge.getEndNode().getPosition();
        Line2D line = comparePoints(arcStart, arcEnd) < 0 // in order not to draw arc edge (1,2) on the top of (2,1)
                ? new Line2D.Double(arcStart, arcEnd)
                : new Line2D.Double(arcEnd, arcStart);
        Point2D arcMiddle = calculatePointAboveLine(line, (int) (middlePointMoved * arcStart.distance(arcEnd) / 2), 0.5);

        g.setColor(GraphEdge.COLOR_NEW);
        Arc2D arc = new Arc2D.Double(Arc2D.OPEN);
        arc.setArcByTangent(arcStart, arcMiddle, arcEnd, arcStart.distance(arcMiddle));
        g.draw(arc);
    }

    private static int comparePoints(Point2D p1, Point2D p2) {
        if(p1.getX() < p2.getX()) {
            return -1;
        } else if(p1.getX() > p2.getX()) {
            return 1;
        } else if(p1.getY() < p2.getY()) {
            return -1;
        } else if(p1.getY() > p2.getY()) {
            return 1;
        } else {
            return 0;
        }
    }

}
