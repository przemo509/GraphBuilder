package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.gui.dialog.ExportDialog;
import pl.edu.pw.eiti.gis.gui.dialog.NewGraphDialog;
import pl.edu.pw.eiti.gis.gui.listener.MenuCloseListener;

import javax.swing.*;

public class MainMenuBar extends JMenuBar {

    private final ExportDialog exportDialog;
    private final NewGraphDialog newGraphDialog;
    private final MenuCloseListener menuCloseListener;

    private JRadioButtonMenuItem toolAddingVertices = new JRadioButtonMenuItem("Dodawanie wierzchołków", true);
    private JRadioButtonMenuItem toolAddingEdges = new JRadioButtonMenuItem("Dodawanie krawędzi");
    private JRadioButtonMenuItem toolMovingVertices = new JRadioButtonMenuItem("Przesuwanie wierzchołków");
    private JRadioButtonMenuItem toolMovingEdges = new JRadioButtonMenuItem("Przesuwanie etykiet krawędzi");

    public MainMenuBar(MainWindow mainWindow) {
        this.exportDialog = new ExportDialog(mainWindow);
        this.newGraphDialog = new NewGraphDialog(mainWindow);
        this.menuCloseListener = new MenuCloseListener(mainWindow);

        addFileMenu();
        addToolsMenu();
    }

    private void addFileMenu() {
        JMenu menu = new JMenu("Plik");
        menu.getPopupMenu().addPopupMenuListener(menuCloseListener);

        addNewGraphMenuItem(menu);
        addExportMenuItem(menu);
        menu.addSeparator();
        addExitMenuItem(menu);

        add(menu);
    }

    private void addNewGraphMenuItem(JMenu menu) {
        JMenuItem menuItem = new JMenuItem("Nowy graf...");
        menuItem.addActionListener(event -> newGraphDialog.setVisible(true));
        menu.add(menuItem);
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
        menu.getPopupMenu().addPopupMenuListener(menuCloseListener);
        ButtonGroup group = new ButtonGroup();
        addRadioButtonMenuItem(menu, group, toolAddingVertices);
        addRadioButtonMenuItem(menu, group, toolAddingEdges);
        menu.addSeparator();
        addRadioButtonMenuItem(menu, group, toolMovingVertices);
        addRadioButtonMenuItem(menu, group, toolMovingEdges);

        add(menu);
    }

    private void addRadioButtonMenuItem(JMenu menu, ButtonGroup group, JRadioButtonMenuItem radioButtonMenuItem) {
        menu.add(radioButtonMenuItem);
        group.add(radioButtonMenuItem);
    }

    public JRadioButtonMenuItem getToolAddingVertices() {
        return toolAddingVertices;
    }

    public JRadioButtonMenuItem getToolAddingEdges() {
        return toolAddingEdges;
    }

    public JRadioButtonMenuItem getToolMovingVertices() {
        return toolMovingVertices;
    }

    public JRadioButtonMenuItem getToolMovingEdges() {
        return toolMovingEdges;
    }
}
