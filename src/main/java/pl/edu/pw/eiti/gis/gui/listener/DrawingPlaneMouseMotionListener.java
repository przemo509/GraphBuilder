package pl.edu.pw.eiti.gis.gui.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.eiti.gis.gui.MainWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class DrawingPlaneMouseMotionListener implements MouseMotionListener {

    private static final Logger logger = LogManager.getLogger();
    private final MainWindow mainWindow;

    public DrawingPlaneMouseMotionListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        logger.debug("mouse dragged: {}", e.paramString());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        logger.debug("mouse moved: {}", e.paramString());
        mainWindow.onMouseMoved(e.getPoint());
    }
}
