package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.gui.dialog.ExportDialog;
import pl.edu.pw.eiti.gis.gui.dialog.NewGraphDialog;
import pl.edu.pw.eiti.gis.gui.listener.MenuCloseListener;

import javax.swing.*;

public class MainMenuBar extends JMenuBar {

    private final ExportDialog exportDialog;
    private final NewGraphDialog newGraphDialog;
    private final MenuCloseListener menuCloseListener;

    public MainMenuBar(MainWindow mainWindow) {
        this.exportDialog = new ExportDialog(mainWindow);
        this.newGraphDialog = new NewGraphDialog(mainWindow);
        this.menuCloseListener = new MenuCloseListener(mainWindow);

        addFileMenu();
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
}
