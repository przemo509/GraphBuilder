package pl.edu.pw.eiti.gis.gui;

import pl.edu.pw.eiti.gis.gui.listener.MainWindowMouseListener;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        super("GraphBuilder");
        setSize(800, 600);
        centerWindow();
        addMouseListener(new MainWindowMouseListener());
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }
}
