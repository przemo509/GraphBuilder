package pl.edu.pw.eiti.gis.options;

public class Options {
    private static Options instance = new Options();

    private boolean showEdgeIndexes = true;
    private boolean showEdgeWeights = true;
    private boolean paintBlackAndWhite = false;

    public static Options getInstance() {
        return instance;
    }

    private Options() {
    }

    public boolean showEdgeIndexes() {
        return showEdgeIndexes;
    }

    public void setShowEdgeIndexes(boolean showEdgeIndexes) {
        this.showEdgeIndexes = showEdgeIndexes;
    }

    public boolean showEdgeWeights() {
        return showEdgeWeights;
    }

    public void setShowEdgeWeights(boolean showEdgeWeights) {
        this.showEdgeWeights = showEdgeWeights;
    }

    public boolean showEdgeLabels() {
        return showEdgeIndexes || showEdgeWeights;
    }

    public boolean paintBlackAndWhite() {
        return paintBlackAndWhite;
    }

    public void setPaintBlackAndWhite(boolean paintBlackAndWhite) {
        this.paintBlackAndWhite = paintBlackAndWhite;
    }
}
