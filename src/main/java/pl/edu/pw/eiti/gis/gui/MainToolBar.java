package pl.edu.pw.eiti.gis.gui;

import javax.swing.*;

public class MainToolBar extends JToolBar {

    private JToggleButton toolAddingVertices = new JToggleButton("Dodawanie wierzchołków", true);
    private JToggleButton toolAddingEdges = new JToggleButton("Dodawanie krawędzi");
    private JToggleButton toolMovingVertices = new JToggleButton("Przesuwanie wierzchołków");
    private JToggleButton toolMovingEdges = new JToggleButton("Przesuwanie etykiet krawędzi");
    private JToggleButton toolEditingEdgesWeights = new JToggleButton("Edycja wag krawędzi");

    public MainToolBar() {
        setFloatable(false);

        ButtonGroup group = new ButtonGroup();
        addTool(group, toolAddingVertices);
        addTool(group, toolAddingEdges);
        addTool(group, toolMovingVertices);
        addTool(group, toolMovingEdges);
        addTool(group, toolEditingEdgesWeights);
    }

    private void addTool(ButtonGroup group, JToggleButton button) {
        group.add(button);
        add(button);
    }

    public JToggleButton getToolAddingVertices() {
        return toolAddingVertices;
    }

    public JToggleButton getToolAddingEdges() {
        return toolAddingEdges;
    }

    public JToggleButton getToolMovingVertices() {
        return toolMovingVertices;
    }

    public JToggleButton getToolMovingEdges() {
        return toolMovingEdges;
    }

    public JToggleButton getToolEditingEdgesWeights() {
        return toolEditingEdgesWeights;
    }

    public void reset() {
        toolAddingVertices.setSelected(true);
    }
}
