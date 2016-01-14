package pl.edu.pw.eiti.gis;

import pl.edu.pw.eiti.gis.gui.MainWindow;
import pl.edu.pw.eiti.gis.model.Graph;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class GraphBuilder {
    public static void main(String[] args) throws IOException {
        LogManager.getLogManager().readConfiguration(GraphBuilder.class.getResourceAsStream("/logging.properties"));

        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
