package pl.edu.pw.eiti.gis.gui.listener;

import pl.edu.pw.eiti.gis.gui.MainWindow;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuCloseListener implements PopupMenuListener {
    private static final Logger logger = Logger.getLogger(MenuCloseListener.class.getName());
    private final MainWindow mainWindow;

    public MenuCloseListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        logger.log(Level.FINEST, "1. popupMenuWillBecomeVisible");
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        logger.log(Level.FINEST, "2. popupMenuWillBecomeInvisible");
        mainWindow.repaint();
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
        logger.log(Level.FINEST, "3. popupMenuCanceled");
    }
}
