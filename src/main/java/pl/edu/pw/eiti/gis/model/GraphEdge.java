package pl.edu.pw.eiti.gis.model;

import java.awt.*;
import java.awt.geom.Point2D;

public class GraphEdge {
    public static final Color COLOR_NEW = Color.DARK_GRAY;

    private final int index;
    private final GraphVertex startVertex;
    private final GraphVertex endVertex;
    private double labelPositionFactor = 0.5;
    private Point2D labelPosition;

    public GraphEdge(int index, GraphVertex startVertex, GraphVertex endVertex) {
        this.index = index;
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

    public int getIndex() {
        return index;
    }

    public GraphVertex getStartVertex() {
        return startVertex;
    }

    public GraphVertex getEndVertex() {
        return endVertex;
    }

    public boolean isSelfEdge() {
        return startVertex.equals(endVertex);
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
