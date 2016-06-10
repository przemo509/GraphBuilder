package pl.edu.pw.eiti.gis.model;

public class GraphType {
    private final boolean multi;
    private final boolean directed;
    private final boolean weighted;
    private final boolean vRep;

    public GraphType(boolean multi, boolean directed, boolean weighted, boolean vRep) {
        this.multi = multi;
        this.directed = directed;
        this.weighted = weighted;
        this.vRep = vRep;
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

    public boolean isvRep() {
        return vRep;
    }

    @Override
    public String toString() {
        return (multi ? "m" : "s") + "_" +
                (directed ? "d" : "nd") + "_" +
                (weighted ? "w" : "nw");
    }
}
