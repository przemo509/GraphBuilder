package pl.edu.pw.eiti.gis.model;

import pl.edu.pw.eiti.gis.options.Options;

import java.awt.*;

public class GraphVertex implements Comparable<GraphVertex>{
    public static final int SIZE = 60;

    private final int index;
    private final Point position;
    private boolean highlighted = false;

    public GraphVertex(int index, Point position) {
        this.index = index;
        this.position = position;
    }

    public int getIndex() {
        return index;
    }

    public Point getPosition() {
        return position;
    }

    public Color getFillColor() {
        return highlighted ? Color.YELLOW : (Options.getInstance().paintBlackAndWhite() ? Color.WHITE : Color.DARK_GRAY);
    }

    public Color getBorderColor() {
        return Options.getInstance().paintBlackAndWhite() ? Color.BLACK : Color.DARK_GRAY;
    }

    public Color getLabelColor() {
        return Options.getInstance().paintBlackAndWhite() ? Color.BLACK : Color.GREEN;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public int compareTo(GraphVertex other) {
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

        GraphVertex graphVortex = (GraphVertex) o;

        return compareTo(graphVortex) == 0;
    }

    @Override
    public String toString() {
        return "v" + index;
    }
}
