package pl.edu.pw.eiti.gis.model;

import pl.edu.pw.eiti.gis.options.Options;

import java.awt.*;
import java.awt.geom.Point2D;

public class GraphEdge {
    public static final Color COLOR_NEW = Color.DARK_GRAY;

    private final int index;
    private int weight;
    private final GraphVertex startVertex;
    private final GraphVertex endVertex;
    private double labelPositionFactor = 0.5;
    private Point2D labelPosition;

    public GraphEdge(int index, GraphVertex startVertex, GraphVertex endVertex) {
        this.index = index;
        this.weight = index;
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

    public int getIndex() {
        return index;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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

    @Override
    public String toString() {
        return "e" + index + "(" + startVertex + "," + endVertex + ")";
    }

    public String getLabel() {
        String label = "";
        if(Options.getInstance().showEdgeIndexes()) {
            label += String.valueOf(index);
            if(Options.getInstance().showEdgeWeights()) {
                label += ": ";
            }
        }
        if(Options.getInstance().showEdgeWeights()) {
            label += String.valueOf(weight);
        }
        return label;
    }
}
