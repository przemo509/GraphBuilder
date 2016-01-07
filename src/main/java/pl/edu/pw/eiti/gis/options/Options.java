package pl.edu.pw.eiti.gis.options;

public class Options {
    private static Options instance = new Options();

    private boolean showEdgeLabels = true;

    public static Options getInstance() {
        return instance;
    }

    private Options() {
    }

    public void setShowEdgeLabels(boolean showEdgeLabels) {
        this.showEdgeLabels = showEdgeLabels;
    }

    public boolean showEdgeLabels() {
        return showEdgeLabels;
    }
}
