package pl.edu.pw.eiti.gis.model;

public class GraphEdgeNodesIndexes implements Comparable<GraphEdgeNodesIndexes> {
    private final int index1;
    private final int index2;

    public GraphEdgeNodesIndexes(int index1, int index2) {
        this.index1 = Math.min(index1, index2);
        this.index2 = Math.max(index1, index2);
    }

    @Override
    public int compareTo(GraphEdgeNodesIndexes other) {
        if(this.index1 < other.index1) {
            return -1;
        } else if(this.index1 > other.index1) {
            return 1;
        } else if(this.index2 < other.index2) {
            return -1;
        } else if(this.index2 > other.index2) {
            return 1;
        } else {
            return 0;
        }
    }
}