package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.model.GraphEdge;

import javax.swing.*;

public class MovingEdgeLabelDialog extends JDialog {

    protected final MainWindow mainWindow;
    private final GraphEdge edge;

    public MovingEdgeLabelDialog(MainWindow mainWindow, GraphEdge edge) {
        super(mainWindow, "Przesuwanie etykiety krawędzi", true);
        this.mainWindow = mainWindow;
        this.edge = edge;

        setSize(300, 100);

        addComponents();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void addComponents() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 100, 1);
        slider.setValue((int)(edge.getLabelPositionFactor() * 100));
        slider.addChangeListener(e -> {
            edge.setLabelPositionFactor(0.01*((JSlider) e.getSource()).getValue());
            mainWindow.repaint();
        });
        add(slider);
    }

}
