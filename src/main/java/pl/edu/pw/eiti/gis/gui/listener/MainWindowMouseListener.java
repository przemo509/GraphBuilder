package pl.edu.pw.eiti.gis.gui.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainWindowMouseListener implements MouseListener {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void mouseClicked(MouseEvent e) {
        logger.debug("click x={}, y={}", e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        logger.debug("press x={}, y={}", e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        logger.debug("release x={}, y={}", e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        logger.debug("enter x={}, y={}", e.getX(), e.getY());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        logger.debug("exit x={}, y={}", e.getX(), e.getY());
    }
}
