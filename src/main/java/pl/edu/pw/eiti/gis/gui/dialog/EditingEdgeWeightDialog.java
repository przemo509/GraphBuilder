package pl.edu.pw.eiti.gis.gui.dialog;

import pl.edu.pw.eiti.gis.gui.MainWindow;
import pl.edu.pw.eiti.gis.model.GraphEdge;

import javax.swing.*;

public class EditingEdgeWeightDialog extends ToolDialog {

    public EditingEdgeWeightDialog(MainWindow mainWindow, GraphEdge edge) {
        super(mainWindow, "Edycja wagi krawędzi");

        addComponents(mainWindow, edge);
    }

    private void addComponents(MainWindow mainWindow, GraphEdge edge) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(edge.getWeight(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        spinner.addChangeListener(e -> {
            edge.setWeight((Integer) ((JSpinner) e.getSource()).getValue());
            mainWindow.repaint();
        });
        add(spinner);
    }

}
