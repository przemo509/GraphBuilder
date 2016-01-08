package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.gui.dialog.ExportDialog;
import pl.edu.pw.eiti.gis.gui.dialog.NewGraphDialog;
import pl.edu.pw.eiti.gis.gui.listener.MenuCloseListener;
import pl.edu.pw.eiti.gis.options.Options;

import javax.swing.*;

public class MainMenuBar extends JMenuBar {

    private final ExportDialog exportDialog;
    private final NewGraphDialog newGraphDialog;
    private final MenuCloseListener menuCloseListener;

    private JCheckBoxMenuItem optionShowEdgeIndexes = new JCheckBoxMenuItem("pokazuj numery krawędzi", Options.getInstance().showEdgeIndexes());
    private JCheckBoxMenuItem optionShowEdgeWeights = new JCheckBoxMenuItem("pokazuj wagi krawędzi", Options.getInstance().showEdgeWeights());

    public MainMenuBar(MainWindow mainWindow) {
        this.exportDialog = new ExportDialog(mainWindow);
        this.newGraphDialog = new NewGraphDialog(mainWindow);
        this.menuCloseListener = new MenuCloseListener(mainWindow);

        addFileMenu();
        addOptionsMenu(mainWindow);
        newGraphDialog.setVisible(true);
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

    private void addOptionsMenu(MainWindow mainWindow) {
        JMenu menu = new JMenu("Opcje");
        menu.getPopupMenu().addPopupMenuListener(menuCloseListener);

        optionShowEdgeIndexes.addActionListener(e -> {
            Options.getInstance().setShowEdgeIndexes(optionShowEdgeIndexes.isSelected());
            mainWindow.repaint();
        });
        menu.add(optionShowEdgeIndexes);

        optionShowEdgeWeights.addActionListener(e -> {
            Options.getInstance().setShowEdgeWeights(optionShowEdgeWeights.isSelected());
            mainWindow.repaint();
        });
        menu.add(optionShowEdgeWeights);

        add(menu);
    }
}
