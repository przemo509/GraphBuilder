package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.gui.dialog.ExportDialog;
import pl.edu.pw.eiti.gis.gui.dialog.NewGraphDialog;
import pl.edu.pw.eiti.gis.model.GraphType;
import pl.edu.pw.eiti.gis.options.Options;

import javax.swing.*;

public class MainMenuBar extends JMenuBar {

    private final MainWindow mainWindow;
    private JCheckBoxMenuItem optionShowEdgeIndexes = new JCheckBoxMenuItem("pokazuj numery krawędzi", Options.getInstance().showEdgeIndexes());
    private JCheckBoxMenuItem optionShowEdgeWeights = new JCheckBoxMenuItem("pokazuj wagi krawędzi", Options.getInstance().showEdgeWeights());
    private JCheckBoxMenuItem optionPaintBlackAndWhite = new JCheckBoxMenuItem("rysuj czarno biały graf", Options.getInstance().paintBlackAndWhite());

    public MainMenuBar(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        addFileMenu();
        addOptionsMenu(mainWindow);
    }

    public void showNewGraphDialogForTheFirstTime() {
        NewGraphDialog newGraphDialog = new NewGraphDialog(mainWindow);
        newGraphDialog.setVisible(true);
    }

    private void addFileMenu() {
        JMenu menu = new JMenu("Plik");

        addNewGraphMenuItem(menu);
        addExportMenuItem(menu);
        menu.addSeparator();
        addExitMenuItem(menu);

        add(menu);
    }

    private void addNewGraphMenuItem(JMenu menu) {
        JMenuItem menuItem = new JMenuItem("Nowy graf...");
        menuItem.addActionListener(event -> new NewGraphDialog(mainWindow).setVisible(true));
        menu.add(menuItem);
    }

    private void addExportMenuItem(JMenu menu) {
        JMenuItem menuItem = new JMenuItem("Eksportuj graf...");
        menuItem.addActionListener(event -> new ExportDialog(mainWindow).setVisible(true));
        menu.add(menuItem);
    }

    private void addExitMenuItem(JMenu menu) {
        JMenuItem menuItem = new JMenuItem("Zakończ");
        menuItem.addActionListener(event -> System.exit(0)); // TODO confirmation dialog + clean exit
        menu.add(menuItem);
    }

    private void addOptionsMenu(MainWindow mainWindow) {
        JMenu menu = new JMenu("Opcje");

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

        optionPaintBlackAndWhite.addActionListener(e -> {
            Options.getInstance().setPaintBlackAndWhite(optionPaintBlackAndWhite.isSelected());
            mainWindow.repaint();
        });
        menu.add(optionPaintBlackAndWhite);

        add(menu);
    }

    public void setInitialState(GraphType graphType) {
        optionShowEdgeWeights.setEnabled(true); // otherwise clicks below won't work

        // weighted should show weights by default
        if (graphType.isWeighted() && !optionShowEdgeWeights.isSelected()) {
            optionShowEdgeWeights.doClick();
        }

        // weighted should not show indexes by default
        if (graphType.isWeighted() && optionShowEdgeIndexes.isSelected()) {
            optionShowEdgeIndexes.doClick();
        }

        // not weighted should not show weights
        if (!graphType.isWeighted() && optionShowEdgeWeights.isSelected()) {
            optionShowEdgeWeights.doClick();
        }

        // not weighted should not allow showing weights
        optionShowEdgeWeights.setEnabled(graphType.isWeighted());
        optionShowEdgeWeights.setToolTipText(graphType.isWeighted() ? null : "Opcja dostępna tylko dla grafu ważonego");

    }
}
