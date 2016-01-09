package pl.edu.pw.eiti.gis.gui.dialog;

import pl.edu.pw.eiti.gis.gui.MainWindow;
import pl.edu.pw.eiti.gis.model.GraphEdge;

import javax.swing.*;
import java.awt.*;

public class EditingEdgeDialog extends ToolDialog {

    public EditingEdgeDialog(MainWindow mainWindow, GraphEdge edge) {
        super(mainWindow, "Edycja krawędzi");

        addComponents(mainWindow, edge);
        pack();
    }

    private void addComponents(MainWindow mainWindow, GraphEdge edge) {
        setLayout(new GridLayout(3, 2, 10, 2));
        addEdgePosition(mainWindow, edge);
        addEdgePositionSide(mainWindow, edge);
        addEdgeWeight(mainWindow, edge);
    }

    private void addEdgePosition(MainWindow mainWindow, GraphEdge edge) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 100, (int)(edge.getLabelPositionFactor() * 100));
        slider.addChangeListener(e -> {
            edge.setLabelPositionFactor(0.01*((JSlider) e.getSource()).getValue());
            mainWindow.repaint();
        });
        addFormItem("Pozycja etykiety", slider);
    }

    private void addEdgePositionSide(MainWindow mainWindow, GraphEdge edge) {
        JCheckBox checkBox = new JCheckBox("", edge.getFlipEdgeLabelSide());
        checkBox.addActionListener(e -> {
            edge.setFlipEdgeLabelSide(((JCheckBox) e.getSource()).isSelected());
            mainWindow.repaint();
        });
        addFormItem("Zmień stronę etykiety", checkBox);
    }

    private void addEdgeWeight(MainWindow mainWindow, GraphEdge edge) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(edge.getWeight(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        spinner.addChangeListener(e -> {
            edge.setWeight((Integer) ((JSpinner) e.getSource()).getValue());
            mainWindow.repaint();
        });
        addFormItem("Waga", spinner);
    }

    private void addFormItem(String label, JComponent component) {
        add(new JLabel(label + ":", SwingConstants.RIGHT));
        add(component);
    }

}
