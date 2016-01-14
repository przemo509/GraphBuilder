package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.model.Graph;
import pl.edu.pw.eiti.gis.model.GraphEdge;
import pl.edu.pw.eiti.gis.model.GraphType;
import pl.edu.pw.eiti.gis.model.GraphVertex;
import pl.edu.pw.eiti.gis.options.Options;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphDrawingUtils {

    private static final Logger logger = Logger.getLogger(GraphDrawingUtils.class.getName());

    public static void drawGraph(Graphics2D g, Graph graph, int imageWidth, int imageHeight) {
        BufferedImage bi = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D bg = bi.createGraphics();
        drawGraphOnGraphics(bg, graph, imageWidth, imageHeight);
        g.drawImage(bi, null, 0, 0);
        bg.dispose();
    }

    private static void drawGraphOnGraphics(Graphics2D g, Graph graph, int imageWidth, int imageHeight) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        clearPlane(g, imageWidth, imageHeight);

        g.setFont(new Font(g.getFont().getName(), Font.BOLD, 15));
        graph.getAdjacency().forEach((verticesIndexes, edgesList) -> drawEdges(edgesList, g, graph.getType()));
        g.setFont(new Font(g.getFont().getName(), Font.BOLD, 17));
        graph.getVertices().forEach((vertexIndex, vertex) -> drawVertex(vertex, g));

        drawErrorMessage(g, graph.consumeLastError(), imageWidth);
    }

    private static void clearPlane(Graphics2D g, int imageWidth, int imageHeight) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imageWidth, imageHeight);
    }

    private static void drawVertex(GraphVertex vertex, Graphics2D g) {
        drawPoint(g, vertex.getPosition(), GraphVertex.SIZE, vertex.getFillColor(), vertex.getBorderColor());
        drawString(g, String.valueOf(vertex.getIndex()), vertex.getPosition(), vertex.getLabelColor());

    }

    private static void drawPoint(Graphics2D g, Point2D point, int size, Color fillColor, Color borderColor) {
        Ellipse2D circle = new Ellipse2D.Double(point.getX() - size / 2, point.getY() - size / 2, size, size);
        g.setColor(fillColor);
        g.fill(circle);
        if(!borderColor.equals(fillColor)) {
            g.setColor(borderColor);
            g.draw(circle);
        }
    }

    private static void drawString(Graphics2D g, String string, Point2D point, Color labelColor) {
        FontMetrics fontMetrics = g.getFontMetrics();
        double x = point.getX() - fontMetrics.stringWidth(string) / 2;
        double y = point.getY() + fontMetrics.getHeight() / 4;
        g.setColor(labelColor);
        g.drawString(string, (int) x, (int) y);
    }

    private static void drawEdges(List<GraphEdge> edges, Graphics2D g, GraphType graphType) {
        if (edges.size() > 0) {
            GraphEdge edge = edges.get(0);
            if (edge.isSelfEdge()) {
                drawSelfEdge(edge, g, graphType);
            } else {
                drawStraightEdge(edge, g, graphType);
            }
        }
        if (edges.size() > 1) {
            drawArcEdge(edges.get(1), g, -1, graphType);
        }
        if (edges.size() > 2) {
            drawArcEdge(edges.get(2), g, 1, graphType);
        }
        if (edges.size() > 3) {
            GraphEdge edge = edges.get(3);
            int edgeIndex = edge.getIndex();
            int startVertexIndex = edge.getStartVertex().getIndex();
            int endVertexIndex = edge.getEndVertex().getIndex();
            logger.log(Level.SEVERE, "Rysowanie więcej niż 3 krawędzi nie jest zaimplementowane! (e{}: w{} -> w{})", new Object[]{edgeIndex, startVertexIndex, endVertexIndex});
        }
    }

    private static void drawStraightEdge(GraphEdge edge, Graphics2D g, GraphType graphType) {
        Line2D line = new Line2D.Double(edge.getStartVertex().getPosition(), edge.getEndVertex().getPosition());
        g.setColor(edge.getEdgeColor());
        g.draw(line);

        if(Options.getInstance().showEdgeLabels()) {
            Point2D edgeLabelPosition = calculatePointAboveLine(line, getLabelDistanceAboveEdge(), edge.getLabelPositionFactor(), edge.getFlipEdgeLabelSide());
            drawEdgeLabel(g, edge, edgeLabelPosition);
        }

        if(graphType.isDirected()) {
            drawStraightEdgeArrow(g, line, edge.getEdgeColor());
        }
    }

    private static void drawStraightEdgeArrow(Graphics2D g, Line2D line, Color color) {
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

        drawEdgeArrow(g, arrowPoint, lineAngle, color);
    }

    private static void drawEdgeArrow(Graphics2D g, Point2D arrowPoint, double lineAngle, Color color) {
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
        g.setColor(color);
        g.draw(new Line2D.Double(arrowPoint, arrowLeft));
        g.draw(new Line2D.Double(arrowPoint, arrowRight));
    }

    private static Point2D calculatePointAboveLine(Line2D line, int distanceAbove, double distanceToEdgeStart, boolean flipSide) {
        Point2D point = new Point2D.Double(
                line.getX1() * distanceToEdgeStart + line.getX2() * (1.0 - distanceToEdgeStart),
                line.getY1() * distanceToEdgeStart + line.getY2() * (1.0 - distanceToEdgeStart));
        double dx = line.getX2() - line.getX1();
        double dy = line.getY2() - line.getY1();
        double length = Math.sqrt(dx * dx + dy * dy);
        dx = distanceAbove * dx / length * (flipSide ? -1 : 1);
        dy = -distanceAbove * dy / length * (flipSide ? -1 : 1);

        return new Point2D.Double(point.getX() + dy, point.getY() + dx);
    }

    private static void drawEdgeLabel(Graphics2D g, GraphEdge edge, Point2D edgeLabelPosition) {
        edge.setLabelPosition(edgeLabelPosition);
        drawPoint(g, edgeLabelPosition, GraphEdge.SIZE, edge.getLabelFillColor(), edge.getLabelBorderColor());
        drawString(g, edge.getLabel(), edgeLabelPosition, edge.getLabelTextColor());
    }

    private static void drawArcEdge(GraphEdge edge, Graphics2D g, double middlePointMoved, GraphType graphType) {
        Point2D arcStart = edge.getStartVertex().getPosition();
        Point2D arcEnd = edge.getEndVertex().getPosition();
        Line2D line = edge.getStartVertex().compareTo(edge.getEndVertex()) < 0 // in order not to draw arc edge (1,2) on the top of (2,1)
                ? new Line2D.Double(arcStart, arcEnd)
                : new Line2D.Double(arcEnd, arcStart);
        Point2D expandingPoint = calculatePointAboveLine(line, (int) (middlePointMoved * arcStart.distance(arcEnd) / 2), 0.5, false);

        Arc2D arc = new Arc2D.Double(Arc2D.OPEN);
        arc.setArcByTangent(arcStart, expandingPoint, arcEnd, arcStart.distance(expandingPoint));
        g.setColor(edge.getEdgeColor());
        g.draw(arc);

        if(Options.getInstance().showEdgeLabels()) {
            Point2D edgeLabelPosition = calculatePointAboveArc(arc, getLabelDistanceAboveEdge(), edge.getLabelPositionFactor(), edge.getFlipEdgeLabelSide());
            drawEdgeLabel(g, edge, edgeLabelPosition);
        }

        if(graphType.isDirected()) {
            drawArcEdgeArrow(g, arc, edge.getEndVertex(), edge.getEdgeColor());
        }
    }

    private static int getLabelDistanceAboveEdge() {
        return GraphEdge.SIZE / 2 + 2;
    }

    private static void drawArcEdgeArrow(Graphics2D g, Arc2D arc, GraphVertex arrowVertex, Color color) {
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

        drawEdgeArrow(g, new Point2D.Double(x5, y5), arrowDir, color);
    }

    private static double arcCurvatureFactor(double radius) {
        double factor = radius <= GraphVertex.SIZE/2 ? 0.2 : 0.0; // TODO non linear function (25.0 -> 0.2; 100 -> 0.0)
        logger.log(Level.FINEST, "Arc radius: {}, curvature factor: {}", new Object[]{radius, factor});
        return factor;
    }

    private static Point2D calculatePointAboveArc(Arc2D arc, int distanceAbove, double distanceToEdgeStart, boolean flipSide) {
        Point2D.Double arcCenter = new Point2D.Double(arc.getCenterX(), arc.getCenterY());
        double arcRadius = arc.getStartPoint().distance(arcCenter) + distanceAbove * (flipSide ? -1 : 1);
        double angle = -(arc.getAngleStart() + distanceToEdgeStart * arc.getAngleExtent());
        double labelX = arcRadius * Math.cos(Math.toRadians(angle)) + arcCenter.getX();
        double labelY = arcRadius * Math.sin(Math.toRadians(angle)) + arcCenter.getY();
        return new Point2D.Double(labelX, labelY);
    }

    private static void drawSelfEdge(GraphEdge edge, Graphics2D g, GraphType graphType) {
        double factor = edge.getLabelPositionFactor();
        Point2D arcCenter = edge.getStartVertex().getPosition();
        double expandingPointX = 0.75 * GraphVertex.SIZE * Math.cos(-factor * Math.PI * 2) + arcCenter.getX();
        double expandingPointY = 0.75 * GraphVertex.SIZE * Math.sin(-factor * Math.PI * 2) + arcCenter.getY();

        Arc2D arc = new Arc2D.Double(Arc2D.OPEN);
        arc.setArcByCenter(expandingPointX, expandingPointY, GraphVertex.SIZE / 2, 0, 360, Arc2D.OPEN);
        g.setColor(edge.getEdgeColor());
        g.draw(arc);

        if(Options.getInstance().showEdgeLabels()) {
            Point2D edgeLabelPosition = calculatePointAboveArc(arc, getLabelDistanceAboveEdge(), factor, edge.getFlipEdgeLabelSide());
            drawEdgeLabel(g, edge, edgeLabelPosition);
        }

        if(graphType.isDirected()) {
            drawArcEdgeArrow(g, arc, edge.getEndVertex(), edge.getEdgeColor());
        }
    }

    private static void drawErrorMessage(Graphics2D g, String msg, int windowWidth) {
        if(msg == null || msg.trim().isEmpty()) {
            return;
        }
        FontMetrics fontMetrics = g.getFontMetrics();
        int textW = fontMetrics.stringWidth(msg);
        int textH = fontMetrics.getHeight();
        int buffer = 10;
        int textX = windowWidth / 2 - textW / 2;
        int textY = 2 * buffer + textH * 3 / 4;

        g.setColor(new Color(255, 240, 240));
        int rX = textX - buffer;
        int rY = buffer;
        int rW = textW + 2 * buffer;
        int rH = textH + 2 * buffer;
        int rB = 1; // border
        g.fillRect(rX, rY, rW, rH);
        g.setColor(Color.RED);
        g.drawRect(rX - rB, rY + rB, rW + rB, rH - rB);
        g.drawString(msg, textX, textY);
    }

}
