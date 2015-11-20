package pl.edu.pw.eiti.gis.model;

import java.awt.*;

public class GraphNode {
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
}
