package pl.edu.pw.eiti.gis.gui;

import javax.swing.*;

public class MainMenuBar extends JMenuBar {

    private final ExportDialog exportDialog;

    private JRadioButtonMenuItem toolAddingNodes = new JRadioButtonMenuItem("Dodawanie wierzchołków", true);
    private JRadioButtonMenuItem toolAddingEdges = new JRadioButtonMenuItem("Dodawanie krawędzi");
    private JRadioButtonMenuItem toolMovingNodes = new JRadioButtonMenuItem("Przesuwanie wierzchołków");

    public MainMenuBar(MainWindow mainWindow) {
        this.exportDialog = new ExportDialog(mainWindow);

        addFileMenu();
        addToolsMenu();
    }

    private void addFileMenu() {
        JMenu menu = new JMenu("Plik");

        addExportMenuItem(menu);
        menu.addSeparator();
        addExitMenuItem(menu);

        add(menu);
    }

    private void addExportMenuItem(JMenu menu) {
        JMenuItem menuItem = new JMenuItem("Eksportuj graf...");
        menuItem.addActionListener(event -> exportDialog.setVisible(true));
        menu.add(menuItem);
    }

    private void addExitMenuItem(JMenu menu) {
        JMenuItem menuItem = new JMenuItem("Zakończ");
        menuItem.addActionListener(event -> System.exit(0)); // TODO confirmation dialog + clean exit
        menu.add(menuItem);
    }

    private void addToolsMenu() {
        JMenu menu = new JMenu("Narzędzia");
        ButtonGroup group = new ButtonGroup();
        addRadioButtonMenuItem(menu, group, toolAddingNodes);
        addRadioButtonMenuItem(menu, group, toolAddingEdges);
        menu.addSeparator();
        addRadioButtonMenuItem(menu, group, toolMovingNodes);

        add(menu);
    }

    private void addRadioButtonMenuItem(JMenu menu, ButtonGroup group, JRadioButtonMenuItem radioButtonMenuItem) {
        menu.add(radioButtonMenuItem);
        group.add(radioButtonMenuItem);
    }

    public JRadioButtonMenuItem getToolAddingNodes() {
        return toolAddingNodes;
    }

    public JRadioButtonMenuItem getToolAddingEdges() {
        return toolAddingEdges;
    }

    public JRadioButtonMenuItem getToolMovingNodes() {
        return toolMovingNodes;
    }
}
