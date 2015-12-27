package pl.edu.pw.eiti.gis.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;

public class NewGraphDialog extends RadioButtonsDialog {

    private JRadioButton simpleGraph;
    private JRadioButton multiGraph;

    private JRadioButton undirectedGraph;
    private JRadioButton directedGraph;

    private JRadioButton unweightedGraph;
    private JRadioButton weightedGraph;

    public NewGraphDialog(MainWindow mainWindow) {
        super(mainWindow, "Eksport grafu", "OK");
    }

    @Override
    protected JPanel buildOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(new TitledBorder("Właściwości nowego grafu"));

        panel.add(buildSimpleMultiGraphTypeOptions());
        panel.add(buildGraphDirectionTypeOptions());
        panel.add(buildGraphWeightTypeOptions());

        return panel;
    }

    private JPanel buildSimpleMultiGraphTypeOptions() {
        simpleGraph = new JRadioButton("prosty", true);
        multiGraph = new JRadioButton("multigraf");

        return buildRadioButtonGroup("Graf prosty / multigraf", simpleGraph, multiGraph);
    }

    private JPanel buildGraphDirectionTypeOptions() {
        undirectedGraph = new JRadioButton("nieskierowany", true);
        directedGraph = new JRadioButton("skierowany");
        return buildRadioButtonGroup("Skierowanie grafu", undirectedGraph, directedGraph);
    }

    private JPanel buildGraphWeightTypeOptions() {
        unweightedGraph = new JRadioButton("nie ważony", true);
        weightedGraph = new JRadioButton("ważony");

        return buildRadioButtonGroup("Wagi krawędzi", unweightedGraph, weightedGraph);
    }

    @Override
    protected ActionListener buildExportButtonListener() {
        return e -> {
            mainWindow.newGraph(multiGraph.isSelected(), directedGraph.isSelected(), weightedGraph.isSelected());
            setVisible(false);
        };
    }
}
