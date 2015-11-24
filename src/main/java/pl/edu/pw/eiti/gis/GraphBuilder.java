package pl.edu.pw.eiti.gis;

import pl.edu.pw.eiti.gis.gui.MainWindow;
import pl.edu.pw.eiti.gis.model.Graph;

import javax.swing.*;

public class GraphBuilder {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
