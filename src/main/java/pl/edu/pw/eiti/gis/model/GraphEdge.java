package pl.edu.pw.eiti.gis.model;

import pl.edu.pw.eiti.gis.options.Options;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class GraphEdge {
    public static final int SIZE = 25;

    private final int index;
    private int weight;
    private final GraphVertex startVertex;
    private final GraphVertex endVertex;
    private double labelPositionFactor;
    private Point2D labelPosition;
    private boolean flipEdgeLabelSide;
    private boolean highlighted = false;

    public GraphEdge(int index, int weight, double labelPositionFactor, boolean flipEdgeLabelSide,
                     GraphVertex startVertex, GraphVertex endVertex) {
        this.index = index;
        this.weight = weight;
        this.labelPositionFactor = labelPositionFactor;
        this.flipEdgeLabelSide = flipEdgeLabelSide;
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

    public void refreshVRepWeight() {
        setWeight((int) startVertex.getPosition().distance(endVertex.getPosition()));
    }

    public boolean touches(GraphVertex v) {
        return v != null && (v.equals(startVertex) || v.equals(endVertex));
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

    public boolean intersects(GraphEdge other) {
        return !touches(other) && Line2D.linesIntersect(
                startVertex.getPosition().getX(),
                startVertex.getPosition().getY(),
                endVertex.getPosition().getX(),
                endVertex.getPosition().getY(),
                other.startVertex.getPosition().getX(),
                other.startVertex.getPosition().getY(),
                other.endVertex.getPosition().getX(),
                other.endVertex.getPosition().getY()
        );
    }

    public boolean touches(GraphEdge other) {
        return touches(other.getStartVertex()) || touches(other.getEndVertex());
    }
}
