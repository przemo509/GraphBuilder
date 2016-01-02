package pl.edu.pw.eiti.gis.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphEdge;
import pl.edu.pw.eiti.gis.model.GraphVertex;

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

        graph.getAdjacency().forEach((verticesIndexes, edgesList) -> drawEdges(edgesList, g));
        graph.getVertices().forEach((vertexIndex, vertex) -> drawVertex(vertex, g));
    }

    private static void clearPlane(Graphics2D g, int imageWidth, int imageHeight) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imageWidth, imageHeight);
    }

    private static void drawVertex(GraphVertex vertex, Graphics2D g) {
        drawPoint(g, vertex.getPosition(), GraphVertex.SIZE, vertex.getColor());
        drawString(g, String.valueOf(vertex.getIndex()), vertex.getPosition(), Color.GREEN);

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
            int startVertexIndex = edge.getStartVertex().getIndex();
            int endVertexIndex = edge.getEndVertex().getIndex();
            logger.error("Rysowanie więcej niż 3 krawędzi nie jest zaimplementowane! (e{}: w{} -> w{})", edgeIndex, startVertexIndex, endVertexIndex);
        }
    }

    private static void drawStraightEdge(GraphEdge edge, Graphics2D g) {
        Line2D line = new Line2D.Double(edge.getStartVertex().getPosition(), edge.getEndVertex().getPosition());
        Point2D edgeLabelPosition = calculatePointAboveLine(line, 10, edge.getLabelPositionFactor());

        g.setColor(GraphEdge.COLOR_NEW);
        g.draw(line);
        drawEdgeLabel(g, edge, edgeLabelPosition);
        drawStraightEdgeArrow(g, line);
    }

    private static void drawStraightEdgeArrow(Graphics2D g, Line2D line) {
        double lineDX = line.getX2() - line.getX1();
        double lineDY = line.getY2() - line.getY1();
        double lineLength = Math.hypot(lineDX, lineDY);
        double lineAngle = Math.atan2(lineDY, lineDX);
        double lineAngleSinus = lineDY / lineLength;
        double lineAngleCosinus = lineDX / lineLength;

        double vertexRadius = 0.5 * GraphVertex.SIZE;
        double linePointDX = vertexRadius * lineAngleCosinus;
        double linePointDY = vertexRadius * lineAngleSinus;
        Point2D arrowPoint = new Point2D.Double(line.getX2() - linePointDX, line.getY2() - linePointDY);

        drawEdgeArrow(g, arrowPoint, lineAngle);
    }

    private static void drawEdgeArrow(Graphics2D g, Point2D arrowPoint, double lineAngle) {
        double arrowAngle = Math.toRadians(40);
        double arrowLength = 15;
        double arrowAngleRight = lineAngle + 0.5 * arrowAngle;
        double arrowAngleLeft = lineAngle - 0.5 * arrowAngle;
        double arrowLeftX = arrowPoint.getX() - arrowLength * Math.cos(arrowAngleRight);
        double arrowLeftY = arrowPoint.getY() - arrowLength * Math.sin(arrowAngleRight);
        double arrowRightX = arrowPoint.getX() - arrowLength * Math.cos(arrowAngleLeft);
        double arrowRightY = arrowPoint.getY() - arrowLength * Math.sin(arrowAngleLeft);
        Point2D arrowLeft = new Point2D.Double(arrowLeftX, arrowLeftY);
        Point2D arrowRight = new Point2D.Double(arrowRightX, arrowRightY);
        g.setColor(GraphEdge.COLOR_NEW);
        g.draw(new Line2D.Double(arrowPoint, arrowLeft));
        g.draw(new Line2D.Double(arrowPoint, arrowRight));
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
        edge.setLabelPosition(edgeLabelPosition);
        drawPoint(g, edgeLabelPosition, 20, Color.GREEN);
        drawString(g, String.valueOf(edge.getIndex()), edgeLabelPosition, Color.BLUE);
    }

    private static void drawArcEdge(GraphEdge edge, Graphics2D g, double middlePointMoved) {
        Point2D arcStart = edge.getStartVertex().getPosition();
        Point2D arcEnd = edge.getEndVertex().getPosition();
        Line2D line = edge.getStartVertex().compareTo(edge.getEndVertex()) < 0 // in order not to draw arc edge (1,2) on the top of (2,1)
                ? new Line2D.Double(arcStart, arcEnd)
                : new Line2D.Double(arcEnd, arcStart);
        Point2D expandingPoint = calculatePointAboveLine(line, (int) (middlePointMoved * arcStart.distance(arcEnd) / 2), 0.5);

        Arc2D arc = new Arc2D.Double(Arc2D.OPEN);
        arc.setArcByTangent(arcStart, expandingPoint, arcEnd, arcStart.distance(expandingPoint));
        g.setColor(GraphEdge.COLOR_NEW);
        g.draw(arc);

        Point2D edgeLabelPosition = calculatePointAboveArc(arc, 10, edge.getLabelPositionFactor());
        drawEdgeLabel(g, edge, edgeLabelPosition);

        drawArcEdgeArrow(g, arc, edge.getEndVertex());
    }

    private static void drawArcEdgeArrow(Graphics2D g, Arc2D arc, GraphVertex arrowVertex) {
        // P0 - vertex center
        double r0 = 0.5 * GraphVertex.SIZE;
        double x0 = arrowVertex.getPosition().getX();
        double y0 = arrowVertex.getPosition().getY();

        // P1 - arc center
        double x1 = arc.getCenterX();
        double y1 = arc.getCenterY();
        double r1 = arc.getEndPoint().distance(x1, y1);

        // distance between circles
        double d = arrowVertex.getPosition().distance(x1, y1);

        // computations from http://stackoverflow.com/a/3349134
        double a = (r0 * r0 - r1 * r1 + d * d) / (2 * d);
        double h = Math.sqrt(r0 * r0 - a * a);
        double x2 = x0 + a * (x1 - x0) / d;
        double y2 = y0 + a * (y1 - y0) / d;
        double x3 = x2 + h * (y1 - y0) / d;
        double y3 = y2 - h * (x1 - x0) / d;
        double x4 = x2 - h * (y1 - y0) / d;
        double y4 = y2 + h * (x1 - x0) / d;

        // arrow is only at one (P5) of points (P3, P4) - fortunately arc does not contains the other one
        int pointBuffer = 2; // Point.intersects works better for rectangles, so we need smallest even buffer
        double x5 = arc.intersects(x3 - pointBuffer / 2, y3 - pointBuffer / 2, pointBuffer, pointBuffer) ? x3 : x4;
        double y5 = arc.intersects(x3 - pointBuffer / 2, y3 - pointBuffer / 2, pointBuffer, pointBuffer) ? y3 : y4;

        // arc radius at arrow point will be perpendicular to arrow direction (which is tangent to arc at arrow point)
        double arcRadiusDir = Math.atan2(y5 - y1, x5 - x1);
        double arrowDir = arcRadiusDir - Math.PI / 2;

        double curvatureFactor = arcCurvatureFactor(r1);
        arrowDir += curvatureFactor;

        // fix for arcs with extent angle less than 0 (reverse direction)
        if (arc.getAngleExtent() < 0) {
            arrowDir += Math.PI - 2 * curvatureFactor;
        }

        drawEdgeArrow(g, new Point2D.Double(x5, y5), arrowDir);
    }

    private static double arcCurvatureFactor(double radius) {
        double factor = radius < 30.0 ? 0.2 : 0.0; // TODO non linear function (25.0 -> 0.2; 100 -> 0.0)
        logger.debug("Arc radius: {}, curvature factor: {}", radius, factor);
        return factor;
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
        double factor = edge.getLabelPositionFactor();
        Point2D arcCenter = edge.getStartVertex().getPosition();
        double expandingPointX = 0.75 * GraphVertex.SIZE * Math.cos(-factor * Math.PI * 2) + arcCenter.getX();
        double expandingPointY = 0.75 * GraphVertex.SIZE * Math.sin(-factor * Math.PI * 2) + arcCenter.getY();

        Arc2D arc = new Arc2D.Double(Arc2D.OPEN);
        arc.setArcByCenter(expandingPointX, expandingPointY, GraphVertex.SIZE / 2, 0, 360, Arc2D.OPEN);
        g.setColor(GraphEdge.COLOR_NEW);
        g.draw(arc);

        Point2D edgeLabelPosition = calculatePointAboveArc(arc, 10, factor);
        drawEdgeLabel(g, edge, edgeLabelPosition);

        drawArcEdgeArrow(g, arc, edge.getEndVertex());
    }

}
