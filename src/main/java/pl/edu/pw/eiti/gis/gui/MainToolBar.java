package pl.edu.pw.eiti.gis.gui;

import javax.swing.*;

public class MainToolBar extends JToolBar {

    private JToggleButton toolAddingVertices = new JToggleButton("Dodawanie wierzchołków", true);
    private JToggleButton toolAddingEdges = new JToggleButton("Dodawanie krawędzi");
    private JToggleButton toolMovingVertices = new JToggleButton("Przesuwanie wierzchołków");
    private JToggleButton toolEditingEdges = new JToggleButton("Edycja krawędzi");
    private JToggleButton toolRemovingVertices = new JToggleButton("Usuwanie wierzchołków");

    public MainToolBar() {
        setFloatable(false);

        ButtonGroup group = new ButtonGroup();
        addTool(group, toolAddingVertices);
        addTool(group, toolAddingEdges);
        addTool(group, toolMovingVertices);
        addTool(group, toolEditingEdges);
        addTool(group, toolRemovingVertices);
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

    public JToggleButton getToolEditingEdges() {
        return toolEditingEdges;
    }

    public JToggleButton getToolRemovingVertices() {
        return toolRemovingVertices;
    }

    public void reset() {
        toolAddingVertices.setSelected(true);
    }
}
