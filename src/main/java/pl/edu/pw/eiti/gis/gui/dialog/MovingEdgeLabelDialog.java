package pl.edu.pw.eiti.gis.gui.dialog;

import pl.edu.pw.eiti.gis.gui.MainWindow;
import pl.edu.pw.eiti.gis.model.GraphEdge;

import javax.swing.*;

public class MovingEdgeLabelDialog extends ToolDialog {

    public MovingEdgeLabelDialog(MainWindow mainWindow, GraphEdge edge) {
        super(mainWindow, "Przesuwanie etykiety krawędzi");
        addComponents(mainWindow, edge);
    }

    private void addComponents(MainWindow mainWindow, GraphEdge edge) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 100, (int)(edge.getLabelPositionFactor() * 100));
        slider.addChangeListener(e -> {
            edge.setLabelPositionFactor(0.01*((JSlider) e.getSource()).getValue());
            mainWindow.repaint();
        });
        add(slider);
    }

}
