package pl.edu.pw.eiti.gis.model;

import java.awt.*;

public class GraphNode implements Comparable<GraphNode>{
    public static final int SIZE = 50;
    public static final Color COLOR_NEW = Color.DARK_GRAY;
    public static final Color COLOR_SELECTED = Color.YELLOW;

    private final int index;
    private final Point position;
    private Color color;

    public GraphNode(int index, Point position, Color color) {
        this.index = index;
        this.position = position;
        this.color = color;
    }

    public int getIndex() {
        return index;
    }

    public Point getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int compareTo(GraphNode other) {
        if(this.index < other.index) {
            return -1;
        } else if(this.index > other.index) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphNode graphNode = (GraphNode) o;

        return compareTo(graphNode) == 0;
    }
}
