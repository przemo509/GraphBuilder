package pl.edu.pw.eiti.gis.gui;

import javax.swing.*;

public class MainMenuBar extends JMenuBar {

    private final ExportDialog exportDialog;

    private JRadioButtonMenuItem toolAddingNodes;
    private JRadioButtonMenuItem toolAddingEdges;

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

        addAddingNodesToolMenuItem(menu, group);
        addAddingEdgesToolMenuItem(menu, group);

        add(menu);
    }

    private void addAddingNodesToolMenuItem(JMenu menu, ButtonGroup group) {
        toolAddingNodes = new JRadioButtonMenuItem("Dodawanie wierzchołków", true);
        menu.add(toolAddingNodes);
        group.add(toolAddingNodes);
    }

    private void addAddingEdgesToolMenuItem(JMenu menu, ButtonGroup group) {
        toolAddingEdges = new JRadioButtonMenuItem("Dodawanie krawędzi");
        menu.add(toolAddingEdges);
        group.add(toolAddingEdges);
    }

    public JRadioButtonMenuItem getToolAddingNodes() {
        return toolAddingNodes;
    }

    public JRadioButtonMenuItem getToolAddingEdges() {
        return toolAddingEdges;
    }
}
