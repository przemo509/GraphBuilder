package pl.edu.pw.eiti.gis.gui.dialog;

import pl.edu.pw.eiti.gis.gui.MainWindow;
import pl.edu.pw.eiti.gis.model.GraphEdge;
import pl.edu.pw.eiti.gis.options.Options;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class EditingEdgeDialog extends JDialog {

    private final MainWindow mainWindow;
    private final GraphEdge edge;

    public EditingEdgeDialog(MainWindow mainWindow, GraphEdge edge) {
        super(mainWindow, "Edycja krawędzi", true);
        this.mainWindow = mainWindow;
        this.edge = edge;
        edge.setHighlighted(true);
        mainWindow.repaint();

        addComponents();
        pack();

        int dialogX = mainWindow.getX() + mainWindow.getWidth() - getWidth() - 15;
        int dialogY = mainWindow.getY() + mainWindow.getHeight() - getHeight() - 15;
        setLocation(dialogX, dialogY);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    @Override
    public void dispose() {
        edge.setHighlighted(false);
        mainWindow.repaint();
        super.dispose();
    }

    private void addComponents() {
        setLayout(new GridLayout(5, 2, 10, 2));
        addEdgeIndex();
        addEdgeWeight();
        addEdgePosition();
        addEdgePositionSide();
        addButtons();
    }

    private void addEdgeIndex() {
        JLabel label = new JLabel(String.valueOf(edge.getIndex()));
        addFormItem("Numer krawędzi", label);
    }

    private void addEdgeWeight() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(edge.getWeight(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        spinner.addChangeListener(e -> {
            edge.setWeight((Integer) ((JSpinner) e.getSource()).getValue());
            mainWindow.repaint();
        });
        spinner.setEnabled(Options.getInstance().showEdgeWeights());
        spinner.setToolTipText(Options.getInstance().showEdgeWeights() ? null : "Opcja dostępna po włączeniu widoczności wag krawędzi");
        addFormItem("Waga krawędzi", spinner);
    }

    private void addEdgePosition() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 100, (int)(edge.getLabelPositionFactor() * 100));
        slider.addChangeListener(e -> {
            edge.setLabelPositionFactor(0.01*((JSlider) e.getSource()).getValue());
            mainWindow.repaint();
        });
        addFormItem("Pozycja etykiety", slider);
    }

    private void addEdgePositionSide() {
        JCheckBox checkBox = new JCheckBox("", edge.getFlipEdgeLabelSide());
        checkBox.addActionListener(e -> {
            edge.setFlipEdgeLabelSide(((JCheckBox) e.getSource()).isSelected());
            mainWindow.repaint();
        });
        addFormItem("Zmień stronę etykiety", checkBox);
    }

    private void addFormItem(String label, JComponent component) {
        add(new JLabel(label + ":", SwingConstants.RIGHT));
        add(component);
    }

    private void addButtons() {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> closeDialog());
        add(okButton);

        JButton removeButton = new JButton("Usuń krawędź");
        removeButton.addActionListener(e -> {
            mainWindow.getGraph().removeEdge(edge);
            mainWindow.repaint();
            closeDialog();
        });
        add(removeButton);
    }

    protected void closeDialog() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

}
