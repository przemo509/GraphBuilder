package pl.edu.pw.eiti.gis.model;

import pl.edu.pw.eiti.gis.options.Options;

import java.awt.*;
import java.awt.geom.Point2D;

public class GraphEdge {
    public static final int SIZE = 25;

    private final int index;
    private int weight;
    private final GraphVertex startVertex;
    private final GraphVertex endVertex;
    private double labelPositionFactor = 0.5;
    private Point2D labelPosition;
    private boolean flipEdgeLabelSide = false;
    private boolean highlighted = false;

    public GraphEdge(int index, int weight, GraphVertex startVertex, GraphVertex endVertex) {
        this.index = index;
        this.weight = weight;
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

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
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

    public boolean getFlipEdgeLabelSide() {
        return flipEdgeLabelSide;
    }

    public void setFlipEdgeLabelSide(boolean flipEdgeLabelSide) {
        this.flipEdgeLabelSide = flipEdgeLabelSide;
    }

    public Color getEdgeColor() {
        return Options.getInstance().paintBlackAndWhite() ? Color.BLACK : Color.DARK_GRAY;
    }

    public Color getLabelFillColor() {
        return highlighted ? Color.YELLOW : (Options.getInstance().paintBlackAndWhite() ? Color.WHITE : Color.GREEN);
    }

    public Color getLabelBorderColor() {
        return highlighted ? Color.DARK_GRAY : (Options.getInstance().paintBlackAndWhite() ? Color.BLACK : Color.GREEN);
    }

    public Color getLabelTextColor() {
        return Options.getInstance().paintBlackAndWhite() ? Color.BLACK : Color.DARK_GRAY;
    }
}
