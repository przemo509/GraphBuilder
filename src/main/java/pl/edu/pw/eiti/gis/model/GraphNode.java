package pl.edu.pw.eiti.gis.model;

import java.awt.*;

public class GraphNode {
    public static final int SIZE = 50;

    private final int index;
    private final Point position;

    public GraphNode(int index, Point position) {
        this.index = index;
        this.position = position;
    }

    public int getIndex() {
        return index;
    }
}
