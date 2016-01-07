package pl.edu.pw.eiti.gis.gui;

import javax.swing.*;

public class MainToolBar extends JToolBar {

    private JRadioButton toolAddingVertices = new JRadioButton("Dodawanie wierzchołków", true);
    private JRadioButton toolAddingEdges = new JRadioButton("Dodawanie krawędzi");
    private JRadioButton toolMovingVertices = new JRadioButton("Przesuwanie wierzchołków");
    private JRadioButton toolMovingEdges = new JRadioButton("Przesuwanie etykiet krawędzi");

    public MainToolBar() {
        setFloatable(false);

        add(toolAddingVertices);
        add(toolAddingEdges);
        add(toolMovingVertices);
        add(toolMovingEdges);

        ButtonGroup group = new ButtonGroup();
        group.add(toolAddingVertices);
        group.add(toolAddingEdges);
        group.add(toolMovingVertices);
        group.add(toolMovingEdges);
    }

    public JRadioButton getToolAddingVertices() {
        return toolAddingVertices;
    }

    public JRadioButton getToolAddingEdges() {
        return toolAddingEdges;
    }

    public JRadioButton getToolMovingVertices() {
        return toolMovingVertices;
    }

    public JRadioButton getToolMovingEdges() {
        return toolMovingEdges;
    }

    public void reset() {
        toolAddingVertices.setSelected(true);
    }
}
