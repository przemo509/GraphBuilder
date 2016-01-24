package pl.edu.pw.eiti.gis.gui;

import javax.swing.*;
import java.awt.*;

public class DrawingPlane extends JPanel {

    private final MainWindow mainWindow;

    public DrawingPlane(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        GraphDrawingUtils.drawGraph((Graphics2D) g, mainWindow.getGraph(), getWidth(), getHeight());
    }
}
