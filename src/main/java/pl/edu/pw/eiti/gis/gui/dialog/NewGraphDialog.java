package pl.edu.pw.eiti.gis.gui.dialog;

import pl.edu.pw.eiti.gis.gui.MainWindow;

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
        super(mainWindow, "Nowy graf");
        addComponents("OK");
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
        simpleGraph = new JRadioButton("graf prosty", true);
        multiGraph = new JRadioButton("multigraf");

        return buildRadioButtonGroup("Multigraf", simpleGraph, multiGraph);
    }

    private JPanel buildGraphDirectionTypeOptions() {
        undirectedGraph = new JRadioButton("graf nieskierowany", true);
        directedGraph = new JRadioButton("graf skierowany");
        return buildRadioButtonGroup("Skierowany", undirectedGraph, directedGraph);
    }

    private JPanel buildGraphWeightTypeOptions() {
        unweightedGraph = new JRadioButton("graf nie ważony", true);
        weightedGraph = new JRadioButton("graf ważony");

        return buildRadioButtonGroup("Ważony", unweightedGraph, weightedGraph);
    }

    @Override
    protected ActionListener buildOkButtonListener() {
        return e -> {
            mainWindow.newGraph(multiGraph.isSelected(), directedGraph.isSelected(), weightedGraph.isSelected());
            closeDialog();
        };
    }
}
