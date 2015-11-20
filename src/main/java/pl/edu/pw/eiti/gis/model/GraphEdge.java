package pl.edu.pw.eiti.gis.model;

import java.awt.*;

public class GraphEdge {
    public static final Color COLOR_NEW = Color.DARK_GRAY;

    private final int index;
    private final GraphNode startNode;
    private final GraphNode endNode;

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
}
