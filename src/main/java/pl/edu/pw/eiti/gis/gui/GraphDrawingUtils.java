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
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
            GraphEdge edge = edges.get(0);
            if (edge.isSelfEdge()) {
                drawSelfEdge(edge, g);
            } else {
                drawStraightEdge(edge, g);
            }
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
        drawEdgeLabel(g, edge, edgeLabelPosition);
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

    private static void drawEdgeLabel(Graphics2D g, GraphEdge edge, Point2D edgeLabelPosition) {
        drawPoint(g, edgeLabelPosition, 20, Color.GREEN);
        drawString(g, String.valueOf(edge.getIndex()), edgeLabelPosition, Color.BLUE);
    }

    private static void drawArcEdge(GraphEdge edge, Graphics2D g, double middlePointMoved) {
        Point2D arcStart = edge.getStartNode().getPosition();
        Point2D arcEnd = edge.getEndNode().getPosition();
        Line2D line = edge.getStartNode().compareTo(edge.getEndNode()) < 0 // in order not to draw arc edge (1,2) on the top of (2,1)
                ? new Line2D.Double(arcStart, arcEnd)
                : new Line2D.Double(arcEnd, arcStart);
        Point2D expandingPoint = calculatePointAboveLine(line, (int) (middlePointMoved * arcStart.distance(arcEnd) / 2), 0.5);

        Arc2D arc = new Arc2D.Double(Arc2D.OPEN);
        arc.setArcByTangent(arcStart, expandingPoint, arcEnd, arcStart.distance(expandingPoint));
        g.setColor(GraphEdge.COLOR_NEW);
        g.draw(arc);

        Point2D edgeLabelPosition = calculatePointAboveArc(arc, 10, 0.5);
        drawEdgeLabel(g, edge, edgeLabelPosition);
    }

    private static Point2D calculatePointAboveArc(Arc2D arc, int distanceAbove, double distanceToEdgeStart) {
        Point2D.Double arcCenter = new Point2D.Double(arc.getCenterX(), arc.getCenterY());
        double arcRadius = arc.getStartPoint().distance(arcCenter) + distanceAbove;
        double angle = -(arc.getAngleStart() + distanceToEdgeStart * arc.getAngleExtent());
        double labelX = arcRadius * Math.cos(Math.toRadians(angle)) + arcCenter.getX();
        double labelY = arcRadius * Math.sin(Math.toRadians(angle)) + arcCenter.getY();
        return new Point2D.Double(labelX, labelY);
    }

    private static void drawSelfEdge(GraphEdge edge, Graphics2D g) {
        double factor = 0.5;
        Point2D arcCenter = edge.getStartNode().getPosition();
        double expandingPointX = 0.75 * GraphNode.SIZE * Math.cos(-factor * Math.PI * 2) + arcCenter.getX();
        double expandingPointY = 0.75 * GraphNode.SIZE * Math.sin(-factor * Math.PI * 2) + arcCenter.getY();

        Arc2D arc = new Arc2D.Double(Arc2D.OPEN);
        arc.setArcByCenter(expandingPointX, expandingPointY, GraphNode.SIZE / 2, 0, 360, Arc2D.OPEN);
        g.setColor(GraphEdge.COLOR_NEW);
        g.draw(arc);

        Point2D edgeLabelPosition = calculatePointAboveArc(arc, 10, factor);
        drawEdgeLabel(g, edge, edgeLabelPosition);
    }

}
