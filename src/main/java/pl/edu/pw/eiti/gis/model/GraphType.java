package pl.edu.pw.eiti.gis.model;

public class GraphType {
    private final boolean multi;
    private final boolean directed;
    private final boolean weighted;

    public GraphType() {
        this(false, false, false);
    }

    public GraphType(boolean multi, boolean directed, boolean weighted) {
        this.multi = multi;
        this.directed = directed;
        this.weighted = weighted;
    }

    public boolean isMulti() {
        return multi;
    }

    public boolean isDirected() {
        return directed;
    }

    public boolean isWeighted() {
        return weighted;
    }

    @Override
    public String toString() {
        return (multi ? "m" : "s") + "_" +
                (directed ? "d" : "nd") + "_" +
                (weighted ? "w" : "nw");
    }
}
