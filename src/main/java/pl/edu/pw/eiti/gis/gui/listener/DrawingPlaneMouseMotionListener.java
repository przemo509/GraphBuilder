package pl.edu.pw.eiti.gis.gui.listener;

import pl.edu.pw.eiti.gis.gui.MainWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DrawingPlaneMouseMotionListener implements MouseMotionListener {

    private static final Logger logger = Logger.getLogger(DrawingPlaneMouseMotionListener.class.getName());
    private final MainWindow mainWindow;

    public DrawingPlaneMouseMotionListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        logger.log(Level.FINEST, "mouse dragged: {0}", e.paramString());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        logger.log(Level.FINEST, "mouse moved: {0}", e.paramString());
        mainWindow.onMouseMoved(e.getPoint());
    }
}
