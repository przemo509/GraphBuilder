package pl.edu.pw.eiti.gis.gui;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        super("GraphBuilder");
        setSize(800, 600);
        centerWindow();
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }
}
