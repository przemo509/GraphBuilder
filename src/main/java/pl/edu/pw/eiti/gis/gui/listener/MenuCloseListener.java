package pl.edu.pw.eiti.gis.gui.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.pw.eiti.gis.gui.MainWindow;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class MenuCloseListener implements PopupMenuListener {
    private static final Logger logger = LogManager.getLogger();
    private final MainWindow mainWindow;

    public MenuCloseListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        logger.debug("1. popupMenuWillBecomeVisible");
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        logger.debug("2. popupMenuWillBecomeInvisible");
        mainWindow.repaint();
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
        logger.debug("3. popupMenuCanceled");
    }
}
