package pl.edu.pw.eiti.gis.model;

import java.awt.*;
import java.awt.geom.Point2D;

public class GraphEdge {
    public static final Color COLOR_NEW = Color.DARK_GRAY;

    private final int index;
    private final GraphNode startNode;
    private final GraphNode endNode;
    private double labelPositionFactor = 0.5;
    private Point2D labelPosition;

    public GraphEdge(int index, GraphNode startNode, GraphNode endNode) {
        this.index = index;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public int getIndex() {
        return index;
    }

    public GraphNode getStartNode() {
        return startNode;
    }

    public GraphNode getEndNode() {
        return endNode;
    }

    public boolean isSelfEdge() {
        return startNode.equals(endNode);
    }

    public Point2D getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(Point2D labelPosition) {
        this.labelPosition = labelPosition;
    }

    public void setLabelPositionFactor(double labelPositionFactor) {
        this.labelPositionFactor = labelPositionFactor;
    }

    public double getLabelPositionFactor() {
        return labelPositionFactor;
    }
}
